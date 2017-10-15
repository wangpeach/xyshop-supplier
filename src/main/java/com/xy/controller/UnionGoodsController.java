package com.xy.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.xy.models.UnionGoods;
import com.xy.pojo.ParamsPojo;
import com.xy.services.UnionGoodService;
import com.xy.utils.Config;
import com.xy.utils.DateUtils;
import com.xy.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.StringUtil;

import java.util.List;

@RestController
@Scope(value = "prototype")
@RequestMapping(value = "goods/")
public class UnionGoodsController {

    @Autowired
    private UnionGoodService goodService;


    @ResponseBody
    @RequestMapping(value = "pagelist")
    private PageInfo<UnionGoods> pageList(@RequestBody JSONObject json) {
        ParamsPojo pj = new ParamsPojo(json);

        Condition cond = new Condition(UnionGoods.class);
        Example.Criteria cri = cond.createCriteria();
        if (StringUtil.isNotEmpty(pj.getSearch())) {
            String[] cols = {"name", "catName", "shopName", "goodNo"};
            String condition = " %s like ", arg = "'%" + pj.getSearch() + "%'";
            for (int i = 0; i < cols.length; i++) {
                cols[i] = String.format(condition, StringUtil.camelhumpToUnderline(cols[i])) + arg;
            }
            String or = org.apache.commons.lang3.StringUtils.join(cols, " or ");
            cri.andCondition("(" + or + ")");
        }
        if (StringUtils.isNotNull(pj.getParams().get("shopUuid"))) {
            cri.andEqualTo("shopUuid", pj.getParams().get("shopUuid"));
        }

        if (StringUtil.isNotEmpty(pj.getParams().get("status"))) {
            cri.andEqualTo("status", pj.getParams().get("status"));
        } else {
            cri.andNotEqualTo("status", "deleted");
        }

        cond.setOrderByClause(pj.getOrder());

        return goodService.selectPageInfoByCondition(cond, pj.getStart(), pj.getLength());
    }


    /**
     * 保存
     *
     * @param good
     * @return
     */
    @ResponseBody
    @RequestMapping("save")
    public int save(@ModelAttribute UnionGoods good) {
        return goodService.saveSelective(good);
    }


    /**
     * 修改
     *
     * @param entity
     * @return
     */
    @ResponseBody
    @RequestMapping("resave")
    public int reSave(@ModelAttribute UnionGoods entity) {
        entity.setStatus("wait");
        entity.setUpdateTime(DateUtils.getCurrentDate());
        return goodService.updateByPrimaryKeySelective(entity);
    }


    /**
     * 驳回
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("reject")
    public int reject(@ModelAttribute UnionGoods unionGoods) {
        unionGoods.setStatus("reject");
        return goodService.updateByPrimaryKeySelective(unionGoods);
    }


    /**
     * 审核通过 / 上架 / 解冻
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping({"resolve", "unfreeze", "up"})
    public int resolveOrUpOrUnFreeze(@ModelAttribute UnionGoods unionGoods) {
        unionGoods.setStatus("online");
        return goodService.updateByPrimaryKeySelective(unionGoods);
    }


    /**
     * 下架
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping("down")
    public int dwon(@ModelAttribute UnionGoods unionGoods) {
        unionGoods.setStatus("offline");
        return goodService.updateByPrimaryKeySelective(unionGoods);
    }


    /**
     * 冻结
     *
     * @param unionGoods
     * @return
     */
    @ResponseBody
    @RequestMapping("freeze")
    public int freeze(@ModelAttribute UnionGoods unionGoods) {
        unionGoods.setStatus("freeze");
        return goodService.updateByPrimaryKeySelective(unionGoods);
    }

    /**
     * 删除
     *
     * @param unionGoods
     * @return
     */
    @ResponseBody
    @RequestMapping("delete")
    public int delete(@ModelAttribute UnionGoods unionGoods) {
        unionGoods.setStatus("deleted");
        return goodService.updateByPrimaryKeySelective(unionGoods);
    }

    @ResponseBody
    @RequestMapping("list")
    public List<UnionGoods> list(@RequestParam String name) {
        Condition cond = new Condition(UnionGoods.class);
        Example.Criteria cri = cond.createCriteria();
        if (StringUtils.isNotNull(name)) {
            cri.andLike("name", "%" + name + "%");
        }
        cond.setOrderByClause(" sale_num desc");
        return goodService.selectListByCondition(cond);
    }


    @ResponseBody
    @RequestMapping("mapi/list")
    public List<UnionGoods> mApiList(@RequestParam String shop, @RequestParam String key, @RequestParam int offset) {
        Condition cond = new Condition(UnionGoods.class);
        Example.Criteria cri = cond.createCriteria();
        if (StringUtils.isNotNull(shop)) {
            cri.andEqualTo(shop);
        }
        if (StringUtils.isNotNull(key)) {
            cri.andLike("name", "%" + key + "%");
        }
        cond.setOrderByClause(" sale_num desc");
        return goodService.selectPageInfoByCondition(cond, offset, Config.LIMIT).getList();
    }

    @ResponseBody
    @RequestMapping("mapi/only")
    public UnionGoods only(@RequestParam String shop) {
        return goodService.handleResult(goodService.selectOnlyByKey(shop));
    }

//    @ResponseBody
//    @RequestMapping(value = "mapi/detail")
//    public Map<String, Object> mApiGoodDetail(@RequestParam String desFile) {
//        Map<String, Object> result = new HashMap<>();
//        desFile = Config.DESGOODSPATH + desFile.substring(desFile.lastIndexOf("/") + 1, desFile.length());
//        File file = new File(desFile);
//        try {
//            result.put("html", FileUtils.readFileToString(file, "UTF-8"));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return result;
//    }
}
