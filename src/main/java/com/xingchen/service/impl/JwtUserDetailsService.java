package com.xingchen.service.impl;

/**
 * @author Li
 * @Date 2022/7/23 1:09
 */
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xingchen.dao.UserMapper;
import com.xingchen.pojo.JwtUser;
import com.xingchen.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


import cn.hutool.core.util.RandomUtil;
/**
 * JwtUserDetailsService
 *	 	实现UserDetailsService,重写loadUserByUsername方法
 *  	返回随机生成的user,pass是密码,这里固定生成的
 *  	如果你自己需要定制查询user的方法,请改造这里
 * @author zhengkai.blog.csdn.net
 */
@Service
public class JwtUserDetailsService implements UserDetailsService{
    @Autowired
    private UserMapper userMapper;
    @Override
    public UserDetails loadUserByUsername(String username) {
        QueryWrapper<User> queryWrapper=new QueryWrapper<>();
        User user = userMapper.selectOne(queryWrapper.eq("user_account",username));
        if(user!=null){
            return new JwtUser(user.getUserId(), username,user.getUserPassword(), String.valueOf(user.getUserAuthority()), true);
        }
        else {
            throw new UsernameNotFoundException(String.format("No user found with username '%s'.", username));
        }
    }
}


