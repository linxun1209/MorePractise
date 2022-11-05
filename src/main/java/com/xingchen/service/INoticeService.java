package com.xingchen.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xingchen.pojo.Notice;
import com.xingchen.utils.R;

/**
 * @author xingchen
 * @version V1.0
 * @Package com.xingchen.service
 * @date 2022/7/22 10:51
 */
public interface INoticeService extends IService<Notice> {


    void saveNotice(Integer toId, String noticeContent, Integer fromId,Integer noticeStatuie);

    IPage<Notice> getNoticeFrom(Integer fromId,Integer currentPage,Integer pageSize);

    boolean deleteNotice(Integer noticeId);

    boolean deleteMoreNotice(Integer[] noticeId);

    R changeReadStatue(Integer noticeId);

    IPage<Notice> getNoticeRead(Integer noticeReadStatue, Integer currentPage, Integer pageSize,Integer fromId);

    boolean booleanRead(Integer fromId);
}
