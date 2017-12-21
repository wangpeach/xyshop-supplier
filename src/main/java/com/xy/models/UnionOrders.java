package com.xy.models;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.math.BigDecimal;

@Table(name = "union_orders")
public class UnionOrders {
    @Id
    private String uuid;

    /**
     * 用户UUID
     */
    @Column(name = "user_uuid")
    private String userUuid;

    /**
     * 用户名
     */
    @Column(name = "user_name")
    private String userName;

    /**
     * 商家UUID
     */
    @Column(name = "shop_uuid")
    private String shopUuid;

    /**
     * 商家名称
     */
    @Column(name = "shop_name")
    private String shopName;

    /**
     * 订单号
     */
    @Column(name = "order_no")
    private String orderNo;

    /**
     * 订单总金额
     */
    @Column(name = "total_price")
    private BigDecimal totalPrice;

    /**
     * 支付金额
     */
    @Column(name = "pay_price")
    private BigDecimal payPrice;

    /**
     * 支付方式：coin(金币支付)、weixin（微信支付）、alipay（支付宝）
     */
    @Column(name = "pay_way")
    private String payWay;

    /**
     * 订单状态：waitPay(待支付)、paySuccess(支付成功)、payFail(支付失败)、waitConsume(待使用)、consumed(已使用)、refunded(已退款)
     */
    private String status;

    /**
     * 录入时间
     */
    @Column(name = "add_time")
    private String addTime;

    /**
     * 支付时间
     */
    @Column(name = "pay_time")
    private String payTime;

    /**
     * 核销时间
     */
    @Column(name = "complete_time")
    private String completeTime;

    /**
     * 产品UUID
     */
    @Column(name = "goods_uuid")
    private String goodsUuid;

    /**
     * 购买数量
     */
    @Column(name = "goods_num")
    private Integer goodsNum;

    /**
     * 是否已评论
     */
    private Boolean judged;

    /**
     * 密码串码，用于核销
     */
    @Column(name = "card_code")
    private String cardCode;

    /**
     * 优惠卷标识
     */
    private String coupon;

    @Column(name = "sys_tips")
    private String sysTips;

    /**
     * 优惠金额
     */
    @Column(name = "preferential_price")
    private String preferentialPrice;

    /**
     * 外部支付编号 支付宝，微信
     */
    @Column(name = "out_trade_no")
    private String outTradeNo;



    @Transient
    private UnionGoods good;

    @Transient
    private String textStatus;

    /**
     * @return uuid
     */
    public String getUuid() {
        return uuid;
    }

    /**
     * @param uuid
     */
    public void setUuid(String uuid) {
        this.uuid = uuid == null ? null : uuid.trim();
    }

    /**
     * 获取用户UUID
     *
     * @return user_uuid - 用户UUID
     */
    public String getUserUuid() {
        return userUuid;
    }

    /**
     * 设置用户UUID
     *
     * @param userUuid 用户UUID
     */
    public void setUserUuid(String userUuid) {
        this.userUuid = userUuid == null ? null : userUuid.trim();
    }

    /**
     * 获取用户名
     *
     * @return user_name - 用户名
     */
    public String getUserName() {
        return userName;
    }

    /**
     * 设置用户名
     *
     * @param userName 用户名
     */
    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    /**
     * 获取商家UUID
     *
     * @return shop_uuid - 商家UUID
     */
    public String getShopUuid() {
        return shopUuid;
    }

    /**
     * 设置商家UUID
     *
     * @param shopUuid 商家UUID
     */
    public void setShopUuid(String shopUuid) {
        this.shopUuid = shopUuid == null ? null : shopUuid.trim();
    }

    /**
     * 获取商家名称
     *
     * @return shop_name - 商家名称
     */
    public String getShopName() {
        return shopName;
    }

    /**
     * 设置商家名称
     *
     * @param shopName 商家名称
     */
    public void setShopName(String shopName) {
        this.shopName = shopName == null ? null : shopName.trim();
    }

    /**
     * 获取订单号
     *
     * @return order_no - 订单号
     */
    public String getOrderNo() {
        return orderNo;
    }

