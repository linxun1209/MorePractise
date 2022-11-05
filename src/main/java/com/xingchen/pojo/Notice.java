package com.xingchen.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Notice {

    @TableId
    private Integer noticeId;

//    接收方
    private Integer toId;
//    发送方
    private Integer fromId;
//    消息
    private String noticeContent;

    //时间
    private String noticeTime;

    private Integer noticeStatue;

    private Integer noticeReadStatue;

    public Notice(Integer toId, Integer fromId, String noticeContent, String noticeTime, Integer noticeStatue) {
        this.toId = toId;
        this.fromId = fromId;
        this.noticeContent = noticeContent;
        this.noticeTime = noticeTime;
        this.noticeStatue = noticeStatue;
        this.noticeReadStatue = 0;
    }

    public Notice(Integer toId, Integer fromId, String noticeContent,String format) {
        this.toId = toId;
        this.fromId = fromId;
        this.noticeContent=noticeContent;
        this.noticeTime=format;

    }

}
