package com.xy.services.impl;

import com.github.pagehelper.PageInfo;
import com.xy.config.OtherConfig;
import com.xy.config.ResourcesConfig;
import com.xy.models.Shop;
import com.xy.models.ShopWallet;
import com.xy.pojo.ParamsPojo;
import com.xy.services.ShopService;
import com.xy.services.ShopCategroyService;
import com.xy.services.ShopWalletService;
import com.xy.utils.*;
import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.StringUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by rjora on 2017/7/14 0014.
 */
@Service
public class ShopServiceImpl extends BaseServiceImpl<Shop> implements ShopService {

    @Autowired
    private ShopWalletService shopWalletService;
    @Autowired
    private ShopCategroyService shopCategroyService;


    @Override
    public int saveSelective(Shop entity) {
        entity = this.handleInfo(entity);
        entity.setUuid(StringUtils.getUuid());
        entity.setAddTime(DateUtils.getCurrentDate());
        entity.setPassword(Md5Util.md5UpperCase("000000"));
        int result = super.saveSelective(entity);
        // 创建钱包信息
        ShopWallet wallet = new ShopWallet();
        wallet.setShopUuid(entity.getUuid());
        wallet.setMoney(0);
        wallet.setUuid(StringUtils.getUuid());
        result = shopWalletService.saveSelective(wallet);
        return result;
    }

    @Override
    public int updateByPrimaryKeySelective(Shop entity) {
        this.handleInfo(entity);
        int result = super.updateByPrimaryKeySelective(entity);
        return result;
    }

    @Override
    public Shop selectOnly(Shop entity) {
        return this.handleInfo(super.selectOnly(entity));
    }

    @Override
    public List<Shop> selectListByParams(ParamsPojo pj) {
        return this.handleResult(super.selectListByCondition(this.createCond(pj)), null);
    }

    @Override
    public List<Shop> selectList(Shop entity) {
        return this.handleResult(super.selectList(entity), null);
    }

    @Override
    public List<Shop> selectListByCondition(Condition condition) {
        return this.handleResult(super.selectListByCondition(condition), null);
    }

    @Override
    public PageInfo<Shop> selectPageListByParams(ParamsPojo pj) {
        PageInfo<Shop> pageInfo = super.selectPageInfoByCondition(this.createCond(pj), pj.getStart(), pj.getStart());
        pageInfo.setList(this.handleResult(pageInfo.getList(), null));
        return pageInfo;
    }

    @Override
    public List<Shop> mApiList(String cats, String key, String position, String distance, String orderBy, int offset, int limit) {
        // 计算距离
        String[] latlng = position.split(",");
        StringBuilder sbr = new StringBuilder();
        sbr.append(" 2 * ASIN(");
        sbr.append("     SQRT(");
        sbr.append("        POWER(");
        sbr.append("            SIN(");
        sbr.append("                (latitude - " + latlng[1] + ") * 3.14159265359 / 180 / 2");
        sbr.append("            ),");
        sbr.append("            2");
        sbr.append("         ) + COS(latitude * 3.14159265359 / 180) * COS(" + latlng[1] + " * 3.14159265359 / 180) * POWER(");
        sbr.append("            SIN(");
        sbr.append("                (longitude - " + latlng[0] + ") * 3.14159265359 / 180 / 2");
        sbr.append("            ),");
        sbr.append("            2");
        sbr.append("        )");
        sbr.append("    )");
        sbr.append(" ) * 6378.137 * 1000");

        Condition cond = new Condition(Shop.class);
        Example.Criteria cri = cond.createCriteria();
        cri.andEqualTo("status", "online");
        if (StringUtils.isNotNull(cats)) {
            if (cats.contains(",")) {
                Iterable<String> catIds = Arrays.asList(StringUtils.strToArray(cats, ","));
                cri.andIn("shopCatId", catIds);
            } else {
                cri.andEqualTo("shopCatId", cats);
            }
        }

        // 关键字搜索
        if (StringUtils.isNotNull(key)) {
            String[] cols = {"name", "shopCatName"};
            String condition = " %s like ", arg = "'%" + key + "%'";

            for (int i = 0; i < cols.length; i++) {
                cols[i] = String.format(condition, StringUtil.camelhumpToUnderline(cols[i])) + arg;
            }

            String or = org.apache.commons.lang3.StringUtils.join(cols, " or ");
            cri.andCondition("(" + or + ")");
        }

        // 限制搜索距离
        if (StringUtils.isNotNull(distance)) {
            cri.andCondition("(" + sbr.toString() + ") <= " + distance);
        }
        // 按条件排序
        if (StringUtils.isNotNull(orderBy)) {
            if ("0".equals(orderBy)) {
                // 按距离从进到远排序
                cond.setOrderByClause(sbr.toString());
            } else {
                // 人气/购买量 排序
                cond.setOrderByClause(" total_sale_num desc");
            }
        }
        return this.handleResult(super.selectPageInfoByCondition(cond, offset, limit).getList(), position);
    }

    @Override
    public int del(Shop shop) {
        return super.updateByPrimaryKeySelective(shop);
    }

    /**
     * 商铺逾期 15 天 自动冻结
     */
    @Override
    public void autoFreeze() {
        Condition condition = new Condition(Shop.class);
        condition.createCriteria().andLessThan(" TIMESTAMPDIFF(day, now(), endTime)", -OtherConfig.SHOP_OVERDUE);
        List<Shop> shops = super.selectListByCondition(condition);
        if (shops != null && !shops.isEmpty()) {
            shops.forEach(shop -> {
                shop.setStatus("freeze");
                super.updateByPrimaryKeySelective(shop);
            });
        }
    }

