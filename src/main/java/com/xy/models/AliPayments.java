package com.xy.models;

import javax.persistence.*;
import java.util.Map;

public class AliPayments {

    public AliPayments() {}

    public AliPayments (Map<String, String> params){
        this.notifyTime = params.get("notify_time");
        this.notifyType = params.get("notify_type");
        this.notifyId = params.get("notify_id");
        this.appId = params.get("app_id");
        this.charset = params.get("charset");
        this.version = params.get("version");
        this.signType = params.get("sign_type");
        this.sign = params.get("sign");
        this.tradeNo = params.get("trade_no");
        this.outTradeNo = params.get("out_trade_no");
        this.outBizNo = params.get("out_biz_no");
        this.buyerId = params.get("buyer_id");
        this.buyerLogonId = params.get("buyer_logon_id");
        this.sellerId = params.get("seller_id");
        this.sellerEmail = params.get("seller_email");
        this.tradeStatus = params.get("trade_status");
        this.totalAmount = params.get("total_amount");
        this.receiptAmount = params.get("receipt_amount");
        this.invoiceAmount = params.get("invoice_amount");
        this.buyerPayAmount = params.get("buyer_pay_amount");
        this.pointAmount = params.get("point_amount");
        this.refundFee = params.get("refund_fee");
        this.subject = params.get("subject");
        this.body = params.get("body");
        this.gmtCreate = params.get("gmt_create");
        this.gmtPayment = params.get("gmt_payment");
        this.gmtRefund = params.get("gmt_refund");
        this.gmtClose = params.get("gmt_close");
        this.fundBillList = params.get("fund_bill_list");
        this.passbackParams = params.get("passback_params");
        this.voucherDetailList = params.get("voucher_detail_list");
    }


    /**
     * 通知时间
     */
    @Column(name = "notify_time")
    private String notifyTime;

    /**
     * 通知类型
     */
    @Column(name = "notify_type")
    private String notifyType;

    /**
     * 通知校验ID
     */
    @Column(name = "notify_id")
    private String notifyId;

    /**
     * 支付宝分配给开发者的应用Id
     */
    @Column(name = "app_id")
    private String appId;

    /**
     * 编码格式
     */
    private String charset;

    /**
     * 接口版本
     */
    private String version;

    /**
     * 签名类型
     */
    @Column(name = "sign_type")
    private String signType;

    /**
     * 签名
     */
    private String sign;

    /**
     * 支付宝交易号
     */
    @Column(name = "trade_no")
    private String tradeNo;

    /**
     * 商户订单号
     */
    @Column(name = "out_trade_no")
    private String outTradeNo;

    /**
     * 商户业务号
     */
    @Column(name = "out_biz_no")
    private String outBizNo;

    /**
     * 买家支付宝用户号
     */
    @Column(name = "buyer_id")
    private String buyerId;

    /**
     * 买家支付宝账号
     */
    @Column(name = "buyer_logon_id")
    private String buyerLogonId;

    /**
     * 卖家支付宝用户号
     */
    @Column(name = "seller_id")
    private String sellerId;

    /**
     * 卖家支付宝账号
     */
    @Column(name = "seller_email")
    private String sellerEmail;

    /**
     * 交易状态
     */
    @Column(name = "trade_status")
    private String tradeStatus;

    /**
     * 订单金额
     */
    @Column(name = "total_amount")
    private String totalAmount;

    /**
     * 实收金额
     */
    @Column(name = "receipt_amount")
    private String receiptAmount;

    /**
     * 开票金额
     */
    @Column(name = "invoice_amount")
    private String invoiceAmount;

    /**
     * 付款金额
     */
    @Column(name = "buyer_pay_amount")
    private String buyerPayAmount;

    /**
     * 集分宝金额
     */
    @Column(name = "point_amount")
    private String pointAmount;

    /**
     * 总退款金额
     */
    @Column(name = "refund_fee")
    private String refundFee;

    /**
     * 订单标题
     */
    private String subject;

    /**
     * 商品描述
     */
    private String body;

    /**
     * 交易创建时间
     */
    @Column(name = "gmt_create")
    private String gmtCreate;

