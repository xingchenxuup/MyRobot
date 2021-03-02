package com.rabbit.robot.facade.factory.message.team;

import com.rabbit.robot.constants.CommonConstant;
import com.rabbit.robot.constants.Groups;
import com.rabbit.robot.enums.EnumKeyWord;
import com.rabbit.robot.facade.factory.message.MessageFacade;
import com.rabbit.robot.helper.SendHelper;
import com.rabbit.robot.utils.MessageUtil;
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
public class TeamAuthority implements MessageFacade {
    @Override
    public EnumKeyWord get() {
        return EnumKeyWord.TEAM_AUTHORITY;
    }

    @Override
    public void execute(Contact sender, Contact group, Message message) {
        String content = message.contentToString();
        if (sender.getId() != CommonConstant.ERROR_SEND_QQ && !this.keyWordVerify(this.get(), content)) {
            log.info("权限校验未通过{}", this.getClass().getName());
            return;
        }
        String flag = MessageUtil.getKeybyWord(content, 2);
        log.info("flag->{}", flag);
        if ("开启".equals(flag)) {
            Groups.teamGroup.add(group.getId());
        }
        if ("关闭".equals(flag)) {
            Groups.teamGroup.remove(group.getId());
        }
        SendHelper.sendSing(group, new PlainText("开团功能已" + flag));
    }
}
