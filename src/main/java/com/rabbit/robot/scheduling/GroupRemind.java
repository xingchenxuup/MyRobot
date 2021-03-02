//package com.rabbit.robot.scheduling;
//
//import com.rabbit.robot.helper.SendHelper;
//import com.rabbit.robot.star.RobotStar;
//import lombok.extern.slf4j.Slf4j;
//import net.mamoe.mirai.message.data.Image;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//import static com.rabbit.robot.constants.ApiURLConstant.GROUP_FAN;
//import static com.rabbit.robot.utils.MessageUtil.initImage;
//
///**
// * @author 邢晨旭
// * @date 2020/11/20
// */
//@Slf4j
//@Component
//public class GroupRemind {
//
//    @Scheduled(cron = "0 0 8,12,18 * * ?")
//    public void demoSchedule() {
//        RobotStar.bot.getGroups().forEach(group -> {
//            Image image = group.uploadImage(initImage(GROUP_FAN));
//            SendHelper.sendSing(group, image);
//        });
////0/10 10 7 ? * 2,5,6 *
//    }
//
//}
