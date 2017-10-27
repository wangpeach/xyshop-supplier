package com.xy.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.xy.models.UserCoupon;
import com.xy.pojo.ParamsPojo;
import com.xy.services.UserCouponService;
import com.xy.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * UserCouponController
 *
 * @author Administrator
 * @date 2017/10/27 9:31
 * @description 用户持有的优惠卷查询控制器
 */
@Controller
@RequestMapping("usercoupon/")
public class UserCouponController {

    @Autowired
    private UserCouponService couponService;

    @ResponseBody
    @RequestMapping("list")
    public PageInfo<UserCoupon> list(@RequestBody JSONObject json) {
        ParamsPojo pj = new ParamsPojo(json);
        Condition cond = new Condition(UserCoupon.class);
        Example.Criteria cri = cond.createCriteria();

        String userid = "userid", value = pj.getParams().get(userid);
        if (StringUtils.isNotNull(value)) {
            cri.andEqualTo(userid, value);
        }
        cond.setOrderByClause(pj.getOrder());
        return couponService.selectPageInfoByCondition(cond, pj.getStart(), pj.getLength());
    }


    @ResponseBody
    @RequestMapping("mapi/list")
    public List<UserCoupon> list(@RequestParam String user, @RequestParam String shopId, @RequestParam String cate, @RequestParam String good) {
        return couponService.selectList(user, shopId, cate, good);
    }

}
