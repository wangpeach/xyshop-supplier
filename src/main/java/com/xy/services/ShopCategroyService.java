package com.xy.services;

import com.xy.models.ShopCategory;

import java.util.List;

/**
 * Created by rjora on 2017/7/13 0013.
 */
public interface ShopCategroyService extends BaseService<ShopCategory> {

    public List<ShopCategory> selectMApiList();

}
