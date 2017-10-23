package com.xy.services.impl;

import com.xy.models.*;
import com.xy.services.CouponService;
import com.xy.services.UnionOrderService;
import com.xy.services.UserCouponService;
import com.xy.utils.DateUtils;
import com.xy.utils.StringUtils;
import com.xy.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class UserCouponServiceImpl extends BaseServiceImpl<UserCoupon> implements UserCouponService {

    @Autowired
    private CouponService couponService;

    @Autowired
    private UnionOrderService orderService;


    /**
     * 符合条件的优惠卷
     */
    Coupon target = null;


    @Override
    public UserCoupon selectList(String userId, String shopId, String goodTypeId, String goodId) {
        UserCoupon userCoupon = new UserCoupon();
        userCoupon.setUserid(userId);
        userCoupon.setStatus("waituser");
        List<UserCoupon> coupons = super.selectList(userCoupon);
        for (UserCoupon item : coupons) {
            Coupon other = couponService.selectOnlyByKey(item.getCouponid());
            // TODO: 2017/10/16 根据参数判断该订单是否可用优惠卷

            item.setCoupon(other);
        }
        return null;
    }


    @Override
    public void autoTrash() {
        // 查询已过期优惠卷
        Condition cond = new Condition(Coupon.class);
        cond.createCriteria()
                .andNotEqualTo("end_time", "forever")
                .andLessThan("end_time", DateUtils.getDate())
                .andEqualTo("status", "online");
        List<Coupon> coupons = couponService.selectListByCondition(cond);

        // TODO: 2017/10/16 废弃已过期的优惠卷, 查询出来的是已过期的，变更状态即可，完了还有用户的优惠卷也需要废弃

//        couponService.updateByConditionSelective();
    }

    @Override
    public Coupon selectOfficialByOrder(User user, UnionOrders order) {

        List<Coupon> coupons = this.selectByCond("lord");
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
        return target;
    }


    @Override
    public Coupon selectShopByOrder(User user, Shop shop, UnionOrders order) {
        List<Coupon> coupons = this.selectByCond(shop.getUuid());
        if(coupons != null && coupons.size() > 0) {
            UnionOrders condition = new UnionOrders();
            condition.setShopUuid(shop.getUuid());
            condition.setUserUuid(user.getUuid());
            condition.setStatus("consumed");
            int buys = orderService.count(condition);
            if (buys == 0) {
                target = this.filter(coupons, order.getTotalPrice(), "newuser");
            } else {
                target = this.filter(coupons, order.getTotalPrice(), "olduser");
            }

            if (target == null) {
                target = this.filter(coupons, order.getTotalPrice(), "all");
            }
        }
        return target;
    }


    private List<Coupon> selectByCond(String author) {
        // 检索隐式使用的优惠卷
        Condition cond = new Condition(Coupon.class);
        Example.Criteria cri = cond.createCriteria();
        cri.andEqualTo("useMethod", "implicit").andEqualTo("status", "online");
        if(StringUtils.isNotNull(author)) {
            cri.andEqualTo("author", author);
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
        return coupons.stream().filter(coupon -> toUser.equals(coupon.getToUser()) && money.compareTo(BigDecimal.valueOf(coupon.getToUserValue())) > -1).sorted(Comparator.comparing(Coupon::getToUserValue)).collect(Collectors.toList()).get(0);
    }
}
