package com.xy.services;

import com.sun.xml.internal.xsom.XSWildcard;
import com.xy.models.Coupon;
import com.xy.models.UnionOrders;
import com.xy.models.User;
import com.xy.models.UserCoupon;
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
     * 查询官方创建的隐式优惠卷
     * @param user 下单用户
     * @param order 订单信息
     * @return
     */
    Coupon selectOfficialByOrder(User user, UnionOrders order);


//    Coupon selectShopByOrder(User user);
}
