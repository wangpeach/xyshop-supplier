package com.xy.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.xy.config.ResourcesConfig;
import com.xy.models.ShopCategory;
import com.xy.pojo.ParamsPojo;
import com.xy.services.ShopCategroyService;
import com.xy.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.StringUtil;

import java.util.List;

/**
 * Created by rjora on 2017/7/13 0013.
 */
@RestController
@RequestMapping(value = "shop-categroy/")
public class ShopCategoryController {

    @Autowired
    private ShopCategroyService shopCategroyService;


    /**
     * 搜索
     * @param json
     * @return
     */
    @PostMapping(value = "pagelist")
    public PageInfo<ShopCategory> pageList(@RequestBody JSONObject json) {
        ParamsPojo pj = new ParamsPojo(json);

        Condition cd = new Condition(ShopCategory.class);
        Example.Criteria criteria = cd.createCriteria();
        if (StringUtil.isNotEmpty(pj.getSearch())) {
            criteria.andLike("name", "%" + pj.getSearch() + "%");
        }

        if(StringUtils.noEmpty(json.getString("catid"))) {
            criteria.andEqualTo("catPid", json.getString("catid"));
        } else {
            criteria.andEqualTo("catPid", "root");
        }

        cd.setOrderByClause(pj.getOrder());
        return shopCategroyService.selectPageInfoByCondition(cd, pj.getStart(), pj.getLength());
    }

    @ResponseBody
    @PostMapping("list")
    public List<ShopCategory> list(@ModelAttribute ShopCategory category) {
        Condition cond = new Condition(ShopCategory.class);

        if(StringUtils.isNotNull(category.getName())) {
            cond.createCriteria().andLike("name", "%"+ category.getName() +"%");
        }

        return shopCategroyService.selectListByCondition(cond);
    }

    /**
     *
     * @return
     */
    @RequestMapping(value = "mapi/list")
    public @ResponseBody
    List<ShopCategory> MApilist(@ModelAttribute ShopCategory category) {
        return shopCategroyService.selectMApiList();
    }

    /**
     * 添加一级分类
     *
     * @param cate
     * @return
     */
    @RequestMapping(value = "save-lv1")
    public @ResponseBody
    int save(@ModelAttribute ShopCategory cate) {

        cate.setUuid(StringUtils.getUuid());
        cate.setCatId(RandomUtil.getRandom(8, RandomUtil.TYPE.CAPITAL));
        cate.setLevel(1);
        cate.setCatPid("root");
        cate.setAddTime(DateUtils.getCurrentDate());

        // 移动文件到目标文件夹
        FileUtils.moveFile(ResourcesConfig.FILETEMP + cate.getIconImg(), ResourcesConfig.ICONPATH);

        return shopCategroyService.saveSelective(cate);
    }



    /**
     * 添加二级分类
     *
     * @return
     */
    @RequestMapping(value = "save-lv2")
    public @ResponseBody
    int save_lev2(@ModelAttribute ShopCategory cate) {

        cate.setUuid(StringUtils.getUuid());
        cate.setCatId(cate.getCatPid() + "_" + RandomUtil.getRandom(6, RandomUtil.TYPE.CAPITAL));
        cate.setLevel(2);
        cate.setAddTime(DateUtils.getCurrentDate());

        // 移动文件到目标文件夹
        FileUtils.moveFile(ResourcesConfig.FILETEMP + cate.getIconImg(), ResourcesConfig.ICONPATH);

        return shopCategroyService.saveSelective(cate);
    }

    /**
     * 修改 shop 分类
     * @param cate
     * @return
     */
    @RequestMapping(value = "revise")
    public @ResponseBody
    int revise(@ModelAttribute ShopCategory cate) {
        ShopCategory sc = shopCategroyService.selectOnlyByKey(cate.getUuid());
        if(StringUtils.isNull(sc.getIconImg()) || !sc.getIconImg().equals(cate.getIconImg())) {
            // 删除原图片
            FileUtils.deleteFile(ResourcesConfig.ICONPATH + sc.getIconImg());
            // 移动文件到目标文件夹
            FileUtils.moveFile(ResourcesConfig.FILETEMP + cate.getIconImg(), ResourcesConfig.ICONPATH);
        }
        return shopCategroyService.updateByPrimaryKeySelective(cate);
    }

    /**
     * 删除 shop 分类
     * @param uuid
     * @return
     */
    @RequestMapping(value = "del")
    public @ResponseBody int del(@RequestParam String uuid) {
        return shopCategroyService.deleteByPrimaryKey(uuid);
    }
}
