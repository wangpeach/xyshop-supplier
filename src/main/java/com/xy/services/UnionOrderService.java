package com.xy.services;

import com.xy.models.UnionOrders;

/**
 * Created by rjora on 2017/7/16 0016.
 */
public interface UnionOrderService extends BaseService<UnionOrders> {
    int saveSelective(String userId, String shopId, String goodId, int num);
}
