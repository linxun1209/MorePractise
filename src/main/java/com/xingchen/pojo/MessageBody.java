package com.xingchen.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 定义消息体的结构
 * @author xingchen
 * @version V1.0
 * @Package com.xingchen.utils
 * @date 2022/7/20 15:55
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageBody {
    @TableId
    private Integer MessageBodyId;

    /**
     * 发送人姓名
     */
    private String fromName;

    /**
     * 接收人姓名
     */
    private String toName;

    /**
     * 消息内容
     */
    private String content;

    /**
     *
     * 发送时间
     */
    private Date sendTime;
}

