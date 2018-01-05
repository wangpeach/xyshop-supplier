package com.xy.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.xy.config.Config;
import com.xy.models.Shop;
import com.xy.models.UnionOrders;
import com.xy.pojo.ParamsPojo;
import com.xy.services.UnionOrderService;
import com.xy.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.StringUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
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
        ParamsPojo pj = new ParamsPojo(jsonObject);

        Condition cond = new Condition(UnionOrders.class);
        Example.Criteria cri = cond.createCriteria();

        String payWhy = pj.getParams().get("payWay"), status = pj.getParams().get("status"), start = pj.getParams().get("startTime"), end = pj.getParams().get("endTime");
        if (StringUtils.isNotNull(status)) {
            cri.andEqualTo("status", status);
        }
        if (StringUtils.isNotNull(payWhy)) {
            cri.andEqualTo("payWay", payWhy);
        }
        if (StringUtils.isNotNull(start)) {
            cri.andGreaterThanOrEqualTo("addTime", start);
        }
        if (StringUtils.isNotNull(end)) {
            cri.andLessThanOrEqualTo("addTime", end);
        }
        if (StringUtils.isNotNull(pj.getSearch())) {
            String[] cols = {"userName", "shopName", "orderNo", "cardCode"};
            String condition = " %s like ", arg = "'%" + pj.getSearch() + "%'";
            for (int i = 0; i < cols.length; i++) {
                cols[i] = String.format(condition, StringUtil.camelhumpToUnderline(cols[i])) + arg;
            }

            String or = org.apache.commons.lang3.StringUtils.join(cols, " or ");
            cri.andCondition("(" + or + ")");
        }

        cond.setOrderByClause(pj.getOrder());

        return orderService.selectPageInfoByCondition(cond, pj.getStart(), pj.getLength());
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
        switch (status) {
            case "waitConsume":
                cond.setOrderByClause(" pay_time desc");
                break;
            case "consumed":
                cond.setOrderByClause(" complete_time desc");
                break;
            case "waitPay":
            default:
                cond.setOrderByClause(" add_time desc");
                break;
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
     * @param order 订单 uuid
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
     *
     * @param order
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "mapi/wx-payment", produces = "application/json")
    public Map<String, String> wxPayment(@RequestParam String order, @RequestParam String ip) {
        Map<String, String> result = orderService.wxPayment(order, ip);
        return result;
    }


    /**
     * 微信异步回调通知
     *
     * @param request
     */
    @RequestMapping("wx_receive_notify")
    public void wxReceiveNotify(HttpServletRequest request, HttpServletResponse response) {
        String result = orderService.wxReceiveNotify(request);
        try {
            PrintWriter writer = response.getWriter();
            writer.print(result);
            writer.close();
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @RequestMapping(value = "mapi/coin-payment")
    public Map<String, Object> coinPayment(@RequestParam String order) {
        return orderService.coinPayment(order);
    }

    /**
     * 检查订单是否支付成功
     *
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


    /**
     * 订单核销查询
     *
     * @param cardcode
     * @param shop
     * @return
     */
    @ResponseBody
    @RequestMapping("search-consume")
    public UnionOrders searchConsume(@RequestParam String cardcode, @SessionAttribute("_loginshop_") Shop shop) {
        UnionOrders order = new UnionOrders();
        order.setShopUuid(shop.getUuid());
        order.setCardCode(cardcode);
        order.setStatus("waitConsume");
        order = orderService.selectOnly(order);
        return order;
    }

    /**
     * 核销订单
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("consume")
    public String consume(@RequestParam String uuid) {
        return orderService.modifyWriteOff(uuid);
    }


    @ResponseBody
    @RequestMapping(value = "charts", produces = "application/json;charset=UTF-8")
    public String chart(@RequestParam String type, @RequestParam Map<String, Object> params) {
        return orderService.charts(type, params);
    }


    @ResponseBody
    @RequestMapping(value = "payway-td-charts")
    public List<Map> paywayChart(@RequestParam String day) {
        return orderService.payTypeCencusOfToday(day);
    }
}
