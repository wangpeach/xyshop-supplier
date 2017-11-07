package com.xy.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.xy.config.Config;
import com.xy.models.UnionOrders;
import com.xy.services.UnionOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * UnionOrderController
 *
 * @author Administrator
 * @date 2017/10/27 14:51
 * @description
 */
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
    public PageInfo<UnionOrders> list(@RequestParam String user, @RequestParam String status, @RequestParam int page) {
        Condition cond = new Condition(UnionOrders.class);
        Example.Criteria cri = cond.createCriteria();
        cri.andEqualTo("userUuid", user);
        if (!"all".equals(status)) {
            cri.andEqualTo("status", status);
        }
        return orderService.selectPageInfoByCondition(cond, page, Config.LIMIT);
    }

    /**
     * 创建订单
     *
     * @param user   用户 uuid
     * @param shop   商户 uuid
     * @param good   商品 uuid
     * @param num    购买数量
     * @param coupon 使用的优惠卷 uuid
     * @return
     */
    @ResponseBody
    @RequestMapping("mapi/create")
    public UnionOrders createOrder(@RequestParam String user, @RequestParam String shop, @RequestParam String good, @RequestParam int num, @RequestParam String coupon) {
        return orderService.saveSelective(user, shop, good, num, coupon);
    }


    /**
     * 删除订单
     *
     * @param order 订单 uuid
     * @param user  用户 uuid
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "mapi/del", produces = "application/text")
    public String del(@RequestParam String order, @RequestParam String user) {
        Condition cond = new Condition(UnionOrders.class);
        cond.createCriteria().andEqualTo("uuid", order).andEqualTo("userUuid", user);
        if (orderService.deleteByCondition(cond) > 0) {
            return "success";
        }
        return "error";
    }

    /**
     * 支付
     *
     * @param order  订单 uuid
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "mapi/ali-payment", produces = "application/text")
    public String aliPayment(@RequestParam String order) {
        return orderService.aliPayment(order);
    }

    /**
     * 支付宝异步回调通知
     *
     * @param request
     */
    @RequestMapping("ali_receive_notify")
    public void aliReceiveNotify(HttpServletRequest request, HttpServletResponse response) {
        String result = orderService.aliReceiveNotify(request);
        try {
            PrintWriter writer = response.getWriter();
            writer.print(result);
            writer.close();
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 微信支付签名
     * @param order
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "mapi/wx-payment", produces = "application/text")
    public String wxPayment(@RequestParam String order) {
        return orderService.wxPayment(order);
    }


    /**
     * 微信异步回调通知
     *
     * @param request
     */
    @RequestMapping("wx_receive_notify")
    public void wxReceiveNotify(HttpServletRequest request, HttpServletResponse response) {

    }


    @RequestMapping(value = "mapi/coin-payment")
    public Map<String, Object> coinPayment(@RequestParam String order) {
        return orderService.coinPayment(order);
    }

    /**
     * 检查订单是否支付成功
     * @param key
     * @return
     */
    @RequestMapping(value = "mapi/check-paysuc", produces = "application/text")
    public String checkOrder(@RequestParam String key) {
        UnionOrders order = orderService.selectOnlyByKey(key);
        if ("waitConsume".equals(order.getStatus())) {
            return "success";
        }
        return "fail";
    }
}
