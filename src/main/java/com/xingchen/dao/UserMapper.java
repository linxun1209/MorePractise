package com.xingchen.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xingchen.pojo.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper extends BaseMapper<User> {
    //增加用户经验值
    Integer addNum(@Param("userId") Integer userId,@Param("num") Integer num);
}
