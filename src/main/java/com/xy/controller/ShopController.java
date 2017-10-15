package com.xy.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.xy.models.Logs;
import com.xy.models.Shop;
import com.xy.models.ShopUpdateWallet;
import com.xy.models.ShopWallet;
import com.xy.pojo.ParamsPojo;
import com.xy.services.LogService;
import com.xy.services.ShopService;
import com.xy.services.ShopUpdateWalletService;
import com.xy.services.ShopWalletService;
import com.xy.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import tk.mybatis.mapper.entity.Condition;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by rjora on 2017/7/21 0021.
 */
@RestController
@RequestMapping(value = "shop/")
@SessionAttributes(value = "_loginshop_")
public class ShopController {

    @Autowired
    private ShopService shopService;
    @Autowired
    private ShopUpdateWalletService shopUpdateWalletService;
    @Autowired
    private ShopWalletService shopWalletService;
    @Autowired
    private LogService logService;


    /**
     * 登录
     *
     * @param email
     * @param pwd
     * @param request
     * @param response
     * @param map
     * @return
     */
    @RequestMapping("/login")
    public @ResponseBody
    String login(@RequestParam("adminEmail") String email, @RequestParam("adminPwd") String pwd, HttpServletRequest request, HttpServletResponse response, ModelMap map) {
        String res = "logerr";
        if (StringUtils.noEmpty(email, pwd)) {
            Shop shop = new Shop();
            shop.setOwnerPhone(email);
            shop.setPassword(Md5Util.md5UpperCase(pwd));
            shop = shopService.selectOnly(shop);
            if (shop != null) {
                if("freeze".equals(shop.getStatus())) {
                    res = "freeze";
                } else {
                    logService.addLog(shop.getUuid(), shop.getName(), IpUtils.getIp(request), Logs.login_success, "登录", "商铺登录成功");
                    CookieUtils.addAccountCookie(request, response, shop.getUuid());
                    map.put("_loginshop_", shop);
                    res = "logsuc";
                }
            }
        }
        return res;
    }

    /**
     * 首页
     *
     * @return
     */
    @RequestMapping(value = "/home.html")
    public ModelAndView record(HttpServletRequest request, ModelAndView view) {
        try {
            Shop shop = (Shop) request.getSession().getAttribute("_loginshop_");
            // 商铺合同 到期日
            String end = shop.getEndtime();
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

            Date endTime = format.parse(end);
            Date today = format.parse(format.format(cal.getTime()));

            long day = (endTime.getTime() - today.getTime()) / (24 * 60 * 60 * 1000);
            if (day > 0 && day < Config.SHOP_OVERDUE) {
                end += "<span style='color:red;'> (您的签约合同即将到期，请续约)</span>";
            } else if (day < 0 && day >= -Config.SHOP_OVERDUE) {
                cal.setTime(endTime);
                cal.add(Calendar.DAY_OF_MONTH, Config.SHOP_OVERDUE + 1);
                end += "<span style='color:red;'> (您的合同已逾期" + Math.abs(day) + "天，请续约。系统将于" + format.format(cal.getTime()) + "自动冻结账号)</span>";
            } else if (day == 0) {
                end += "<span style='color:red;'> (您的签约合同已到期，请续约)</span>";
            }

            // 登录日志
            Condition condition = new Condition(Logs.class);
            condition.setOrderByClause("addTime desc");
            condition.createCriteria().andEqualTo("userUuid", shop.getUuid()).andEqualTo("key", Logs.login_success);
            Logs log = logService.selectOnly(condition, 2);

            if(log != null) {
                view.addObject("end", end);
                view.addObject("lastTime", log.getAddtime());
            }
            view.addObject("lastIp", log.getIp());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        view.setViewName("supplier/record");
        return view;
    }

    /**
     * 商铺 首页信息
     * @return
     */
    @RequestMapping(value = "wallet-info")
    public @ResponseBody
    Map shopInfo(HttpServletRequest request) {
        Shop shop = (Shop) request.getSession().getAttribute("_loginshop_");
        Map<String, Object> map = new HashMap<>();
        // 提现信息
        ShopUpdateWallet suw = new ShopUpdateWallet();
        suw.setShopUuid(shop.getUuid());
        suw = shopUpdateWalletService.selectOnly(suw);

        // 余额信息
        ShopWallet sw = new ShopWallet();
        sw.setShopUuid(shop.getUuid());
        sw = shopWalletService.selectOnly(sw);

        map.put("upwallet", suw);
        map.put("wallet", sw);
        return map;
    }


    @RequestMapping(value = "pagelist")
    public @ResponseBody
    PageInfo<Shop> pageList(@RequestBody JSONObject json) {
        ParamsPojo pj = new ParamsPojo(json);
        return shopService.selectPageListByParams(pj);
    }

    @ResponseBody
    @RequestMapping(value = "list")
    public List<Shop> list(@ModelAttribute Shop shop) {
        Condition cond = new Condition(Shop.class);
        cond.createCriteria().andLike("name", "%"+ shop.getName() +"%").andEqualTo("status");
        return shopService.selectListByCondition(cond);
    }


    /**
     * 客户端搜索商10家
     * @param cates 分类
     * @param key
     * @param position 用户坐标
     * @param distance 搜索周围距离
     * @param orderBy 排序方式
     * @return
     */
    @PostMapping("mapi/list")
    public List<Shop> mApiList(@RequestParam String cates, @RequestParam String key, @RequestParam String position, @RequestParam String distance, @RequestParam String orderBy, @RequestParam int offset) {
        return shopService.mApiList(cates, key, position, distance, orderBy, offset, Config.LIMIT);
    }

    @RequestMapping(value = {"only", "mapi/only"})
    public @ResponseBody Shop only(@ModelAttribute Shop shop) {
        shop.setStatus("online");
        return shopService.selectOnly(shop);
    }

    /**
     *
     * @param shop
     * @return
     */
    @RequestMapping(value = "save")
    public @ResponseBody int save(@ModelAttribute Shop shop) {
        return shopService.saveSelective(shop);
    }



    @RequestMapping(value = "update")
    public @ResponseBody int update(@ModelAttribute Shop shop) {
        return shopService.updateByPrimaryKeySelective(shop);
    }



    @RequestMapping(value = "del")
    public @ResponseBody int delete(@ModelAttribute Shop shop) {
        shop.setStatus("deleted");
        return shopService.del(shop);
    }
}
