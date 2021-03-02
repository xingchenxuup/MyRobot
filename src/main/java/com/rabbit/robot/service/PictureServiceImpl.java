package com.rabbit.robot.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rabbit.robot.DO.Picture;
import com.rabbit.robot.mapper.PictureDAO;
import org.springframework.stereotype.Service;

/**
 * @author 邢晨旭
 * @date 2020/12/02
 */
@Service
public class PictureServiceImpl extends ServiceImpl<PictureDAO, Picture> implements PictureService {
}
