package com.rabbit.robot.listener;

import com.rabbit.robot.constants.CommonConstant;
import com.rabbit.robot.helper.SendHelper;
import com.rabbit.robot.star.RobotStar;
import kotlin.coroutines.CoroutineContext;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.contact.Member;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.MemberJoinEvent;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.Image;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.util.Random;

import static com.rabbit.robot.constants.ApiURLConstant.GROUP_JOIN;
import static com.rabbit.robot.constants.ApiURLConstant.GROUP_JOIN2;
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
public class GroupRequestListener extends SimpleListenerHost {

    @EventHandler
    public void onMessage(MemberJoinEvent event) throws MalformedURLException {
        Member member = event.getMember();
        Group group = event.getGroup();
        At at = new At(member.getId());
        String url = "";
        int i = new Random().nextInt(10);
        if (i == 4) {
            url = GROUP_JOIN2;
        } else {
            url = String.format(GROUP_JOIN, i);
        }
        Image image = event.getGroup().uploadImage(initImage(url));
        SendHelper.sendSing(group, at.plus("(●'◡'●)欢迎小可爱\n改下id哦(职业+昵称)").plus(image));

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
