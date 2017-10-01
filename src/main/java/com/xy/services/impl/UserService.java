package com.xy.services.impl;

import com.github.pagehelper.PageInfo;
import com.xy.models.User;
import com.xy.pojo.ParamsPojo;
import com.xy.services.IUserService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

/**
 * Created by rjora on 2017/6/29 0029.
 */
@Service
public class UserService extends BaseService<User> implements IUserService {

    public Condition createCond(ParamsPojo pj) {
        Condition condition = new Condition(User.class);
        Example.Criteria cri = condition.createCriteria();
//        if();
        return condition;
    }

    @Override
    public PageInfo<User> selectPageListByParams(ParamsPojo pj) {

        this.createCond(pj);

//        super.selectPageInfoByCondition();
        return null;
    }
}