    /**
     * 交易付款时间
     */
    @Column(name = "gmt_payment")
    private String gmtPayment;

    /**
     * 交易退款时间
     */
    @Column(name = "gmt_refund")
    private String gmtRefund;

    /**
     * 交易结束时间
     */
    @Column(name = "gmt_close")
    private String gmtClose;

    /**
     * 支付金额信息
     */
    @Column(name = "fund_bill_list")
    private String fundBillList;

    /**
     * 回传参数
     */
    @Column(name = "passback_params")
    private String passbackParams;

    /**
     * 优惠券信息
     */
    @Column(name = "voucher_detail_list")
    private String voucherDetailList;

    /**
     * 获取通知时间
     *
     * @return notify_time - 通知时间
     */
    public String getNotifyTime() {
        return notifyTime;
    }

    /**
     * 设置通知时间
     *
     * @param notifyTime 通知时间
     */
    public void setNotifyTime(String notifyTime) {
        this.notifyTime = notifyTime == null ? null : notifyTime.trim();
    }

    /**
     * 获取通知类型
     *
     * @return notify_type - 通知类型
     */
    public String getNotifyType() {
        return notifyType;
    }

    /**
     * 设置通知类型
     *
     * @param notifyType 通知类型
     */
    public void setNotifyType(String notifyType) {
        this.notifyType = notifyType == null ? null : notifyType.trim();
    }

    /**
     * 获取通知校验ID
     *
     * @return notify_id - 通知校验ID
     */
    public String getNotifyId() {
        return notifyId;
    }

    /**
     * 设置通知校验ID
     *
     * @param notifyId 通知校验ID
     */
    public void setNotifyId(String notifyId) {
        this.notifyId = notifyId == null ? null : notifyId.trim();
    }

    /**
     * 获取支付宝分配给开发者的应用Id
     *
     * @return app_id - 支付宝分配给开发者的应用Id
     */
    public String getAppId() {
        return appId;
    }

    /**
     * 设置支付宝分配给开发者的应用Id
     *
     * @param appId 支付宝分配给开发者的应用Id
     */
    public void setAppId(String appId) {
        this.appId = appId == null ? null : appId.trim();
    }

    /**
     * 获取编码格式
     *
     * @return charset - 编码格式
     */
    public String getCharset() {
        return charset;
    }

    /**
     * 设置编码格式
     *
     * @param charset 编码格式
     */
    public void setCharset(String charset) {
        this.charset = charset == null ? null : charset.trim();
    }

    /**
     * 获取接口版本
     *
     * @return version - 接口版本
     */
    public String getVersion() {
        return version;
    }

    /**
     * 设置接口版本
     *
     * @param version 接口版本
     */
    public void setVersion(String version) {
        this.version = version == null ? null : version.trim();
    }

    /**
     * 获取签名类型
     *
     * @return sign_type - 签名类型
     */
    public String getSignType() {
        return signType;
    }

    /**
     * 设置签名类型
     *
     * @param signType 签名类型
     */
    public void setSignType(String signType) {
        this.signType = signType == null ? null : signType.trim();
    }

    /**
     * 获取签名
     *
     * @return sign - 签名
     */
    public String getSign() {
        return sign;
    }

    /**
     * 设置签名
     *
     * @param sign 签名
     */
    public void setSign(String sign) {
        this.sign = sign == null ? null : sign.trim();
    }

    /**
     * 获取支付宝交易号
     *
     * @return trade_no - 支付宝交易号
     */
    public String getTradeNo() {
        return tradeNo;
    }

