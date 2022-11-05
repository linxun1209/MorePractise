package com.xingchen.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xingchen.dao.VipMapper;
import com.xingchen.pojo.Vip;
import com.xingchen.service.VipService;
import com.xingchen.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * @author Li
 * @Date 2022/7/19 18:35
 */
@Service
public class VipServiceImpl extends ServiceImpl<VipMapper, Vip> implements VipService {
    @Autowired
    private VipMapper vipMapper;

    //添加vip付费规则
    @Override
    public R add(Integer time, Double price) {
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("vip_time",time);
        if(vipMapper.selectList(queryWrapper).size()>0)
            return new R(false,"该项收费规则已经存在");
        Vip vip=new Vip(null,time,price);
        vipMapper.insert(vip);
        return new R(true,"添加成功");
    }

    //删除vip付费规则
    @Override
    public void delete(Integer vipId) {
        vipMapper.deleteById(vipId);
    }

    //批量删除
    @Override
    public void deleteMore(Integer[] vipId) {
        vipMapper.deleteBatchIds(Arrays.asList(vipId));
    }

    //修改vip价格
    @Override
    public void update(Integer vipId, Double price) {
        Vip vip=new Vip(vipId,null,price);
        vipMapper.updateById(vip);
    }

    //查看所有收费标准
    @Override
    public List<Vip> viewAll() {
        QueryWrapper<Vip> queryWrapper=new QueryWrapper<>();
        queryWrapper.orderByAsc("vip_time");
        return vipMapper.selectList(queryWrapper);
    }


}
