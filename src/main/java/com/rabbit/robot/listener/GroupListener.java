package com.rabbit.robot.listener;

import com.rabbit.robot.DO.GroupMessage;
import com.rabbit.robot.constants.CommonConstant;
import com.rabbit.robot.constants.Groups;
import com.rabbit.robot.constants.RobotMenu;
import com.rabbit.robot.enums.EnumKeyWord;
import com.rabbit.robot.facade.factory.ImageFacade;
import com.rabbit.robot.facade.factory.MessageFactory;
import com.rabbit.robot.facade.factory.message.MessageFacade;
import com.rabbit.robot.mapper.GroupMessageDao;
import com.rabbit.robot.utils.MessageUtil;
import kotlin.coroutines.CoroutineContext;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageContent;
import net.mamoe.mirai.message.data.PlainText;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.net.MalformedURLException;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import static com.rabbit.robot.star.RobotStar.myself;

/**
 * @author 邢晨旭
 * @date 2020/7/1 下午12:26
 */
@Slf4j
@Service
public class GroupListener extends SimpleListenerHost {

    @Autowired
    private MessageFactory messageFactory;

    @Autowired
    ImageFacade imageFacade;
    //    @Autowired
//    ZzFacade zzFacade;
    @Resource(name = "myTaskAsyncPool")
    ThreadPoolTaskExecutor threadPool;

    @EventHandler
    public void onMessage(GroupMessageEvent event) throws InterruptedException, MalformedURLException {
        if (event.getSender().getId() != CommonConstant.ERROR_SEND_QQ && !Groups.groups.get(event.getGroup().getId())) {
            return;
        }
        /**
         * 消息链
         */
        MessageChain messageChain = event.getMessage();
        MessageContent plainText = messageChain.get(PlainText.Key);
        String content = Objects.requireNonNull(plainText).contentToString();
        if (!StringUtils.isEmpty(content)) {
            // 关键词检索
            EnumKeyWord ruleEnum = EnumKeyWord.groupFind(MessageUtil.getKeybyWord(content, 1));
            // 会话处理器
            MessageFacade messageFacade = messageFactory.get(ruleEnum);
            if (messageFacade != null) {
                messageFacade.execute(event.getSender(), event.getGroup(), plainText);
            }
        }
        if ("菜单".equals(content)) {
            event.getGroup().sendMessage(new PlainText("功能菜单：\n").plus(RobotMenu.getMenu()));
        }
        CompletableFuture.runAsync(() -> {
            imageFacade.image(event);
        }, threadPool);
        CompletableFuture.runAsync(() -> {
            this.save(event);
        }, threadPool);

    }

    @Override
    public void handleException(@NotNull CoroutineContext context, @NotNull Throwable exception) {
        /**
         * 异常处理方式
         * 给自己发消息
         */
        myself.sendMessage("群聊消息处理错误!" + exception.getMessage());
    }

    public static void main(String[] args) {

    }


    @Autowired
    GroupMessageDao dao;

    private void save(GroupMessageEvent event) {
        GroupMessage groupMessage = new GroupMessage();
        groupMessage.setGroupId(event.getGroup().getId());
        groupMessage.setNameCard(event.getSender().getNameCard());
        groupMessage.setSpecialTitle(event.getSender().getSpecialTitle());
        groupMessage.setSenderNum(event.getSender().getId());
        groupMessage.setNickName(event.getSender().getNick());
        groupMessage.setMessageTime(event.getTime());
        groupMessage.setMessageInfo(event.getMessage().toString());
        dao.insert(groupMessage);
    }

}
