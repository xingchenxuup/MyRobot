package com.rabbit.robot.test;

import com.rabbit.robot.star.RobotStar;
import net.mamoe.mirai.message.data.Voice;
import net.mamoe.mirai.utils.ExternalResource;

import java.io.IOException;
import java.io.InputStream;

/**
 * @Author: 邢晨旭
 * @Date: 2021/4/12 15:20
 * @Description:
 */
public class Test {
    public static void main(String[] args) throws IOException {
        byte[] bytesArray = new byte[1];
        ExternalResource externalResource = ExternalResource.create(bytesArray);
        ExternalResource.uploadAsVoice(externalResource, RobotStar.bot.getAsFriend());
    }
}
