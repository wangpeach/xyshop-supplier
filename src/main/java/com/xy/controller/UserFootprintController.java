package com.xy.controller;

import com.github.pagehelper.PageInfo;
import com.xy.config.Config;
import com.xy.models.UnionGoods;
import com.xy.models.UserFootprint;
import com.xy.services.UnionGoodService;
import com.xy.services.UserFootprintService;
import com.xy.utils.DateUtils;
import com.xy.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Condition;

@Scope("prototype")
@RestController
@RequestMapping("userfoot/")
public class UserFootprintController {

    @Autowired
    private UnionGoodService goodService;

    @Autowired
    private UserFootprintService userFootprintService;

    /**
     * 用户浏览足迹
     * @param good
     * @param user
     */
    @RequestMapping("mapi/addfoot")
    public void mapiUserFoot(@RequestParam String good, @RequestParam String user) {
        UserFootprint foot = null;
        Condition cond = new Condition(UserFootprint.class);
        cond.createCriteria().andEqualTo("goodUuid", good).andEqualTo("userUuid", user);
        foot = userFootprintService.selectOnly(cond, 0);
        if(foot != null) {
            foot.setAddTime(DateUtils.getCurrentDate());
            userFootprintService.updateByPrimaryKeySelective(foot);
        } else {
            foot = new UserFootprint();
            UnionGoods unionGoods = goodService.selectOnlyByKey(good);
            foot.setGoodUuid(good);
            foot.setUserUuid(user);
            foot.setUuid(StringUtils.getUuid());
            foot.setGoodName(unionGoods.getName());
            foot.setThumbImg(unionGoods.getThumbImg());
            foot.setAddTime(DateUtils.getCurrentDate());
            userFootprintService.saveSelective(foot);
        }
    }

    @ResponseBody
    @RequestMapping("mapi/list")
    public PageInfo<UserFootprint> list(@RequestParam String user, @RequestParam int offset){
        UserFootprint foot = new UserFootprint();
        foot.setUserUuid(user);
        return userFootprintService.selectPageInfo(foot, offset, Config.LIMIT);
    }
}


