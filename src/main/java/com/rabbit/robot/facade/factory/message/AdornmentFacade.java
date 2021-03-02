package com.rabbit.robot.facade.factory.message;

import com.google.gson.Gson;
import com.rabbit.robot.client.HttpClient;
import com.rabbit.robot.constants.ApiURLConstant;
import com.rabbit.robot.constants.CommonConstant;
import com.rabbit.robot.constants.jx.AdornmentEnum;
import com.rabbit.robot.constants.jx.ResultInfo;
import com.rabbit.robot.constants.jx.ResultInfos;
import com.rabbit.robot.enums.EnumKeyWord;
import com.rabbit.robot.helper.SendHelper;
import com.rabbit.robot.star.RobotStar;
import com.rabbit.robot.utils.MessageUtil;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.Message;
import net.mamoe.mirai.message.data.PlainText;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.concurrent.TimeUnit;

import static com.rabbit.robot.star.RobotStar.myself;
import static com.rabbit.robot.utils.MessageUtil.initImage;

/**
 * @author 邢晨旭
 * @date 2020/11/11
 */
@Component
public class AdornmentFacade implements MessageFacade {

    @Override
    public EnumKeyWord get() {
        return EnumKeyWord.ADORNMENT;
    }

    @Override
    public void execute(Contact sender, Contact group, Message message) {
        String content = message.contentToString();
        if (!this.keyWordVerify(this.get(), content)) {
            return;
        }
        try {

            String result = HttpClient.sendGet(ApiURLConstant.ADORNMENT, HttpClient.creatParamsForName(MessageUtil.getKeybyWord(content, 2)));
            ResultInfos resultInfos = new Gson().fromJson(result, ResultInfos.class);
            if (resultInfos.getCode() == 0) {
                SendHelper.sendSing(group, new At(sender.getId()).plus("没有查到哦。"));
                return;
            }
            Image image = group.uploadImage(initImage(resultInfos.getData().getImagePath()));
            SendHelper.sendSing(group, image.plus(new PlainText(creatResult(resultInfos))));
            TimeUnit.SECONDS.sleep(30);
        } catch (Exception e) {
            SendHelper.sendSing(group, new PlainText("网络繁忙,求助兔兔!!!"));
            SendHelper.sendSing(myself, new PlainText("ChickenSoupURL错误," + e));
        }
    }

    private static String creatResult(ResultInfos resultInfos) {
        ResultInfo data = resultInfos.getData();
        return data.getName() + "\n" +
                "装饰品质：" + data.getQualityLevel() + " " + AdornmentEnum.get(data.getQuality()) + "\n" +
                "装饰来源：" + data.getSource() + "\n" +
                "装饰价格：" + data.getArchitecture() + "\n" +
                "家园所需等级：" + data.getLevelLimit() + "\n" +
                "观赏等级：" + data.getViewScore() + "\n" +
                "实用等级：" + data.getPracticalScore() + "\n" +
                "坚固等级：" + data.getHardScore() + "\n" +
                "风水等级：" + data.getGeomanticScore() + "\n" +
                "趣味等级：" + data.getInterestingScore() + "\n" +
                "装饰介绍：" + data.getTip() + "\n";
    }

    public static void main(String[] args) {
        String result = "{\n" +
                "    \"code\": 1,\n" +
                "    \"data\": { // 数据来源与推栏or魔盒\n" +
                "        \"name\": \"龙门香梦\", // 装饰名称\n" +
                "        \"type\": \"1\",// 装饰类型\n" +
                "        \"quality\": \"4\",// 装饰品质(颜色)\n" +
                "        \"source\": \"园宅会赛\",// 装饰来源\n" +
                "        \"architecture\": \"0\",// 装饰价格\n" +
                "        \"levelLimit\": \"10\",// 家园所需等级\n" +
                "        \"qualityLevel\": \"1000\",// 品质等级\n" +
                "        \"viewScore\": \"7931\",// 观赏等级\n" +
                "        \"practicalScore\": \"3525\",// 实用等级\n" +
                "        \"hardScore\": \"3525\",// 坚固等级\n" +
                "        \"geomanticScore\": \"3525\",// 风水等级\n" +
                "        \"interestingScore\": \"3525\",// 趣味等级\n" +
                "        \"imagePath\": \"https://dl.pvp.xoyo.com/prod/icons/ui/image/homeland/data/source/home/furniture/cloth/cloth5_4_1001.mesh.png\",// 图鉴链接\n" +
                "        \"tip\": \"跳影如流泉，香梦过龙门。山中逍遥客，化为唤雨人。\"// 装饰介绍\n" +
                "    },\n" +
                "    \"time\": 1605502427184\n" +
                "}";
        System.out.println(result);
        ResultInfos resultInfos = new Gson().fromJson(result, ResultInfos.class);
        System.out.println(resultInfos);
        System.out.println(creatResult(resultInfos));
    }

}
