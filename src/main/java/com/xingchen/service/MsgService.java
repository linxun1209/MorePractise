package com.xingchen.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xingchen.pojo.Msg;

/**
 * @author xingchen
 * @version V1.0
 * @Package com.xingchen.service
 * @date 2022/7/17 8:59
 */
public interface MsgService extends IService<Msg> {
    void isWatch(Integer messageId);

    IPage<Msg> allMes(Integer currentPage, Integer pageSize);
}
