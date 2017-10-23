package com.xy.services;

import com.sun.xml.internal.xsom.XSWildcard;
import com.xy.models.*;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;

public interface UserCouponService extends BaseService<UserCoupon> {

    /**
     * 查询优惠卷，默认查询用户拥有的优惠卷
     * @param userId 查询用户优惠卷，其他参数可为空， 必填
     * @param shopId 如果传入此参数，查询该用户拥有该店铺所有优惠卷
     * @param goodTypeId 该商品类型用户的优惠卷
     * @param goodId 该商品拥有的优惠卷
     * @return
     */
    UserCoupon selectList(@NotNull String userId, String shopId, String goodTypeId, String goodId);

    /**
     * 处理过期的优惠卷，自动过期
     */
    void autoTrash();

    /**
     * 查询订单满足使用条件的官方优惠卷
     *
     * @param user  下单用户
     * @param order 订单信息
     * @return 返回满足条件的优惠卷
     */
    Coupon selectOfficialByOrder(User user, UnionOrders order);

    /**
     * 查询订单满足使用条件的商铺优惠卷
     * @param user 下单用户
     * @param shop 接单商户
     * @param order 订单信息
     * @return
     */
    Coupon selectShopByOrder(User user, Shop shop, UnionOrders order);
}
