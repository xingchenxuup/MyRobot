package com.rabbit.robot.facade.factory;

import com.rabbit.robot.helper.SendHelper;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.contact.Member;
import net.mamoe.mirai.message.data.*;
import org.springframework.stereotype.Component;

import static com.rabbit.robot.star.RobotStar.myself;

/**
 * @author 邢晨旭
 * @date 2020/11/16
 */
public class FlashImageFacade {

    public static void flashImage(Group group, Member sender, String userName, FlashImage flashImage) {
        try {
            MessageChain plus = new At(sender.getId()).plus(new PlainText(userName)).plus("发送了闪照");
            Image image = flashImage.getImage();
            SendHelper.sendSing(group, plus.plus(image));
            SendHelper.sendSing(myself, image.plus("群：" + group.getName() + "(" + group.getId() + ")" + userName + "(" + sender.getId() + ")").plus("发送了闪照"));
        } catch (Exception e) {
            SendHelper.sendSing(myself, new PlainText("ChickenSoupURL错误," + e));
        }
    }

}
