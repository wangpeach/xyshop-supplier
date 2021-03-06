package com.xy.services.impl;

import com.xy.models.ShopWallet;
import com.xy.services.ShopWalletService;
import com.xy.utils.MoneyUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Created by rjora on 2017/7/16 0016.
 */
@Service
public class ShopWalletServiceImpl extends BaseServiceImpl<ShopWallet> implements ShopWalletService {
    @Override
    public ShopWallet selectOnly(ShopWallet entity) {
        entity = super.selectOnly(entity);
        if(null == entity.getMoney()) {
            entity.setMoney(BigDecimal.ZERO);
        }
        entity.setDoubleMoney(MoneyUtils.fen2Yuan(entity.getMoney().longValue()));
        return entity;
    }

    @Override
    public ShopWallet selectOnlyByKey(Object key) {
        ShopWallet wallet = super.selectOnlyByKey(key);
        if(null == wallet.getMoney()) {
            wallet.setMoney(BigDecimal.ZERO);
        }
        wallet.setDoubleMoney(MoneyUtils.fen2Yuan(wallet.getMoney().longValue()));
        return wallet;
    }
}
