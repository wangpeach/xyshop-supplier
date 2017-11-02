package com.xy.models;

import javax.persistence.*;

@Table(name = "user_collect")
public class UserCollect {
    @Id
    private String uuid;

    /**
     * 用户UUID
     */
    @Column(name = "user_uuid")
    private String userUuid;

    /**
     * 商品名称
     */
    private String name;

    /**
     * 商品/商铺UUID
     */
    private String value;

    /**
     * 商品缩略图
     */
    @Column(name = "thumb_img")
    private String thumbImg;

    /**
     * 收藏类型
     */
    @Column(name = "collect_type")
    private String collectType;

    /**
     * 录入时间
     */
    @Column(name = "add_time")
    private String addTime;

    private String status;

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
     * 获取商品名称
     *
     * @return name - 商品名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置商品名称
     *
     * @param name 商品名称
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * 获取商品/商铺UUID
     *
     * @return value - 商品/商铺UUID
     */
    public String getValue() {
        return value;
    }

    /**
     * 设置商品/商铺UUID
     *
     * @param value 商品/商铺UUID
     */
    public void setValue(String value) {
        this.value = value == null ? null : value.trim();
    }

    /**
     * 获取商品缩略图
     *
     * @return thumb_img - 商品缩略图
     */
    public String getThumbImg() {
        return thumbImg;
    }

    /**
     * 设置商品缩略图
     *
     * @param thumbImg 商品缩略图
     */
    public void setThumbImg(String thumbImg) {
        this.thumbImg = thumbImg == null ? null : thumbImg.trim();
    }

    /**
     * 获取收藏类型
     *
     * @return collect_type - 收藏类型
     */
    public String getCollectType() {
        return collectType;
    }

    /**
     * 设置收藏类型
     *
     * @param collectType 收藏类型
     */
    public void setCollectType(String collectType) {
        this.collectType = collectType == null ? null : collectType.trim();
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}