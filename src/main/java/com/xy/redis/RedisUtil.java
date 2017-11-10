package com.xy.redis;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.xy.models.SystemParams;
import com.xy.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by rjora on 2017/7/16 0016.
 */
@Service
public class RedisUtil {

    @Autowired
    private Redis redis;


    public List<SystemParams> getSysParams(String key) {
        List<SystemParams> sps = null;
        String strParams = redis.valueGet("sysparams");
        if(StringUtils.isNotNull(strParams)) {
            Type type = new TypeToken<List<SystemParams>>(){}.getType();
            sps = new Gson().fromJson(strParams, type);
        }
        if(StringUtils.isNotNull(key)) {
            sps = sps.stream().filter(params -> params.getType().equals(key)).collect(Collectors.toList());
        }
        return sps;
    }
}
