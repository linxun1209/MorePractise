package com.xingchen.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xingchen.pojo.JwtUser;
import com.xingchen.pojo.User;
import com.xingchen.utils.R;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface UserService extends IService<User> {
    //账号注册
    R register(String userAccount,String userPassword);
    //发送邮件
    R sendEmail(String email);
    //邮箱注册
    R registerByEmail(String email,String userPassword,String code);
    //密码登录
    R login(String userAccount,String userPassword);
    //邮箱发送验证码登录
    R loginByCode(String email,String code);
    //验证登录
    JwtUser isLogin(HttpServletRequest request);
    //退出登录
    void quit(HttpServletRequest request);
    //删除用户
    void delete(Integer userId);
    //批量删除用户
    void deleteMore(Integer[] id);
    //修改用户信息
    R update(Integer userId,String name,String sex, String birthday, String introduce, String address, String school);
    //修改用户头像
    R updateHeadShot(Integer userId, MultipartFile file);
    //查看单个用户信息
    User viewOne(Integer userId);
    //分页查看用户
    IPage<User> viewByPage(Integer currentPage,Integer pageSize);
    //分页搜索用户
    IPage<User> searchByPage(Integer currentPage,Integer pageSize,String search);
    //关注用户（取关）
    R follow(Integer followId, Integer fanId);
    //判断是否关注
    boolean isFollow(Integer followId, Integer fanId);
    //查看用户关注
    List<User> viewFollow(Integer userId);
    //查看用户粉丝
    List<User> viewFan(Integer userId);
    //开通vip
    void setVip(Integer userId,Double price);
    //用户禁号
    R userLimit(Integer userId,Integer day);
    //判断用户是否禁号
    boolean userIsLimit(Integer userId);
    //发送邮件
    R sendEmailSelf(Integer userId,String content);
}
