package com.xy.models;

import javax.persistence.*;

@Table(name = "user_coin_record")
public class UserCoinRecord {
    @Id
    private String uuid;

    /**
     * 用户UUID
     */
    @Column(name = "user_uuid")
    private String userUuid;

    /**
     * 涉及金币
     */
    private Integer coin;

    /**
     * 类型：income（收入）、expend（支出）
     */
    private String type;

    /**
     * 收支后剩余金币
     */
    @Column(name = "left_coin")
    private Float leftCoin;

    /**
     * 备注
     */
    private String remarks;

    /**
     * 录入时间
     */
    @Column(name = "add_time")
    private String addTime;

    /**
     * 收入类型:'other',(其他)'commission'(返佣),'recharge'(充值),'backreturn'(返还)
     */
    @Column(name = "detail_type")
    private String detailType;

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
     * 获取涉及金币
     *
     * @return coin - 涉及金币
     */
    public Integer getCoin() {
        return coin;
    }

    /**
     * 设置涉及金币
     *
     * @param coin 涉及金币
     */
    public void setCoin(Integer coin) {
        this.coin = coin;
    }

    /**
     * 获取类型：income（收入）、expend（支出）
     *
     * @return type - 类型：income（收入）、expend（支出）
     */
    public String getType() {
        return type;
    }

    /**
     * 设置类型：income（收入）、expend（支出）
     *
     * @param type 类型：income（收入）、expend（支出）
     */
    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    /**
     * 获取收支后剩余金币
     *
     * @return left_coin - 收支后剩余金币
     */
    public Float getLeftCoin() {
        return leftCoin;
    }

    /**
     * 设置收支后剩余金币
     *
     * @param leftCoin 收支后剩余金币
     */
    public void setLeftCoin(Float leftCoin) {
        this.leftCoin = leftCoin;
    }

    /**
     * 获取备注
     *
     * @return remarks - 备注
     */
    public String getRemarks() {
        return remarks;
    }

    /**
     * 设置备注
     *
     * @param remarks 备注
     */
    public void setRemarks(String remarks) {
        this.remarks = remarks == null ? null : remarks.trim();
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
     * 获取收入类型:'other',(其他)'commission'(返佣),'recharge'(充值),'backreturn'(返还)
     *
     * @return detail_type - 收入类型:'other',(其他)'commission'(返佣),'recharge'(充值),'backreturn'(返还)
     */
    public String getDetailType() {
        return detailType;
    }

    /**
     * 设置收入类型:'other',(其他)'commission'(返佣),'recharge'(充值),'backreturn'(返还)
     *
     * @param detailType 收入类型:'other',(其他)'commission'(返佣),'recharge'(充值),'backreturn'(返还)
     */
    public void setDetailType(String detailType) {
        this.detailType = detailType == null ? null : detailType.trim();
    }
}