package com.rabbit.robot.facade.factory.message;

import com.rabbit.robot.constants.CommonConstant;
import com.rabbit.robot.constants.Groups;
import com.rabbit.robot.enums.EnumKeyWord;
import com.rabbit.robot.helper.SendHelper;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.message.data.Message;
import net.mamoe.mirai.message.data.PlainText;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @author 邢晨旭
 * @date 2020/12/14
 */
@Component
@Order(1)
@Slf4j
public class OtherAuthorityClose implements MessageFacade {
    @Override
    public EnumKeyWord get() {
        return EnumKeyWord.OTHER_AUTHORITY_CLOSE;
    }

    @Override
    public void execute(Contact sender, Contact group, Message message) throws InterruptedException {
        String content = message.contentToString();
        if (sender.getId() != CommonConstant.ERROR_SEND_QQ && !this.keyWordVerify(this.get(), content)) {
            return;
        }
        Groups.groups.put(group.getId(), false);
        SendHelper.sendSing(group, new PlainText("群聊功能已关闭"));
    }
}
