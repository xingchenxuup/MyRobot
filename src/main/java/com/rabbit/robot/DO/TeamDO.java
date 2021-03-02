package com.rabbit.robot.DO;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author 邢晨旭
 * @date 2020/7/22
 */
@Data
@TableName("team")
public class TeamDO extends BaseDO{


    /**
     * 团标题
     */
    private String teamName;
    /**
     * QQ群ID
     */
    private Long groupId;



}

