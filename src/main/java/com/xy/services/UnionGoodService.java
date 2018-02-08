package com.xy.services;

import com.xy.models.UnionGoods;

import java.util.List;

/**
 * Created by rjora on 2017/7/16 0016.
 */
public interface UnionGoodService extends BaseService<UnionGoods> {

    public int updateOnlineGood(UnionGoods arg);

    public List<UnionGoods> mApiList(String user, String cats, String key, String position, String distance, String orderBy, int offset, int limit);
}
