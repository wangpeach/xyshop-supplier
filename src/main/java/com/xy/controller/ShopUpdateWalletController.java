package com.xy.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.xy.models.ShopUpdateWallet;
import com.xy.pojo.ParamsPojo;
import com.xy.services.ShopUpdateWalletService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.StringUtil;


@RestController
@Scope(value = "prototype")
@RequestMapping(value = "updateWallet/")
public class ShopUpdateWalletController {

    @Autowired
    private ShopUpdateWalletService updateWalletService;


    @RequestMapping(value = "pagelist")
    public @ResponseBody
    PageInfo<ShopUpdateWallet> pageList(@RequestBody JSONObject json) {
        ParamsPojo pj = new ParamsPojo(json);
        Condition cond = new Condition(ShopUpdateWallet.class);

        Example.Criteria cri = cond.createCriteria();
        if (StringUtils.isNotEmpty(pj.getSearch())) {
            String[] cols = {"shopName", "cartName", "cartOpenAddr"};
            String condition = "%s like", arg = "%"+ pj.getSearch() +"%";
            for (String item : cols) {
                cri.andCondition(String.format(condition, StringUtil.camelhumpToUnderline(item)), arg);
            }
        }
        cri.andEqualTo("status", "wait");
        return updateWalletService.selectPageInfoByCondition(cond, pj.getStart(), pj.getLength());
    }


    @RequestMapping(value = "update")
    public @ResponseBody
    int updateWallet(@ModelAttribute ShopUpdateWallet updateWallet) {
        return updateWalletService.updateByPrimaryKeySelective(updateWallet);
    }


    /**
     * 审核通过
     *
     * @param updateWallet
     * @return
     */
    @RequestMapping(value = "pass")
    public @ResponseBody
    int pass(@ModelAttribute ShopUpdateWallet updateWallet) {
        return updateWalletService.updatePass(updateWallet);
    }


    /**
     * 驳回
     *
     * @param updateWallet
     * @return
     */
    @RequestMapping(value = "fail")
    public @ResponseBody
    int fail(@ModelAttribute ShopUpdateWallet updateWallet) {
        return updateWalletService.updateFail(updateWallet);
    }

}
