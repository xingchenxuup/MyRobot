package com.rabbit.robot.facade.factory.message;

import com.rabbit.robot.enums.EnumKeyWord;
import com.rabbit.robot.star.RobotStar;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.message.data.Message;
import org.springframework.scheduling.annotation.Async;

import java.net.MalformedURLException;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author 邢晨旭
 * @date 2020/11/11
 */
public interface MessageFacade {


    /**
     * 关键字诶类型
     *
     * @return
     */
    public EnumKeyWord get();

    /**
     * 操作
     *
     * @param sender
     * @param group
     * @param message
     */
    public void execute(Contact sender, Contact group, Message message) throws InterruptedException, MalformedURLException;

    /**
     * 关键词验证
     *
     * @param enumKeyWord
     * @param content
     */
    default boolean keyWordVerify(EnumKeyWord enumKeyWord, String content) {
        if (enumKeyWord.code == 1 && content.indexOf(enumKeyWord.keyWord + " ") == 0) {
            return true;
        }
        if (enumKeyWord.code == 2 && enumKeyWord.keyWord.equals(content)) {
            return true;
        }
        return false;

    }
}
