package com.xy.config;

import com.github.wxpay.sdk.WXPayConfig;

import java.io.*;

public class WXConfig implements WXPayConfig{
    private final String KEY = "Ar6biRqek8X1zAtfv5632AxPv2x039SVFvW0";
    private final String WX_APPID = "wx9b1fa09595b5d87c";
    private final String MCH_ID = "1491858182";
    public static final String notifyUrl = "http://219.141.127.213:8087/xyshop-supplier/order/wx_receive_notify";

    private byte[] certData;
    public WXConfig() {
        File file = new File(ResourcesConfig.wxCert);
        InputStream certStream = null;
        try {
            certStream = new FileInputStream(file);
            this.certData = new byte[(int) file.length()];
            certStream.read(this.certData);
            certStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public String getAppID() {
        return this.WX_APPID;
    }

    @Override
    public String getMchID() {
        return this.MCH_ID;
    }

    @Override
    public String getKey() {
        return this.KEY;
    }

    @Override
    public InputStream getCertStream() {
        ByteArrayInputStream certBis = new ByteArrayInputStream(this.certData);
        return certBis;
    }

    @Override
    public int getHttpConnectTimeoutMs() {
        return 8000;
    }

    @Override
    public int getHttpReadTimeoutMs() {
        return 10000;
    }
}
