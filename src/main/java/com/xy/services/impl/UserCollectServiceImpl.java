package com.xy.services.impl;

import com.xy.config.ResourcesConfig;
import com.xy.models.UserCollect;
import com.xy.services.UserCollectService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Condition;

import java.util.List;

/**
 * UserCollectServiceImpl
 * @author Administrator
 * @date 2017/10/27 17:14
 * @description
 */
@Service
public class UserCollectServiceImpl extends BaseServiceImpl<UserCollect> implements UserCollectService{

    @Override
    public List<UserCollect> selectListByCondition(Condition condition) {
        List<UserCollect> collects = super.selectListByCondition(condition);
        this.handleResult(collects);
        return collects;
    }

    @Override
    public List<UserCollect> handleResult(List<UserCollect> args) {
        args.forEach(userCollect -> {
            if("shop".equals(userCollect.getCollectType())) {
                 userCollect.setShowThumbImg(ResourcesConfig.SHOPURL + userCollect.getThumbImg());
            } else {
                userCollect.setShowThumbImg(ResourcesConfig.PRODUCTIMGURL + userCollect.getThumbImg());
            }
        });
        return args;
    }
}
