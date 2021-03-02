package com.rabbit.robot.facade.factory.message;

import com.rabbit.robot.client.HttpClient;
import com.rabbit.robot.constants.ApiURLConstant;
import com.rabbit.robot.constants.CommonConstant;
import com.rabbit.robot.enums.EnumKeyWord;
import com.rabbit.robot.enums.EnumPositionLocation;
import com.rabbit.robot.helper.SendHelper;
import com.rabbit.robot.star.RobotStar;
import com.rabbit.robot.utils.MessageUtil;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.contact.Member;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.Message;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.PlainText;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

import static com.rabbit.robot.star.RobotStar.myself;

/**
 * @author 邢晨旭
 * @date 2020/11/11
 */
@Component
public class XiaoYaoFacade implements MessageFacade {

    @Override
    public EnumKeyWord get() {
        return EnumKeyWord.XIAO_YAO;
    }

    @Override
    public void execute(Contact sender, Contact group, Message message) {
        String content = message.contentToString();
        if (!this.keyWordVerify(this.get(), content)) {
            return;
        }
        try {
            EnumPositionLocation enumPositionLocation = EnumPositionLocation.get(MessageUtil.getKeybyWord(content, 2));
            if (enumPositionLocation == null) {
                SendHelper.sendSing(group, new PlainText("没有查到哦,换个称呼试试"));
                return;
            }
            String result = HttpClient.sendGet(ApiURLConstant.XIAO_YAO, HttpClient.creatParamsForName(enumPositionLocation.fullName)).replace("\"",
                    "").replace(",", "");
            MessageChain plus = new At(sender.getId()).plus(new PlainText(result.substring(1, result.length() - 1).concat("\n")));
            SendHelper.sendSing(group, plus);
            TimeUnit.SECONDS.sleep(30);
        } catch (Exception e) {
            SendHelper.sendSing(group, new PlainText("网络繁忙,求助兔兔!!!"));
            SendHelper.sendSing(myself, new PlainText("ChickenSoupURL错误," + e));
        }
    }

    public static void main(String[] args) {
        String content = "日常";
        EnumKeyWord enumKeyWord = EnumKeyWord.RI_CHANG;
        EnumPositionLocation enumPositionLocation = EnumPositionLocation.get(MessageUtil.getKeybyWord(content, 2));
        if (enumKeyWord.code == 1 && content.indexOf(enumKeyWord.keyWord + " ") == 0) {
            System.out.println("过");
        }
        if (enumKeyWord.code == 2 && enumKeyWord.keyWord.equals(content)) {
            System.out.println("过");
        }
    }

}
