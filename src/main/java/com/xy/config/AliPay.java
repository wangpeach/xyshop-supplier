package com.xy.config;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;

import java.util.Map;

/**
 * AliPay
 *
 * @author Administrator
 * @date 2017/10/31 14:19
 * @description
 */
public class AliPay {

    public static final String ali_appid = "2017080208001883";
    private static final String ali_gateway = "https://openapi.alipaydev.com/gateway.do";
    private static final String ali_public_key = "+EK4mEdxh5dM7AxtY51Fct1f4jCme5hu7L+wLfi4PCM0r78IOoEs4kABWasB2SkieBvyyV7JwR5gpyL+SvUzEGmf3WCNLmTUU4at3aavkxbE4qWBJbtcb1JJ3p6ZMcsg0QARr3XftEnLrA1JyoG+T1LKEz3BlGM2XwF86+V9cH5FKALd8vbIEceEof53zlfqWhPv5EQaZA47bwJnHicjav3ttPAoRYTErWmVsuCKuaH4chNoYvlzwfFRSg1UMuWGGsCZaO3DxskFnV8/SzlK7A+o1z0umqnodjF4oltsu6fRLY0Fou2CwIDAQAB";
    private static final String app_private_key = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDOPjgnhnjD2krqFMS3KcXaO96Fkr0EgGobFxmB9F93XvCwf42SMJOOFVpypCyS+8h4D7+KyM3DGw9It3LIpBXRVweuUJDdv8+CdRV8veXAsx2K+/PH3Gx+Xt2qGOvRBNtV3VK+EJdAF26ZiZoQv6tfgoo2xqTj6Ux6lRa3f3iijOoNE3DL/8H4XRFJRw9AQcgmrY/3oc2+s47AlPrWbkjh6JlhWkiO6SOIC6J8EYFRgArdVgwrLv2fbh0dQgydHTVoCaZmy+I/hGISGACGfuicOYNjtQmRe3bjIXgAj/tE8NIHxmEWSdSPmzTiQUbVot/kShli7Qa7w6gl5dRw7OFjAgMBAAECggEBAMKmTF9Zdc76zmuAsGxiO36NtKWCaWy/OeVZdtSE7DkAXM9ngxjvScP2eUfrlLogcgKGcnOssy4p8tHhea7N1l2d8vzcwuzWpCVvFZmGrDmjkwViZ6FAcxENvc1U+fNqzwuJ+Ba0F9wAgXzdNpAKJdbdoE60SllkDa8/2JHU9+kIvemcUAeQLgHDXv88/EiUx/706sZ4LizCtPDsCA7UvOR0f5HTnnF6hl3eitgUuanK5DhXtF9cYHz6dE0flad0OvQc8My/mz5Qf29mR7tUevSETWHPeMMExutb/B9o5wc2mTn/2Z91opAIwKhlYuG/ByYWtA1Fruvv21cxqmH/tCkCgYEA6jiZ49ny09JvT56ccL6d6bezAqRGhvmRNvFjIBVit8XqZBJrCr9sXlM8+C5h5Uz3vOJ0paLzBMAWLbhiBMSHF1qdKULLVPGN+D99G/Dy80KcvwS047BB/aIHbVyp/ELQaACyLzvYGj6QU4bhys0ZcMfj0zUZON1F1VLGLyp6Xl0CgYEA4Wug7uRM5uwGVG+VpC68EPpporjKoz+oKVCqjTWRIwT3jHmnqLQ8w4QYdCovSzvL0DIEuxrlMZCbOSuzmu6ibtMg6wL/UIJmDj0dgptddAHLFMe1t3yJXbShNLsMAFctYzu7oy6Poo+brdggOamK3dMWuIbyMeX2iTPk2Ma2wr8CgYAUkGeig644Ps+bE/f0RigjLwe+gaHXk+3V/xeO55HFRNExfBR92xSuq2w89gWOVlWh9df7+vSPRDYuSXQBqyeZO1F1H/IuQQ8P7rMTUvJhvUV51/Xxz7XbiPBR/8LE7TDTEMyULRLqKvgM/Vixr5Q+PK6aCp1uSq8o7Z2M4pPf2QKBgQCqLx2S/d1pQawNtyiciYQGtWdnKGgkE30bpAD/Fv/6FlfmQDppzZlKasIjgW2vOPfIuNqFf+qlHu1xniK8x4jubosdomerGqd9/Hn5Dt7hJDF0AMqaOnViv2ota0HGSArGLmSc8BwUuKLp/o9luTBUBHfU3Fyhe2JDd2BAoNV99QKBgFL1vUJkI6X6nOI6uPqZI+xcGuRnsDwjRjEFCc6IrBoeHlGBgor4YxaS4mZkgV0ZmtNQf/FRIFC5mfZu7egLdLQCTkmcOVwxVVj8xfBJu2XS2+DTpk7Epy6dD2dE1U/CPv2LbOq8bB2x19NeLVwL5tPiFCwe5dTLDLl6/67yYAJb";

    private static final String ali_content_key = "WeO21vZwMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAuAH4PnWQ13392fTL79nVAKAAPfvJxWd0PcpUXlLYxlQM98w==";

    private static final String format = "json";
    private static final String charset = "utf-8";
    private static final String sign_typesign_type = "RSA2";

    private static final String timeout = "1d";

    private static final String notifyUrl = "http://219.141.127.213:8087/xyshop-supplier/order/ali_receive_notify";

    private AlipayClient client = null;


    private static class Singleton {
        private static final AliPay alipay = new AliPay();
    }

    public static AliPay getInstance() {
        return Singleton.alipay;
    }

    private AliPay() {
        client = new DefaultAlipayClient(ali_gateway, ali_appid, app_private_key, format, charset, ali_public_key, sign_typesign_type);
    }

    /**
     * 签名支付数据
     * @param outTradeNo
     * @param subject
     * @param body
     * @param amount
     * @return
     */
    public String createOrderString(String outTradeNo, String subject, String body, String amount, String passbackParams) {
        String str = null;
        //实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
        AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
        //SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
        AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();

        model.setOutTradeNo(outTradeNo);
        model.setSubject(subject);
        model.setBody(body);
        model.setTotalAmount(amount);
        model.setGoodsType("1");
        model.setTimeoutExpress(timeout);
        model.setPassbackParams(passbackParams);
        model.setProductCode("QUICK_MSECURITY_PAY");

        request.setBizModel(model);
        request.setNotifyUrl(notifyUrl);

        try {
            AlipayTradeAppPayResponse response = client.sdkExecute(request);
            str = response.getBody();
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return str;
    }

    /**
     * 验证回调签名是否正确
     * @param arg
     * @return
     */
    public boolean rsaCheck(Map<String, String> arg) {
        boolean flag = false;
        try {
            flag = AlipaySignature.rsaCheckV1(arg, ali_public_key, charset, sign_typesign_type);
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return flag;
    }
}
