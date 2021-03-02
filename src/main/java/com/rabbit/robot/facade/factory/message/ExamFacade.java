package com.rabbit.robot.facade.factory.message;

import com.google.gson.Gson;
import com.rabbit.robot.client.HttpClient;
import com.rabbit.robot.constants.ApiURLConstant;
import com.rabbit.robot.constants.jx.Exams;
import com.rabbit.robot.enums.EnumKeyWord;
import com.rabbit.robot.helper.SendHelper;
import com.rabbit.robot.utils.MessageUtil;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.message.data.Face;
import net.mamoe.mirai.message.data.Message;
import net.mamoe.mirai.message.data.PlainText;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author 邢晨旭
 * @date 2020/11/11
 */
@Component
public class ExamFacade implements MessageFacade {

    @Override
    public EnumKeyWord get() {
        return EnumKeyWord.EXAM;
    }

    @Override
    public void execute(Contact sender, Contact group, Message message) {
        String content = message.contentToString();
        if (!this.keyWordVerify(this.get(), content)) {
            return;
        }
        String question = MessageUtil.getKeybyWord(content, 2);
        String result = "";
        if (isAbc(question)) {
            result = HttpClient.sendGet(ApiURLConstant.EXAM1, creatParamsHm(question));
        } else {
            result = HttpClient.sendGet(ApiURLConstant.EXAM2, creatParams(question));
        }
        Exams exams = new Gson().fromJson(result, Exams.class);
        StringBuilder s = new StringBuilder();
        String tag = "";
        if (exams.getData().size() > 1) {
            tag = "输入越详细，查找结果越精确哦\n";
        }
        if (exams.getCode() == 1 && exams.getErrcode() == 0) {
            exams.getData().forEach(exam -> {
                s.append("问：").append(exam.getQuestion()).append("\n");
                s.append("答：").append(exam.getAnswer()).append("\n\n");
            });
        } else {
            SendHelper.sendSing(group, new Face(193).plus("没有找到哦，请再精确一点"));
            return;
        }
        SendHelper.sendSing(group, StringUtils.isEmpty(tag)?new Face(199).plus(new Face(199)).plus(new Face(199)).plus(tag+s):new PlainText(s));
    }

    private Boolean isAbc(String tap) {
        return Pattern.matches("([a-z]|[A-Z])*", tap);
    }

    private static List<NameValuePair> creatParams(String question) {
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("question", question.trim()));
        params.add(new BasicNameValuePair("token", "jx3zhenhaowan"));
        return params;
    }

    private static List<NameValuePair> creatParamsHm(String question) {
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("q", question.trim()));
        params.add(new BasicNameValuePair("offset", "0"));
        params.add(new BasicNameValuePair("limit", "25"));
        return params;
    }

}
