package com.xy.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.xy.models.Category;
import com.xy.pojo.ParamsPojo;
import com.xy.services.CategoryService;
import com.xy.config.ResourcesConfig;
import com.xy.utils.DateUtils;
import com.xy.utils.FileUtils;
import com.xy.utils.RandomUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.StringUtil;

import java.util.List;


/**
 * 商品类别服务
 */
@RestController
@Scope(value = "prototype")
@RequestMapping(value = "category/")
public class CategoryController{

    @Autowired
    private CategoryService categoryService;

    /**
     * 加载所有类别
     *
     * @param json the json
     * @return the page info
     */
    @PostMapping(value = "pagelist")
    public PageInfo<Category> pageList(@RequestBody JSONObject json) {
        ParamsPojo pj = new ParamsPojo(json);

        Condition cd = new Condition(Category.class);
        Example.Criteria criteria = cd.createCriteria();
        if (StringUtil.isNotEmpty(pj.getSearch())) {
            criteria.andLike("name", "%" + pj.getSearch() + "%");
        }

        if(com.xy.utils.StringUtils.noEmpty(json.getString("catid"))) {
            criteria.andEqualTo("catPid", json.getString("catid"));
        } else {
            criteria.andIsNull("catPid");
        }

        cd.setOrderByClause(pj.getOrder());

        PageInfo<Category> scs = categoryService.selectPageInfoByCondition(cd, pj.getStart(), pj.getLength());
        scs.getList().forEach(s -> s.setIconImgShow(ResourcesConfig.ICONURL + s.getIconImg()));
        return scs;
    }

    /**
     * 通过类别名搜索
     *
     * @param key the key
     * @return the list
     */
    @PostMapping(value = "list")
    public @ResponseBody
    List<Category> list(@RequestParam String key) {
        Condition cond = new Condition(Category.class);
        if(StringUtils.isNotEmpty(key)) {
            cond.createCriteria().andLike("name", "%"+ key +"%");
        }
        return categoryService.selectListByCondition(cond);
    }


    /**
     * 保存一级类别
     *
     * @param category the category
     * @return the int
     */
    @ResponseBody
    @RequestMapping("save-lv1")
    public int saveLv1(@ModelAttribute Category category) {
        category.setUuid(com.xy.utils.StringUtils.getUuid());
        category.setLevel(1);
        category.setCatId(RandomUtil.getRandom(8, RandomUtil.TYPE.CAPITAL));
        category.setAddTime(DateUtils.getCurrentDate());
        FileUtils.moveFile(ResourcesConfig.FILETEMP + category.getIconImg(), ResourcesConfig.ICONPATH);
        return categoryService.saveSelective(category);
    }

    /**
     * 保存二级类别
     *
     * @param name    the name
     * @param iconImg the icon img
     * @param attr    the attr
     * @return the int
     */
    @ResponseBody
    @RequestMapping("save-lv2")
    public int saveLv2(@RequestParam String name, @RequestParam String iconImg, @RequestParam String attr) {
        Category cate = new Category();
        cate.setName(name);
        cate.setIconImg(iconImg);
        cate.setUuid(com.xy.utils.StringUtils.getUuid());
        cate.setLevel(2);
        cate.setCatId(cate.getCatPid() +"_"+ RandomUtil.getRandom(8, RandomUtil.TYPE.CAPITAL));
        cate.setAddTime(DateUtils.getCurrentDate());
        FileUtils.moveFile(ResourcesConfig.FILETEMP + cate.getIconImg(), ResourcesConfig.ICONPATH);
        int result = categoryService.saveSelective(cate);



        return result;
    }


    /**
     * 更新
     *
     * @param uuid    the uuid
     * @param name    the name
     * @param iconImg the icon img
     * @param attr    the attr
     * @param delAttr the del attr
     * @param delOpt  the del opt
     * @return the int
     */
    @RequestMapping(value = "revise")
    public @ResponseBody
    int revise( @RequestParam String uuid, @RequestParam String name, @RequestParam String iconImg, @RequestParam String attr, @RequestParam String delAttr, @RequestParam String delOpt) {
        Category sc = categoryService.selectOnlyByKey(uuid);
        if(com.xy.utils.StringUtils.isNull(sc.getIconImg()) || !sc.getIconImg().equals(iconImg)) {
            // 删除原图片
            FileUtils.deleteFile(ResourcesConfig.ICONPATH + sc.getIconImg());
            // 移动文件到目标文件夹
            FileUtils.moveFile(ResourcesConfig.FILETEMP + iconImg, ResourcesConfig.ICONPATH);

            sc.setIconImg(iconImg);
        }

        sc.setName(name);

        int result = categoryService.updateByPrimaryKeySelective(sc);

        // 修改属性

        return result;
    }


    /**
     * 删除
     *
     * @param uuid the uuid
     * @return the int
     */
    @RequestMapping(value = "del")
    public @ResponseBody int del(@RequestParam String uuid) {
        return categoryService.deleteByPrimaryKey(uuid);
    }

}
