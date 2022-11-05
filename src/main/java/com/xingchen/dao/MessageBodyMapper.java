package com.xingchen.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xingchen.pojo.MessageBody;
import org.springframework.stereotype.Repository;

/**
 * @author xingchen
 * @version V1.0
 * @Package com.xingchen.dao
 * @date 2022/7/21 9:09
 */
@Repository
public interface MessageBodyMapper extends BaseMapper<MessageBody> {


    void insert(String message);
}
