package com.xy.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.xy.models.UnionOrders;
import com.xy.services.UnionOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("order/")
public class UnionOrderController {

    @Autowired
    private UnionOrderService orderService;

    @ResponseBody
    @RequestMapping(value = {"list"})
    public PageInfo<UnionOrders> list(@RequestBody JSONObject jsonObject) {

        return null;
    }

    @ResponseBody
    @RequestMapping(value = "mapi/list")
    public List<UnionOrders> list(@RequestParam String user) {

        return null;
    }

    @ResponseBody
    @RequestMapping("mapi/create")
    public Map<String, Object> createOrder(@RequestParam String user, @RequestParam String shop, @RequestParam String good, @RequestParam int num) {
        orderService.saveSelective(user, shop, good, num);
        return null;
    }

    @ResponseBody
    @RequestMapping("mapi/del")
    public String del(@RequestParam String order, @RequestParam String user) {

        return "success";
    }

    /**
     * 支付
     *
     * @param orderNo
     * @param paywhy
     * @param coupon
     * @return
     */
    @RequestMapping("mapi/payment")
    public String payment(@RequestParam String orderNo, @RequestParam String paywhy, @RequestParam String coupon) {

        return null;
    }
}
