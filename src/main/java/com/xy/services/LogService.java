package com.xy.services;

import com.xy.models.Logs;

/**
 * Created by rjora on 2017/7/14 0014.
 */
public interface LogService extends BaseService<Logs> {
    void addLog(String userUuid, String userName, String ip, String key, String summary, String content);
}
