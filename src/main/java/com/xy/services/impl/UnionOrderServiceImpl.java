package com.xy.services.impl;

import com.github.pagehelper.PageInfo;
import com.xy.config.AliPay;
import com.xy.models.*;
import com.xy.services.*;
import com.xy.utils.DateUtils;
import com.xy.utils.RandomUtil;
import com.xy.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Condition;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by rjora on 2017/7/16 0016.
 */
@Service
public class UnionOrderServiceImpl extends BaseServiceImpl<UnionOrders> implements UnionOrderService {

    @Autowired
    private ShopService shopService;

    @Autowired
    private UnionGoodService goodService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserCouponService couponService;

    @Autowired
    private AliPaymentsService aliPaymentsService;

    @Override
    public PageInfo<UnionOrders> selectPageInfoByCondition(Condition condition, int offset, int limit) {
        PageInfo<UnionOrders> orders = super.selectPageInfoByCondition(condition, offset, limit);

        List<String> gids = orders.getList().stream().map(UnionOrders::getGoodsUuid).collect(Collectors.toList());
        if (gids != null && gids.size() > 0) {
            Condition cond = new Condition(UnionGoods.class);
            cond.createCriteria().andIn("uuid", gids);
            List<UnionGoods> goods = goodService.selectListByCondition(cond);
            orders.getList().forEach(unionOrders -> {
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
                UnionGoods item = goods.stream().filter(unionGoods -> unionOrders.getGoodsUuid().equals(unionGoods.getUuid())).findAny().get();
                unionOrders.setGood(item);
            });
        }
        return orders;
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
            order.setCardCode(StringUtils.splitWithChar(RandomUtil.getRandom(12, RandomUtil.TYPE.NUMBER), 4, ' '));


            if (StringUtils.isNotNull(order.getCoupon())) {
                UserCoupon userCoupon = couponService.selectOnlyByKey(order.getCoupon());
                order = this.cprole(order, userCoupon.getCoupon());
            } else {
                order = this.implicitCoupon(user, shop, order);
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
    private UnionOrders implicitCoupon(User user, Shop shop, UnionOrders order) {
        Coupon coupon = null;
        // 如果商户处于促销活动期间，则查询商户发布的优惠卷信息
        if (shop.isActive()) {
            // 查询满足使用条件的优惠卷
            coupon = couponService.selectShopByOrder(user, shop, order);
            if (coupon != null) {
                order.setSysTips(coupon.getName());
            }
        }
        // 该订单无法使用商户发布的优惠卷
        if (coupon == null) {
            //查询满足使用条件的官方优惠卷
            coupon = couponService.selectOfficialByOrder(user, order);
            if (coupon != null) {
                order.setSysTips(coupon.getName());
            }
        }

        // 应支付金额
        order = this.cprole(order, coupon);
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
            order.setSysTips(coupon.getName());

            BigDecimal discount = new BigDecimal(coupon.getRuleValue());
            switch (coupon.getRule()) {
                case "discount":
                    // 折扣
                    discount = discount.divide(BigDecimal.valueOf(100));
                    // 应付金额
                    payAmount = order.getTotalPrice().subtract(order.getTotalPrice().multiply(discount));
                    break;
                case "fulldown":
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
    public String payment(String orderUuid, String paywhy) {
        String result = null;
        UnionOrders order = super.selectOnlyByKey(orderUuid);
        UnionGoods good = goodService.selectOnlyByKey(order.getGoodsUuid());
        switch (paywhy) {
            case "wxpay":
                break;
            case "alpay":
                result = AliPay.getInstance().createOrderString(order.getOrderNo(), good.getName(), order.getShopName() + ":" + order.getOrderNo(), order.getPayPrice().toString(), order.getUserUuid());
                break;
            case "gold":
                break;
            default:
                result = "error:支付类型无效";
                break;
        }
        return result;
    }


    @Override
    public String aliReceiveNotify(HttpServletRequest request) {
        String result = "error";
        Map<String, String> params = new HashMap<>();
        Map requestParams = request.getParameterMap();
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
                payments.getOutTradeNo();
                UnionOrders order = new UnionOrders();
                order.setOrderNo(payments.getOutTradeNo());
                // 签名支付信息时 传入了自定义信息，携带了用户uuid，所以在这里可以取到
                order.setUserUuid(payments.getPassbackParams());
                order = super.selectOnly(order);
                // 验证支付金额是否于订单金额相等，和是否为商户本身(对于支付宝角度而言)
                if (order.getPayPrice().compareTo(new BigDecimal(payments.getTotalAmount())) == 0 && payments.getAppId().equals(AliPay.ali_appid)) {
                    order.setStatus("waitConsume");
                    order.setPayWay("alipay");
                    super.updateByPrimaryKeySelective(order);
                    aliPaymentsService.saveSelective(payments);
                    result = "success";
                }
            }
        }
        return result;
    }
}
