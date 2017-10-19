package com.xy.services.impl;

import com.xy.models.Coupon;
import com.xy.models.UnionOrders;
import com.xy.models.User;
import com.xy.models.UserCoupon;
import com.xy.services.CouponService;
import com.xy.services.UnionOrderService;
import com.xy.services.UserCouponService;
import com.xy.utils.DateUtils;
import com.xy.utils.StringUtils;
import com.xy.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class UserCouponServiceImpl extends BaseServiceImpl<UserCoupon> implements UserCouponService {

    @Autowired
    private CouponService couponService;

    @Autowired
    private UnionOrderService orderService;

    @Override
    public UserCoupon selectList(String userId, String shopId, String goodTypeId, String goodId) {
        UserCoupon userCoupon = new UserCoupon();
        userCoupon.setUserid(userId);
        userCoupon.setStatus("waituser");
        List<UserCoupon> coupons = super.selectList(userCoupon);
        for (UserCoupon item : coupons) {
            Coupon other = couponService.selectOnlyByKey(item.getCouponid());
            // TODO: 2017/10/16 根据参数判断该订单是否可用优惠卷

            item.setCoupon(other);
        }
        return null;
    }


    @Override
    public void autoTrash() {
        // 查询已过期优惠卷
        Condition cond = new Condition(Coupon.class);
        cond.createCriteria()
                .andNotEqualTo("end_time", "forever")
                .andLessThan("end_time", DateUtils.getDate())
                .andEqualTo("status", "online");
        List<Coupon> coupons = couponService.selectListByCondition(cond);

        // TODO: 2017/10/16 废弃已过期的优惠卷, 查询出来的是已过期的，变更状态即可，完了还有用户的优惠卷也需要废弃

//        couponService.updateByConditionSelective();
    }


    /**
     * 查询订单满足使用条件的优惠卷
     *
     * @param user  下单用户
     * @param order 下单金额
     * @return 返回满足条件的优惠卷
     */
    @Override
    public Coupon selectByOrder(User user, UnionOrders order) {
        // 符合条件的优惠卷
        Coupon target = null;
        // 检索隐式使用的优惠卷
        Condition cond = new Condition(Coupon.class);
        Example.Criteria cri = cond.createCriteria();
        cri.andEqualTo("useMethod", "implicit").andEqualTo("status", "online");
        cond.setOrderByClause("to_user_value desc");
        List<Coupon> coupons = couponService.selectListByCondition(cond);
        if (coupons != null && coupons.size() > 0) {
            // TODO: 2017/10/16 订单自动使用隐式优惠卷功能，目前因数据库未同步到服务器，不是最新版本，无法开发，待同步最新版本数据库后开发，
            // TODO: 2017/10/16 如果有合适的优惠卷，根据优惠卷使用条件和订单总金额对比，判断需要使用哪个优惠卷
            // TODO: 2017/10/19 测试工作未做 ，上面两个已经做了..
            // TODO: 2017/10/19 新用户专享优惠卷仅限 管理员后台 添加， 并且只能面向全场商品  


            String orientedUser = null;
            if (user.isNew()) {
                // 查找出仅限新用户并且满足消费条件的优惠卷
                target = this.find(coupons, order.getTotalPrice(), "newuser");
            } else {
                // 查找出仅限老用户并且满足消费条件的优惠卷
                target = this.find(coupons, order.getTotalPrice(), "olduser");
            }
            if(target == null) {
                // 查找出面向所有用户并且满足消费条件的优惠卷
                target = this.find(coupons, order.getTotalPrice(), "all");
            }
        }
        return target;
    }

    /**
     * 查找符合条件的优惠卷
     * @param coupons 可以使用的
     * @param money 消费金额
     * @param toUser 面向用户类型，所有，新用户，老用户
     * @return
     */
    private Coupon find(List<Coupon> coupons, BigDecimal money, String toUser) {
        // 过滤出所有符合 消费金额，用户类型的优惠卷
        return coupons.stream().filter(coupon -> toUser.equals(coupon.getToUser()) && money.compareTo(BigDecimal.valueOf(coupon.getToUserValue())) > -1).sorted(Comparator.comparing(Coupon::getToUserValue)).collect(Collectors.toList()).get(0);
    }
}
