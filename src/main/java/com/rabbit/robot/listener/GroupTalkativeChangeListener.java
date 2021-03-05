package com.rabbit.robot.listener;

import com.rabbit.robot.enums.CD;
import com.rabbit.robot.helper.SendHelper;
import kotlin.coroutines.CoroutineContext;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.contact.Member;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.GroupTalkativeChangeEvent;
import net.mamoe.mirai.event.events.MemberJoinEvent;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.PlainText;
import org.jetbrains.annotations.NotNull;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.net.MalformedURLException;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static com.rabbit.robot.constants.ApiURLConstant.*;
import static com.rabbit.robot.star.RobotStar.myself;
import static com.rabbit.robot.utils.MessageUtil.initImage;

/**
 * @author 邢晨旭
 * @date 2020/10/17 5:58 下午
 * 致终于来到这里的勇敢的人:
 * 永远不要放弃！永远不要对自己失望！永远不要逃走辜负了自己。
 * 永远不要哭啼！永远不要说再见！永远不要说慌来伤害目己。
 */
@Service
@Slf4j
public class GroupTalkativeChangeListener extends SimpleListenerHost {

    @Resource(name = "myTaskAsyncPool")
    ThreadPoolTaskExecutor threadPool;

    @EventHandler
    public void onMessage(GroupTalkativeChangeEvent event) throws MalformedURLException {
        Member newKing = event.getNow();
        Member oldKing = event.getPrevious();
        Group group = event.getGroup();
        String url = "";
        int i = new Random().nextInt(10);
        url = String.format(GROUP_KING, i);
        String finalUrl = url;
        CompletableFuture.runAsync(() -> {
            At atNew = new At(newKing.getId());
            At atOld = new At(oldKing.getId());
            SendHelper.sendSing(group,atOld.plus("旧王谢幕\n").plus(atNew.plus("新王登基")));
            try {
                TimeUnit.MILLISECONDS.sleep(2000);
            } catch (InterruptedException e) {
                log.error("龙王线程休眠出错");
            }
            Image image = event.getGroup().uploadImage(initImage(finalUrl));
            SendHelper.sendSing(group,image);
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
}
