package com.rabbit.robot.facade.factory.message;

import com.rabbit.robot.client.CreeperClient;
import com.rabbit.robot.constants.ApiURLConstant;
import com.rabbit.robot.constants.CommonConstant;
import com.rabbit.robot.enums.EnumKeyWord;
import com.rabbit.robot.helper.SendHelper;
import com.rabbit.robot.star.RobotStar;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.contact.Member;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.Message;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.PlainText;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import static com.rabbit.robot.star.RobotStar.myself;

/**
 * @author 邢晨旭
 * @date 2020/11/11
 */
@Component
public class SoupFacade implements MessageFacade {

    @Override
    public EnumKeyWord get() {
        return EnumKeyWord.GROUP_SOUP;
    }

    @Override
    public void execute(Contact sender, Contact group, Message message) {
        if (!this.keyWordVerify(this.get(), message.contentToString())) {
            return;
        }
        try {
            Document htmlDocument = CreeperClient.getHtmlDocument(ApiURLConstant.CHICKEN_SOUP_URL, null, null);
            Elements select = htmlDocument.select("p[id=words]");
            MessageChain plus = new At(sender.getId()).plus(new PlainText(select.text()));
            SendHelper.sendSing(group, plus);
        } catch (Exception e) {
            SendHelper.sendSing(group, new PlainText("网络繁忙,求助兔兔!!!"));
            SendHelper.sendSing(myself, new PlainText("ChickenSoupURL错误," + e));
        }
    }
}
