package com.xy.services.impl;

import com.xy.models.Coupon;
import com.xy.models.UserCoupon;
import com.xy.services.CouponService;
import com.xy.services.UserCouponService;
import com.xy.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Condition;

import java.util.List;


@Service
public class UserCouponServiceImpl extends BaseServiceImpl<UserCoupon> implements UserCouponService {

    @Autowired
    private CouponService couponService;

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
}
