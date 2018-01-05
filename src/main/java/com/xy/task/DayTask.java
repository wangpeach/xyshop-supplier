package com.xy.task;

import com.xy.services.ShopService;
import com.xy.services.UserCouponService;
import com.xy.utils.DateUtils;
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
        System.out.println("冻结合同逾期商铺 - " + DateUtils.getCurrentDate());
        shopService.comAutoFreeze();
        System.out.println("处理完成 - " + DateUtils.getCurrentDate());
    }

    /**
     * 每天凌晨5秒时执行优惠卷是否过期功能
     */
    @Scheduled(cron = "5 0 0 * * ?")
    public void coupon() {
        System.out.println("冻结过期的优惠卷 - " + DateUtils.getCurrentDate());
        couponService.comAutoTrash();
        System.out.println("处理完成 - " + DateUtils.getCurrentDate());
    }

}
