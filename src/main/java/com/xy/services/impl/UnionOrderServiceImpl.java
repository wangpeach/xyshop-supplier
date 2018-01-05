package com.xy.services.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayConstants;
import com.github.wxpay.sdk.WXPayUtil;
import com.google.gson.Gson;
import com.xy.config.AliPay;
import com.xy.config.WXConfig;
import com.xy.mapper.UnionOrdersMapper;
import com.xy.models.*;
import com.xy.redis.RedisUtil;
import com.xy.services.*;
import com.xy.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author rjora
 * @date 2017/7/16 0016
 */
@Service
public class UnionOrderServiceImpl extends BaseServiceImpl<UnionOrders> implements UnionOrderService {

    @Autowired
    private UnionOrdersMapper ordersMapper;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private ShopService shopService;

    @Autowired
    private UnionGoodService goodService;

    @Autowired
    private UserService userService;

    @Autowired
    private CouponService couponService;

    @Autowired
    private UserCouponService userCouponService;

    @Autowired
    private AliPaymentsService aliPaymentsService;

    @Autowired
    private WxPaymentService wxPaymentService;

    @Autowired
    private ShopWalletService shopWalletService;

    @Autowired
    private ShopMoneyRecordService shopMoneyRecordService;

    @Autowired
    private SystemParamsService systemParamsService;

    @Autowired
    private PlatformMoneyRecordService platformMoneyRecordService;

    @Override
    public PageInfo<UnionOrders> selectPageInfoByCondition(Condition condition, int offset, int limit) {
        PageInfo<UnionOrders> orders = super.selectPageInfoByCondition(condition, offset, limit);
        orders.setList(this.handleResult(orders.getList()));
        return orders;
    }


    @Override
    public UnionOrders selectOnly(UnionOrders entity) {
        return this.handleResult(super.selectOnly(entity));
    }

    @Override
    public UnionOrders saveSelective(String userId, String shopId, String goodId, int num, String coupon) {
        UnionOrders order = new UnionOrders();

        User user = userService.selectOnlyByKey(userId);
        Shop shop = shopService.selectOnlyByKey(shopId);
        UnionGoods entity = new UnionGoods();
        entity.setUuid(goodId);
        entity.setShopUuid(shopId);
        UnionGoods good = goodService.selectOnly(entity);

        if (good != null && shop != null && good != null) {

            order.setCoupon(coupon);

            order.setUuid(StringUtils.getUuid());
            order.setAddTime(DateUtils.getCurrentDate());

            order.setUserUuid(user.getUuid());
            order.setUserName(user.getName());
            order.setShopUuid(shop.getUuid());
            order.setShopName(shop.getName());
            order.setOrderNo(RandomUtil.getRandom(17, RandomUtil.TYPE.NUMBER));
            order.setGoodsUuid(entity.getUuid());
            order.setGoodsNum(num);
            order.setJudged(false);
            order.setStatus("waitPay");
            // 订单总金额，应付金额
            order.setTotalPrice(good.getPrice().multiply(BigDecimal.valueOf(num)));
            // 密码串码, 用户到店核销
            String cordCode = RandomUtil.getRandom(12, RandomUtil.TYPE.NUMBER);
            System.out.println("----------------------------------------------------------------" + cordCode);
            order.setCardCode(cordCode);


            if (StringUtils.isNotNull(order.getCoupon())) {
                UserCoupon userCoupon = userCouponService.selectOnlyByKey(order.getCoupon());
                order = this.cprole(order, userCoupon.getCoupon());
            } else {
                order = this.implicitCoupon(user, shop, good, order);
            }

            // 用户支付后更改支付方式
            order.setPayWay(null);

            if (super.saveSelective(order) > 0) {
                return order;
            }
        }
        return null;
    }


