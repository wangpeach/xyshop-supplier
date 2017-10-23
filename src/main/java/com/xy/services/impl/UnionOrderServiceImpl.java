package com.xy.services.impl;

import com.xy.models.*;
import com.xy.services.*;
import com.xy.utils.DateUtils;
import com.xy.utils.RandomUtil;
import com.xy.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rjora on 2017/7/16 0016.
 */
@Service
public class UnionOrderServiceImpl extends BaseServiceImpl<UnionOrders> implements UnionOrderService {

    @Autowired
    private ShopService shopService;

    @Autowired
    private UnionGoodService goodService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserCouponService couponService;


    @Override
    public int saveSelective(String userId, String shopId, String goodId, int num) {
        UnionOrders order = new UnionOrders();

        User user = userService.selectOnlyByKey(userId);
        Shop shop = shopService.selectOnlyByKey(shopId);
        UnionGoods entity = new UnionGoods();
        entity.setUuid(goodId);
        entity.setShopUuid(shopId);
        UnionGoods good = goodService.selectOnly(entity);

        if (good != null && shop != null && good != null) {
            order.setUuid(StringUtils.getUuid());
            order.setAddTime(DateUtils.getCurrentDate());

            order.setUserUuid(user.getUuid());
            order.setUserName(user.getName());
            order.setShopUuid(shop.getUuid());
            order.setShopName(shop.getName());
            order.setOrderNo(RandomUtil.getRandom(17, RandomUtil.TYPE.NUMBER));
            order.setGoodsUuid(entity.getUuid());
            order.setGoodsNum(num);
            order.setJudged(false);
            order.setStatus("waitPay");
            // 订单总金额，应付金额
            order.setTotalPrice(good.getPrice().multiply(BigDecimal.valueOf(num)));
            // 密码串码, 用户到店核销
            order.setCardCode(StringUtils.splitWithChar(RandomUtil.getRandom(17, RandomUtil.TYPE.NUMBER), 4, ' '));

            // 官方和商品创建的优惠卷合并
            List<Coupon> offAndShop = new ArrayList<>();
            //查询满足使用条件的优惠卷
            Coupon coupon = couponService.selectOfficialByOrder(user, order);
            offAndShop.add(coupon);

//            couponService.selectShopByOrder();


            if (coupon != null) {
                order.setCoupon(coupon.getUuid());
                order.setPreferentialPrice(coupon.getRuleValue());

            }

            // 实际支付金额
            order.setPayPrice(BigDecimal.valueOf(0));

            // 用户支付后更改支付方式
            order.setPayWay(null);

            return super.saveSelective(order);
        } else {
            // 参数不完整
            return -1;
        }
    }
}
