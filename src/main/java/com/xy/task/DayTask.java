package com.xy.task;

import com.xy.services.ShopService;
import com.xy.services.UserCouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class DayTask {

    @Autowired
    private ShopService shopService;

    @Autowired
    private UserCouponService couponService;



    @Scheduled(cron = "0 0 1 ? * *")
    public void exec() {
        shopService.autoFreeze();
    }

    /**
     * 每天凌晨5秒时执行优惠卷是否过期功能
     */
    @Scheduled(cron = "5 0 0 * * ?")
    public void coupon() {
        couponService.autoTrash();
    }

}
