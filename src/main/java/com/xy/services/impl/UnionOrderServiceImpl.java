package com.xy.services.impl;

import com.xy.models.*;
import com.xy.services.*;
import com.xy.utils.DateUtils;
import com.xy.utils.RandomUtil;
import com.xy.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Condition;

import java.math.BigDecimal;
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
    private CouponService couponService;


    @Override
    public int saveSelective(String userId, String shopId, String goodId, int num) {
        UnionOrders order = new UnionOrders();

        User user = userService.selectOnlyByKey(userId);
        Shop shop = shopService.selectOnlyByKey(shopId);
        UnionGoods entity = new UnionGoods();
        entity.setUuid(goodId);
        entity.setShopUuid(shopId);
        UnionGoods good = goodService.selectOnly(entity);

        if(good != null && shop != null && good != null) {
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

            // 检索隐式使用的优惠卷
            Condition cond = new Condition(Coupon.class);
            cond.createCriteria().andEqualTo("useMethod", "implicit").andEqualTo("status", "online");
            cond.setOrderByClause("to_user_value desc");
            List<Coupon> coupons = couponService.selectListByCondition(cond);
            if(coupons != null && coupons.size() > 0) {
                order.setCoupon(null);
                order.setPreferentialPrice(BigDecimal.valueOf(0));

                // TODO: 2017/10/16 订单自动使用隐式优惠卷功能，目前因数据库未同步到服务器，不是最新版本，无法开发，待同步最新版本数据库后开发，
                // TODO: 2017/10/16 如果有合适的优惠卷，根据优惠卷使用条件和订单总金额对比，判断需要使用哪个优惠卷


            }

            order.setPayPrice(BigDecimal.valueOf(0));
            order.setPayWay(null);

            return super.saveSelective(order);
        } else {
            // 参数不完整
            return -1;
        }
    }
}
