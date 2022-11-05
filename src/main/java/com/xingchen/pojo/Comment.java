package com.xingchen.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Comment {
    @TableId
    private String commentId;
    private Integer topicId;//题目id
    private String commentContent;//题解内容
    private Integer commenterId;//评论者id(存在的用户id)
    private String commentTime;//评论时间
    private Integer commenterSonId;//子评论id(存在的用户id)
    private Integer commentStatue;//如果是1则为父评论,为零是子评论

    public Comment(Integer topicId, String commentContent, Integer commenterId, String commentTime,Integer commentStatue) {
        this.topicId = topicId;
        this.commentContent = commentContent;
        this.commenterId = commenterId;
        this.commentTime = commentTime;
        this.commentStatue=commentStatue;

    }
    public Comment(Integer topicId, String commentContent,Integer commenterId, String commentTime,Integer commenterSonId,Integer commentStatue) {
        this.topicId = topicId;
        this.commentContent = commentContent;
        this.commenterId = commenterId;
        this.commentTime = commentTime;
        this.commenterSonId=commenterSonId;
        this.commentStatue=commentStatue;
    }

}
