package com.xy.models;

import com.xy.utils.StringUtils;

import javax.persistence.*;
import java.util.Map;

@Table(name = "wxpayments")
public class WXPayments {

    public WXPayments() {
    }

    public WXPayments(Map<String, String> arg) {
        this.appid = arg.get("appid");
        this.mchId = arg.get("mch_id");
        this.deviceInfo = arg.get("device_info");
        this.nonceStr = arg.get("nonce_str");
        this.sign = arg.get("sign");
        this.resultCode = arg.get("result_code");
        this.errCode = arg.get("err_code");
        this.errCodeDes = arg.get("err_code_des");
        this.openid = arg.get("openid");
        this.isSubscribe = arg.get("is_subscribe");
        this.tradeType = arg.get("trade_type");
        this.bankType = arg.get("bank_type");
        this.totalFee = setIntValue(arg.get("total_fee"));
        this.feeType = arg.get("fee_type");
        this.cashFee = setIntValue(arg.get("cash_fee"));
        this.cashFeeType = arg.get("cash_fee_type");
        this.couponFee = setIntValue(arg.get("coupon_fee"));
        this.couponCount = setIntValue(arg.get("coupon_count"));
        this.couponIdN = arg.get("coupon_id_$n");
        this.couponFeeN = setIntValue(arg.get("coupon_fee_$n"));
        this.transactionId = arg.get("transaction_id");
        this.outTradeNo = arg.get("out_trade_no");
        this.attach = arg.get("attach");
        this.timeEnd = arg.get("time_end");
    }

    private int setIntValue(String value){
        if(StringUtils.isNotNull(value)) {
            return Integer.parseInt(value);
        }
        return 0;
    }

    /**
     * 应用ID
     */
    private String appid;

    /**
     * 商户号
     */
    @Column(name = "mch_id")
    private String mchId;

    /**
     * 设备号
     */
    @Column(name = "device_info")
    private String deviceInfo;

    /**
     * 随机字符串
     */
    @Column(name = "nonce_str")
    private String nonceStr;

    /**
     * 签名
     */
    private String sign;

    /**
     * 业务结果
     */
    @Column(name = "result_code")
    private String resultCode;

    /**
     * 错误代码
     */
    @Column(name = "err_code")
    private String errCode;

    /**
     * 错误代码描述
     */
    @Column(name = "err_code_des")
    private String errCodeDes;

    /**
     * 用户标识
     */
    private String openid;

    /**
     * 是否关注公众账号
     */
    @Column(name = "is_subscribe")
    private String isSubscribe;

    /**
     * 交易类型
     */
    @Column(name = "trade_type")
    private String tradeType;

    /**
     * 付款银行
     */
    @Column(name = "bank_type")
    private String bankType;

    /**
     * 总金额
     */
    @Column(name = "total_fee")
    private Integer totalFee;

    /**
     * 货币种类
     */
    @Column(name = "fee_type")
    private String feeType;

    /**
     * 现金支付金额
     */
    @Column(name = "cash_fee")
    private Integer cashFee;

    /**
     * 现金支付货币类型
     */
    @Column(name = "cash_fee_type")
    private String cashFeeType;

    /**
     * 代金券金额
     */
    @Column(name = "coupon_fee")
    private Integer couponFee;

    /**
     * 代金券使用数量
     */
    @Column(name = "coupon_count")
    private Integer couponCount;

    /**
     * 代金券ID
     */
    @Column(name = "coupon_id_$n")
    private String couponIdN;

    /**
     * 单个代金券支付金额
     */
    @Column(name = "coupon_fee_$n")
    private Integer couponFeeN;

    /**
     * 微信支付订单号
     */
    @Column(name = "transaction_id")
    private String transactionId;

    /**
     * 商户订单号
     */
    @Column(name = "out_trade_no")
    private String outTradeNo;

    /**
     * 商家数据包
     */
    private String attach;

    /**
     * 支付完成时间
     */
    @Column(name = "time_end")
    private String timeEnd;

