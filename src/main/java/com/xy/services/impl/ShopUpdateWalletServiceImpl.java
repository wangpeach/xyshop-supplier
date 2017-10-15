package com.xy.services.impl;

import com.xy.models.ShopUpdateWallet;
import com.xy.services.ShopUpdateWalletService;
import com.xy.utils.DateUtils;
import com.xy.utils.StringUtils;
import org.springframework.stereotype.Service;

/**
 * Created by rjora on 2017/7/16 0016.
 */
@Service
public class ShopUpdateWalletServiceImpl extends BaseServiceImpl<ShopUpdateWallet> implements ShopUpdateWalletService {

    @Override
    public int updateByPrimaryKeySelective(ShopUpdateWallet entity) {
        int result = 0;

        entity.setCartId(StringUtils.splitWithChar(entity.getCartId(), 4, ' '));
        entity.setStatus("wait");

        if(super.count(new ShopUpdateWallet(entity.getShopUuid())) > 0) {
            ShopUpdateWallet updateWallet = super.selectOnlyByKey(entity.getUuid());
            entity.setOldCartId(updateWallet.getCartId());
            entity.setOldCartName(updateWallet.getCartName());
            entity.setOldCartUName(updateWallet.getCartUName());
            entity.setOldCartOpenAddr(updateWallet.getCartOpenAddr());
            result = super.updateByPrimaryKeySelective(entity);
        } else {
            entity.setUuid(StringUtils.getUuid());
            entity.setAddTime(DateUtils.getCurrentDate());
            result = super.saveSelective(entity);
        }
        return result;
    }

    @Override
    public int updatePass(ShopUpdateWallet updateWallet) {
        updateWallet.setStatus("success");
        return super.updateByPrimaryKeySelective(updateWallet);
    }

    @Override
    public int updateFail(ShopUpdateWallet updateWallet) {
        updateWallet.setStatus("fail");
        return super.updateByPrimaryKeySelective(updateWallet);
    }
}
