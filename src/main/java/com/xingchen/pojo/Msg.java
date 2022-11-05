package com.xingchen.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author xingchen
 * @version V1.0
 * @Package com.xingchen.pojo
 * @date 2022/7/17 8:55
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Msg {
    @TableId
    private Integer messageId;
    private String messageContent;
    private String messageTime;
    private Integer messageStatue;

    public Msg(String messageContent,String messageTime) {
        this.messageContent=messageContent;
        this.messageTime=messageTime;
        this.messageStatue=0;
    }
}
