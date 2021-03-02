package com.rabbit.robot.listener;

import com.rabbit.robot.constants.CommonConstant;
import com.rabbit.robot.constants.jx.JxNum;
import com.rabbit.robot.facade.factory.MessageFactory;
import com.rabbit.robot.utils.MessageUtil;
import kotlin.coroutines.CoroutineContext;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.FriendMessageEvent;
import net.mamoe.mirai.message.data.Message;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.PlainText;
import net.mamoe.mirai.message.data.Voice;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.rabbit.robot.star.RobotStar.myself;

/**
 * @author 邢晨旭
 * @date 2020/6/28 下午2:22
 */
@Service
public class FriendListener extends SimpleListenerHost {

    @Autowired
    private MessageFactory messageFactory;

    /**
     * Java方法级别注解,标注一个方法为事件监听器
     *
     * @param event
     */
    @EventHandler
    public void onMessage(FriendMessageEvent event) throws InterruptedException {

        MessageChain messageChain = event.getMessage();
        Message plainText = messageChain.get(PlainText.Key);
        if (plainText != null && event.getSender().getId() == CommonConstant.ERROR_SEND_QQ) {
            if (plainText.contentToString().startsWith("职业人数 ")) {
                updateNum(plainText);
            }
        }
        Voice voice = messageChain.get(Voice.Key);
        if (voice != null) {
            myself.sendMessage(voice);
        }
    }

    @Override
    public void handleException(@NotNull CoroutineContext context, @NotNull Throwable exception) {
        /**
         * 异常处理方式
         * 先直接打印堆栈吧～
         */
        myself.sendMessage("私聊消息处理错误!" + exception.getMessage());
    }

    private void updateNum(Message plainText) {
        String name = MessageUtil.getKeybyWord(plainText.contentToString(), 2);
        String num = MessageUtil.getKeybyWord(plainText.contentToString(), 3);
        Integer newNum = Integer.parseInt(num);
        if (newNum > 5) {
            myself.sendMessage("修改失败!\n职业：" + name + "-->" + newNum);
            return;
        }
        switch (name) {
            case "凌雪":
                JxNum.LING_XUE = newNum;
                break;
            case "蓬莱":
                JxNum.PENG_LAI = newNum;
                break;
            case "霸刀":
                JxNum.BA_DAO = newNum;
                break;
            case "莫问":
                JxNum.MO_WEN = newNum;
                break;
            case "奶歌":
            case "相知":
                JxNum.NAI_GE = newNum;
                break;
            case "分山":
            case "苍云":
                JxNum.FEN_SHAN = newNum;
                break;
            case "铁骨":
            case "苍云t":
            case "苍云T":
                JxNum.TIE_GU = newNum;
                break;
            case "丐帮":
                JxNum.GAI_BANG = newNum;
                break;
            case "焚影":
                JxNum.FEN_YING = newNum;
                break;
            case "明教T":
            case "明教t":
            case "明尊":
                JxNum.MING_ZUN = newNum;
                break;
            case "田螺":
            case "天罗":
                JxNum.TIAN_LUO = newNum;
                break;
            case "鲸鱼":
            case "惊羽":
                JxNum.JING_YU = newNum;
                break;
            case "毒经":
                JxNum.DU_JING = newNum;
                break;
            case "奶毒":
                JxNum.NAI_DU = newNum;
                break;
            case "藏剑":
                JxNum.CANG_JIAN = newNum;
                break;
            case "傲雪":
            case "傲血":
                JxNum.AO_XUE = newNum;
                break;
            case "铁牢":
            case "天策t":
            case "天策T":
                JxNum.TIE_LAO = newNum;
                break;
            case "剑纯":
                JxNum.JIAN_CHUN = newNum;
                break;
            case "气纯":
                JxNum.QI_CHUN = newNum;
                break;
            case "冰心":
                JxNum.BING_XIN = newNum;
                break;
            case "奶秀":
                JxNum.NAI_XIU = newNum;
                break;
            case "花间":
                JxNum.HUA_JIAN = newNum;
                break;
            case "奶花":
                JxNum.NAI_HUA = newNum;
                break;
            case "易筋":
            case "少林":
            case "和尚":
                JxNum.YI_JIN = newNum;
                break;
            case "洗髓":
            case "和尚t":
            case "和尚T":
                JxNum.XI_SUI = newNum;
                break;
            case "衍天宗":
            case "衍天":
            case "衍员":
            case "演员":
                JxNum.YAN_TIAN = newNum;
                break;
            case "老板":
                JxNum.LAO_BAN = newNum;
                break;
            case "外功":
                JxNum.W_DPS = newNum;
                break;
            case "内功":
                JxNum.N_DPS = newNum;
                break;
            case "奶妈":
                JxNum.HPS = newNum;
                break;
            case "T":
            case "MT":
            case "t":
                JxNum.MT = newNum;
                break;
            default:
        }
        myself.sendMessage("修改完毕!\n职业：" + name + "-->" + newNum);
    }

}
