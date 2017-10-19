package com.xy.services;

import com.xy.models.Coupon;
import com.xy.models.UnionOrders;
import com.xy.models.User;
import com.xy.models.UserCoupon;

import java.math.BigDecimal;

public interface UserCouponService extends BaseService<UserCoupon> {

    UserCoupon selectList(String userId, String shopId, String goodTypeId, String goodId);

    /**
     * 处理过期的优惠卷
     */
    void autoTrash();

    Coupon selectByOrder(User user, UnionOrders order);
}
