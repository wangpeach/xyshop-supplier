package com.xy.services.impl;

import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;
import com.xy.models.User;
import com.xy.pojo.ParamsPojo;
import com.xy.services.UserService;
import com.xy.config.ResourcesConfig;
import com.xy.utils.DateUtils;
import com.xy.utils.StringUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        Condition cond = new Condition(User.class);
        Example.Criteria cri = cond.createCriteria();
        cri.andEqualTo("phoneNum", entity.getPhoneNum()).andEqualTo("password", entity.getPassword());
        return this.handleResult(super.selectOnly(cond, 0));
    }

    @Override
    public PageInfo<User> selectPageListByParams(ParamsPojo pj) {
        PageInfo<User> pi = super.selectPageInfoByCondition(this.createCond(pj), pj.getStart(), pj.getLength());
        this.handleResult(pi.getList());
        return pi;
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
            } else {
                s.setHeadImg("");
            }
        });
        return args;
    }

    @Override
    public User handleResult(User arg) {
        if(arg != null && StringUtils.isNotNull(arg.getHeadImg())) {
            arg.setHeadImg(ResourcesConfig.HEADURL + arg.getHeadImg());
        }
        return arg;
    }

    @Override
    public String charts(String type, Map<String, Object> params) {
        String result = null;
        switch (type) {
            case "M":
                params.get("year");
                break;
            case "W":
                result = this.weekCount();
                break;
            case "Custom":
                params.get("start");
                params.get("end");
                break;
            default:
                break;
        }
        return result;
    }

    /**
     * 统计今日起会员增长情况
     * @return
     */
    private String weekCount() {
        Map<String, Object> result = new HashMap<>();
        List<String> date = new ArrayList<>();
        List<Integer> value = new ArrayList<>();

        for (int i = 6; i >= 0; i--) {
            String time = DateUtils.format(DateUtils.getCurrentOffsetDay(-i));
            date.add(time);

            Example example = new Example(User.class);
            example.createCriteria().andGreaterThanOrEqualTo("addTime", time + " 00:00:00").andLessThanOrEqualTo("addTime", time + " 23:59:59");
            value.add(super.count(example));
        }
        result.put("date", date);
        result.put("value", value);
        return new Gson().toJson(result);
    }
}
