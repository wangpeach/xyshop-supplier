package com.xy.services.impl;

import com.xy.models.ShopWallet;
import com.xy.services.IShopWalletService;
import com.xy.utils.MoneyUtils;
import org.springframework.stereotype.Service;

/**
 * Created by rjora on 2017/7/16 0016.
 */
@Service
public class ShopWalletService extends BaseService<ShopWallet> implements IShopWalletService{
    @Override
    public ShopWallet selectOnly(ShopWallet entity) {
        entity = super.selectOnly(entity);
        if(null == entity.getMoney()) {
            entity.setMoney(0);
        }
        entity.setDoubleMoney(MoneyUtils.fen2Yuan(entity.getMoney().longValue()));
        return entity;
    }

    @Override
    public ShopWallet selectOnlyByKey(Object key) {
        ShopWallet wallet = super.selectOnlyByKey(key);
        if(null == wallet.getMoney()) {
            wallet.setMoney(0);
        }
        wallet.setDoubleMoney(MoneyUtils.fen2Yuan(wallet.getMoney().longValue()));
        return wallet;
    }
}
