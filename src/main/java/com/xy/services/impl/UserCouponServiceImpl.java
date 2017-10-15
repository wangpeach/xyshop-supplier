package com.xy.services.impl;

import com.xy.models.UserCoupon;
import com.xy.services.CouponService;
import com.xy.services.UserCouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;



@Service
public class UserCouponServiceImpl extends BaseServiceImpl<UserCoupon> implements UserCouponService {

    @Autowired
    private CouponService couponService;

    @Override
    public List<UserCoupon> selectList(UserCoupon entity) {
        List<UserCoupon> coupons = super.selectList(entity);
        for (UserCoupon item : coupons) {
            item.setCoupon(couponService.selectOnlyByKey(item.getCouponid()));
        }
        return coupons;
    }
}
