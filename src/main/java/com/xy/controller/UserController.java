package com.xy.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.xy.models.Ad;
import com.xy.models.User;
import com.xy.pojo.ParamsPojo;
import com.xy.services.IAdService;
import com.xy.services.IUserService;
import com.xy.utils.DateUtils;
import com.xy.utils.Md5Util;
import com.xy.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by rjora on 2017/6/29 0029.
 */
@Scope("prototype")
@Controller
@RequestMapping("user/")
public class UserController {

    @Autowired
    private IUserService userService;

    @Autowired
    private IAdService adService;

    @ResponseBody
    @RequestMapping(value = "mapi/login")
    public User exec(@ModelAttribute User user) {
        if(StringUtils.isNotNull(user.getPhoneNum()) && StringUtils.isNotNull(user.getPassword())) {
            user.setPassword(Md5Util.md5UpperCase(user.getPassword()));
        }
        return userService.selectOnly(user);
    }

    @ResponseBody
    @RequestMapping(value = "mapi/register")
    public Map<String, Object> login(@ModelAttribute User user) {
        Map<String, Object> result = new HashMap<>();

        User other = new User();
        other.setPhoneNum(user.getPhoneNum());
        if(userService.count(user) == 0) {
            user.setUuid(StringUtils.getUuid());
            user.setPassword(Md5Util.md5UpperCase(user.getPassword()));
            user.setAddTime(DateUtils.getCurrentDate());
            if(userService.saveSelective(user) > 0) {
                result.put("status", true);
                result.put("msg", "注册成功");
            } else {
                result.put("status", false);
                result.put("msg", "注册失败");
            }
        } else {
            result.put("status", false);
            result.put("msg", "手机号码已注册");
        }
        return result;
    }

    @ResponseBody
    @RequestMapping("mapi/reload")
    public User reload(@RequestParam String uuid) {
        return userService.selectOnlyByKey(uuid);
    }

    /**
     * 金币奖励
     * @return
     */
    @ResponseBody
    @RequestMapping("mapi/coin-reward")
    public ResJsonVo coinReward(@RequestParam String user, @RequestParam String ad) {
        ResJsonVo jsonVo = new ResJsonVo();
        User m_user = userService.selectOnlyByKey(user);
        Ad m_ad = adService.selectOnlyByKey(ad);
        if(m_ad != null && user != null) {
            if(StringUtils.isNotNull(m_ad.getCoin())) {
                m_user.setCoin(m_user.getCoin().add(m_ad.getCoin()));
                userService.updateByPrimaryKeySelective(m_user);
                jsonVo.setData(m_user.getCoin());
            }
        } else {
            jsonVo.setParaCheckFail();
        }
        return jsonVo;
    }




    @ResponseBody
    @RequestMapping("list")
    public PageInfo<User> pageList(@RequestBody JSONObject jsonObject) {
        ParamsPojo pj = new ParamsPojo(jsonObject);
        userService.selectPageListByParams(pj);
        return null;
    }
}
