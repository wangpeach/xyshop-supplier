package com.xy.services.impl;

import com.github.pagehelper.PageInfo;
import com.xy.models.UnionGoods;
import com.xy.services.UnionGoodService;
import com.xy.utils.*;
import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Condition;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by rjora on 2017/7/16 0016.
 */
@Service
public class UnionGoodServiceImpl extends BaseServiceImpl<UnionGoods> implements UnionGoodService {

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
                com.xy.utils.FileUtils.moveFile(Config.FILETEMP + entity.getThumbImg(), Config.PRODUCTIMGPATH);
            }
            if (StringUtils.isNotNull(entity.getMoreImg())) {
                List<String> moreImg = Arrays.asList(StringUtils.strToArray(entity.getMoreImg(), "#", Config.FILETEMP));
                com.xy.utils.FileUtils.moveFile(moreImg, Config.PRODUCTIMGPATH);
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
                    com.xy.utils.FileUtils.moveFile(Config.FILETEMP + element.attr("title"), Config.PRODUCTIMGPATH);
                    element.attr("src", Config.PRODUCTIMGURL + element.attr("title"));
                });

                byte[] detail = etDoc.getElementsByTag("body").get(0).children().toString().getBytes();
                String detailName = StringUtils.getUuid() + ".spd";
                FileUtils.writeByteArrayToFile(new File(Config.DESGOODSPATH + detailName), detail, false);

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

                File shopDetilHtml = new File(Config.DESGOODSPATH + other.getDesFile());
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
                        com.xy.utils.FileUtils.deleteFile(Config.PRODUCTIMGPATH + s);
                    });
                    com.xy.utils.FileUtils.deleteFile(Config.DESGOODSPATH + other.getDesFile());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return entity;
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
        good.setThumbImgShow(Config.PRODUCTIMGURL + good.getThumbImg());
        good.setMoreImgShow(org.apache.commons.lang3.StringUtils.join(Arrays.asList(StringUtils.strToArray(good.getMoreImg(), "#", Config.PRODUCTIMGURL)), "#"));
        good.setDesFile(Config.DESGOODSURL + good.getDesFile());
        return good;
    }
}
