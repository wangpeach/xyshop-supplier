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
import java.text.DecimalFormat;
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


            if (StringUtils.isNotNull(order.getCoupon())) {
                UserCoupon userCoupon = couponService.selectOnlyByKey(order.getCoupon());
                order = this.cprole(order, userCoupon.getCoupon());
            } else {
                order = this.implicitCoupon(user, shop, order);
            }

            // 实际支付金额
            order.setPayPrice(BigDecimal.valueOf(0));

            // 用户支付后更改支付方式
            order.setPayWay(null);

            return super.saveSelective(order);
        } else {
            // 参数不完整
            return -1;
        }
    }

    /**
     * 查询隐式优惠卷
     * 商户处于活动期并且该订单有可用的商户优惠卷，则使用商户的优惠卷。否则使用系统发放的优惠卷
     *
     * @param user
     * @param shop
     * @param order
     */
    private UnionOrders implicitCoupon(User user, Shop shop, UnionOrders order) {
        Coupon coupon = null;
        // 如果商户处于促销活动期间，则查询商户发布的优惠卷信息
        if (shop.isActive()) {
            // 查询满足使用条件的优惠卷
            coupon = couponService.selectShopByOrder(user, shop, order);
            if (coupon != null) {
                order.setSysTips(coupon.getName());
            }
        }
        // 该订单无法使用商户发布的优惠卷
        if (coupon == null) {
            //查询满足使用条件的官方优惠卷
            coupon = couponService.selectOfficialByOrder(user, order);
            if (coupon != null) {
                order.setSysTips(coupon.getName());
            }
        }

        // 应支付金额
        order = this.cprole(order, coupon);
        order.setCoupon(coupon.getUuid());
        order.setPreferentialPrice(coupon.getRuleValue());
        return order;
    }


    /**
     * 计算订单应支付金额
     *
     * @param order
     * @param coupon
     * @return
     */
    private UnionOrders cprole(UnionOrders order, Coupon coupon) {
        if (coupon != null) {
            order.setSysTips(coupon.getName());
            // 支付金额
            BigDecimal payAmount = BigDecimal.ZERO;
            BigDecimal discount = new BigDecimal(coupon.getRuleValue());
            switch (coupon.getRule()) {
                case "discount":
                    // 折扣
                    discount = discount.divide(BigDecimal.valueOf(100));
                    // 应付金额
                    payAmount = order.getTotalPrice().subtract(order.getTotalPrice().multiply(discount));
                    break;
                case "fulldown":
                    // 满减
                    payAmount = order.getTotalPrice().subtract(discount);
                    break;
                default:
                    // recoupon 返券，需要等用户消费成功才可以发放优惠卷
                    payAmount = order.getTotalPrice();
                    break;
            }
            order.setPayPrice(payAmount);
        }
        return order;
    }
}
