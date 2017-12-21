package com.xy.services;

import com.xy.models.UnionOrders;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 *
 * @author rjora
 * @date 2017/7/16 0016
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
     * 支付，对支付宝支付签名处理，返回支付信息
     * @param orderUuid
     * @return
     */
    String aliPayment(String orderUuid);

    /**
     * 支付宝异步回调通知验证
     * @param request
     * @return
     */
    String aliReceiveNotify(HttpServletRequest request);

    /**
     * 支付，对微信支付签名处理，返回支付信息
     * @param orderUuid
     * @return
     */
    Map<String, String> wxPayment(String orderUuid);

    /**
     *微信异步回调通知验证
     * @param request
     * @return
     */
    String wxReceiveNotify(HttpServletRequest request);

    /**
     * 金币支付
     * @param orderUuid
     * @return
     */
    Map<String, Object> coinPayment(String orderUuid);


    /**
     * 订单核销
     * @param orderUuid
     * @return
     */
    String modifyWriteOff(String orderUuid);

    /**
     * 订单统计
     * @param type M, W, Custom
     *             年，月，近七日，自定义
     * @param params type=M : year 年份
     *                   =W : 没有参数，默认近七天数据
     *                   =Custom : 自定义选择
     *
     * @return JSON 格式
     */
    String charts(String type, Map<String, Object> params);

    /**
     * 某日订单支付类型统计
     * @param day
     * @return
     */
    public List<Map> payTypeCencusOfToday(String day);
}
