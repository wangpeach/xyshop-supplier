package com.xy.services;

import com.xy.models.UserFootprint;

import java.util.List;

public interface UserFootprintService extends BaseService<UserFootprint> {
    public List<UserFootprint> selectList(String user, int offset, int limit);
}
