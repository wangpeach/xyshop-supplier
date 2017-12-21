package com.xy.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.xy.models.Shop;
import com.xy.models.ShopMoneyRecord;
import com.xy.pojo.ParamsPojo;
import com.xy.services.ShopMoneyRecordService;
import com.xy.utils.MoneyUtils;
import com.xy.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.util.Map;

/**
 * @author Administrator
 */
@Controller
@Scope(value = "prototype")
@RequestMapping("money-record/")
public class ShopMoneyRecordController {

    @Autowired
    private ShopMoneyRecordService moneyRecordService;

    /**
     * 商铺消费统计
     *
     * @param shopUuid
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "csinfo")
    public Map<String, Object> statistics(@RequestParam String shopUuid) {
        Map<String, Object> result = moneyRecordService.statistics(shopUuid);
        Object money = result.get("money");
        result.put("money", money);
        result.put("everyMoney", MoneyUtils.fen2Yuan(result.get("everyMoney")));
        int buyedNum = Integer.parseInt(result.get("buyedNum").toString());
//        double everyMoney = MoneyUtils.fen2Yuan(money) / (buyedNum == 0 ? 1 : buyedNum);
        BigDecimal everyMoney = new BigDecimal(money.toString()).divide(BigDecimal.valueOf(buyedNum == 0 ? 1 : buyedNum), 3, BigDecimal.ROUND_FLOOR);
        result.put("everyMoney", everyMoney);
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "list")
    public PageInfo<ShopMoneyRecord> list(@RequestBody JSONObject json) {
        ParamsPojo pj = new ParamsPojo(json);
        Condition cond = new Condition(ShopMoneyRecord.class);
        Example.Criteria cri = cond.createCriteria();
        cri.andEqualTo("shopUuid", pj.getParams().get("shopUuid"));
        if (StringUtils.isNotNull(pj.getParams().get("type"))) {
            cri.andEqualTo("type", pj.getParams().get("type"));
        }

        cond.setOrderByClause(pj.getOrder());
        return moneyRecordService.selectPageInfoByCondition(cond, pj.getStart(), pj.getLength());
    }
}
