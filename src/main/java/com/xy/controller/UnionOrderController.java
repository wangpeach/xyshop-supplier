package com.xy.controller;

import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("order/")
public class UnionOrderController {

    @ResponseBody
    @RequestMapping(value = {"list", "mapi/list"})
    public List<Order> list() {

        return null;
    }

    @ResponseBody
    @RequestMapping("mapi/create")
    public Map<String, Object> createOrder(@RequestParam String uuid, @RequestParam String good, @RequestParam int num) {

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
