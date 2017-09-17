package com.xy.services.impl;

import com.xy.models.ShopMoneyRecord;
import com.xy.services.IShopMoneyRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by rjora on 2017/7/16 0016.
 */
@Service
public class ShopMoneyRecordService extends BaseService<ShopMoneyRecord> implements IShopMoneyRecordService {

    @Autowired
    private SqlService sqlService;

    @Override
    public Map<String, Object> statistics(String shopUuid) {
        String sql = "SELECT IFNULL(SUM(money), 0) AS money, COUNT(*) AS buyedNum, (SELECT COUNT(*) FROM shop_money_record WHERE shop_uuid = '%s' AND type = 'expend' and status='success') AS withdrawNum FROM shop_money_record WHERE shop_uuid = '%s' AND type = 'income' AND status = 'success'";
        return (Map<String, Object>) sqlService.exec(sql, shopUuid, shopUuid);
    }
}
