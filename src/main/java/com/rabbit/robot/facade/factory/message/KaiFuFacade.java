package com.rabbit.robot.facade.factory.message;

import com.google.gson.Gson;
import com.rabbit.robot.client.HttpClient;
import com.rabbit.robot.constants.ApiURLConstant;
import com.rabbit.robot.constants.jx.ResultInfo;
import com.rabbit.robot.constants.jx.ResultInfos;
import com.rabbit.robot.enums.EnumKeyWord;
import com.rabbit.robot.helper.SendHelper;
import com.rabbit.robot.scheduling.crons.CronTaskRegistrar;
import com.rabbit.robot.scheduling.crons.SchedulingRunnable;
import com.rabbit.robot.scheduling.jx3.ServerMonitoring;
import com.rabbit.robot.utils.MessageUtil;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.Message;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.PlainText;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

import static com.rabbit.robot.star.RobotStar.myself;

/**
 * @author 邢晨旭
 * @date 2020/11/11
 */
@Component
@Slf4j
public class KaiFuFacade implements MessageFacade {

    @Override
    public EnumKeyWord get() {
        return EnumKeyWord.KAI_FU;
    }

    @Override
    public void execute(Contact sender, Contact group, Message message) {
        String content = message.contentToString();
        if (!this.keyWordVerify(this.get(), content)) {
            return;
        }
        try {

            String result = HttpClient.sendGet(ApiURLConstant.KAI_FU, HttpClient.creatParamsForServer(MessageUtil.getKeybyWord(content, 2)));
            ResultInfos resultInfos = new Gson().fromJson(result, ResultInfos.class);
            if (resultInfos.getCode() == 0) {
                SendHelper.sendSing(group, new At(sender.getId()).plus("没有查到服务器信息。"));
                return;
            }
            MessageChain plus = new At(sender.getId()).plus(new PlainText(creatServer(resultInfos, group.getId())));
            SendHelper.sendSing(group, plus);
            TimeUnit.SECONDS.sleep(30);
        } catch (Exception e) {
            SendHelper.sendSing(group, new PlainText("网络繁忙,求助兔兔!!!"));
            SendHelper.sendSing(myself, new PlainText("ChickenSoupURL错误," + e));
        }
    }

    @Autowired
    CronTaskRegistrar cronTaskRegistrar;

    private String creatServer(ResultInfos resultInfos, Long groupId) {
        ResultInfo data = resultInfos.getData();
        StringBuilder stringBuilder = new StringBuilder("\n服务器：");
        stringBuilder.append(data.getServer()).append("\n开服状态：").append(data.getStatus() == 1 ? "已开服" : "维护中");
        if (data.getStatus() != 1) {
            ServerMonitoring.groups.add(groupId);
            //只能存在一个开服监控定时任务
            if (ServerMonitoring.serverMonitoringTask == null) {
                SchedulingRunnable task = new SchedulingRunnable("ServerMonitoring", "serverMonitoring", null);
                ServerMonitoring.serverMonitoringTask = task;
                cronTaskRegistrar.addCronTask(task, "0/10 * * * * ?");
            } else {
                log.info("已存在一个开服监控任务{}", ServerMonitoring.serverMonitoringTask);
            }
        }
        return stringBuilder.toString();
    }


}
