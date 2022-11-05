package com.xingchen.controller;

import com.xingchen.pojo.Vip;
import com.xingchen.service.VipService;
import com.xingchen.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Li
 * @Date 2022/7/20 14:10
 */
@Controller
@CrossOrigin
@Api(tags = "Vip")
public class VipController {
    @Autowired
    private VipService vipService;

    /**
     *添加vip付费规则
     * @date 2022/7/20 14:13
     * @param time 时间
     * @param price 价格
     * @return com.xingchen.utils.R
    */
    @ApiOperation(value = "添加vip付费规则")
    @PostMapping(value = "/administrator/vipAdd")
    @ResponseBody
    public R vipAdd(Integer time,Double price){
        return vipService.add(time,price);
    }

    /**
     *删除vip付费规则
     * @date 2022/7/20 14:16
     * @param vipId id
     * @return com.xingchen.utils.R
    */
    @ApiOperation(value = "删除vip付费规则")
    @DeleteMapping(value = "/administrator/vipDelete")
    @ResponseBody
    public R vipDelete(Integer vipId){
        vipService.delete(vipId);
        return new R(true,"删除成功");
    }

    /**
     *批量删除vip付费规则
     * @date 2022/7/20 14:16
     * @param vipId id
     * @return com.xingchen.utils.R
    */
    @ApiOperation(value = "批量删除vip付费规则")
    @DeleteMapping(value = "/administrator/vipDeleteMore")
    @ResponseBody
    public R vipDeleteMore(Integer[] vipId){
        vipService.deleteMore(vipId);
        return new R(true,"删除成功");
    }

    /**
     *修改vip付费价格
     * @date 2022/7/20 14:18
     * @param vipId id
     * @param price 价格
     * @return com.xingchen.utils.R
    */
    @ApiOperation(value = "修改vip付费价格")
    @PutMapping(value = "/administrator/vipUpdate")
    @ResponseBody
    public R vipUpdate(Integer vipId,Double price){
        vipService.update(vipId,price);
        return new R(true,"修改成功");
    }

    /**
     *查看所有付费规则
     * @date 2022/7/20 14:20
     * @return com.xingchen.utils.R
    */
    @ApiOperation(value = "查看所有付费规则")
    @GetMapping(value = "/all/vipView")
    @ResponseBody
    public R vipView(){
        List<Vip> vips = vipService.viewAll();
        return new R(true,vips);
    }

}
