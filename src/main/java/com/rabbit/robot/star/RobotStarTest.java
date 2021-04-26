package com.rabbit.robot.star;

import com.rabbit.robot.constants.CommonConstant;
import com.rabbit.robot.listener.FriendListener;
import com.rabbit.robot.listener.GroupListener;
import com.rabbit.robot.listener.GroupRequestListener;
import com.rabbit.robot.listener.LoginListener;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.BotFactory;
import net.mamoe.mirai.contact.Friend;
import net.mamoe.mirai.utils.BotConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


/**
 * @author 邢晨旭
 * @date 2020/6/28 上午11:48
 */
@Component
public class RobotStarTest {

    public static Bot bot = null;
    public static Friend myself = null;

    private static Long QQ = 111L;
    private static String PASSWORD = "password";

    @Value("qq")
    public void setQQ(Long qq){
        QQ = qq;
    }
    @Value("password")
    public void setPassword(String password){
        PASSWORD = password;
    }

    static {
//        // 机器人

        bot = BotFactory.INSTANCE.newBot(QQ, PASSWORD, new BotConfiguration() {
            {

                //保存设备信息到文件
                fileBasedDeviceInfo("test.json");
                setProtocol(MiraiProtocol.ANDROID_PAD);
            }
        });
        bot.login();
        myself = bot.getFriend(CommonConstant.ERROR_SEND_QQ);
    }

    public static void star(FriendListener friendListener, GroupListener groupListener, GroupRequestListener groupRequestListener, LoginListener loginListener) {

        /**
         * 事件监听器注册
         */
        bot.getEventChannel().registerListenerHost(friendListener);
        bot.getEventChannel().registerListenerHost(groupListener);
        bot.getEventChannel().registerListenerHost(groupRequestListener);
        bot.getEventChannel().registerListenerHost(loginListener);

//        Events.registerEvents(bot, friendListener);
//        Events.registerEvents(bot, groupListener);
//        Events.registerEvents(bot, groupRequestListener);
        /**
         * 挂载该机器人的协程
         */
        bot.join();
    }

    public static void main(String[] args) {
        System.out.println(111);
    }
}
