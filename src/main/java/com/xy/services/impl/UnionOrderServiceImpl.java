package com.xy.services.impl;

import com.xy.models.*;
import com.xy.services.*;
import com.xy.utils.DateUtils;
import com.xy.utils.RandomUtil;
import com.xy.utils.StringUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rjora on 2017/7/16 0016.
 */
@Service
public class UnionOrderServiceImpl extends BaseServiceImpl<UnionOrders> implements UnionOrderService {

    @Autowired
    private ShopService shopService;

    @Autowired
    private UnionGoodService goodService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserCouponService couponService;


    @Override
    public int saveSelective(String userId, String shopId, String goodId, int num) {
        UnionOrders order = new UnionOrders();

        User user = userService.selectOnlyByKey(userId);
        Shop shop = shopService.selectOnlyByKey(shopId);
        UnionGoods entity = new UnionGoods();
        entity.setUuid(goodId);
        entity.setShopUuid(shopId);
        UnionGoods good = goodService.selectOnly(entity);

        if (good != null && shop != null && good != null) {
            order.setUuid(StringUtils.getUuid());
            order.setAddTime(DateUtils.getCurrentDate());

            order.setUserUuid(user.getUuid());
            order.setUserName(user.getName());
            order.setShopUuid(shop.getUuid());
            order.setShopName(shop.getName());
            order.setOrderNo(RandomUtil.getRandom(17, RandomUtil.TYPE.NUMBER));
            order.setGoodsUuid(entity.getUuid());
            order.setGoodsNum(num);
            order.setJudged(false);
            order.setStatus("waitPay");
            // 订单总金额，应付金额
            order.setTotalPrice(good.getPrice().multiply(BigDecimal.valueOf(num)));
            // 密码串码, 用户到店核销
            order.setCardCode(StringUtils.splitWithChar(RandomUtil.getRandom(17, RandomUtil.TYPE.NUMBER), 4, ' '));

            order = this.implicitCoupon(user, shop, order);

            // 实际支付金额
            order.setPayPrice(BigDecimal.valueOf(0));

            // 用户支付后更改支付方式
            order.setPayWay(null);

//            return super.saveSelective(order);
            return 0;
        } else {
            // 参数不完整
            return -1;
        }
    }

    /**
     * 查询隐式优惠卷
     *
     * @param user
     * @param shop
     * @param order
     */
    private UnionOrders implicitCoupon(User user, Shop shop, UnionOrders order) {
        // 官方和商品创建的优惠卷合并
        List<Coupon> couponList = new ArrayList<>();

        // 如果商户处于促销活动期间，则查询商户发布的优惠卷信息
        if (shop.isActive()) {
            Coupon shopCoupon = couponService.selectShopByOrder(user, shop, order);

            switch (shopCoupon.getRule()) {
                case "recoupon":
                    // 返券
                    shopCoupon.getRuleValue();
                    // TODO: 2017/10/24 当前优惠卷使用量已经+1，并且已经处理了商户是否处于活动期， 接下来去要做的是对当前优惠卷使用规则区分

                    break;
                case "discount":
                    // 折扣
                    
                    break;
                case "fulldown":
                    // 满减

                    break;
                default:
                    break;
            }

            couponList.add(shopCoupon);
            // TODO: 2017/10/23

        }

        //查询满足使用条件的官方优惠卷
        Coupon offCoupon = couponService.selectOfficialByOrder(user, order);
        couponList.add(offCoupon);
        if (offCoupon != null) {
            order.setCoupon(offCoupon.getUuid());
            order.setPreferentialPrice(offCoupon.getRuleValue());
        }


        String cpstr = org.apache.commons.lang3.StringUtils.join(couponList.stream().map(Coupon::getUuid).toArray(), "|");

        System.out.println(cpstr);

        return order;
    }
}
