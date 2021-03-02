package com.rabbit.robot.facade.factory.message;

import com.google.gson.Gson;
import com.rabbit.robot.client.HttpClient;
import com.rabbit.robot.constants.ApiURLConstant;
import com.rabbit.robot.constants.jx.ResultInfos;
import com.rabbit.robot.enums.EnumKeyWord;
import com.rabbit.robot.helper.SendHelper;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.message.data.Face;
import net.mamoe.mirai.message.data.Message;
import net.mamoe.mirai.message.data.PlainText;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static com.rabbit.robot.star.RobotStar.myself;

/**
 * @author 邢晨旭
 * @date 2020/11/11
 */
@Component
public class TianGouFacade implements MessageFacade {

    @Override
    public EnumKeyWord get() {
        return EnumKeyWord.TIAN_GOU;
    }

    @Override
    public void execute(Contact sender, Contact group, Message message) {
        if (!this.keyWordVerify(this.get(), message.contentToString())) {
            return;
        }
        try {
            String result = HttpClient.sendGet(ApiURLConstant.TIAN_GOU, null);
            SendHelper.sendSing(group, new Face(193).plus(new Gson().fromJson(result, ResultInfos.class).getData().getText()));
        } catch (Exception e) {
            SendHelper.sendSing(group, new PlainText("网络繁忙,求助兔兔!!!"));
            SendHelper.sendSing(Objects.requireNonNull(myself), new PlainText("ChickenSoupURL错误," + e));
        }
    }

    public static void main(String[] args) {
        String result = HttpClient.sendGet(ApiURLConstant.TIAN_GOU, null);
        System.out.println(result);
        System.out.println(new Gson().fromJson(result, ResultInfos.class).getData().getText());
    }


}
