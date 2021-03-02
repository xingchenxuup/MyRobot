package com.rabbit.robot.constants.jx;

/**
 * @author 邢晨旭
 * @date 2020/12/14
 */
public enum AdornmentEnum {

    /**
     *
     */
    Level1("1","白色"),
    Level2("2","绿色"),
    Level3("3","蓝色"),
    Level4("4","紫色"),


    ;
    private String level;
    private String color;

    AdornmentEnum(String level, String color) {
        this.level = level;
        this.color = color;
    }
    public static String get(String level) {
        for (AdornmentEnum value : AdornmentEnum.values()) {
            if (value.level.equals(level)) {
                return value.color;
            }
        }
        return null;
    }
}
