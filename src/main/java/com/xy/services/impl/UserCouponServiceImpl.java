package com.xy.services.impl;

import com.xy.config.Config;
import com.xy.config.CouponConfig;
import com.xy.models.*;
import com.xy.services.CouponService;
import com.xy.services.ShopService;
import com.xy.services.UnionOrderService;
import com.xy.services.UserCouponService;
import com.xy.utils.DateUtils;
import com.xy.utils.StringUtils;
import org.apache.commons.collections.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.StringUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


/**
 * @author Administrator
 */
@EnableAsync
@Service
public class UserCouponServiceImpl extends BaseServiceImpl<UserCoupon> implements UserCouponService {

    @Autowired
    private CouponService couponService;

    @Autowired
    private UnionOrderService orderService;

    @Autowired
    private ShopService shopService;

    @Autowired
    private SqlService sqlService;

    /**
     * 符合条件的优惠卷
     */
    Coupon target = null;


    @Override
    public UserCoupon selectOnlyByKey(Object key) {
        UserCoupon userCoupon = new UserCoupon();
        userCoupon.setCouponid(key.toString());
        userCoupon = super.selectOnly(userCoupon);
        userCoupon.setCoupon(couponService.selectOnlyByKey(key));
        return userCoupon;
    }


    @Override
    public List<UserCoupon> selectList(String userId, String shopId, String cate, String goodId) {
        UserCoupon userCoupon = new UserCoupon();
        userCoupon.setUserid(userId);
        userCoupon.setStatus("waituser");

        List<UserCoupon> result = new ArrayList<>();

        List<UserCoupon> userCoupons = super.selectList(userCoupon);

        if (userCoupons != null && userCoupons.size() > 0) {
            // 查询优惠卷信息
            Condition cond = new Condition(Coupon.class);
            cond.createCriteria().andIn("uuid", userCoupons.stream().map(UserCoupon::getCoupon).collect(Collectors.toList()));
            List<Coupon> coupons = couponService.selectListByCondition(cond);


            for (UserCoupon item : userCoupons) {
                Coupon coupon = coupons.stream().filter(arg -> arg.getUuid().equals(item.getCoupon())).collect(Collectors.toList()).get(0);
                item.setCoupon(coupon);
                // 根据参数判断该订单是否可用优惠卷
                boolean legal = (coupon.getToGoods().equals("good") && coupon.getToGoodsValue().equals(goodId)) || (coupon.getToGoods().equals("cate") && coupon.getToGoodsValue().equals(cate)) || coupon.getToGoods().equals("all");
                if (legal) {
                    legal = StringUtils.isNull(shopId) || (StringUtils.isNotNull(shopId) && coupon.getAuthor().equals(shopId));
                    if (legal) {
                        result.add(item);
                        break;
                    }
                }
            }
        }
        return userCoupons;
    }


    @Override
    public void comAutoTrash() {
        // 查询已过期优惠卷
        Condition cond = new Condition(Coupon.class);
        cond.createCriteria()
                .andNotEqualTo("endTime", "forever")
                .andLessThan("endTime", DateUtils.getDate())
                .andEqualTo("status", "online");
        List<Coupon> coupons = couponService.selectListByCondition(cond);

        // 所有已过期优惠卷标识
        List<String> uuids = new ArrayList<>();
        // 商铺创建的优惠卷，需要检查该商铺是否有未过期的优惠卷,如果没有了需要把商铺设置为为活动期间，： Active = Y / N
        List<String> shopCoupon = new ArrayList<>();
        coupons.stream().forEach(coupon -> {
            uuids.add(coupon.getUuid());
            // 保存商铺创建的优惠卷
            if (!coupon.getAuthor().equals(Config.lord)) {
                shopCoupon.add(coupon.getAuthor());
            }
        });

        // 优惠卷已过期
        cond.clear();
        cond.createCriteria().andIn("uuid", uuids);

        Coupon other = new Coupon();
        other.setStatus("expired");
        couponService.updateByConditionSelective(other, cond);

        // 用户拥有的优惠卷已过期
        UserCoupon userCoupon = new UserCoupon();
        userCoupon.setStatus("expired");

        cond = new Condition(UserCoupon.class);
        cond.createCriteria().andIn("couponid", uuids);
        super.updateByConditionSelective(userCoupon, cond);

        // 开始检查商铺是否处于活动期间
        Example example = new Example(Coupon.class);
        shopCoupon.forEach(uuid -> {
            example.clear();
            example.createCriteria().andEqualTo("author", uuid).andEqualTo("status", "online");
            int result = couponService.count(example);
            if (result < 1) {
                Shop shop = new Shop();
                shop.setActive("N");
                shop.setUuid(uuid);
                shopService.updateByPrimaryKeySelective(shop);
            }
        });
    }

    @Override
    public Coupon selectOfficialByOrder(User user, UnionGoods good, UnionOrders order) {
        List<Coupon> coupons = null;
        if (good != null) {
            coupons = this.selectByCond(null, null, good.getUuid());
            if (coupons == null || coupons.isEmpty()) {
                coupons = this.selectByCond(null, good.getCatId(), null);
            }
        }
        if (coupons == null || coupons.isEmpty()) {
            coupons = this.selectByCond(Config.lord, null, null);
        }

        if (coupons != null && coupons.size() > 0) {
            if (user.isNew()) {
                // 查找出仅限新用户并且满足消费条件的优惠卷
                target = this.filter(coupons, order.getTotalPrice(), "newuser");
            } else {
                // 查找出仅限老用户并且满足消费条件的优惠卷
                target = this.filter(coupons, order.getTotalPrice(), "olduser");
            }
            if (target == null) {
                // 查找出面向所有用户并且满足消费条件的优惠卷
                target = this.filter(coupons, order.getTotalPrice(), "all");
            }
        }
        this.giveover(target);
        return target;
    }


