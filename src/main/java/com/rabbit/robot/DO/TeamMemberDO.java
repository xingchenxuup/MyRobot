package com.rabbit.robot.DO;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author 邢晨旭
 * @date 2020/7/22
 */
@Data
@TableName("team_member")
public class TeamMemberDO extends BaseDO {


    /**
     * 团ID
     */
    private Long teamId;
    /**
     * 队伍位置
     */
    private Long location;
    /**
     * 职业
     */
    private String position;
    /**
     * 填充颜色
     */
    private String color;
    /**
     * 0 外功 1内功  2奶 3t
     */
    private Integer type;
    private String src;
    /**
     * 角色名
     */
    private String memberName;
    /**
     * QQ
     */
    private Long qq;
    /**
     * 插队标识报名默认0,插队1
     */
    private Integer isInsert;


}

