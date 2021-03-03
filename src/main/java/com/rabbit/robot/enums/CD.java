package com.rabbit.robot.enums;

import com.rabbit.robot.constants.jx.JxNum;

/**
 * @author 邢晨旭
 * @date 2021/03/03
 */
public enum CD {

    /**
     *
     */
    TEN(10000, "功能CD10秒"),
    TWENTY(20000, "功能CD20秒"),
    THIRTY(30000, "功能CD30秒"),
    FORTY(40000, "功能CD40秒"),
    FIFTY(50000, "功能CD50秒"),
    SIXTY(60000, "功能CD60秒"),
    SEVENTY(70000,"功能CD70秒"),
    EIGHTY(80000, "功能CD80秒"),
    NINETY(90000, "功能CD90秒"),
    ;
    /**
     * 类型
     * 1\群聊 2\私聊
     */
    public Integer cd;
    /**
     * 关键码
     */
    public String msg;

    CD(Integer cd, String msg) {
        this.cd = cd;
        this.msg = msg;
    }

}