    /**
     * 查询条件
     *
     * @param pj
     * @return
     */
    private Condition createCond(ParamsPojo pj) {
        Condition cond = new Condition(Shop.class);
        cond.setOrderByClause(pj.getOrder());


        Example.Criteria cri = cond.createCriteria();
        Object status = pj.getParams().get("status");
        if (StringUtils.isNotNull(status)) {
            cri.andEqualTo("status", status.toString());
        } else {
            cri.andNotEqualTo("status", "deleted");
        }

        if (StringUtils.isNotNull(pj.getSearch())) {
            String[] cols = {"name", "shopPhone", "ownerPhone", "shopCatName"};
            String condition = " %s like ", arg = "'%" + pj.getSearch() + "%'";
            for (int i = 0; i < cols.length; i++) {
                cols[i] = String.format(condition, StringUtil.camelhumpToUnderline(cols[i])) + arg;
            }

            String or = org.apache.commons.lang3.StringUtils.join(cols, " or ");
            cri.andCondition("(" + or + ")");
        }
        return cond;
    }

    /**
     * 处理添加/修改的商铺图片数据
     *
     * @param entity
     */
    private Shop handleInfo(Shop entity) {
        try {
            if (entity == null) {
                return null;
            }
            Document etDoc = null, otDoc = null;
            Elements etImgEle = null, otImgEle = null;

            // 保存图片
            if (StringUtils.isNotNull(entity.getThumbImg())) {
                com.xy.utils.FileUtils.moveFile(ResourcesConfig.FILETEMP + entity.getThumbImg(), ResourcesConfig.SHOPPATH);
            }
            if (StringUtils.isNotNull(entity.getMoreImg())) {
                List<String> moreImg = Arrays.asList(StringUtils.strToArray(entity.getMoreImg(), "#", ResourcesConfig.FILETEMP));
                com.xy.utils.FileUtils.moveFile(moreImg, ResourcesConfig.SHOPPATH);
            }

            /**
             * 保存商铺详情到文件
             * 1.解析html，移动上传的临时图片到正式文件夹
             * 2.修改图片指向的地址
             * 3.保存至文件夹
             */
            List<String> etDetailImg = new ArrayList<>();
            if (StringUtils.isNotNull(entity.getShopDetail())) {
                etDoc = Jsoup.parse(entity.getShopDetail());
                etImgEle = etDoc.select("img");
                // 上传的图片名
                etImgEle.forEach(element -> {
                    etDetailImg.add(element.attr("title"));
                    com.xy.utils.FileUtils.moveFile(ResourcesConfig.FILETEMP + element.attr("title"), ResourcesConfig.SHOPPATH);
                    element.attr("src", ResourcesConfig.SHOPURL + element.attr("title"));
                });

                byte[] detail = etDoc.getElementsByTag("body").get(0).children().toString().getBytes();
                String detailName = StringUtils.getUuid() + ".spd";
                FileUtils.writeByteArrayToFile(new File(ResourcesConfig.DESSHOPPATH + detailName), detail, false);

                entity.setShopDetail(detailName);
            }


            if (StringUtils.isNotNull(entity.getUuid())) {

                Shop other = this.selectOnlyByKey(entity.getUuid());

                List<String> otherMoreImg = new ArrayList<>(), entityMoreImg = new ArrayList<>();
                if (StringUtils.isNotNull(entity.getMoreImg())) {
                    entityMoreImg = new ArrayList<>(Arrays.asList(StringUtils.strToArray(entity.getMoreImg(), "#")));
                    otherMoreImg = new ArrayList<>(Arrays.asList(StringUtils.strToArray(other.getMoreImg(), "#")));
                    // 得到用户删除的图片，从磁盘中删除
                    otherMoreImg.removeAll(entityMoreImg);
                }

                File shopDetilHtml = new File(ResourcesConfig.DESSHOPPATH + other.getShopDetail());
                if (StringUtils.isNotNull(entity.getShopDetail()) && shopDetilHtml.exists()) {
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
                        com.xy.utils.FileUtils.deleteFile(ResourcesConfig.SHOPPATH + s);
                    });
                    com.xy.utils.FileUtils.deleteFile(ResourcesConfig.DESSHOPPATH + other.getShopDetail());
                }
            }

            entity.setShopCatId(shopCategroyService.selectOnlyByKey(entity.getShopCatUuid()).getCatId());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return entity;
    }

    /**
     * 处理查询的多条商铺数据
     *
     * @param shops
     * @return
     */
    public List<Shop> handleResult(List<Shop> shops, String lanlng) {
        shops.forEach(s -> {
            s = this.handleResult(s, lanlng);
        });
        return shops;
    }

    /**
     * 处理查询出的商铺信息
     *
     * @param shop
     * @return
     */
    public Shop handleResult(Shop shop, String lanlng) {
        shop.setThumbImgShow(ResourcesConfig.SHOPURL + shop.getThumbImg());
        String moreImg = org.apache.commons.lang3.StringUtils.join(StringUtils.strToArray(shop.getMoreImg(), "#", ResourcesConfig.SHOPURL), "#");
        shop.setMoreImgShow(moreImg);
        shop.setShopDetail(ResourcesConfig.DESSHOPURL + shop.getShopDetail());
        if (StringUtils.isNotNull(lanlng)) {
            // 计算两坐标距离
            String[] _lanlng = lanlng.split(",");
            shop.setDistance(Utils.distance(shop.getLongitude().doubleValue(), shop.getLatitude().doubleValue(), Double.valueOf(_lanlng[0]), Double.valueOf(_lanlng[1])));
        }
        return shop;
    }
}
