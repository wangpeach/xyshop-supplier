package com.xy.utils;

import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

/**
 * Created by rjora on 2017/6/29 0029.
 * <p>
 * 系统配置类
 */
@Configuration
@PropertySource(value = "classpath:config.properties")
public class Config implements EnvironmentAware {

    private Environment env;


    public static String WX_APPID = "";


    public static String ali_appid = "2016082000294577";
    public static String ali_gateway = "https://openapi.alipaydev.com/gateway.do";
    public static String ali_public_key= "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAwVkCvnCNJGXLgr6jTjT0Cd3uU0Y3iAQ4BaPjd/YiHqdJ/3Tk++q9sV4shAPOy+IbodNsv1uuxUVITRmootfJEHld8Hm/X2pWseGjq6aPH9mA2dAQNwZ2TdmyyJMrSH0CM1ARliqbcnpJ/SPp3wEuS28NPHtgVutd71hcmlPUHygHi5LnGNIszRaIy+ERdXfo51hWR3g4coyInn8XI6CtQgo7THHPSrFtj5ouwZHU0KKGS6/ANVK63Udn9rmxvZruBZLf3nekehT03fgZoSbulbvbIuhsnmrurCr9P4voMuw5qcDXQq+zxUyJE6UPUSm2vA8+KsUplBwP7VnV3LG37wIDAQAB";
    public static String ali_private_key = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCNUm5WJJRP2gZ0uFs6eRv4sQchBzZup0eHJa0fQ5UvBZhTMbRs/ywvJ2Pn5omsWqNGWjypBZ9gKL9leqFaiMZmC/nux09oZS2JTQFriDrix3yxkE1I+nvjy+ldUdccXA/UfZI/YKXUcKwc+s128BEQs9Mh47DYEwJchIoTcsdFG3I+u+R912ztm5Oh4UIt+YlLNTfy6k8d4Aj1XAcZmwKm5QeDFtWM/xDwjumIIiHhf7b4naU83/J4E+aVAVPMjn9EHF/0tcl/rPHZgrBANO8ugDOf+EvFKpws80MCKb23Mw7sugcTe72G88ZWgpFugYHZGMnZewExH/ZgvQnYG5EvAgMBAAECggEAfnQuCqK8mx8J+9MivpWz87nkGSetCyJfnIyX9ouwp9ynynmFaqqBzMfAW7crpa+MvdzsvokrbQ3JBXLPgoU0kgdRYtmxMkamkl73UFMb7eR2rSPkI9MOnGzPqhMbGmM+PvMFs/Q1LnyLZXr98tkpS/q8h2yHxzBrQoHOU0y3BhwK1wKKXMVDAHK+2x91cxchF/ydTpkQzVaoPhUSayL+H8k9ELea5fKzeNZXqTouTsytBg0cB4nnbLXuBMnvRlDnwE1WNzmylSOvsU8Tj8QnE4vf2gWwkVCjdNpx4D0tWJcZFLklPRe+0syBtvPr9lJfCTDJXlQtA21YWgMjFnYMcQKBgQDcvxcm8xTMxHJ9b72x6oPn/jlQewckyXcXgMj9qclSienWBYEKz76arp+QxZQRaHX8Od8ZgpK4zYD9gzBVV5iQkdroK4xARgrJGHSHaNLmAJypFBeTQhWI2VI5PQGbtjt6XKRrCuV3gGulJ/vKSsyCRoxC57GxvecTyin/GjmJ6wKBgQCj5C7bocJbIGDWOYzUfBX7VouwPWNvK+RZIdEV4t6LCcjfiIxbAKpIlEt0vF2KL8W5lKkcagMfHOHX/R2Soyn+prVr21ugVRCcWPPyCJVD7lu75yK5XzyNrzDeWSIIHhdzH8TAnXrv9BhNK6xMq2JqRnWkEOfK/qLn6rd66PBgzQKBgBIeMQVGYEeqPAdlm3k12Vu6NvQPFPgE/RxVuqlvPRHkNegWDZBYmrlRadFVFiETpNNt4IzUdbxrSZIFKXtntCLxhbkWke1YklwNAMt4ZA7yr+kEuCai/ud7hs8h3bbtDxas8eXDQDA9vxgQHw9scjqjFbIMGD6RapzqM+Y3Pc+/AoGAaYQ1ru4cRbM45XOz+fHR7TmxQcykQiUOJVyiUub3xKiODWzKgKSS/ZuMYSINTzSmpm7R2hte2x8rQHYUHh6yoOMQFtrpyPY7dY3HsWJZR50krnuHXbW1NLtASUc1o+hLzLD9ac16IxcStkyR5+LHwAiRIQpJUeZTV+FEM/vZF1kCgYBNufxjMF/J0tm+j2OeQZ+egKWXsgNc+RANz2FhxRpnl4YtmnWxs3YCNVu1mpXLRT6jOAFV8W+jwzeYJMdPqYrP1hQlbQinqj23lehvbN0a2i1D5w958egQnh+b8PpADPvK3ppuu7SF/vsedgkTPk17TDqWpqH9l8y/+ezjEMLBOQ==";



