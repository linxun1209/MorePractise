package com.xingchen.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Subjects {
    @TableId
    @ApiModelProperty(value="科目id")
    private Integer subjectsId;
    @ApiModelProperty(value="学科名字不能重复")
    private String subjectsName;
    public Subjects(String professionalName) {
        this.subjectsName = subjectsName;
    }
}