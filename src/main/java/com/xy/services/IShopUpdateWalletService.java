package com.xy.services;

import com.xy.models.ShopUpdateWallet;

/**
 * Created by rjora on 2017/7/16 0016.
 */
public interface IShopUpdateWalletService extends IService<ShopUpdateWallet> {

    int updatePass(ShopUpdateWallet updateWallet);

    int updateFail(ShopUpdateWallet updateWallet);

}
