package com.rabbit.robot.helper;

import com.rabbit.robot.constants.CommonConstant;
import com.rabbit.robot.star.RobotStar;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.contact.User;
import net.mamoe.mirai.message.MessageReceipt;
import net.mamoe.mirai.message.data.Message;
import net.mamoe.mirai.message.data.PlainText;

import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * @author 邢晨旭
 * @date 2020/4/4 上午2:14
 */
@Slf4j
public class SendHelper {

    /**
     * 发送消息
     *
     * @param contact  即可以与{@link Bot} 互动的对象. 包含[用户]{@link User}和[群]{@link Group}.
     * @param messages 可发送的或从服务器接收的消息.
     */
    public static void sendSing(Contact contact, Message messages) {
//        log.info("消息长度->{}消息内容->{}", messages.contentToString().length(), messages);
        contact.sendMessage(messages);

    }

    /**
     * 多群批发
     *
     * @param bot      机器人对象. 一个机器人实例登录一个 QQ 账号
     * @param groupIds 群号集合. 需要发送信息的群
     * @param messages 消息集合. 消息集合
     */
    public static void sendGroupBatch(Bot bot, List<Long> groupIds, List<Message> messages) {

        messages.forEach(message -> {
            for (Long groupId : groupIds) {
                try {
                    Group group = bot.getGroup(groupId);
                    group.sendMessage(message);
                } catch (Exception e) {
                    PlainText plainText = new PlainText("群:" + groupId + "未找到。");
                    bot.getFriend(CommonConstant.ERROR_SEND_QQ).sendMessage(plainText);
                }
            }
        });
    }

    public static void sendGroupBatch(Set<Long> groupIds, Message message) {
        Bot bot = RobotStar.bot;
        groupIds.forEach(groupId -> {
            try {
                Group group = bot.getGroup(groupId);
                assert group != null;
                group.sendMessage(message);
            } catch (Exception e) {
                PlainText plainText = new PlainText("群:" + groupId + "未找到？。" + e.getMessage());
                Objects.requireNonNull(bot.getFriend(CommonConstant.ERROR_SEND_QQ)).sendMessage(plainText);
            }
        });
    }

}
