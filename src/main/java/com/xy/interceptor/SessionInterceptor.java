package com.xy.interceptor;

import com.xy.models.Shop;
import com.xy.services.ShopService;
import com.xy.config.ResourcesConfig;
import com.xy.config.CookieUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by rjora on 2017/7/9 0009.
 */
@Component
public class SessionInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private ShopService shopService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Cookie cookie = CookieUtils.getCookieByName(request, ResourcesConfig.XY_TICKET);
        if (cookie == null) {
            this.goToLogin(request, response, "sessionInvalid");
            return false;
        }
        try {
            if (request.getSession().getAttribute("_loginshop_") == null) {
                String[] values = StringUtils.split(cookie.getValue(), ResourcesConfig.XY_TICKET_SEPERATOR);
                String ticket = values[0];
                Shop shop = new Shop();
                shop.setUuid(ticket);
                shop.setStatus("online");
                shop = shopService.selectOnly(shop);
                if (shop == null) {
                    CookieUtils.delCookieByName(response, request);
                    return false;
                }
                Thread.sleep(1000);
                request.getSession().setAttribute("_loginshop_", shop);
                CookieUtils.addAccountCookie(request, response, shop.getUuid());
            }
        } catch (Exception e) {
            this.goToLogin(request, response, "sessionInvalid");
            return false;
        }
        return super.preHandle(request, response, handler);
    }

    private void goToLogin(HttpServletRequest request,
                           HttpServletResponse response, String status) throws Exception {
        request.setAttribute("status", status); // 为request对象添加参数
        RequestDispatcher dispatcher = request.getRequestDispatcher("/invalid"); // 使用req对象获取RequestDispatcher对象
        dispatcher.forward(request, response); // 使用RequestDispatcher对象在服务器端向目的路径跳转
    }
}
