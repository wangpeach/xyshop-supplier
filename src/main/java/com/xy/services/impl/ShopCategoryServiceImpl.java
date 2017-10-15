package com.xy.services.impl;

import com.github.pagehelper.PageInfo;
import com.xy.models.ShopCategory;
import com.xy.services.ShopCategroyService;
import com.xy.utils.Config;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Condition;

import java.util.List;

/**
 * Created by rjora on 2017/7/13 0013.
 */
@Service
public class ShopCategoryServiceImpl extends BaseServiceImpl<ShopCategory> implements ShopCategroyService {


    @Override
    public PageInfo<ShopCategory> selectPageInfoByCondition(Condition condition, int offset, int limit) {
        PageInfo<ShopCategory> pis = super.selectPageInfoByCondition(condition, offset, limit);
        pis.setList(this.handleResult(pis.getList()));
        return pis;
    }

    @Override
    public List<ShopCategory> selectListByCondition(Condition condition) {
        return this.handleResult(super.selectListByCondition(condition));
    }

    @Override
    public List<ShopCategory> selectMApiList() {
        ShopCategory sc = new ShopCategory();
        sc.setCatPid("root");
        List<ShopCategory> list = super.selectList(sc);
        list.forEach(category -> {
            sc.setCatPid(category.getCatId());
            category = this.handleResult(category);
            category.putChildIds(category.getCatId());

            List<ShopCategory> childs = super.selectList(sc);
            for (ShopCategory item :
                    childs) {
                category.putChildIds(item.getCatId());
            }

            category.setChilds(this.handleResult(childs));
        });
        return list;
    }

    @Override
    public List<ShopCategory> handleResult(List<ShopCategory> arg) {
        arg.forEach(s -> {
            s = this.handleResult(s);
        });
        return arg;
    }

    @Override
    public ShopCategory handleResult(ShopCategory arg) {
        arg.setIconImgPath(Config.ICONURL + arg.getIconImg());
        return arg;
    }
}
