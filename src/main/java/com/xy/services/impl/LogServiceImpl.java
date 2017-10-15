package com.xy.services.impl;

import com.xy.models.Logs;
import com.xy.services.LogService;
import com.xy.utils.DateUtils;
import com.xy.utils.StringUtils;
import org.springframework.stereotype.Service;

/**
 * Created by rjora on 2017/7/14 0014.
 */
@Service
public class LogServiceImpl extends BaseServiceImpl<Logs> implements LogService {
    @Override
    public void addLog(String userUuid, String userName, String ip, String key, String summary, String content) {
        Logs log = new Logs();
        log.setUuid(StringUtils.getUuid());
        log.setUserUuid(userUuid);
        log.setUserName(userName);
        log.setIp(ip);
        log.setKey(key);
        log.setSummary(summary);
        log.setContent(content);
        log.setAddtime(DateUtils.getCurrentDate());
        super.saveSelective(log);
    }
}
