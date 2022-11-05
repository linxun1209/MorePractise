package com.xingchen.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Professional {
    @TableId
    @ApiModelProperty(value="专业id")
    private Integer professionalId;
    @ApiModelProperty(value="专业名字不能重复")
    private String professionalName;

    public Professional(String professionalName) {
        this.professionalName = professionalName;
    }
}
