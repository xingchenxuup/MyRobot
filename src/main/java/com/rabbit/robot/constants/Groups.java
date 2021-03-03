package com.rabbit.robot.constants;

import com.rabbit.robot.star.RobotStar;
import net.mamoe.mirai.contact.Group;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 群组名单
 *
 * @author 邢晨旭
 * @date 2020/12/14
 */
public class Groups {
    /**
     * 机器人过滤的群组名单,默认全部过滤
     */
    public static Map<Long, Boolean> groups = RobotStar.bot.getGroups().stream().collect(Collectors.toMap(Group::getId, group -> true, (k1, k2) -> true));
    public static Set<Long> teamGroup = new HashSet<>();

}
