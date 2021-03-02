package com.rabbit.robot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rabbit.robot.DO.Picture;
import org.apache.ibatis.annotations.Param;

/**
 * @author 邢晨旭
 * @date 2020/7/22
 */
public interface PictureDAO extends BaseMapper<Picture> {
    String getPicture(@Param("flag") Integer flag);
}
