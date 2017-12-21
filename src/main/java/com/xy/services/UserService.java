package com.xy.services;


import com.xy.models.User;

import java.util.Map;

/**
 * Created by rjora on 2017/6/29 0029.
 */
public interface UserService extends BaseService<User> {

    /**
     * 订单统计
     * @param type M, W, Custom
     *             年，月，近七日，自定义
     * @param params type=M : year 年份
     *                   =W : 没有参数，默认近七天数据
     *                   =Custom : 自定义选择
     *
     * @return JSON 格式
     */
    String charts(String type, Map<String, Object> params);
}
