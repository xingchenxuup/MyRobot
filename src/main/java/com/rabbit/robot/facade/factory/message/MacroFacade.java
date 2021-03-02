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
import net.mamoe.mirai.message.data.PlainText;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.rabbit.robot.star.RobotStar.myself;

/**
 * @author 邢晨旭
 * @date 2020/11/11
 */
@Component
public class MacroFacade implements MessageFacade {

    @Override
    public EnumKeyWord get() {
        return EnumKeyWord.MACRO;
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
                SendHelper.sendSing(group, new At(sender.getId()).plus("没有查询到该职业，换个称呼试试"));
                return;
            }
            SendHelper.sendSing(group, new PlainText(enumPositionLocation.position+"宏："+enumPositionLocation.macroUrl));
//            String result = HttpClient.sendGet(ApiURLConstant.MACRO, HttpClient.creatParamsForName(enumPositionLocation.fullName));
//            SendHelper.sendSing(group, new At(sender.getId()).plus("宏命令已发送至私聊"));
//            SendHelper.sendSing(sender, new PlainText(result));
            //int index = StringUtils.ordinalIndexOf(result, "]\n", 2);
//            String qy = result.substring(0, index + 1);
//            String macro = result.substring(index + 1);
//            SendHelper.sendSing(group, new At(sender.getId()).plus("\n" + new PlainText(qy)));
//            List<String> macros = makeMacro(macro);
//            macros.forEach(m -> {
//                SendHelper.sendSing(group, new At(sender.getId()).plus("\n" + new PlainText(m)));
//            });
            TimeUnit.SECONDS.sleep(30);
        } catch (Exception e) {
            SendHelper.sendSing(group, new PlainText("网络繁忙,求助兔兔!!!"));
            SendHelper.sendSing(myself, new PlainText("ChickenSoupURL错误," + e));
        }
    }

    private static List<String> makeMacro(String macro) {
        List<String> resultList = new ArrayList<>();
        List<String> collect = Arrays.stream(macro.split("·")).filter(StringUtils::isNotBlank).collect(Collectors.toList());
        int size = collect.size();
        if (size == 0) {
            resultList.add(macro);
            return resultList;
        }
        for (int i = 1; i < size; i++) {
            StringBuffer m = new StringBuffer(collect.get(i));
            m.deleteCharAt(StringUtils.ordinalIndexOf(m, "]", 1));
            if (i < 3 && size == 4 || (i == 1)) {
                m.delete(m.lastIndexOf("["), m.length());
            }
            resultList.add(m.toString());
        }
        return resultList;
    }


}
