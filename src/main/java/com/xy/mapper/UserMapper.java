package com.xy.mapper;

import com.xy.models.User;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;
@Repository
public interface UserMapper extends Mapper<User> {
}