    /**
     * 查询隐式优惠卷
     * 商户处于活动期并且该订单有可用的商户优惠卷，则使用商户的优惠卷。否则使用系统发放的优惠卷
     *
     * @param user
     * @param shop
     * @param order
     */
    private UnionOrders implicitCoupon(User user, Shop shop, UnionGoods good, UnionOrders order) {
        Coupon coupon = null;
        // 如果商户处于促销活动期间，则查询商户发布的优惠卷信息
        if ("Y".equals(shop.getActive())) {
            // 查询满足使用条件的优惠卷
            coupon = userCouponService.selectShopByOrder(user, shop, good, order);
        }
        // 该订单无法使用商户发布的优惠卷
        if (coupon == null) {
            //查询满足使用条件的官方优惠卷
            coupon = userCouponService.selectOfficialByOrder(user, good, order);
        }
        if (coupon != null) {
            // 检查优惠卷单用户使用次数限制
            UnionOrders entity = new UnionOrders();
            entity.setUserUuid(user.getUuid());
            entity.setCoupon(coupon.getUuid());
            int used = super.count(entity);
            if (used < coupon.getUserMaxNum()) {
                order.setSysTips(coupon.getNumber() + ": " + coupon.getName());
            }
            // 应支付金额
            order = this.cprole(order, coupon);
        } else {
            order.setPayPrice(order.getTotalPrice());
        }
        return order;
    }


    /**
     * 计算订单应支付金额
     *
     * @param order
     * @param coupon
     * @return
     */
    private UnionOrders cprole(UnionOrders order, Coupon coupon) {
        // 支付金额
        BigDecimal payAmount = BigDecimal.ZERO;
        if (coupon != null) {
            BigDecimal discount = BigDecimal.ZERO;
            switch (coupon.getRule()) {
                case "discount":
                    discount = new BigDecimal(coupon.getRuleValue());
                    // 折扣
                    discount = discount.divide(BigDecimal.valueOf(100));
                    // 应付金额
                    payAmount = order.getTotalPrice().subtract(order.getTotalPrice().multiply(discount));
                    break;
                case "fulldown":
                    discount = new BigDecimal(coupon.getRuleValue());
                    // 满减
                    payAmount = order.getTotalPrice().subtract(discount);
                    break;
                default:
                    // recoupon 返券，需要等用户消费成功才可以发放优惠卷
                    payAmount = order.getTotalPrice();
                    break;
            }
            order.setCoupon(coupon.getUuid());
            order.setPreferentialPrice(coupon.getRuleValue());
        } else {
            payAmount = order.getTotalPrice();
        }
        order.setPayPrice(payAmount);
        return order;
    }


    @Override
    public String aliPayment(String orderUuid) {
        String result = null;
        UnionOrders order = super.selectOnlyByKey(orderUuid);
        UnionGoods good = goodService.selectOnlyByKey(order.getGoodsUuid());
        result = AliPay.getInstance().createOrderString(order.getOrderNo(), good.getName(), order.getShopName() + ":" + order.getOrderNo(), order.getPayPrice().toString(), order.getUserUuid());
        return result;
    }


    @Override
    public String aliReceiveNotify(HttpServletRequest request) {
        String result = "error";
        Map<String, String> params = new HashMap<>();
        Map requestParams = request.getParameterMap();

        System.out.println(requestParams);

        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用。
            //valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            params.put(name, valueStr);
        }

