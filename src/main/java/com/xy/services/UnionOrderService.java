package com.xy.services;

import com.xy.models.UnionOrders;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by rjora on 2017/7/16 0016.
 */
public interface UnionOrderService extends BaseService<UnionOrders> {

    /**
     * 创建订单
     * @param userId 用户
     * @param shopId 商户
     * @param goodId 商品
     * @param num 数量
     * @param coupon 优惠卷编号
     * @return
     */
    UnionOrders saveSelective(String userId, String shopId, String goodId, int num, String coupon);

    /**
     * 支付，对支付宝和微信支付签名处理，返回支付信息
     * @param orderUuid
     * @param paywhy
     * @return
     */
    String payment(String orderUuid, String paywhy);

    /**
     * 支付宝异步回调通知验证
     * @param request
     * @return
     */
    String aliReceiveNotify(HttpServletRequest request);
}
