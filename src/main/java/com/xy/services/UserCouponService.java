package com.xy.services;

import com.xy.models.UserCoupon;

public interface UserCouponService extends BaseService<UserCoupon> {

    UserCoupon selectList(String userId, String shopId, String goodTypeId, String goodId);

    /**
     * 处理过期的优惠卷
     */
    void autoTrash();
}