    @Override
    public Coupon selectShopByOrder(User user, Shop shop, UnionGoods good, UnionOrders order) {
        List<Coupon> coupons = null;
        if (good != null) {
            coupons = this.selectByCond(null, null, good.getUuid());
            if (coupons == null || coupons.isEmpty()) {
                coupons = this.selectByCond(null, good.getCatId(), null);
            }
        }
        if (coupons == null || coupons.isEmpty()) {
            coupons = this.selectByCond(shop.getUuid(), null, null);
        }

        if (coupons != null && coupons.size() > 0) {
            UnionOrders condition = new UnionOrders();
            condition.setShopUuid(shop.getUuid());
            condition.setUserUuid(user.getUuid());
            condition.setStatus("consumed");
            int buys = orderService.count(condition);
            if (buys == 0) {
                // 新用户
                target = this.filter(coupons, order.getTotalPrice(), "newuser");
            } else {
                // 老用户
                target = this.filter(coupons, order.getTotalPrice(), "olduser");
            }
            // 在面向所有用户优惠卷里查询
            if (target == null) {
                target = this.filter(coupons, order.getTotalPrice(), "all");
            }
        }
        this.giveover(target);
        return target;
    }

    /**
     * 根据创建人查询
     *
     * @param author
     * @return
     */
    private List<Coupon> selectByCond(String author, String type, String good) {
        // 检索隐式使用的优惠卷
        Condition cond = new Condition(Coupon.class);
        Example.Criteria cri = cond.createCriteria();
        cri.andEqualTo("useMethod", "implicit").andEqualTo("status", "online");
        if (StringUtils.isNotNull(author)) {
            cri.andEqualTo("author", author);
        }

        if (StringUtils.isNotNull(type)) {
            cri.andEqualTo("toGoodsValue", type);
        }

        if (StringUtils.isNotNull(good)) {
            cri.andEqualTo("toGoodsValue", good);
        }
        cond.setOrderByClause("to_user_value desc");
        return couponService.selectListByCondition(cond);
    }


    /**
     * 查找符合条件的优惠卷
     *
     * @param coupons 可以使用的
     * @param money   消费金额
     * @param toUser  面向用户类型，所有，新用户，老用户
     * @return
     */
    private Coupon filter(List<Coupon> coupons, BigDecimal money, String toUser) {
        // 过滤出所有符合 消费金额，用户类型的优惠卷
        List<Coupon> others = coupons.stream().filter(coupon -> toUser.equals(coupon.getToUser()) && money.compareTo(BigDecimal.valueOf(coupon.getToUserValue())) > -1).sorted(Comparator.comparing(Coupon::getToUserValue)).collect(Collectors.toList());
        if (others != null && others.size() > 0) {
            return others.get(0);
        }
        return null;
    }


    /**
     * 更新优惠卷发放量，或者优惠卷发放完毕更改状态为已售罄
     *
     * @param target
     * @return
     */
    @Async
    protected boolean giveover(Coupon target) {
        int result = 0;
        // 最终合适的优惠卷
        if (target != null) {
//            target.setUsed(target.getUsed() + 1);
            if (target.getTotal() > 0 && target.getUsed().equals(target.getTotal())) {
                // 优惠卷已售罄
                target.setStatus("giveover");
                if (!Config.lord.equals(target.getAuthor())) {
                    // 如果是商户某优惠卷售罄，检查商户是否还存在其他优惠卷信息，如果没有取消商户处于活动期间状态
                    target.getAuthor();
                    Coupon other = new Coupon();
                    other.setAuthor(target.getAuthor());
                    other.setStatus("online");
                    result = couponService.count(target);
                    // 因为到此还没有对优惠卷做出状态更改，所以查询出来有一条数据需要减去当前的优惠卷对象, 等于0标识该商户没有优惠卷了
                    if (0 == (result - 1)) {
                        Shop shop = new Shop();
                        shop.setUuid(target.getAuthor());
                        shop.setActive("N");
                        shopService.updateByPrimaryKeySelective(shop);
                    }
                }
            }
//            result = couponService.updateByPrimaryKeySelective(target);
//            if (result > 0) {
            return true;
//            }
        }
        return false;
    }

    @Override
    public boolean giveCoupon(String user, String _couponTarget) {
        // 订单赠送优惠卷
        Condition cond = new Condition(Coupon.class);
        cond.createCriteria().andEqualTo("number", _couponTarget).andEqualTo("status", "online");
        Coupon coupon = couponService.selectOnly(cond, 0);
        if(coupon != null) {
            UserCoupon userCoupon = new UserCoupon(user, coupon.getUuid(), 1, coupon.getEndTime());
            this.saveSelective(userCoupon);
            return true;
        }
        return false;
    }

    @Override
    public void updateCouponExpend(String id) {
        if (StringUtils.isNotNull(id)) {
            sqlService.exec("UPDATE coupon SET used = IFNULL(used, 0)+1 WHERE uuid = '%s'", id);
        }
    }
}
