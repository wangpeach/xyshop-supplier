package com.xy.mapper;

import com.xy.models.UnionGoods;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

public interface UnionGoodsMapper extends Mapper<UnionGoods> {
    public List<UnionGoods> searchShopDetails(Map<String, Object> arg);
}