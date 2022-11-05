package com.xingchen.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xingchen.dao.MsgMapper;
import com.xingchen.pojo.Msg;
import com.xingchen.pojo.Topic;
import com.xingchen.service.MsgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author xingchen
 * @version V1.0
 * @Package com.xingchen.service.impl
 * @date 2022/7/17 9:00
 */
@Service
public class MsgServiceImpl extends ServiceImpl<MsgMapper, Msg> implements MsgService {
    @Autowired
    private MsgMapper msgMapper;

    @Override
    public void isWatch(Integer messageId) {
        Msg msg = msgMapper.selectById(messageId);
        if(msg==null){
            return;
        }
        msg.setMessageStatue(1);
        msgMapper.updateById(msg);

    }

    @Override
    public IPage<Msg> allMes(Integer currentPage, Integer pageSize) {
        QueryWrapper<Msg> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("message_time");
        IPage page=new Page(currentPage,pageSize);
        msgMapper.selectPage(page,queryWrapper);
        return page;
    }
}
