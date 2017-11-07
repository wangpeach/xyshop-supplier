package com.xy.services.impl;

import com.github.pagehelper.PageInfo;
import com.xy.config.ResourcesConfig;
import com.xy.models.UserFootprint;
import com.xy.services.UserFootprintService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Condition;

import java.util.List;

@Service
public class UserFootprintServiceImpl extends BaseServiceImpl<UserFootprint> implements UserFootprintService {

    @Override
    public List<UserFootprint> selectList(String user, int offset, int limit) {
        Condition cond = new Condition(UserFootprint.class);
        cond.createCriteria().andEqualTo("userUuid", user);
        cond.setOrderByClause(" add_time desc");
        List<UserFootprint> lists = super.selectPageInfoByCondition(cond, offset, limit).getList();
        return this.handleResult(lists);
    }


    @Override
    public List<UserFootprint> handleResult(List<UserFootprint> args) {
        args.forEach(arg -> {
            arg.setThumbImg(ResourcesConfig.PRODUCTIMGURL + arg.getThumbImg());
        });
        return args;
    }
}