    /**
     * 设置支付宝交易号
     *
     * @param tradeNo 支付宝交易号
     */
    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo == null ? null : tradeNo.trim();
    }

    /**
     * 获取商户订单号
     *
     * @return out_trade_no - 商户订单号
     */
    public String getOutTradeNo() {
        return outTradeNo;
    }

    /**
     * 设置商户订单号
     *
     * @param outTradeNo 商户订单号
     */
    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo == null ? null : outTradeNo.trim();
    }

    /**
     * 获取商户业务号
     *
     * @return out_biz_no - 商户业务号
     */
    public String getOutBizNo() {
        return outBizNo;
    }

    /**
     * 设置商户业务号
     *
     * @param outBizNo 商户业务号
     */
    public void setOutBizNo(String outBizNo) {
        this.outBizNo = outBizNo == null ? null : outBizNo.trim();
    }

    /**
     * 获取买家支付宝用户号
     *
     * @return buyer_id - 买家支付宝用户号
     */
    public String getBuyerId() {
        return buyerId;
    }

    /**
     * 设置买家支付宝用户号
     *
     * @param buyerId 买家支付宝用户号
     */
    public void setBuyerId(String buyerId) {
        this.buyerId = buyerId == null ? null : buyerId.trim();
    }

    /**
     * 获取买家支付宝账号
     *
     * @return buyer_logon_id - 买家支付宝账号
     */
    public String getBuyerLogonId() {
        return buyerLogonId;
    }

    /**
     * 设置买家支付宝账号
     *
     * @param buyerLogonId 买家支付宝账号
     */
    public void setBuyerLogonId(String buyerLogonId) {
        this.buyerLogonId = buyerLogonId == null ? null : buyerLogonId.trim();
    }

    /**
     * 获取卖家支付宝用户号
     *
     * @return seller_id - 卖家支付宝用户号
     */
    public String getSellerId() {
        return sellerId;
    }

    /**
     * 设置卖家支付宝用户号
     *
     * @param sellerId 卖家支付宝用户号
     */
    public void setSellerId(String sellerId) {
        this.sellerId = sellerId == null ? null : sellerId.trim();
    }

    /**
     * 获取卖家支付宝账号
     *
     * @return seller_email - 卖家支付宝账号
     */
    public String getSellerEmail() {
        return sellerEmail;
    }

    /**
     * 设置卖家支付宝账号
     *
     * @param sellerEmail 卖家支付宝账号
     */
    public void setSellerEmail(String sellerEmail) {
        this.sellerEmail = sellerEmail == null ? null : sellerEmail.trim();
    }

    /**
     * 获取交易状态
     *
     * @return trade_status - 交易状态
     */
    public String getTradeStatus() {
        return tradeStatus;
    }

    /**
     * 设置交易状态
     *
     * @param tradeStatus 交易状态
     */
    public void setTradeStatus(String tradeStatus) {
        this.tradeStatus = tradeStatus == null ? null : tradeStatus.trim();
    }

    /**
     * 获取订单金额
     *
     * @return total_amount - 订单金额
     */
    public String getTotalAmount() {
        return totalAmount;
    }

    /**
     * 设置订单金额
     *
     * @param totalAmount 订单金额
     */
    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount == null ? null : totalAmount.trim();
    }

    /**
     * 获取实收金额
     *
     * @return receipt_amount - 实收金额
     */
    public String getReceiptAmount() {
        return receiptAmount;
    }

    /**
     * 设置实收金额
     *
     * @param receiptAmount 实收金额
     */
    public void setReceiptAmount(String receiptAmount) {
        this.receiptAmount = receiptAmount == null ? null : receiptAmount.trim();
    }

    /**
     * 获取开票金额
     *
     * @return invoice_amount - 开票金额
     */
    public String getInvoiceAmount() {
        return invoiceAmount;
    }

    /**
     * 设置开票金额
     *
     * @param invoiceAmount 开票金额
     */
    public void setInvoiceAmount(String invoiceAmount) {
        this.invoiceAmount = invoiceAmount == null ? null : invoiceAmount.trim();
    }

    /**
     * 获取付款金额
     *
     * @return buyer_pay_amount - 付款金额
     */
    public String getBuyerPayAmount() {
        return buyerPayAmount;
    }

    /**
     * 设置付款金额
     *
     * @param buyerPayAmount 付款金额
     */
    public void setBuyerPayAmount(String buyerPayAmount) {
        this.buyerPayAmount = buyerPayAmount == null ? null : buyerPayAmount.trim();
    }

    /**
     * 获取集分宝金额
     *
     * @return point_amount - 集分宝金额
     */
    public String getPointAmount() {
        return pointAmount;
    }

    /**
     * 设置集分宝金额
     *
     * @param pointAmount 集分宝金额
     */
    public void setPointAmount(String pointAmount) {
        this.pointAmount = pointAmount == null ? null : pointAmount.trim();
    }

    /**
     * 获取总退款金额
     *
     * @return refund_fee - 总退款金额
     */
    public String getRefundFee() {
        return refundFee;
    }

    /**
     * 设置总退款金额
     *
     * @param refundFee 总退款金额
     */
    public void setRefundFee(String refundFee) {
        this.refundFee = refundFee == null ? null : refundFee.trim();
    }

    /**
     * 获取订单标题
     *
     * @return subject - 订单标题
     */
    public String getSubject() {
        return subject;
    }

    /**
     * 设置订单标题
     *
     * @param subject 订单标题
     */
    public void setSubject(String subject) {
        this.subject = subject == null ? null : subject.trim();
    }

    /**
     * 获取商品描述
     *
     * @return body - 商品描述
     */
    public String getBody() {
        return body;
    }

    /**
     * 设置商品描述
     *
     * @param body 商品描述
     */
    public void setBody(String body) {
        this.body = body == null ? null : body.trim();
    }

    /**
     * 获取交易创建时间
     *
     * @return gmt_create - 交易创建时间
     */
    public String getGmtCreate() {
        return gmtCreate;
    }

    /**
     * 设置交易创建时间
     *
     * @param gmtCreate 交易创建时间
     */
    public void setGmtCreate(String gmtCreate) {
        this.gmtCreate = gmtCreate == null ? null : gmtCreate.trim();
    }

    /**
     * 获取交易付款时间
     *
     * @return gmt_payment - 交易付款时间
     */
    public String getGmtPayment() {
        return gmtPayment;
    }

    /**
     * 设置交易付款时间
     *
     * @param gmtPayment 交易付款时间
     */
    public void setGmtPayment(String gmtPayment) {
        this.gmtPayment = gmtPayment == null ? null : gmtPayment.trim();
    }

    /**
     * 获取交易退款时间
     *
     * @return gmt_refund - 交易退款时间
     */
    public String getGmtRefund() {
        return gmtRefund;
    }

    /**
     * 设置交易退款时间
     *
     * @param gmtRefund 交易退款时间
     */
    public void setGmtRefund(String gmtRefund) {
        this.gmtRefund = gmtRefund == null ? null : gmtRefund.trim();
    }

    /**
     * 获取交易结束时间
     *
     * @return gmt_close - 交易结束时间
     */
    public String getGmtClose() {
        return gmtClose;
    }

    /**
     * 设置交易结束时间
     *
     * @param gmtClose 交易结束时间
     */
    public void setGmtClose(String gmtClose) {
        this.gmtClose = gmtClose == null ? null : gmtClose.trim();
    }

    /**
     * 获取支付金额信息
     *
     * @return fund_bill_list - 支付金额信息
     */
    public String getFundBillList() {
        return fundBillList;
    }

    /**
     * 设置支付金额信息
     *
     * @param fundBillList 支付金额信息
     */
    public void setFundBillList(String fundBillList) {
        this.fundBillList = fundBillList == null ? null : fundBillList.trim();
    }

    /**
     * 获取回传参数
     *
     * @return passback_params - 回传参数
     */
    public String getPassbackParams() {
        return passbackParams;
    }

    /**
     * 设置回传参数
     *
     * @param passbackParams 回传参数
     */
    public void setPassbackParams(String passbackParams) {
        this.passbackParams = passbackParams == null ? null : passbackParams.trim();
    }

    /**
     * 获取优惠券信息
     *
     * @return voucher_detail_list - 优惠券信息
     */
    public String getVoucherDetailList() {
        return voucherDetailList;
    }

    /**
     * 设置优惠券信息
     *
     * @param voucherDetailList 优惠券信息
     */
    public void setVoucherDetailList(String voucherDetailList) {
        this.voucherDetailList = voucherDetailList == null ? null : voucherDetailList.trim();
    }
}