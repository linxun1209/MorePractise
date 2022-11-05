package com.xingchen.pojo;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Li
 * @Date 2022/7/13 21:12
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("post_comment")
public class PostComment {
    @TableId("post_comment_id")
    private Integer PostCommentId;
    private Integer userId;
    @TableField(exist=false)
    private String userName;
    @TableField(exist=false)
    private String userHeadshotUrl;
    private Integer commentFather;
    private Integer objectId;
    @TableField(exist=false)
    private String objectName;
    private String commentContent;
    private String commentTime;
    private Integer commentState;
    @TableField(exist = false)
    private Integer likes;
    @TableField(exist = false)
    private Integer childrenNum;
    @TableField(exist=false)
    private List<PostComment> childrenComment;

    public PostComment( Integer userId,Integer commentFather, Integer objectId, String commentContent, String commentTime, Integer commentState){
        this.userId = userId;
        this.commentFather=commentFather;
        this.objectId = objectId;
        this.commentContent = commentContent;
        this.commentTime = commentTime;
        this.commentState = commentState;
    }
}
