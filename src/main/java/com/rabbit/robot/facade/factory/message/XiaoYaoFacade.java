package com.rabbit.robot.facade.factory.message;

import com.rabbit.robot.client.HttpClient;
import com.rabbit.robot.constants.ApiURLConstant;
import com.rabbit.robot.enums.CD;
import com.rabbit.robot.enums.EnumKeyWord;
import com.rabbit.robot.enums.EnumPositionLocation;
import com.rabbit.robot.helper.SendHelper;
import com.rabbit.robot.star.RobotStar;
import com.rabbit.robot.utils.MessageUtil;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.Message;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.PlainText;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static com.rabbit.robot.star.RobotStar.myself;

/**
 * @author 邢晨旭
 * @date 2020/11/11
 */
@Component
@Slf4j
public class XiaoYaoFacade implements MessageFacade {

    private static final Map<Long, Boolean> GroupCD = RobotStar.bot.getGroups().stream().collect(Collectors.toMap(Group::getId, group -> false, (k1, k2) -> false));

    @Resource(name = "myTaskAsyncPool")
    ThreadPoolTaskExecutor threadPool;

    @Override
    public EnumKeyWord get() {
        return EnumKeyWord.XIAO_YAO;
    }

    @Override
    public void execute(Contact sender, Contact group, Message message) {
        String content = message.contentToString();
        if (!this.keyWordVerify(this.get(), content) || GroupCD.get(group.getId())) {
            return;
        }
        GroupCD.put(group.getId(), true);
        try {
            EnumPositionLocation enumPositionLocation = EnumPositionLocation.get(MessageUtil.getKeybyWord(content, 2));
            if (enumPositionLocation == null) {
                SendHelper.sendSing(group, new PlainText("没有查到哦,换个称呼试试"));
                return;
            }
            CompletableFuture.runAsync(() -> {
                String result = HttpClient.sendGet(ApiURLConstant.XIAO_YAO, HttpClient.creatParamsForName(enumPositionLocation.fullName)).replace("\"",
                        "").replace(",", "");
                MessageChain plus = new At(sender.getId()).plus(new PlainText(result.substring(1, result.length() - 1)));
                SendHelper.sendSing(group, plus.plus(CD.THIRTY.msg));
                try {
                    Thread.sleep(CD.THIRTY.cd);
                    GroupCD.put(group.getId(), false);
                } catch (InterruptedException e) {
                    log.error("线程等待异常-小药");
                }
            }, threadPool);
        } catch (Exception e) {
            SendHelper.sendSing(group, new PlainText("网络繁忙,求助兔兔!!!"));
            SendHelper.sendSing(myself, new PlainText("ChickenSoupURL错误," + e));
        }

    }

    public static void main(String[] args) {
        String result = HttpClient.sendGet(ApiURLConstant.XIAO_YAO, HttpClient.creatParamsForName("鲸鱼")).replace("\"",
                "").replace(",", "");
        System.out.println(result.substring(1, result.length() - 1).concat("\n"));
        System.out.println(result.substring(1, result.length() - 1));
    }

}
