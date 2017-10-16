package com.xy.services.impl;

import com.github.pagehelper.PageInfo;
import com.xy.models.User;
import com.xy.pojo.ParamsPojo;
import com.xy.services.UserService;
import com.xy.config.ResourcesConfig;
import com.xy.utils.StringUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.StringUtil;

import java.util.List;

/**
 * Created by rjora on 2017/6/29 0029.
 */
@Service
public class UserServiceImpl extends BaseServiceImpl<User> implements UserService {

    public Condition createCond(ParamsPojo pj) {
        Condition cond = new Condition(User.class);
        Example.Criteria cri = cond.createCriteria();
        if(StringUtils.isNotNull(pj.getSearch())) {
            String[] cols = {"name", "phoneNum"};
            String condition = "%s like ", arg = "'%"+ pj.getSearch() +"%'";

            for (int i = 0; i < cols.length; i++) {
                cols[i] = String.format(condition, StringUtil.camelhumpToUnderline(cols[i])) + arg;
            }

            String or = org.apache.commons.lang3.StringUtils.join(cols, " or ");

            cri.andCondition("("+ or +")");
        }

        if(StringUtils.isNotNull(pj.getParams().get("status"))) {
            cri.andEqualTo("status", pj.getParams().get("status"));
        }
        return cond;
    }

    @Override
    public User selectOnlyByKey(Object key) {
        return this.handleResult(super.selectOnlyByKey(key));
    }

    @Override
    public User selectOnly(User entity) {
        return this.handleResult(super.selectOnly(entity));
    }

    @Override
    public PageInfo<User> selectPageListByParams(ParamsPojo pj) {
        return super.selectPageInfoByCondition(this.createCond(pj), pj.getStart(), pj.getLength());
    }

    /**
     * 处理用户头像
     * @param args
     * @return
     */
    @Override
    public List<User> handleResult(List<User> args) {
        args.forEach(s -> {
            if(StringUtils.isNotNull(s.getHeadImg())) {
                s = this.handleResult(s);
            }
        });
        return args;
    }

    @Override
    public User handleResult(User arg) {
        arg.setHeadImg(ResourcesConfig.HEADURL + arg.getHeadImg());
        return arg;
    }
}
