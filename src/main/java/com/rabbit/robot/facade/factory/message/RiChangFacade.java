package com.rabbit.robot.facade.factory.message;

import com.rabbit.robot.client.HttpClient;
import com.rabbit.robot.constants.ApiURLConstant;
import com.rabbit.robot.enums.CD;
import com.rabbit.robot.enums.EnumKeyWord;
import com.rabbit.robot.helper.SendHelper;
import com.rabbit.robot.star.RobotStar;
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
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.rabbit.robot.star.RobotStar.myself;

/**
 * @author 邢晨旭
 * @date 2020/11/11
 */
@Component
public class RiChangFacade implements MessageFacade {
    private static final Map<Long, Boolean> GroupCD = RobotStar.bot.getGroups().stream().collect(Collectors.toMap(Group::getId, group -> false, (k1, k2) -> false));

    @Resource(name = "myTaskAsyncPool")
    ThreadPoolTaskExecutor threadPool;

    @Override
    public EnumKeyWord get() {
        return EnumKeyWord.RI_CHANG;
    }

    @Override
    public void execute(Contact sender, Contact group, Message message) {
        if (!this.keyWordVerify(this.get(), message.contentToString()) || GroupCD.get(group.getId())) {
            return;
        }
        GroupCD.put(group.getId(), true);
        try {
            CompletableFuture.runAsync(() -> {
                String result = HttpClient.sendGet(ApiURLConstant.RICHANG, HttpClient.creatParamsForServer("剑胆琴心")).replace("\"",
                        "").replace(",", "");
                MessageChain plus = new At(sender.getId()).plus(new PlainText(result.substring(1, result.length() - 1)));
                SendHelper.sendSing(group, plus.plus(CD.TEN.msg));
                try {
                    TimeUnit.MILLISECONDS.sleep(CD.TEN.cd);
                    GroupCD.put(group.getId(), false);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }, threadPool);

        } catch (Exception e) {
            SendHelper.sendSing(group, new PlainText("网络繁忙,求助兔兔!!!"));
            SendHelper.sendSing(myself, new PlainText("ChickenSoupURL错误," + e));
        }
    }

}
