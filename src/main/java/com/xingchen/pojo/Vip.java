package com.xingchen.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Li
 * @Date 2022/7/19 18:26
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Vip {
    @TableId("vip_id")
    private Integer vipId;
    private Integer vipTime;
    private Double vipPrice;
}
