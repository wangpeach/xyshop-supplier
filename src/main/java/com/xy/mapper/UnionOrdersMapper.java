package com.xy.mapper;

import com.xy.models.UnionOrders;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

public interface UnionOrdersMapper extends Mapper<UnionOrders> {

    public List<Map> payTypeCencusOfToday(Map<String, Object> params);

}