        boolean flag = AliPay.getInstance().rsaCheck(params);
        if (flag) {
            AliPayments payments = new AliPayments(params);

            String finished = "TRADE_FINISHED", success = "TRADE_SUCCESS";
            if (success.equals(payments.getTradeStatus()) || finished.equals(payments.getTradeStatus())) {
                UnionOrders order = new UnionOrders();
                order.setOrderNo(payments.getOutTradeNo());
                // 签名支付信息时 传入了自定义信息，携带了用户uuid，所以在这里可以取到
                order.setUserUuid(payments.getPassbackParams());
                order = super.selectOnly(order);
                // 验证支付金额是否于订单金额相等，和是否为商户本身(对于支付宝角度而言)
                if (order.getPayPrice().compareTo(new BigDecimal(payments.getTotalAmount())) == 0 && payments.getAppId().equals(AliPay.ali_appid)) {
                    order.setStatus("waitConsume");
                    order.setPayWay("alipay");
                    order.setPayTime(DateUtils.getCurrentDate());
                    super.updateByPrimaryKeySelective(order);
                    // 保存支付宝同步支付通知信息
                    aliPaymentsService.saveSelective(payments);
                    User buyer = userService.selectOnlyByKey(order.getUserUuid());

                    userCouponService.updateCouponExpend(order.getCoupon());

                    this.sendPaySucMsg(order, buyer.getPhoneNum());
                    result = "success";
                }
            }
        }
        return result;
    }


    @Override
    public Map<String, String> wxPayment(String orderUuid, String ip) {
        Map<String, String> result = null;
        Map<String, String> appArgs = new HashMap<>();
        try {
            UnionOrders order = this.selectOnlyByKey(orderUuid);
            UnionGoods good = goodService.selectOnlyByKey(order.getGoodsUuid());

            WXConfig wxConfig = new WXConfig();
            WXPay wxPay = new WXPay(wxConfig);
            Map<String, String> paydata = new HashMap<>();
            paydata.put("attach", order.getUserUuid());
            paydata.put("body", order.getShopName() + ":" + order.getOrderNo());
//            paydata.put("body", good.getName());
            paydata.put("out_trade_no", order.getOrderNo());
            paydata.put("total_fee", String.valueOf(MoneyUtils.yuan2Fen(order.getPayPrice().doubleValue())));
            paydata.put("spbill_create_ip", ip);
            paydata.put("notify_url", WXConfig.notifyUrl);
            paydata.put("trade_type", "APP");
            result = wxPay.unifiedOrder(paydata);


            appArgs.put("appid", wxConfig.getAppID());
            appArgs.put("partnerid", wxConfig.getMchID());
            appArgs.put("prepayid", result.get("prepay_id"));
            appArgs.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
            appArgs.put("noncestr", WXPayUtil.generateNonceStr());
            appArgs.put("package", "Sign=WXPay");
            String sign = WXPayUtil.generateSignature(appArgs, wxConfig.getKey(), WXPayConstants.SignType.MD5);
            appArgs.put("sign", sign);
            System.out.println(new Gson().toJson(appArgs));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return appArgs;
    }


    @Override
    public String wxReceiveNotify(HttpServletRequest request) {
        String notifyXml = "";
        Map<String, String> notifyWx = new HashMap<>();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream(), "utf-8"));
            String buffer = null;
            StringBuffer sbr = new StringBuffer();
            while ((buffer = br.readLine()) != null) {
                sbr.append(buffer);
            }
            WXPay wxPay = new WXPay(new WXConfig());
            Map<String, String> notifyMap = WXPayUtil.xmlToMap(sbr.toString());

            System.out.println(notifyMap);

            if (wxPay.isPayResultNotifySignatureValid(notifyMap) && notifyMap.get("return_code").equals("SUCCESS")) {
                // 验证成功
                WXPayments wxPayments = new WXPayments(notifyMap);
                UnionOrders order = new UnionOrders();
                order.setOrderNo(wxPayments.getOutTradeNo());
                // 下单时保存用户ID在 attach 里，所以可以直接取到
                order.setUserUuid(wxPayments.getAttach());
                order = this.selectOnly(order);

                // 验证成功 金额匹配
                if (wxPayments.getTotalFee().toString().equals(String.valueOf(MoneyUtils.yuan2Fen(order.getPayPrice().doubleValue())))) {
                    order.setStatus("waitConsume");
                    order.setPayWay("wxpay");
                    order.setPayTime(DateUtils.getCurrentDate());
                    super.updateByPrimaryKeySelective(order);
                    // 保存微信同步支付通知信息
                    wxPaymentService.saveSelective(wxPayments);
                    User buyer = userService.selectOnlyByKey(order.getUserUuid());
                    this.sendPaySucMsg(order, buyer.getPhoneNum());

                    notifyMap.put("return_code", "<![CDATA[SUCCESS]]>");
                    notifyMap.put("return_msg", "<![CDATA[OK]]>");
                }

                wxPaymentService.saveSelective(wxPayments);

                userCouponService.updateCouponExpend(order.getCoupon());
            } else {
                notifyMap.put("return_code", "FAIL");
                notifyMap.put("return_msg", "签名失败,参数格式校验错误");
            }
            notifyXml = WXPayUtil.mapToXml(notifyMap);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return notifyXml;
    }

    @Override
    public Map<String, Object> coinPayment(String orderUuid) {
        Map<String, Object> result = new HashMap<>();

        UnionOrders order = super.selectOnlyByKey(orderUuid);
        User buyer = userService.selectOnlyByKey(order.getUserUuid());
        if (buyer.getCoin().compareTo(order.getPayPrice()) >= 0) {

            order.setStatus("waitConsume");
            order.setPayWay("coin");
            order.setPayTime(DateUtils.getCurrentDate());

            int affect = super.updateByPrimaryKeySelective(order);
            if (affect < 1) {
                result.put("status", "error");
                result.put("tips", "出现内部错误");
                return result;
            }

            User other = new User();
            other.setUuid(buyer.getUuid());
            other.setCoin(buyer.getCoin().subtract(order.getPayPrice()));
            affect = userService.updateByPrimaryKeySelective(other);
            if (affect < 1) {
                if (affect < 1) {
                    result.put("status", "error");
                    result.put("tips", "出现内部错误");
                    return result;
                }
            }

            result.put("status", "success");

            userCouponService.updateCouponExpend(order.getCoupon());

            this.sendPaySucMsg(order, buyer.getPhoneNum());
            return result;
        } else {
            result.put("status", "error");
            result.put("tips", "金币不足");
        }
        return result;
    }


    public void sendPaySucMsg(UnionOrders order, String phoneNum) {
        // 发送支付成功短信
        SystemParams sps = redisUtil.getSysParams("payok_sendmsg").get(0);
        if (sps.getParamValue().equals("Y")) {
            UnionGoods good = goodService.selectOnlyByKey(order.getGoodsUuid());
            Map<String, String> params = new HashMap<>();
            params.put("date", DateUtils.getCurrentDate());
            params.put("shop", order.getShopName());
            params.put("name", good.getName());
            params.put("number", order.getCardCode());
            new SmsUtil().sendTempSms(phoneNum, params);
        }
    }


    @Override
    public String modifyWriteOff(String orderUuid) {
        UnionOrders orders = new UnionOrders();
        orders.setUuid(orderUuid);
        orders.setStatus("waitConsume");
        orders = this.selectOnly(orders);
        if (orders != null) {
            UnionOrders over = new UnionOrders();
            over.setUuid(orderUuid);
            over.setStatus("consumed");
            over.setCompleteTime(DateUtils.getCurrentDate());
            int result = super.updateByPrimaryKeySelective(over);
            if (result > 0) {
                Shop shop = shopService.selectOnlyByKey(orders.getShopUuid());
                // 优惠卷处理
                Coupon coupon = couponService.selectOnlyByKey(orders.getCoupon());
                // 赠送优惠卷
                String recoupon = "recoupon";
                if (recoupon.equals(coupon.getRule())) {
                    userCouponService.giveCoupon(orders.getUserUuid(), coupon.getRuleValue());
                }

                // 平台收支信息
                PlatformMoneyRecord platformMoney = new PlatformMoneyRecord();
                platformMoney.setTotalMoney(orders.getTotalPrice());
                platformMoney.setPayMoney(orders.getPayPrice());
                platformMoney.setOrderuuid(orders.getUuid());
                platformMoney.setShop(shop.getUuid());
                platformMoney.setShopName(shop.getName());

                // 更新商铺余额
                ShopWallet wallet = new ShopWallet();
                wallet.setShopUuid(orders.getShopUuid());
                wallet = shopWalletService.selectOnly(wallet);

                ShopWallet updWallet = new ShopWallet();

                /**
                 * 优惠金额承担方，商家承担则按实际支付金额给商家结算，运营商承担则按订单金额结算
                 */
                BigDecimal shopMoney = BigDecimal.ZERO;
                // 平台收如金额
                BigDecimal platformIncomeMoney = BigDecimal.ZERO;
                // 如果商户有独立结算比例则使用商户的，不存在则使用全局结算比例
                BigDecimal scale = BigDecimal.ZERO;
                if (shop.getScale() != null) {
                    scale = shop.getScale();
                } else {
                    SystemParams sp = new SystemParams();
                    sp.setParamKey("clear_scale");
                    sp = systemParamsService.selectOnly(sp);
                    scale = BigDecimal.valueOf(Double.parseDouble(sp.getParamValue()));
                }

                if ("supplier".equals(coupon.getBearParty())) {
                    // 商家承担优惠费用
                    BigDecimal balance = orders.getPayPrice().multiply(scale);
                    shopMoney = wallet.getMoney().add(balance);
                    platformIncomeMoney = orders.getPayPrice().subtract(balance);
                } else {
                    // 平台支出
                    platformMoney.setMoney(orders.getTotalPrice().subtract(orders.getPayPrice()));
                    platformMoney.setType("expend");
                    platformMoney.setCreatetime(DateUtils.getCurrentDate());
                    platformMoney.setRemark("存有优惠信息，平台承担优惠金额");
                    platformMoney.setUuid(StringUtils.getUuid());
                    platformMoneyRecordService.saveSelective(platformMoney);

                    // 平台承担优惠费用
                    BigDecimal balance = orders.getTotalPrice().multiply(scale);
                    shopMoney = wallet.getMoney().add(balance);
                    platformIncomeMoney = orders.getTotalPrice().subtract(balance);
                }
                updWallet.setMoney(shopMoney);
                Condition cond = new Condition(ShopWallet.class);
                cond.createCriteria().andEqualTo("uuid", wallet.getUuid()).andEqualTo("shopUuid", wallet.getShopUuid());
                shopWalletService.updateByConditionSelective(updWallet, cond);


                platformMoney.setMoney(platformIncomeMoney);
                platformMoney.setType("income");
                platformMoney.setCreatetime(DateUtils.getCurrentDate());
                platformMoney.setRemark("平台应得商户结算后金额");
                platformMoney.setScale(scale);
                platformMoney.setUuid(StringUtils.getUuid());
                platformMoneyRecordService.saveSelective(platformMoney);

                // 商家收入记录
                ShopMoneyRecord moneyRecord = new ShopMoneyRecord();
                moneyRecord.setUuid(StringUtils.getUuid());
                moneyRecord.setShopUuid(orders.getShopUuid());
                moneyRecord.setShopName(orders.getShopName());
                moneyRecord.setMoney(orders.getPayPrice());
                moneyRecord.setLeftMoney(updWallet.getMoney());
                moneyRecord.setType("income");
                moneyRecord.setStatus("success");
                moneyRecord.setRemarks("会员(" + orders.getUserName() + ")购买" + orders.getGood().getName() + ",数量:" + orders.getGoodsNum());
                moneyRecord.setAddTime(DateUtils.getCurrentDate());
                moneyRecord.setOperateTime(DateUtils.getCurrentDate());
                moneyRecord.setPayType(orders.getPayWay());
                moneyRecord.setUnionOrderUuid(orders.getUuid());
                shopMoneyRecordService.saveSelective(moneyRecord);
            } else {
                return "error";
            }
        } else {
            return "null";
        }
        return "success";
    }

    @Override
    public String charts(String type, Map<String, Object> params) {
        String result = null;
        switch (type) {
            case "M":
                params.get("year");
                break;
            case "W":
                result = this.weekCount();
                break;
            case "Custom":
                params.get("start");
                params.get("end");
                break;
            default:
                break;
        }
        return result;
    }

    /**
     * 按月统计
     *
     * @return
     */
    public String monthCount(String year) {
        Map<String, Object> result = new HashMap<>();

        String json = new Gson().toJson(result);
        return json;
    }

    /**
     * 统计今七天销量
     *
     * @return
     */
    public String weekCount() {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> titleMap = new HashMap<>();
        titleMap.put("subtext", "近七日销量");
        Map<String, Object> legendMap = new HashMap<>();
        legendMap.put("data", new String[]{"支付完成", "订单完成"});
        result.put("title", titleMap);
        result.put("legend", legendMap);

        List<Map<String, Object>> series = new ArrayList<>();
        Map<String, Object> payover = new HashMap<>();
        payover.put("name", "支付完成");
        payover.put("type", "bar");
        Map<String, Object> orderover = new HashMap<>();
        orderover.put("name", "订单完成");
        orderover.put("type", "bar");

        result.put("tooltip", new Object());

        List<String> xAxisData = new ArrayList<>();

        List<Integer> payOverData = new ArrayList<>();
        List<Integer> orderOverData = new ArrayList<>();

        for (int i = 6; i >= 0; i--) {
            String time = DateUtils.format(DateUtils.getCurrentOffsetDay(-i));
            xAxisData.add(time);


            Example example = new Example(UnionOrders.class);

            example.createCriteria().andGreaterThanOrEqualTo("payTime", time + " 00:00:00").andLessThanOrEqualTo("payTime", time + " 23:59:59").andEqualTo("status", "waitConsume");
            payOverData.add(super.count(example));

            example.clear();
            example.createCriteria().andGreaterThanOrEqualTo("completeTime", time + " 00:00:00").andLessThanOrEqualTo("completeTime", time + " 23:59:59").andEqualTo("status", "consumed");
            orderOverData.add(super.count(example));
        }
        Map<String, Object> xaxisMap = new HashMap<>();
        xaxisMap.put("data", xAxisData);
        result.put("xAxis", xaxisMap);

        result.put("yAxis", new Object());

        payover.put("data", payOverData);
        orderover.put("data", orderOverData);
        series.add(payover);
        series.add(orderover);
        result.put("series", series);
        String json = new Gson().toJson(result);
        return json;
    }

    /**
     * 自定义统计
     *
     * @param start
     * @param end
     * @return
     */
    private String customCount(String start, String end) {
        Map<String, Object> result = new HashMap<>();

        String json = new Gson().toJson(result);
        return json;
    }


    @Override
    public List<UnionOrders> handleResult(List<UnionOrders> args) {
        if (args == null || args.size() == 0) {
            return args;
        }

        List<String> gids = args.stream().map(UnionOrders::getGoodsUuid).collect(Collectors.toList());

        Condition cond = new Condition(UnionGoods.class);
        cond.createCriteria().andIn("uuid", gids);
        List<UnionGoods> goods = goodService.selectListByCondition(cond);

        args.forEach(unionOrders -> {
            String text = "";
            switch (unionOrders.getStatus()) {
                case "waitPay":
                    text = "待支付";
                    break;
                case "paySuccess":
                    text = "支付成功";
                    break;
                case "payFail":
                    text = "支付失败";
                    break;
                case "waitConsume":
                    text = "待使用";
                    break;
                case "consumed":
                    text = "已使用";
                    break;
                default:
                    text = "已退款";
                    break;
            }
            unionOrders.setTextStatus(text);
            unionOrders.setCardCode(StringUtils.splitWithChar(unionOrders.getCardCode(), 4, ' '));


            if (goods != null && goods.size() > 0) {
                UnionGoods item = goods.stream().filter(unionGoods -> unionOrders.getGoodsUuid().equals(unionGoods.getUuid())).findAny().get();
                unionOrders.setGood(item);
            }
        });
        return args;
    }


    @Override
    public UnionOrders handleResult(UnionOrders arg) {
        String text = "";
        if (arg == null) {
            return null;
        }
        switch (arg.getStatus()) {
            case "waitPay":
                text = "待支付";
                break;
            case "paySuccess":
                text = "支付成功";
                break;
            case "payFail":
                text = "支付失败";
                break;
            case "waitConsume":
                text = "待使用";
                break;
            case "consumed":
                text = "已使用";
                break;
            default:
                text = "已退款";
                break;
        }
        arg.setTextStatus(text);
        arg.setCardCode(StringUtils.splitWithChar(arg.getCardCode(), 4, ' '));
        UnionGoods good = goodService.selectOnlyByKey(arg.getGoodsUuid());
        arg.setGood(good);
        return arg;
    }

    @Override
    public List<Map> payTypeCencusOfToday(String day) {
        Map<String, Object> params = new HashMap<>();
        if (StringUtils.isNull(day)) {
            day = DateUtils.getDate();
        }
        params.put("start", day + " 00:00:00");
        params.put("end", day + " 23:59:59");
        return ordersMapper.payTypeCencusOfToday(params);
    }
}

