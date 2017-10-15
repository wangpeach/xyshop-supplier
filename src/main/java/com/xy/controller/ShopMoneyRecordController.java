package com.xy.controller;

import com.xy.services.ShopMoneyRecordService;
import com.xy.utils.MoneyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@Scope(value = "prototype")
@RequestMapping("money-record")
public class ShopMoneyRecordController {

    @Autowired
    private ShopMoneyRecordService moneyRecordService;

    /**
     * 商铺消费统计
     *
     * @param shopUuid
     * @return
     */
    @RequestMapping(value = "csinfo")
    public @ResponseBody
    Map<String, Object> statistics(@RequestParam String shopUuid) {
        Map<String, Object> result = moneyRecordService.statistics(shopUuid);
        Object money = result.get("money");
        result.put("money", MoneyUtils.fen2Yuan(money));
        result.put("everyMoney", MoneyUtils.fen2Yuan(result.get("everyMoney")));
        int buyedNum = Integer.parseInt(result.get("buyedNum").toString());
        double everyMoney = MoneyUtils.fen2Yuan(money) / (buyedNum == 0 ? 1 : buyedNum);
        result.put("everyMoney", everyMoney);
        return result;
    }

}
