package com.xy.models;

import javax.persistence.*;

@Table(name = "user_coupon")
public class UserCoupon {
    private String uuid;

    /**
     * 会员
     */
    @Column(name = "userId")
    private String userid;

    /**
     * 优惠卷
     */
    @Column(name = "couponId")
    private String couponid;

    /**
     * 拥有数量
     */
    private Integer num;

    /**
     * 有效期
     */
    @Column(name = "end_time")
    private String endTime;

    /**
     * waituser(待使用)，used(已使用)，expired(已过期)
     */
    private String status;

    @Transient
    private Coupon coupon;

    @Column(name = "add_time")
    private String addTime;

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
     * 获取会员
     *
     * @return userId - 会员
     */
    public String getUserid() {
        return userid;
    }

    /**
     * 设置会员
     *
     * @param userid 会员
     */
    public void setUserid(String userid) {
        this.userid = userid == null ? null : userid.trim();
    }

    /**
     * 获取优惠卷
     *
     * @return couponId - 优惠卷
     */
    public String getCouponid() {
        return couponid;
    }

    /**
     * 设置优惠卷
     *
     * @param couponid 优惠卷
     */
    public void setCouponid(String couponid) {
        this.couponid = couponid == null ? null : couponid.trim();
    }

    /**
     * 获取拥有数量
     *
     * @return num - 拥有数量
     */
    public Integer getNum() {
        return num;
    }

    /**
     * 设置拥有数量
     *
     * @param num 拥有数量
     */
    public void setNum(Integer num) {
        this.num = num;
    }

    /**
     * 获取有效期
     *
     * @return end_time - 有效期
     */
    public String getEndTime() {
        return endTime;
    }

    /**
     * 设置有效期
     *
     * @param endTime 有效期
     */
    public void setEndTime(String endTime) {
        this.endTime = endTime == null ? null : endTime.trim();
    }

    /**
     * 获取waituser(待使用)，used(已使用)，expired(已过期)
     *
     * @return status - waituser(待使用)，used(已使用)，expired(已过期)
     */
    public String getStatus() {
        return status;
    }

    /**
     * 设置waituser(待使用)，used(已使用)，expired(已过期)
     *
     * @param status waituser(待使用)，used(已使用)，expired(已过期)
     */
    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    /**
     * @return add_time
     */
    public String getAddTime() {
        return addTime;
    }

    /**
     * @param addTime
     */
    public void setAddTime(String addTime) {
        this.addTime = addTime == null ? null : addTime.trim();
    }

    public Coupon getCoupon() {
        return coupon;
    }

    public void setCoupon(Coupon coupon) {
        this.coupon = coupon;
    }
}