package com.xy.controller;

import com.xy.models.UserCollect;
import com.xy.services.UserCollectService;
import com.xy.utils.DateUtils;
import com.xy.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import tk.mybatis.mapper.entity.Condition;

import java.util.List;

/**
 * UserCollectController
 *
 * @author Administrator
 * @date 2017/11/1 18:03
 * @description
 */
@Controller
@RequestMapping("collect/")
public class UserCollectController {

    @Autowired
    private UserCollectService collectService;


    @ResponseBody
    @RequestMapping("mapi/list")
    public List<UserCollect> list(@RequestParam String user, @RequestParam String type) {
        Condition cond = new Condition(UserCollect.class);
        cond.createCriteria().andEqualTo("userUuid", user).andEqualTo("collectType", type);
        List<UserCollect> collects = collectService.selectListByCondition(cond);
        return collects;
    }

    @ResponseBody
    @RequestMapping(value = "mapi/sd", produces = "application/text")
    public String save(@ModelAttribute UserCollect sender, @RequestParam String method) {
        if("del".equals(method)) {
            if(collectService.delete(sender) > 0) {
                return "success";
            }
        } else {
            sender.setUuid(StringUtils.getUuid());
            sender.setAddTime(DateUtils.getCurrentDate());
            sender.setStatus("online");
            if(collectService.saveSelective(sender) > 0) {
                return "success";
            }
        }
        return "success";
    }


}
