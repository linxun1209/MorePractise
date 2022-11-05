package com.xingchen.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Conclusion {
    @TableId
    private Integer conclusionId;
    private String conclusionContent;
    private Integer userId;
    private String conclusionTime;
    private Integer conclusionVip;
    private Integer conclusionStatue;
    private String conclusionTitle;
    private Integer viewsCount;
    @TableField(exist=false)
    public String userName;
    @TableField(exist=false)
    public String userHeadshotUrl;

    public Conclusion(String conclusionContent, Integer userId, String conclusionTime, Integer conclusionVip,String conclusionTitle) {
        this.conclusionContent = conclusionContent;
        this.userId = userId;
        this.conclusionVip = conclusionVip;
        this.conclusionStatue = 0;
        this.conclusionTime=conclusionTime;
        this.conclusionTitle=conclusionTitle;
        this.viewsCount=0;
    }

    public Conclusion(Integer conclusionId, String conclusionContent, Integer userId, String conclusionTime, Integer conclusionVip,String conclusionTitle) {
        this.conclusionId=conclusionId;
        this.conclusionContent = conclusionContent;
        this.userId = userId;
        this.conclusionVip = conclusionVip;
        this.conclusionTime = conclusionTime;
        this.conclusionTitle=conclusionTitle;
    }
}
