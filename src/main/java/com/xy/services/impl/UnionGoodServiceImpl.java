package com.xy.services.impl;

import com.github.pagehelper.PageInfo;
import com.xy.config.Config;
import com.xy.config.ResourcesConfig;
import com.xy.mapper.ShopMapper;
import com.xy.mapper.UnionGoodsMapper;
import com.xy.models.SearchRecord;
import com.xy.models.Shop;
import com.xy.models.UnionGoods;
import com.xy.services.SearchRecrodService;
import com.xy.services.UnionGoodService;
import com.xy.utils.*;
import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Condition;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created by rjora on 2017/7/16 0016.
 */
@Service
public class UnionGoodServiceImpl extends BaseServiceImpl<UnionGoods> implements UnionGoodService {


    @Autowired
    private SearchRecrodService searchRecrodService;

    @Autowired
    private UnionGoodsMapper goodsMapper;


    @Override
    public int saveSelective(UnionGoods entity) {
        entity = this.handleInfo(entity);
        entity.setUuid(StringUtils.getUuid());
        entity.setGoodNo(Config.GOOD_PREFIX + RandomUtil.getRandom(12, RandomUtil.TYPE.NUMBER));
        entity.setAddTime(DateUtils.getCurrentDate());
        entity.setStatus("wait");
        return super.saveSelective(entity);
    }

    @Override
    public int updateByPrimaryKeySelective(UnionGoods entity) {
        entity = this.handleInfo(entity);
        entity.setUpdateTime(DateUtils.getCurrentDate());
        entity.setStatus("wait");
        return super.updateByPrimaryKeySelective(entity);
    }

    @Override
    public List<UnionGoods> selectListByCondition(Condition condition) {
        return this.handleResult(super.selectListByCondition(condition));
    }

    @Override
    public PageInfo<UnionGoods> selectPageInfoByCondition(Condition condition, int offset, int limit) {
        PageInfo<UnionGoods> pi = super.selectPageInfoByCondition(condition, offset, limit);
        pi.setList(this.handleResult(pi.getList()));
        return pi;
    }


    @Override
    public int updateOnlineGood(UnionGoods arg) {
        return super.updateByPrimaryKeySelective(arg);
    }

