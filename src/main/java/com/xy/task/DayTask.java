package com.xy.task;

import com.xy.services.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class DayTask {

    @Autowired
    private ShopService shopService;

    @Scheduled(cron = "0 0 1 ? * *")
    public void exec() {
        shopService.autoFreeze();
    }

}
