package com.rabbit.robot.scheduling.jx3;

import com.google.gson.Gson;
import com.rabbit.robot.client.HttpClient;
import com.rabbit.robot.constants.jx.ResultInfo;
import com.rabbit.robot.helper.SendHelper;
import com.rabbit.robot.scheduling.crons.CronTaskRegistrar;
import com.rabbit.robot.scheduling.crons.SchedulingRunnable;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.message.data.AtAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 开服监控
 *
 * @author 邢晨旭
 * @date 2020/12/29
 */
@Slf4j
@Component("ServerMonitoring")
public class ServerMonitoring {

    public static SchedulingRunnable serverMonitoringTask = null;
    public static Set<Long> groups = new HashSet<>(2);

    @Autowired
    CronTaskRegistrar cronTaskRegistrar;

    public void serverMonitoring() {
        try {
            String result = HttpClient.sendGet("https://jx3api.com/next/server.php", HttpClient.creatParams());
            Result servers = new Gson().fromJson(result, Result.class);
            ResultInfo resultInfo = new ResultInfo("剑胆琴心", "电信五区", 1);
            if (servers.getCode() == 0) {
                log.error("服务器查询异常");
                return;
            }
            if (servers.getData().contains(resultInfo)) {
                cronTaskRegistrar.removeCronTask(serverMonitoringTask);
                serverMonitoringTask = null;
                SendHelper.sendGroupBatch(groups, AtAll.INSTANCE.plus("₍ᐢ..ᐢ₎♡开服啦"));
                groups.clear();
            }
        } catch (Exception e) {
            cronTaskRegistrar.removeCronTask(serverMonitoringTask);
            serverMonitoringTask = null;
            groups.clear();
        }

    }
}

@Data
class Result {
    private Integer code;
    private List<ResultInfo> data;
}
