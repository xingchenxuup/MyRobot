package com.rabbit.robot.facade.factory.message;

import com.google.gson.Gson;
import com.rabbit.robot.client.HttpClient;
import com.rabbit.robot.constants.CommonConstant;
import com.rabbit.robot.enums.EnumKeyWord;
import com.rabbit.robot.helper.SendHelper;
import com.rabbit.robot.star.RobotStar;
import com.rabbit.robot.utils.MessageUtil;
import lombok.Data;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.contact.Member;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.Message;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.PlainText;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

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
public class HuaJiaFacade implements MessageFacade {

    @Override
    public EnumKeyWord get() {
        return EnumKeyWord.HUA_JIA;
    }

    @Override
    public void execute(Contact sender, Contact group, Message message) {
        String content = message.contentToString();
        if (!this.keyWordVerify(this.get(), content)) {
            return;
        }
        try {
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("token", "jx3zhenhaowan"));
            params.add(new BasicNameValuePair("server", "剑胆琴心"));
            params.add(new BasicNameValuePair("flower", getName(MessageUtil.getKeybyWord(content, 2))));
            String result = HttpClient.sendGet("https://jx3api.com/next/flower.php", params);
            MessageChain plus = new At(sender.getId()).plus(new PlainText(getHuaJia(result)));
            SendHelper.sendSing(group, plus);
            TimeUnit.SECONDS.sleep(30);
        } catch (Exception e) {
            SendHelper.sendSing(group, new PlainText("网络繁忙,求助兔兔!!!"));
            SendHelper.sendSing(myself, new PlainText("ChickenSoupURL错误," + e));
        }
    }

    private String getName(String name) {
        if (name.contains("玫瑰")) {
            return "玫瑰";
        }
        if (name.contains("荧光")) {
            return "荧光菌";
        }
        if (name.contains("绣球")) {
            return "绣球花";
        }
        if (name.contains("葫芦")) {
            return "葫芦";
        }
        if (name.contains("麦")) {
            return "麦";
        }
        if (name.contains("青菜")) {
            return "青菜";
        }
        if (name.contains("百合")) {
            return "百合";
        }
        if (name.contains("芜菁")) {
            return "芜菁";
        }
        if (name.contains("牵牛")) {
            return "牵牛花";
        }
        if (name.contains("郁金")) {
            return "郁金香";
        }
        if (name.contains("豆花") || name.contains("羽扇") || name.contains("扇豆")) {
            return "羽扇豆花";
        }
        return "绣球花";
    }

    private static String getHuaJia(String result) {
        StringBuffer s1 = new StringBuffer();
        花价查询 花价 = new Gson().fromJson(result, 花价查询.class);
        if (花价.code < 0) {
            return "没有查到哦,请稍后再试";
        }
        if (CollectionUtils.isEmpty(花价.data.get("广陵邑"))) {

        } else {
            s1.append("\n广陵邑：\n");
            花价.data.get("广陵邑").forEach(花 -> {
                s1.append(花.getName()).append(StringUtils.isEmpty(花.color) ? "" : "(" + 花.color + ")\n\t");
                花.getLine().forEach(line -> {
                    s1.append(line).append("、");
                });
                s1.deleteCharAt(s1.length() - 1);
                s1.append("线\n");
                s1.append("\t价格：").append(花.price).append("\n");
            });
        }
        if (CollectionUtils.isEmpty(花价.data.get("枫叶泊·乐苑"))) {
            return s1.toString();
        }
        s1.append("枫叶泊·天苑\n");
        花价.data.get("枫叶泊·天苑").forEach(花 -> {
            s1.append(花.getName()).append(StringUtils.isEmpty(花.color) ? "" : "(" + 花.color + ")\n\t");
            花.getLine().forEach(line -> {
                s1.append(line).append("、");
            });
            s1.deleteCharAt(s1.length() - 1);
            s1.append("线\n");
            s1.append("\t价格：").append(花.price).append("\n");
        });
        s1.append("枫叶泊·乐苑\n");
        花价.data.get("枫叶泊·乐苑").forEach(花 -> {
            s1.append(花.getName()).append(StringUtils.isEmpty(花.color) ? "" : "(" + 花.color + ")\n\t");
            花.getLine().forEach(line -> {
                s1.append(line).append("、");
            });
            s1.deleteCharAt(s1.length() - 1);
            s1.append("线\n");
            s1.append("\t价格：").append(花.price).append("\n");
        });
        System.out.println("花价消息长度" + s1.length());
        return s1.toString();
    }

    @Data
    class 花价查询 {
        private Integer code;
        private Map<String, List<花>> data;
    }

    @Data
    class 花 {
        private String name;
        private String color;
        private String price;
        private List<String> line;
    }

}
