package com.xingchen.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Collection {
    @TableId("collection_id")
    @ApiModelProperty(value="收藏id")
    private Integer collectionId;
    @ApiModelProperty(value="用户id")
    private Integer userId;
    @ApiModelProperty(value="被收藏的题目id")
    private Integer topicId;

}