    // 系统真实路径
    public static final String SYSTEM_PATH = System.getProperty("evan.xyslr");

    public static final String XY_TICKET = "xy-PVxfFClQP7HcRdw33gfL6yWYUfxP5y37";
    public static final String XY_TICKET_SEPERATOR = "_";
    public static final String XY_TICKET_KEY = "xy-shop-supplier";

    // 商品编号前缀
    public static final String GOOD_PREFIX = "GN-";

    // 查询多少条数据
    public static int LIMIT = 20;


    // 优惠卷前缀
    public static final String XY_COUPON_PREFIX = "YH-";

    // 商铺允许逾期天数
    public static int SHOP_OVERDUE = 0;

    //文件临时保存路径
    public static String FILETEMP;
    public static String REQTEMP;

    public static String SHOPPATH;
    public static String SHOPURL;

    // 图标保存路径
    public static String ICONPATH;
    public static String ICONURL;

    // 头像保存路径
    public static String HEADPATH;
    public static String HEADURL;

    // 产品图片保存路径
    public static String PRODUCTIMGPATH;
    public static String PRODUCTIMGURL;

    // 评论图片保存路径
    public static String JUDGEIMGPATH;
    public static String JUDGEIMGURL;

    // app 相关图片资源路径
    public static String APPPATH;
    public static String APPURL;

    // 广告相关图片
    public static String ADVIMGPATH;
    public static String ADVIMGURL;

    // 广告相关视频
    public static String ADVIDEOPATH;
    public static String ADVIDEOURL;


    // 商铺详情文件
    public static String DESSHOPPATH;
    public static String DESSHOPURL;

    // 商品详情文件
    public static String DESGOODSPATH;
    public static String DESGOODSURL;

    // 广告详情文件
    public static String DESADPATH;
    public static String DESADURL;


    public void setEnvironment(Environment environment) {
        this.env = environment;

        Config.SHOP_OVERDUE = Integer.parseInt(env.getProperty("shop.overdue"));

        Config.LIMIT = Integer.parseInt(env.getProperty("select.limit"));

        // 资源保存根目标
        String basePath = env.getProperty("file.basePath");
        // 资源访问根目录
        String baseUrl = env.getProperty("req.baseUrl");

        Config.FILETEMP = basePath + env.getProperty("file.temp");
        Config.SHOPPATH = basePath + env.getProperty("file.shop");
        Config.ICONPATH = basePath + env.getProperty("file.icon");
        Config.HEADPATH = basePath + env.getProperty("file.headImg");
        Config.PRODUCTIMGPATH = basePath + env.getProperty("file.productImg");
        Config.JUDGEIMGPATH = basePath + env.getProperty("file.judgeImg");
        Config.APPPATH = basePath + env.getProperty("file.appImg");
        Config.ADVIMGPATH = basePath + env.getProperty("file.adImg");
        Config.ADVIDEOPATH = basePath + env.getProperty("file.adVideo");
        Config.DESSHOPPATH = basePath + env.getProperty("file.desShop");
        Config.DESGOODSPATH = basePath + env.getProperty("file.desgoods");
        Config.DESADPATH = basePath + env.getProperty("file.descAd");



        Config.REQTEMP = baseUrl + env.getProperty("req.temp");
        Config.SHOPURL = baseUrl + env.getProperty("req.shop");
        Config.ICONURL = baseUrl + env.getProperty("req.icon");
        Config.HEADURL = baseUrl + env.getProperty("req.headImg");
        Config.PRODUCTIMGURL = baseUrl + env.getProperty("req.productImg");
        Config.JUDGEIMGURL = baseUrl + env.getProperty("req.judgeImg");
        Config.APPURL = baseUrl + env.getProperty("req.appImg");
        Config.ADVIMGURL = baseUrl + env.getProperty("req.adImg");
        Config.ADVIDEOURL = baseUrl + env.getProperty("req.adVideo");
        Config.DESSHOPURL = baseUrl + env.getProperty("req.desShop");
        Config.DESGOODSURL = baseUrl + env.getProperty("req.desgoods");
        Config.DESADURL = baseUrl + env.getProperty("req.descAd");


//        FileUtils.createPath(Config.FILETEMP, Config.SHOPPATH, Config.ICONPATH, Config.HEADPATH,
//                Config.PRODUCTIMGPATH, Config.JUDGEIMGPATH, Config.APPPATH, Config.ADVIMGPATH,
//                Config.ADVIDEOPATH, Config.DESSHOPPATH, Config.DESGOODSPATH, Config.DESADPATH);
    }


}