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
public class Answer {
    @TableId
    @ApiModelProperty(value="答题id")
    private Integer answerId;
    @ApiModelProperty(value="题目id")
    private Integer topicId;
    @ApiModelProperty(value="答题人id")
    private Integer answerUid;
    @ApiModelProperty(value="回答")
    private String answerContent;
    @ApiModelProperty(value="回答时间")
    private String answerTime;
    @ApiModelProperty(value="回答的正确与否")
    private Integer answerResult;
    @TableField(exist=false)
    public String userName;
    @TableField(exist=false)
    public String userHeadshotUrl;
    @TableField(exist=false)
    private String topicDifficulty;
    @TableField(exist=false)
    private String topicTime;
    @TableField(exist=false)
    private Integer topicCollections;
    @TableField(exist=false)
    private Integer topicSearch;
    @TableField(exist=false)
    private String topicName;
    @TableField(exist=false)
    private String topicType;
    @TableField(exist=false)
    private String topicVip;



    public Answer(Integer topicId, Integer answerUid, String answerContent, String answerTime) {
        this.topicId = topicId;
        this.answerUid = answerUid;
        this.answerContent = answerContent;
        this.answerTime = answerTime;
    }
    public Answer(Integer answerId,Integer topicId, Integer answerUid, String answerContent, String answerTime) {
        this.answerId=answerId;
        this.topicId = topicId;
        this.answerUid = answerUid;
        this.answerContent = answerContent;
        this.answerTime = answerTime;
    }
}