    /**
     * 处理商品数据
     *
     * @param entity
     * @return
     */
    private UnionGoods handleInfo(UnionGoods entity) {
        try {
            Document etDoc = null, otDoc = null;
            Elements etImgEle = null, otImgEle = null;

            // 保存图片
            if (StringUtils.isNotNull(entity.getThumbImg())) {
                com.xy.utils.FileUtils.moveFile(ResourcesConfig.FILETEMP + entity.getThumbImg(), ResourcesConfig.PRODUCTIMGPATH);
            }
            if (StringUtils.isNotNull(entity.getMoreImg())) {
                List<String> moreImg = Arrays.asList(StringUtils.strToArray(entity.getMoreImg(), "#", ResourcesConfig.FILETEMP));
                com.xy.utils.FileUtils.moveFile(moreImg, ResourcesConfig.PRODUCTIMGPATH);
            }

            /**
             * 保存商铺详情到文件
             * 1.解析html，移动上传的临时图片到正式文件夹
             * 2.修改图片指向的地址
             * 3.保存至文件夹
             */
            List<String> etDetailImg = new ArrayList<>();
            if (StringUtils.isNotNull(entity.getDesFile())) {
                etDoc = Jsoup.parse(entity.getDesFile());
                etImgEle = etDoc.select("img");
                // 上传的图片名
                etImgEle.forEach(element -> {
                    etDetailImg.add(element.attr("title"));
                    com.xy.utils.FileUtils.moveFile(ResourcesConfig.FILETEMP + element.attr("title"), ResourcesConfig.PRODUCTIMGPATH);
                    element.attr("src", ResourcesConfig.PRODUCTIMGURL + element.attr("title"));
                });

                byte[] detail = etDoc.getElementsByTag("body").get(0).children().toString().getBytes();
                String detailName = StringUtils.getUuid() + ".spd";
                FileUtils.writeByteArrayToFile(new File(ResourcesConfig.DESGOODSPATH + detailName), detail, false);

                entity.setDesFile(detailName);
            }


            if (StringUtils.isNotNull(entity.getUuid())) {

                UnionGoods other = this.selectOnlyByKey(entity.getUuid());

                List<String> otherMoreImg = new ArrayList<>(), entityMoreImg = new ArrayList<>();
                if (StringUtils.isNotNull(entity.getMoreImg())) {
                    entityMoreImg = new ArrayList<>(Arrays.asList(StringUtils.strToArray(entity.getMoreImg(), "#")));
                    otherMoreImg = new ArrayList<>(Arrays.asList(StringUtils.strToArray(other.getMoreImg(), "#")));
                    // 得到用户删除的图片，从磁盘中删除
                    otherMoreImg.removeAll(entityMoreImg);
                }

                File shopDetilHtml = new File(ResourcesConfig.DESGOODSPATH + other.getDesFile());
                if (StringUtils.isNotNull(entity.getDesFile()) && shopDetilHtml.exists()) {
                    otDoc = Jsoup.parse(shopDetilHtml, "UTF-8");
                    if (otDoc != null && otDoc.childNodeSize() > 0) {
                        List<String> otDetailsImg = new ArrayList<>();
                        etImgEle = otDoc.select("img");
                        etImgEle.forEach(element -> {
                            otDetailsImg.add(element.attr("title"));
                        });
                        otDetailsImg.removeAll(etDetailImg);

                        otherMoreImg.addAll(otDetailsImg);
                    }

                    if (StringUtils.isNotNull(entity.getThumbImg()) && !other.getThumbImg().equals(entity.getThumbImg())) {
                        otherMoreImg.add(other.getThumbImg());
                    }

                    otherMoreImg.forEach(s -> {
                        com.xy.utils.FileUtils.deleteFile(ResourcesConfig.PRODUCTIMGPATH + s);
                    });
                    com.xy.utils.FileUtils.deleteFile(ResourcesConfig.DESGOODSPATH + other.getDesFile());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return entity;
    }

    @Override
    public PageInfo<UnionGoods> selectPageInfo(UnionGoods entity, int offset, int limit) {
         PageInfo<UnionGoods> result = super.selectPageInfo(entity, (offset-1) * limit, limit);
         result.setList(this.handleResult(result.getList()));
         return result;
    }

    @Override
    public List<UnionGoods> mApiList(String user, String cats, String key, String position, String distance, String orderBy, int offset, int limit) {
        // 计算距离

        String[] latlng = position.split(",");

        // 关键字搜索
        if (StringUtils.isNotNull(key)) {
            // 添加用户搜索记录
            SearchRecord record = new SearchRecord();
            if(StringUtils.isNull(user)){
                user = "游客";
            }
            // 所有关键字记录
            record.setUuid(StringUtils.getUuid());
            record.setUserUuid(user);
            record.setKeyWord(key);
            record.setAddTime(DateUtils.getCurrentDate());
            searchRecrodService.saveSelective(record);
        }
        Map<String, Object> args = new HashMap<>();
        args.put("name", key);
        args.put("shopCatId", cats);
        args.put("distance", distance);
        args.put("lat", latlng[1]);
        args.put("log", latlng[0]);
        args.put("orderBy", orderBy);
        args.put("offset", (offset-1) * limit);
        args.put("limit", limit);

        List<UnionGoods> goods = goodsMapper.searchShopDetails(args);
//        super.selectPageInfoByCondition(cond, offset, limit).getList()
        return this.handleResult(goods);
    }

    /**
     * 处理查询结果
     *
     * @param goods
     * @return
     */
    @Override
    public List<UnionGoods> handleResult(List<UnionGoods> goods) {
        goods.forEach(s -> {
            s = this.handleResult(s);
        });
        return goods;
    }

    @Override
    public UnionGoods handleResult(UnionGoods good) {
        good.setThumbImgShow(ResourcesConfig.PRODUCTIMGURL + good.getThumbImg());
        good.setMoreImgShow(org.apache.commons.lang3.StringUtils.join(Arrays.asList(StringUtils.strToArray(good.getMoreImg(), "#", ResourcesConfig.PRODUCTIMGURL)), "#"));
        good.setDesFile(ResourcesConfig.DESGOODSURL + good.getDesFile());
        return good;
    }
}
