package com.rabbit.robot.facade.factory.message;

import com.google.gson.Gson;
import com.rabbit.robot.client.HttpClient;
import com.rabbit.robot.constants.ApiURLConstant;
import com.rabbit.robot.constants.CommonConstant;
import com.rabbit.robot.enums.EnumKeyWord;
import com.rabbit.robot.enums.EnumPositionLocation;
import com.rabbit.robot.helper.SendHelper;
import com.rabbit.robot.star.RobotStar;
import com.rabbit.robot.utils.MessageUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.contact.Member;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.Message;
import net.mamoe.mirai.message.data.PlainText;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.rabbit.robot.star.RobotStar.myself;
import static com.rabbit.robot.utils.MessageUtil.initImage;

/**
 * @author 邢晨旭
 * @date 2020/11/11
 */
@Slf4j
@Component
public class PeiZhuangFacade implements MessageFacade {

    @Override
    public EnumKeyWord get() {
        return EnumKeyWord.PEI_ZHUANG;
    }

    @Override
    public void execute(Contact sender, Contact group, Message message) {
        String content = message.contentToString();
        if (!this.keyWordVerify(this.get(), message.contentToString())) {
            return;
        }
        try {
            EnumPositionLocation enumPositionLocation = EnumPositionLocation.get(MessageUtil.getKeybyWord(content, 2));
            if (enumPositionLocation == null) {
                SendHelper.sendSing(group, new PlainText("没有查到哦,换个称呼试试"));
                return;
            }
            String keyWord = MessageUtil.getKeybyWord(content, 3);
            if (StringUtils.equalsAny(MessageUtil.getKeybyWord(content, 3), Pz.e, Pz.p)) {
                String result = HttpClient.sendGet(ApiURLConstant.PEIZHUANG, creatParams(enumPositionLocation.fullName));
                Pzs pzs = new Gson().fromJson(result, Pzs.class);
                if (Pz.e.equals(keyWord)) {
                    log.info("配装返回结果", pzs);
                    SendHelper.sendSing(group, new At(sender.getId()).plus(group.uploadImage(initImage(pzs.data.pve))));
                    return;
                }
                if (Pz.p.equals(keyWord)) {
                    SendHelper.sendSing(group, new At(sender.getId()).plus(group.uploadImage(initImage(pzs.data.pvp))));
                    return;
                }
                SendHelper.sendSing(group, new At(sender.getId()).plus("没有查到哦,稍后再试"));
            }
            SendHelper.sendSing(group, new At(sender.getId()).plus(new PlainText("请输入正确格式：配装？职业？类型(pve/pvp)\nps:配装 分山 pve")));
            TimeUnit.SECONDS.sleep(10);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            SendHelper.sendSing(group, new PlainText("网络繁忙,求助兔兔!!!"));
            SendHelper.sendSing(myself, new PlainText("ChickenSoupURL错误," + e));
        }
    }

    @Data
    private
    class Pzs {
        private Integer code;
        private Pz data;
    }

    @Data
    private
    class Pz {
        private String pve;
        private String pvp;
        static final String e = "pve";
        static final String p = "pvp";
    }

    private static List<NameValuePair> creatParams(String param) {
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("name", param));
        params.add(new BasicNameValuePair("token", "jx3zhenhaowan"));
        return params;
    }

}
