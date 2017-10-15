package com.xy.services;

import com.xy.models.Shop;

import java.util.List;

/**
 * Created by rjora on 2017/7/14 0014.
 */
public interface ShopService extends BaseService<Shop> {

    int del(Shop shop);

    /**
     * 自动冻结商家
     */
    void autoFreeze();

    /**
     * 客户端搜索商家
     * @param cats 商家类别，有可能是单独类别，有可能是集合
     * @param key 搜索关键字
     * @param position 用户坐标
     * @param distance 距离
     * @param orderBy 排序 0(按距离排序)，1(人气排序)
     * @param offset 起始索引
     * @param limit 查询几条
     * @return
     */
    List<Shop> mApiList(String cats, String key, String position, String distance, String orderBy, int offset, int limit);
}
