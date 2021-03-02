package com.rabbit.robot.constants;

import net.mamoe.mirai.message.data.Face;
import net.mamoe.mirai.message.data.MessageChain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author 邢晨旭
 * @date 2020/12/23
 */
public class RobotMenu {
    private static List<String> menu = Arrays.asList(
            "日常查询（日常）",
            "小药查询（小药 心法）",
            "配装查询（配装 心法 pve/p）",
            "宏查询（宏 心法）",
            "装饰查询（装饰 装饰品）",
            "花价查询（花价 植物名）",
            "科举答题（科举/kj 题目）",
            "开服监控（开服 服务器）"
    );
    private static List<String> teamMenu = Arrays.asList(
            "团队报名（报名 心法 ID）",
            "取消报名（取消报名）",
            "查看团队（查看团队）"
    );
    private static List<String> teamMenuAdmin = Arrays.asList(
            "手动插队（插队 心法 ID 位置）",
            "取消插队（取消插队 位置）",
            "开组口令（开组了报名进组）",
            "创建团队（开团 团名）",
            "取消团队（取消开团）",
            "修改团名（修改团名 团名）"
    );

    private static List<MessageChain> menuResult = getMenu();


    public static List<MessageChain> getMenu() {
        if (menuResult == null) {
            List<MessageChain> list = new ArrayList<>();
            Face face = new Face(190);
            menu.forEach(s -> {
                list.add(face.plus(s + "\n"));
            });
            list.add(new Face(199).plus("开团工具").plus(new Face(199)));
            teamMenu.forEach(s -> {
                list.add(face.plus(s + "\n"));
            });
            list.add(new Face(194).plus("管理口令").plus(new Face(194)));
            teamMenuAdmin.forEach(s -> {
                list.add(face.plus(s + "\n"));
            });
            return list;
        }
        return menuResult;
    }

}
