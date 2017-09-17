package com.xy.services;

import com.xy.models.ShopMoneyRecord;

import java.util.Map;

/**
 * Created by rjora on 2017/7/16 0016.
 */
public interface IShopMoneyRecordService extends IService<ShopMoneyRecord> {

    Map<String, Object> statistics(String shopUuid);

}
