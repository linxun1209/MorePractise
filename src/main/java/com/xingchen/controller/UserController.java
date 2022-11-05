package com.xingchen.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xingchen.pojo.JwtUser;
import com.xingchen.pojo.User;
import com.xingchen.service.impl.UserServiceImpl;
import com.xingchen.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@CrossOrigin
@Api(tags = "用户")
public class UserController {
    @Autowired
    private UserServiceImpl userService;

    /**
     *账号注册
     * @date 2022/7/1 22:07
     * @param userAccount 账号
     * @param userPassword 密码
     * @return com.xingchen.utils.R
    */
    @ApiOperation(value = "账号注册")
    @PostMapping(value = "/register")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userAccount",value = "账号",required = true),
            @ApiImplicitParam(name = "userPassword",value = "密码",required = true)
    })
    @ResponseBody
    public R register(String userAccount,String userPassword){
        return userService.register(userAccount,userPassword);
    }

    /**
     *发送邮件
     * @date 2022/7/1 22:15
     * @param email 邮箱号
     * @return com.xingchen.utils.R
    */
    @ApiOperation(value = "发送邮件")
    @PostMapping(value = "/sendEmail")
    @ResponseBody
    public R sendEmail(String email){
        return userService.sendEmail(email);
    }

    /**
     *邮箱号注册
     * @date 2022/7/1 22:20
     * @param email 邮箱
     * @param userPassword 密码
     * @param code 验证码
     * @return com.xingchen.utils.R
    */
    @ApiOperation(value = "邮箱号注册")
    @PostMapping(value = "/registerByEmail")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "email",value = "邮箱",required = true),
            @ApiImplicitParam(name = "userPassword",value = "密码",required = true),
            @ApiImplicitParam(name = "code",value = "验证码",required = true)
    })
    @ResponseBody
    public R registerByEmail(String email,String userPassword,String code){
        return userService.registerByEmail(email,userPassword,code);
    }

    /**
     *用户登录
     * @date 2022/7/2 13:05
     * @param userAccount 账号
     * @param userPassword 密码
     * @return com.xingchen.utils.R
    */
    @ApiOperation(value = "用户密码登录")
    @PostMapping(value = "/login")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userAccount",value = "账号",required = true),
            @ApiImplicitParam(name = "userPassword",value = "密码",required = true)
    })
    @ResponseBody
    public R login(String userAccount,String userPassword){
        return userService.login(userAccount,userPassword);
    }

    /**
     *验证码登录
     * @date 2022/7/2 20:05
     * @param email 邮箱
     * @param code 验证码
     * @return com.xingchen.utils.R
    */
    @ApiOperation(value = "用户验证码登录")
    @PostMapping(value = "/loginByCode")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "email",value = "邮箱",required = true),
            @ApiImplicitParam(name = "code",value = "验证码",required = true)
    })
    @ResponseBody
    public R loginByCode(String email,String code){
        return userService.loginByCode(email,code);
    }


    /**
     *判断登录
     * @date 2022/7/5 21:31
     * @return com.xingchen.utils.R
    */
    @ApiOperation(value = "判断登录")
    @GetMapping(value = "/isLogin")
    @ResponseBody
    public JwtUser isLogin( HttpServletRequest request){
        return userService.isLogin(request);
    }

    /**
     *退出登录
     * @date 2022/7/26 14:59
     * @return com.xingchen.pojo.JwtUser
    */
    @ApiOperation(value = "退出登录")
    @GetMapping(value = "/quit")
    @ResponseBody
    public R quit(HttpServletRequest request){
        userService.quit(request);
        return new R(true,"退出成功");
    }

    /**
     *删除单个用户
     * @date 2022/7/2 20:06
     * @param userId 用户id
     * @return com.xingchen.utils.R
    */
    @ApiOperation(value = "删除单个用户")
    @DeleteMapping(value = "/administrator/delete")
    @ResponseBody
    public R delete(Integer userId){
        userService.delete(userId);

        return new R(true,"删除成功");
    }

    /**
     *删除多个用户
     * @date 2022/7/2 20:11
     * @param userId id数组
     * @return com.xingchen.utils.R
    */
    @ApiOperation(value = "删除多个用户")
    @DeleteMapping(value = "/administrator/deleteMore")
    @ResponseBody
    public R deleteMore(Integer[] userId){
        userService.deleteMore(userId);
        return new R(true,"删除成功");
    }

    /**
     *修改用户信息
     * @date 2022/7/3 0:22
     * @param userId 主键id
     * @param name 昵称
     * @param sex 性别
     * @param birthday 生日
     * @param introduce 简介
     * @param address 地址
     * @param school 学校
     * @return com.xingchen.utils.R
    */
    @ApiOperation(value = "修改用户信息")
    @PutMapping(value = "/all/update")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId",value = "id",required = true),
            @ApiImplicitParam(name = "name",value = "用户昵称"),
            @ApiImplicitParam(name = "sex",value = "性别"),
            @ApiImplicitParam(name = "birthday",value = "生日"),
            @ApiImplicitParam(name = "introduce",value = "简介"),
            @ApiImplicitParam(name = "address",value = "地址"),
            @ApiImplicitParam(name = "school",value = "学校"),
    })
    @ResponseBody
    public R update(Integer userId,String name,String sex, String birthday, String introduce, String address, String school){
        return userService.update(userId, name, sex, birthday, introduce, address, school);
    }

    /**
     *修改用户头像
     * @date 2022/7/3 16:14
     * @param userId 用户id
     * @param file 头像文件
     * @return com.xingchen.utils.R
    */
    @ApiOperation(value = "修改用户头像")
    @PutMapping(value = "/all/updateHeadShot")
    @ResponseBody
    public R updateHeadShot(Integer userId, MultipartFile file){
        return userService.updateHeadShot(userId,file);
    }

    /**
     *查看单个用户详细信息
     * @date 2022/7/3 16:26
     * @param userId 用户id
     * @return com.xingchen.utils.R
    */
    @ApiOperation(value = "查看单个用户详细信息")
    @GetMapping(value = "/viewOne")
    @ResponseBody
    public R viewOne(Integer userId){
        User user=userService.viewOne(userId);
        if(user==null)
            return new R(false,"没有找到该用户");
        return new R(true,user);
    }

    /**
     *分页查看用户
     * @date 2022/7/2 19:48
     * @param currentPage 当前页数
     * @param pageSize 每页个数
     * @return com.xingchen.utils.R
    */
    @ApiOperation(value = "分页查看用户")
    @GetMapping(value = "/administrator/viewByPage")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "currentPage",value = "当前页数",required = true),
            @ApiImplicitParam(name = "pageSize",value = "每页个数",required = true)
    })
    @ResponseBody
    public R viewByPage(Integer currentPage,Integer pageSize){
        IPage<User> page = userService.viewByPage(currentPage, pageSize);
        if(page==null){
            return new R(false,"页数不合规范");
        }
        return new R(true,page);
    }

    /**
     *分页搜索用户
     * @date 2022/7/8 18:20
     * @param currentPage 当前页数
     * @param pageSize 每页个数
     * @param search 搜索词
     * @return com.xingchen.utils.R
    */
    @ApiOperation(value = "分页搜索用户")
    @GetMapping(value = "/searchByPage")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "currentPage",value = "当前页数",required = true),
            @ApiImplicitParam(name = "pageSize",value = "每页个数",required = true),
            @ApiImplicitParam(name = "search",value = "搜索词",required = true)
    })
    @ResponseBody
    public R searchByPage(Integer currentPage,Integer pageSize,String search){
        IPage<User> page = userService.searchByPage(currentPage, pageSize,search);
        if(page==null){
            return new R(false,"页数不合规范");
        }
        return new R(true,page);
    }

    /**
     *关注用户（取关）
     * @date 2022/7/6 0:01
     * @param followId 被关注的用户id
     * @param fanId 粉丝用户id
     * @return com.xingchen.utils.R
    */
    @ApiOperation(value = "关注用户（取关）")
    @PostMapping(value = "/user/follow")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "followId",value = "被关注的用户id",required = true),
            @ApiImplicitParam(name = "fanId",value = "粉丝用户id",required = true)
    })
    @ResponseBody
    public R follow(Integer followId,Integer fanId){
        return userService.follow(followId,fanId);
    }

    /**
     *判断是否关注
     * @date 2022/7/11 18:30
     * @param followId 被关注的用户id
     * @param fanId 粉丝用户id
     * @return com.xingchen.utils.R
    */
    @ApiOperation(value = "判断是否关注")
    @GetMapping(value = "/user/isFollow")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "followId",value = "被关注的用户id",required = true),
            @ApiImplicitParam(name = "fanId",value = "粉丝用户id",required = true)
    })
    @ResponseBody
    public R isFollow(Integer followId,Integer fanId){
        boolean follow = userService.isFollow(followId, fanId);
        if(follow)
            return new R(true,"已关注");
        return new R(true,"未关注");
    }

    /**
     *查看用户关注列表
     * @date 2022/7/6 0:32
     * @param userId 用户Id
     * @return com.xingchen.utils.R
    */
    @ApiOperation(value = "查看用户关注列表")
    @GetMapping(value = "/user/viewFollow")
    @ResponseBody
    public R viewFollow(Integer userId){
        List<User> follow = userService.viewFollow(userId);
        return new R(true,follow);
    }

    /**
     *查看用户粉丝列表
     * @date 2022/7/6 0:32
     * @param userId 用户id
     * @return com.xingchen.utils.R
    */
    @ApiOperation(value = "查看用户粉丝列表")
    @GetMapping(value = "/user/viewFan")
    @ResponseBody
    public R viewFan(Integer userId){
        List<User> fan = userService.viewFan(userId);
        return new R(true,fan);
    }

    /**
     *用户封号
     * @date 2022/7/25 10:42
     * @param userId 用户id
     * @param day 天数
     * @return com.xingchen.utils.R
    */
    @ApiOperation(value = "用户封号")
    @PutMapping(value = "/administrator/userLimit")
    @ResponseBody
    public R userLimit(Integer userId,Integer day){
        return userService.userLimit(userId,day);
    }

    /**
     *判断用户是否被封号
     * @date 2022/7/26 14:51
     * @param userId
     * @return com.xingchen.utils.R
    */
    @ApiOperation(value = "判断用户封号")
    @GetMapping(value = "/administrator/userIsLimit")
    @ResponseBody
    public R userIsLimit(Integer userId){
        if(userService.userIsLimit(userId))
            return new R(true,"该用户已经被封禁");
        return new R(true,"该用户未被封禁");
    }

    /**
     *发送邮件
     * @date 2022/7/25 12:00
     * @param userId 用户id
     * @param content 内容
     * @return com.xingchen.utils.R
    */
    @ApiOperation(value = "发送邮件")
    @GetMapping(value = "/administrator/sendEmailSelf")
    @ResponseBody
    public R sendEmailSelf(Integer userId,String content){
        return userService.sendEmailSelf(userId,content);
    }

}
