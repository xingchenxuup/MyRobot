package com.rabbit.robot.utils.wrapper;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.rabbit.robot.DO.BaseDO;

/**
 * @author 邢晨旭
 * @date 2020/11/14
 */
public class CreatWrapper {
    public static LambdaUpdateWrapper<BaseDO> delete(Long id) {

        LambdaUpdateWrapper<BaseDO> wrapper = Wrappers.lambdaUpdate();
        wrapper.eq(BaseDO::getId, id);
        return wrapper;
    }
}
