package com.xy.models;

import javax.persistence.*;

@Table(name = "search_record")
public class SearchRecord {
    @Id
    private String uuid;

    /**
     * 录入时间
     */
    @Column(name = "add_time")
    private String addTime;

    /**
     * 类型: index:首页搜索, top:推荐搜索，置顶
     */
    private String type;

    /**
     * 检索人uuid
     */
    @Column(name = "user_uuid")
    private String userUuid;

    /**
     * 检索关键字
     */
    @Column(name = "key_word")
    private String keyWord;

    /**
     * 1 代表推荐关键字，系统（管理员）成功。默认为0，用户生成，不推荐
     */
    @Column(name = "to_top")
    private Integer toTop;

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
     * 获取类型: index:首页搜索, top:推荐搜索，置顶
     *
     * @return type - 类型: index:首页搜索, top:推荐搜索，置顶
     */
    public String getType() {
        return type;
    }

    /**
     * 设置类型: index:首页搜索, top:推荐搜索，置顶
     *
     * @param type 类型: index:首页搜索, top:推荐搜索，置顶
     */
    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    /**
     * 获取检索人uuid
     *
     * @return user_uuid - 检索人uuid
     */
    public String getUserUuid() {
        return userUuid;
    }

    /**
     * 设置检索人uuid
     *
     * @param userUuid 检索人uuid
     */
    public void setUserUuid(String userUuid) {
        this.userUuid = userUuid == null ? null : userUuid.trim();
    }

    /**
     * 获取检索关键字
     *
     * @return key_word - 检索关键字
     */
    public String getKeyWord() {
        return keyWord;
    }

    /**
     * 设置检索关键字
     *
     * @param keyWord 检索关键字
     */
    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord == null ? null : keyWord.trim();
    }

    /**
     * 获取1 代表推荐关键字，系统（管理员）成功。默认为0，用户生成，不推荐
     *
     * @return to_top - 1 代表推荐关键字，系统（管理员）成功。默认为0，用户生成，不推荐
     */
    public Integer getToTop() {
        return toTop;
    }

    /**
     * 设置1 代表推荐关键字，系统（管理员）成功。默认为0，用户生成，不推荐
     *
     * @param toTop 1 代表推荐关键字，系统（管理员）成功。默认为0，用户生成，不推荐
     */
    public void setToTop(Integer toTop) {
        this.toTop = toTop;
    }
}