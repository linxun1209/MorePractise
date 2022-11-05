package com.xingchen.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Course {
    @TableId
    @ApiModelProperty(value="课程id")
    private Integer courseId;
    @ApiModelProperty(value="专业下的课程")
    private Integer professionalId;
    @ApiModelProperty(value="课程名字")
    private String courseName;

    public Course(Integer professionId, String courseName) {
        this.professionalId = professionId;
        this.courseName = courseName;
    }
    public Course(Integer professionId) {
        this.professionalId = professionId;
    }
}
