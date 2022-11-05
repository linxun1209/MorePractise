package com.xingchen.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xingchen.pojo.Vip;
import com.xingchen.utils.R;

import java.util.List;

/**
 * @author Li
 * @Date 2022/7/19 18:34
 */
public interface VipService extends IService<Vip> {
    //添加vip付费规则
    R add(Integer time,Double price);
    //删除vip付费规则
    void delete(Integer vipId);
    //批量删除
    void deleteMore(Integer[] vipId);
    //修改vip价格
    void update(Integer vipId,Double price);
    //查看所有收费标准
    List<Vip> viewAll();
}
