package com.xingchen.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Topic {
    @TableId("topic_id")
    @ApiModelProperty(value="题目id")
    private Integer topicId;
    @ApiModelProperty(value="题目难易程度")
    private String topicDifficulty;
    @ApiModelProperty(value="题目内容")
    private String topicContent;
    @ApiModelProperty(value="题目学科类别")
    private String topicCourse;
    @ApiModelProperty(value="题目专业类别")
    private String topicProfessional;
    @ApiModelProperty(value="题目作者id")
    private Integer topicUid;
    @ApiModelProperty(value="题目发布时间")
    private String topicTime;
    @ApiModelProperty(value="题目状态")
    private Integer topicStatue;
    @ApiModelProperty(value="题目收藏数")
    private Integer topicCollections;
    @ApiModelProperty(value="题目浏览搜索数")
    private Integer topicSearch;
    @ApiModelProperty(value="标题")
    private String topicName;
    @ApiModelProperty(value="题型")
    private String topicType;
    @ApiModelProperty(value="选择题的正确选项")
    private String successResult;
    @ApiModelProperty(value="选择题的四个选项")
    private String failResult;
    @ApiModelProperty(value="题目vip")
    private String topicVip;
    @ApiModelProperty(value="用户刷题状态")
    private Integer topicPractise;
    @ApiModelProperty(value = "题号(审核通过的顺序钉序号")
    private Integer topicNumber;
    //发布题目相关的构造器
    public Topic(String topicDifficulty, String topicContent, String topicCourse, String topicProfessional, Integer topicUid, String topicTime, String topicName, String topicType, String successResult, String failResult,String topicVIp) {
        this.topicDifficulty = topicDifficulty;
        this.topicContent = topicContent;
        this.topicCourse = topicCourse;
        this.topicProfessional = topicProfessional;
        this.topicUid = topicUid;
        this.topicTime = topicTime;
        this.topicStatue = 0;
        this.topicName = topicName;
        this.topicType = topicType;
        this.successResult = successResult;
        this.failResult = failResult;
        this.topicVip=topicVIp;
        this.topicCollections=0;
        this.topicSearch=0;
        this.topicPractise=0;

    }
    //修改
    public Topic(Integer topicId,String topicDifficulty, String topicContent, String topicCourse, String topicProfessional, Integer topicUid, String topicTime,String topicName, String topicType, String successResult, String failResult,String topicVip) {
        this.topicDifficulty = topicDifficulty;
        this.topicContent = topicContent;
        this.topicCourse = topicCourse;
        this.topicProfessional = topicProfessional;
        this.topicUid = topicUid;
        this.topicTime = topicTime;
        this.topicName = topicName;
        this.topicType = topicType;
        this.successResult = successResult;
        this.failResult = failResult;
        this.topicVip=topicVip;
    }


    public Topic(String topicType, String topicProfessional, String topicCourse,String topicDifficulty ,String topicVip, Integer topicStatue) {
        this.topicType=topicType;
        this.topicProfessional=topicProfessional;
        this.topicCourse=topicCourse;
        this.topicDifficulty = topicDifficulty;
        this.topicVip=topicVip;
        this.topicStatue=topicStatue;
    }

    public Topic(String topicType, String topicProfessional, String topicCourse,String topicDifficulty ,String topicVip) {
        this.topicType=topicType;
        this.topicProfessional=topicProfessional;
        this.topicCourse=topicCourse;
        this.topicDifficulty = topicDifficulty;
        this.topicVip=topicVip;
    }
}