    /**
     * 获取应用ID
     *
     * @return appid - 应用ID
     */
    public String getAppid() {
        return appid;
    }

    /**
     * 设置应用ID
     *
     * @param appid 应用ID
     */
    public void setAppid(String appid) {
        this.appid = appid == null ? null : appid.trim();
    }

    /**
     * 获取商户号
     *
     * @return mch_id - 商户号
     */
    public String getMchId() {
        return mchId;
    }

    /**
     * 设置商户号
     *
     * @param mchId 商户号
     */
    public void setMchId(String mchId) {
        this.mchId = mchId == null ? null : mchId.trim();
    }

    /**
     * 获取设备号
     *
     * @return device_info - 设备号
     */
    public String getDeviceInfo() {
        return deviceInfo;
    }

    /**
     * 设置设备号
     *
     * @param deviceInfo 设备号
     */
    public void setDeviceInfo(String deviceInfo) {
        this.deviceInfo = deviceInfo == null ? null : deviceInfo.trim();
    }

    /**
     * 获取随机字符串
     *
     * @return nonce_str - 随机字符串
     */
    public String getNonceStr() {
        return nonceStr;
    }

    /**
     * 设置随机字符串
     *
     * @param nonceStr 随机字符串
     */
    public void setNonceStr(String nonceStr) {
        this.nonceStr = nonceStr == null ? null : nonceStr.trim();
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
     * 获取业务结果
     *
     * @return result_code - 业务结果
     */
    public String getResultCode() {
        return resultCode;
    }

    /**
     * 设置业务结果
     *
     * @param resultCode 业务结果
     */
    public void setResultCode(String resultCode) {
        this.resultCode = resultCode == null ? null : resultCode.trim();
    }

    /**
     * 获取错误代码
     *
     * @return err_code - 错误代码
     */
    public String getErrCode() {
        return errCode;
    }

    /**
     * 设置错误代码
     *
     * @param errCode 错误代码
     */
    public void setErrCode(String errCode) {
        this.errCode = errCode == null ? null : errCode.trim();
    }

    /**
     * 获取错误代码描述
     *
     * @return err_code_des - 错误代码描述
     */
    public String getErrCodeDes() {
        return errCodeDes;
    }

    /**
     * 设置错误代码描述
     *
     * @param errCodeDes 错误代码描述
     */
    public void setErrCodeDes(String errCodeDes) {
        this.errCodeDes = errCodeDes == null ? null : errCodeDes.trim();
    }

    /**
     * 获取用户标识
     *
     * @return openid - 用户标识
     */
    public String getOpenid() {
        return openid;
    }

    /**
     * 设置用户标识
     *
     * @param openid 用户标识
     */
    public void setOpenid(String openid) {
        this.openid = openid == null ? null : openid.trim();
    }

    /**
     * 获取是否关注公众账号
     *
     * @return is_subscribe - 是否关注公众账号
     */
    public String getIsSubscribe() {
        return isSubscribe;
    }

    /**
     * 设置是否关注公众账号
     *
     * @param isSubscribe 是否关注公众账号
     */
    public void setIsSubscribe(String isSubscribe) {
        this.isSubscribe = isSubscribe == null ? null : isSubscribe.trim();
    }

    /**
     * 获取交易类型
     *
     * @return trade_type - 交易类型
     */
    public String getTradeType() {
        return tradeType;
    }

    /**
     * 设置交易类型
     *
     * @param tradeType 交易类型
     */
    public void setTradeType(String tradeType) {
        this.tradeType = tradeType == null ? null : tradeType.trim();
    }

    /**
     * 获取付款银行
     *
     * @return bank_type - 付款银行
     */
    public String getBankType() {
        return bankType;
    }

    /**
     * 设置付款银行
     *
     * @param bankType 付款银行
     */
    public void setBankType(String bankType) {
        this.bankType = bankType == null ? null : bankType.trim();
    }

    /**
     * 获取总金额
     *
     * @return total_fee - 总金额
     */
    public Integer getTotalFee() {
        return totalFee;
    }

    /**
     * 设置总金额
     *
     * @param totalFee 总金额
     */
    public void setTotalFee(Integer totalFee) {
        this.totalFee = totalFee;
    }

    /**
     * 获取货币种类
     *
     * @return fee_type - 货币种类
     */
    public String getFeeType() {
        return feeType;
    }

    /**
     * 设置货币种类
     *
     * @param feeType 货币种类
     */
    public void setFeeType(String feeType) {
        this.feeType = feeType == null ? null : feeType.trim();
    }

    /**
     * 获取现金支付金额
     *
     * @return cash_fee - 现金支付金额
     */
    public Integer getCashFee() {
        return cashFee;
    }

    /**
     * 设置现金支付金额
     *
     * @param cashFee 现金支付金额
     */
    public void setCashFee(Integer cashFee) {
        this.cashFee = cashFee;
    }

    /**
     * 获取现金支付货币类型
     *
     * @return cash_fee_type - 现金支付货币类型
     */
    public String getCashFeeType() {
        return cashFeeType;
    }

    /**
     * 设置现金支付货币类型
     *
     * @param cashFeeType 现金支付货币类型
     */
    public void setCashFeeType(String cashFeeType) {
        this.cashFeeType = cashFeeType == null ? null : cashFeeType.trim();
    }

    /**
     * 获取代金券金额
     *
     * @return coupon_fee - 代金券金额
     */
    public Integer getCouponFee() {
        return couponFee;
    }

    /**
     * 设置代金券金额
     *
     * @param couponFee 代金券金额
     */
    public void setCouponFee(Integer couponFee) {
        this.couponFee = couponFee;
    }

    /**
     * 获取代金券使用数量
     *
     * @return coupon_count - 代金券使用数量
     */
    public Integer getCouponCount() {
        return couponCount;
    }

    /**
     * 设置代金券使用数量
     *
     * @param couponCount 代金券使用数量
     */
    public void setCouponCount(Integer couponCount) {
        this.couponCount = couponCount;
    }

    /**
     * 获取代金券ID
     *
     * @return coupon_id_$n - 代金券ID
     */
    public String getCouponIdN() {
        return couponIdN;
    }

    /**
     * 设置代金券ID
     *
     * @param couponIdN 代金券ID
     */
    public void setCouponIdN(String couponIdN) {
        this.couponIdN = couponIdN == null ? null : couponIdN.trim();
    }

    /**
     * 获取单个代金券支付金额
     *
     * @return coupon_fee_$n - 单个代金券支付金额
     */
    public Integer getCouponFeeN() {
        return couponFeeN;
    }

    /**
     * 设置单个代金券支付金额
     *
     * @param couponFeeN 单个代金券支付金额
     */
    public void setCouponFeeN(Integer couponFeeN) {
        this.couponFeeN = couponFeeN;
    }

    /**
     * 获取微信支付订单号
     *
     * @return transaction_id - 微信支付订单号
     */
    public String getTransactionId() {
        return transactionId;
    }

    /**
     * 设置微信支付订单号
     *
     * @param transactionId 微信支付订单号
     */
    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId == null ? null : transactionId.trim();
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
     * 获取商家数据包
     *
     * @return attach - 商家数据包
     */
    public String getAttach() {
        return attach;
    }

    /**
     * 设置商家数据包
     *
     * @param attach 商家数据包
     */
    public void setAttach(String attach) {
        this.attach = attach == null ? null : attach.trim();
    }

    /**
     * 获取支付完成时间
     *
     * @return time_end - 支付完成时间
     */
    public String getTimeEnd() {
        return timeEnd;
    }

    /**
     * 设置支付完成时间
     *
     * @param timeEnd 支付完成时间
     */
    public void setTimeEnd(String timeEnd) {
        this.timeEnd = timeEnd == null ? null : timeEnd.trim();
    }
}