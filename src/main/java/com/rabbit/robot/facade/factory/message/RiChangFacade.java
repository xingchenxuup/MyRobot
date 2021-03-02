package com.rabbit.robot.facade.factory.message;

import com.google.gson.Gson;
import com.rabbit.robot.client.HttpClient;
import com.rabbit.robot.constants.ApiURLConstant;
import com.rabbit.robot.constants.CommonConstant;
import com.rabbit.robot.enums.EnumKeyWord;
import com.rabbit.robot.helper.SendHelper;
import com.rabbit.robot.star.RobotStar;
import lombok.Data;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.contact.Member;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.Message;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.PlainText;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.rabbit.robot.star.RobotStar.myself;

/**
 * @author 邢晨旭
 * @date 2020/11/11
 */
@Component
public class RiChangFacade implements MessageFacade {

    @Override
    public EnumKeyWord get() {
        return EnumKeyWord.RI_CHANG;
    }

    @Override
    public void execute(Contact sender, Contact group, Message message) {
        if (!this.keyWordVerify(this.get(), message.contentToString())) {
            return;
        }
        try {
            String result = HttpClient.sendGet(ApiURLConstant.RICHANG, HttpClient.creatParamsForServer("剑胆琴心")).replace("\"",
                    "").replace(",", "");
            MessageChain plus = new At(sender.getId()).plus(new PlainText(result.substring(1, result.length() - 1)));
            SendHelper.sendSing(group, plus);
            TimeUnit.SECONDS.sleep(10);
        } catch (Exception e) {
            SendHelper.sendSing(group, new PlainText("网络繁忙,求助兔兔!!!"));
            SendHelper.sendSing(myself, new PlainText("ChickenSoupURL错误," + e));
        }
    }

}
