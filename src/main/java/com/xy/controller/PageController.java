package com.xy.controller;

import com.xy.models.Shop;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.ModelAndView;

import javax.jms.Session;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created by rjora on 2017/7/2 0002.
 */
@Scope("prototype")
@Controller
@RequestMapping("/")
public class PageController {

    /**
     * 登录
     *
     * @param session
     * @return
     */
    @RequestMapping(value = {"/logout", "/invalid"})
    public String logout(HttpSession session) {
        session.removeAttribute("_login_shop_");
        session.invalidate();
        return "login";
    }

    /**
     * 后台管理主页
     *
     * @return
     */
    @RequestMapping(value = "/index.html")
    public String indexPage() {
        return "index";
    }


    /**
     * 商品管理
     * @return
     */
    @RequestMapping(value = "/goodspage.html")
    public String goodsPage() {
        return "goods/goods_list";
    }

    /**
     * 订单管理
      * @return
     */
    @RequestMapping(value = "/orderpage.html")
    public String ordersPage() {
        return "order/order_list";
    }

    /**
     * 商铺设置
     * @return
     */
    @RequestMapping(value = "/setting.html")
    public ModelAndView settingPage(@SessionAttribute("_loginshop_") Shop shop, ModelAndView view) {
        view.addObject("self", shop);
        view.setViewName("supplier/setting");
        return view;
    }


    /**
     * 商铺设置
     * @return
     */
    @RequestMapping(value = "/coupon.html")
    public ModelAndView settingPage(ModelAndView view, @SessionAttribute("_loginshop_") Shop shop) {
        System.out.println(shop.getUuid());
        view.addObject("uuid", shop.getUuid());
        view.setViewName("supplier/coupon_list");
        return view;
    }



    /**
     * 404 页面
     *
     * @param request
     * @param model
     * @return
     */
    @RequestMapping(value = "/404.html")
    public String toEror404Page(HttpServletRequest request, Model model) {
        return "404";
    }

    /**
     * 500 页面
     *
     * @param request
     * @param model
     * @return
     */
    @RequestMapping(value = "/500.html")
    public String toEror500Page(HttpServletRequest request, Model model) {
        return "500";
    }

}
