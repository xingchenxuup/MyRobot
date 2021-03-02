package com.rabbit.robot.DO;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author 邢晨旭
 * @date 2020/11/14
 */
@Data
public class BaseDO implements Serializable {

    /**
     * 主键
     */
    @TableId
    private Long id;

    /**
     * 修改时间
     */
    private Date gmtModify;
    /**
     * 创建时间
     */
    private Date gmtCreate;
    /**
     * 0:否 1:删除
     */
    private Integer deleteYn;

}
