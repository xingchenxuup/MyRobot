package com.rabbit.robot.DO;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author 邢晨旭
 * @date 2020/12/01
 */
@Data
@TableName("pictures")
public class Picture {
    @TableId
    private Long id;
    private String url;
}
