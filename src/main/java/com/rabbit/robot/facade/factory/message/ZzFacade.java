//package com.rabbit.robot.facade.factory.message;
//
//import com.google.gson.Gson;
//import com.rabbit.robot.client.HttpClient;
//import com.rabbit.robot.constants.ApiURLConstant;
//import com.rabbit.robot.constants.CommonConstant;
//import com.rabbit.robot.helper.SendHelper;
//import com.rabbit.robot.mapper.PictureDAO;
//import com.rabbit.robot.service.PictureService;
//import com.rabbit.robot.star.RobotStar;
//import lombok.Data;
//import lombok.extern.slf4j.Slf4j;
//import net.mamoe.mirai.event.events.GroupMessageEvent;
//import net.mamoe.mirai.message.data.*;
//import org.apache.commons.lang3.StringUtils;
//import org.apache.http.NameValuePair;
//import org.apache.http.message.BasicNameValuePair;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.concurrent.TimeUnit;
//
//import static com.rabbit.robot.star.RobotStar.myself;
//
///**
// * @author 邢晨旭
// * @date 2020/11/11
// */
//@Slf4j
//@Component
//public class ZzFacade {
//    private static boolean flag = true;
//
//    @Autowired
//    PictureDAO dao;
//
//    @Autowired
//    PictureService service;
//
//    public void zz(GroupMessageEvent event, MessageContent at) {
//        if (at == null || at.getTarget() != RobotStar.bot.getId()) {
//            return;
//        }
//        log.info("at->{}", at);
//        MessageChain messageChain = event.getMessage();
//        log.info("messageChain->{}", messageChain);
//        MessageContent plainText = messageChain.get(PlainText.Key);
//        if (plainText == null) {
//            return;
//        }
//        String content = plainText.contentToString();
//        log.info("content->{}", content);
//        String trim = content.substring(StringUtils.lastIndexOf(content, "]") + 1).replace("\n", "").trim();
//        if (StringUtils.isEmpty(trim)) {
//            trim = content.substring(0, StringUtils.indexOf(content, "[")).replace("\n", "").trim();
//        }
//        if (CommonConstant.ERROR_SEND_QQ == event.getSender().getId()) {
//            if ("出来".equals(trim) && flag) {
//                SendHelper.sendSing(event.getGroup(), new PlainText("我回来啦！"));
//                flag = false;
//                return;
//            }
//            if ("闭嘴".equals(trim) && !flag) {
//                SendHelper.sendSing(event.getGroup(), new PlainText("有内鬼，停止交易"));
//                flag = true;
//                return;
//            }
//        }
//        if (flag) {
//            return;
//        }
//        try {
//            String result = HttpClient.sendGet(ApiURLConstant.ARTIFICIAL_INTELLIGENCE, creatParams(trim));
//            String answer = new Gson().fromJson(result, Result.class).data.answer;
//            log.info("回答->{}", answer);
//            SendHelper.sendSing(event.getGroup(), new PlainText(answer));
//            TimeUnit.SECONDS.sleep(10);
//        } catch (Exception e) {
//            log.error(e.getMessage(), e);
//            SendHelper.sendSing(myself, new PlainText("ChickenSoupURL错误," + e));
//        }
//    }
//
//    @Data
//    private
//    class Result {
//        private Answer data;
//    }
//
//    @Data
//    private
//    class Answer {
//        private String answer;
//    }
//
//    private List<NameValuePair> creatParams(String param) {
//        List<NameValuePair> params = new ArrayList<>();
//        params.add(new BasicNameValuePair("question", StringUtils.isEmpty(param) ? "哈哈" : param));
//        params.add(new BasicNameValuePair("token", "jx3zhenhaowan"));
//        return params;
//    }
//
//}
//
