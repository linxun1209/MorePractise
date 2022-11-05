package com.xingchen.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Note {
    @TableId
    @ApiModelProperty(value="笔记id")
    private Integer noteId;
    @ApiModelProperty(value="题目id")
    private Integer topicId;
    @ApiModelProperty(value="笔记内容")
    private String noteContent;
    @ApiModelProperty(value="用户id")
    private Integer userId;
    @ApiModelProperty(value="笔记发布时间")
    private String noteTime;
    @TableField(exist=false)
    public String userName;
    @TableField(exist=false)
    public String userHeadshotUrl;

    public Note(Integer topicId, String noteContent,Integer userId,String noteTime) {
        this.topicId = topicId;
        this.noteContent = noteContent;
        this.userId = userId;
        this.noteTime=noteTime;
    }

    public Note(Integer noteId, Integer topicId, String noteContent, Integer userId, String noteTime) {
        this.noteId=noteId;
        this.topicId = topicId;
        this.noteContent = noteContent;
        this.userId = userId;
        this.noteTime=noteTime;
    }
}
