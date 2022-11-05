package com.xingchen.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xingchen.dao.NoticeMapper;
import com.xingchen.pojo.Answer;
import com.xingchen.pojo.Notice;
import com.xingchen.pojo.User;
import com.xingchen.service.INoticeService;
import com.xingchen.service.UserService;
import com.xingchen.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

/**
 * @author xingchen
 * @version V1.0
 * @Package com.xingchen.service.impl
 * @date 2022/7/22 10:52
 */
@Service
public class NoticeServiceImpl extends ServiceImpl<NoticeMapper, Notice> implements INoticeService {

    @Autowired
    private NoticeMapper noticeMapper;
    @Autowired
    private UserService userService;



    @Override
    public void saveNotice(Integer toId, String noticeContent, Integer fromId,Integer noticeStatue) {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Notice notice=new Notice(toId,fromId,noticeContent,format.format(date),noticeStatue);
        noticeMapper.insert(notice);
    }

    @Override
    public IPage<Notice> getNoticeFrom(Integer fromId,Integer currentPage,Integer pageSize) {
        QueryWrapper<Notice> courseQueryWrapper=new QueryWrapper<>();
        courseQueryWrapper.eq("from_id",fromId);
        courseQueryWrapper.orderByDesc("notice_time");
        IPage<Notice> page=new Page<>(currentPage,pageSize);
        noticeMapper.selectPage(page,courseQueryWrapper);
        return page;
    }

    @Override
    public boolean deleteNotice(Integer noticeId) {
        return noticeMapper.deleteById(noticeId)>0;
    }

    @Override
    public boolean deleteMoreNotice(Integer[] noticeId) {
        return noticeMapper.deleteBatchIds(Arrays.asList(noticeId))>0;
    }

    @Override
    public R changeReadStatue(Integer noticeId) {
        Notice notice = noticeMapper.selectById(noticeId);
        if(notice==null){
            return new R(false,"消息异常,请刷新");
        }
        notice.setNoticeReadStatue(1);
        noticeMapper.updateById(notice);
        return new R(false,"该通知已读");
    }

    @Override
    public IPage<Notice> getNoticeRead(Integer noticeReadStatue, Integer currentPage, Integer pageSize,Integer fromId) {
        QueryWrapper<Notice> queryWrapper=new QueryWrapper();
        queryWrapper.eq("notice_read_statue",noticeReadStatue);
        queryWrapper.eq("from_id", fromId);
        queryWrapper.orderByDesc("notice_time");
        IPage page=new Page<>(currentPage,pageSize);
        noticeMapper.selectPage(page,queryWrapper);
        return page;
    }

    @Override
    public boolean booleanRead(Integer fromId) {
        QueryWrapper<Notice> noticeQueryWrapper=new QueryWrapper<>();
        noticeQueryWrapper.eq("from_id", fromId);
        noticeQueryWrapper.eq("notice_statue",0);
        noticeQueryWrapper.orderByDesc("notice_time");
        Long aLong = noticeMapper.selectCount(noticeQueryWrapper);
        return aLong>0;
    }

}