    /**
     * 设置订单号
     *
     * @param orderNo 订单号
     */
    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo == null ? null : orderNo.trim();
    }

    /**
     * 获取订单总金额
     *
     * @return total_price - 订单总金额
     */
    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    /**
     * 设置订单总金额
     *
     * @param totalPrice 订单总金额
     */
    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    /**
     * 获取支付金额
     *
     * @return pay_price - 支付金额
     */
    public BigDecimal getPayPrice() {
        return payPrice;
    }

    /**
     * 设置支付金额
     *
     * @param payPrice 支付金额
     */
    public void setPayPrice(BigDecimal payPrice) {
        this.payPrice = payPrice;
    }

    /**
     * 获取支付方式：coin(金币支付)、weixin（微信支付）、alipay（支付宝）
     *
     * @return pay_way - 支付方式：coin(金币支付)、weixin（微信支付）、alipay（支付宝）
     */
    public String getPayWay() {
        return payWay;
    }

    /**
     * 设置支付方式：coin(金币支付)、weixin（微信支付）、alipay（支付宝）
     *
     * @param payWay 支付方式：coin(金币支付)、weixin（微信支付）、alipay（支付宝）
     */
    public void setPayWay(String payWay) {
        this.payWay = payWay == null ? null : payWay.trim();
    }

    /**
     * 获取订单状态：waitPay(待支付)、paySuccess(支付成功)、payFail(支付失败)、waitConsume(待使用)、consumed(已使用)、refunded(已退款)
     *
     * @return status - 订单状态：waitPay(待支付)、paySuccess(支付成功)、payFail(支付失败)、waitConsume(待使用)、consumed(已使用)、refunded(已退款)
     */
    public String getStatus() {
        return status;
    }

    /**
     * 设置订单状态：waitPay(待支付)、paySuccess(支付成功)、payFail(支付失败)、waitConsume(待使用)、consumed(已使用)、refunded(已退款)
     *
     * @param status 订单状态：waitPay(待支付)、paySuccess(支付成功)、payFail(支付失败)、waitConsume(待使用)、consumed(已使用)、refunded(已退款)
     */
    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    /**
     * 获取录入时间
     *
     * @return add_time - 录入时间
     */
    public String getAddTime() {
        return addTime;
    }

    /**
     * 设置录入时间
     *
     * @param addTime 录入时间
     */
    public void setAddTime(String addTime) {
        this.addTime = addTime == null ? null : addTime.trim();
    }

    /**
     * 获取产品UUID
     *
     * @return goods_uuid - 产品UUID
     */
    public String getGoodsUuid() {
        return goodsUuid;
    }

    /**
     * 设置产品UUID
     *
     * @param goodsUuid 产品UUID
     */
    public void setGoodsUuid(String goodsUuid) {
        this.goodsUuid = goodsUuid == null ? null : goodsUuid.trim();
    }

    /**
     * 获取购买数量
     *
     * @return goods_num - 购买数量
     */
    public Integer getGoodsNum() {
        return goodsNum;
    }

    /**
     * 设置购买数量
     *
     * @param goodsNum 购买数量
     */
    public void setGoodsNum(Integer goodsNum) {
        this.goodsNum = goodsNum;
    }

    /**
     * 获取是否已评论
     *
     * @return judged - 是否已评论
     */
    public Boolean getJudged() {
        return judged;
    }

    /**
     * 设置是否已评论
     *
     * @param judged 是否已评论
     */
    public void setJudged(Boolean judged) {
        this.judged = judged;
    }

    /**
     * 获取密码串码，用于核销
     *
     * @return card_code - 密码串码，用于核销
     */
    public String getCardCode() {
        return cardCode;
    }

    /**
     * 设置密码串码，用于核销
     *
     * @param cardCode 密码串码，用于核销
     */
    public void setCardCode(String cardCode) {
        this.cardCode = cardCode == null ? null : cardCode.trim();
    }

    /**
     * 获取优惠卷标识
     *
     * @return coupon - 优惠卷标识
     */
    public String getCoupon() {
        return coupon;
    }

    /**
     * 设置优惠卷标识
     *
     * @param coupon 优惠卷标识
     */
    public void setCoupon(String coupon) {
        this.coupon = coupon == null ? null : coupon.trim();
    }

    /**
     * 获取优惠金额
     *
     * @return preferential_price - 优惠金额
     */
    public String getPreferentialPrice() {
        return preferentialPrice;
    }

    /**
     * 设置优惠金额
     *
     * @param preferentialPrice 优惠金额
     */
    public void setPreferentialPrice(String preferentialPrice) {
        this.preferentialPrice = preferentialPrice;
    }

    public String getPayTime() {
        return payTime;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }

    public String getCompleteTime() {
        return completeTime;
    }

    public void setCompleteTime(String completeTime) {
        this.completeTime = completeTime;
    }

    public String getSysTips() {
        return sysTips;
    }

    public void setSysTips(String sysTips) {
        this.sysTips = sysTips;
    }

    public UnionGoods getGood() {
        return good;
    }

    public void setGood(UnionGoods good) {
        this.good = good;
    }

    public String getTextStatus() {
        return textStatus;
    }

    public void setTextStatus(String textStatus) {
        this.textStatus = textStatus;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }
}