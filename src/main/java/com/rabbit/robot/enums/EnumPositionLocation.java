package com.rabbit.robot.enums;

/**
 * @author 邢晨旭
 * @date 2020/7/22 下午8:28
 * <p>
 * 报名职业枚举
 */
public enum EnumPositionLocation {

    /**
     * 第一位置
     *
     */
    GAI_BANG("丐帮", 11L, "笑尘诀", "https://docs.qq.com/doc/DT1VhQkRTTWRJYVBP"),
    JIAN_CHUN("剑纯", 12L, "太虚剑意", "https://docs.qq.com/doc/DT2R5S3NtVmlQYUh2"),
    CANG_JIAN("藏剑", 13L, "问水诀", "https://docs.qq.com/doc/DT0J1U0tMYmhYZ0p5"),
    AO_XUE("傲血", 14L, "傲血战意", "https://docs.qq.com/doc/DT1pWVnFyUVBxYU9x"),
    FEN_SHAN("分山", 15L, "分山劲", "https://docs.qq.com/doc/DT2J5Z3N1RUF6cEJN"),

    PENG_LAI("蓬莱", 21L, "凌海诀", "https://docs.qq.com/doc/DT21LQ2doTGN0WmZs"),
    LING_XUE("凌雪", 22L, "隐龙诀", "https://docs.qq.com/doc/DT21DRllvWEFxVkFh"),
    JING_YU("惊羽", 23L, "惊羽诀", "https://docs.qq.com/doc/DT0FlQ1FLSVl4Uk5W"),
    BAO_DAO("霸刀", 24L, "北傲诀", "https://docs.qq.com/doc/DT3ZQcVdDRlNQZXJw"),


    QI_CHUN("气纯", 31L, "紫霞功", "https://docs.qq.com/doc/DT3VQZkNhdXF5YXlq"),
    YAN_TIAN("衍天", 32L, "太玄经", "https://docs.qq.com/doc/DT21kaGxoRGhGd253"),
    DU_JING("毒经", 33L, "毒经", "https://docs.qq.com/doc/DT2lRamJKekNwcXFM"),
    FEN_YING("焚影", 34L, "焚影圣诀", "https://docs.qq.com/doc/DT2NyWHJnUFJsR2Zm"),
    BING_XIN("冰心", 35L, "冰心诀", "https://docs.qq.com/doc/DT3JTR3lKbVl2b01k"),

    YI_JIN("易筋", 41L, "易筋经", "https://docs.qq.com/doc/DT2xGc09aSWxoT013"),
    MO_WEN("莫问", 42L, "莫问", "https://docs.qq.com/doc/DT2NUSURURnFGdVpP"),
    HUA_JIAN("花间", 43L, "花间游", "https://docs.qq.com/doc/DT3NQU1p4eUxzRHFa"),
    TIAN_LUO("田螺", 44L, "天罗诡道", "https://docs.qq.com/doc/DT0hjaFh3ZEV3R3FJ"),

    TIE_GU("铁骨", 52L, "铁骨衣", "https://docs.qq.com/doc/DT1FPdGpuYXlFeGZj"),
    MING_ZUN("明尊", 51L, "明尊琉璃体", "https://docs.qq.com/doc/DT0Rrd1NSUVNjUlNJ"),
    XI_SUI("洗髓", 52L, "洗髓经", "https://docs.qq.com/doc/DT1BCS1lwUU9Nb1NB"),
    TIE_LAO("铁牢", 52L, "铁牢律", "https://docs.qq.com/doc/DT2VKb3ZuUkFXWVVh"),

    NAI_GE("奶歌", 45L, "相知", "奶妈都是仙女，仙女不需要宏"),
    NAI_DU("奶毒", 53L, "补天诀", "奶妈都是仙女，仙女不需要宏"),
    NAI_HUA("奶花", 54L, "离经易道", "奶妈都是仙女，仙女不需要宏"),
    NAI_XIU("奶秀", 55L, "云裳心经", "奶妈都是仙女，仙女不需要宏"),


    LAO_BAN("老板", 25L, "老板", "/cast 躺尸"),


    ;

    /**
     * 职业
     */
    public String position;
    /**
     * 位置
     */
    public Long location;

    public String fullName;

    public String macroUrl;

    EnumPositionLocation(String position, Long location, String fullName, String macroUrl) {
        this.position = position;
        this.location = location;
        this.fullName = fullName;
        this.macroUrl = macroUrl;
    }

    public static EnumPositionLocation get(String position) {
        switch (position) {
            case "凌雪":
                return LING_XUE;
            case "蓬莱":
                return PENG_LAI;
            case "霸刀":
                return BAO_DAO;
            case "莫问":
                return MO_WEN;
            case "奶歌":
            case "相知":
                return NAI_GE;
            case "分山":
            case "苍云":
                return FEN_SHAN;
            case "铁骨":
            case "苍云t":
            case "苍云T":
                return TIE_GU;
            case "丐帮":
                return GAI_BANG;
            case "焚影":
                return FEN_YING;
            case "明教T":
            case "明教t":
            case "明尊":
                return MING_ZUN;
            case "田螺":
            case "天罗":
                return TIAN_LUO;
            case "鲸鱼":
            case "惊羽":
                return JING_YU;
            case "毒经":
                return DU_JING;
            case "奶毒":
                return NAI_DU;
            case "藏剑":
                return CANG_JIAN;
            case "傲雪":
            case "傲血":
                return AO_XUE;
            case "铁牢":
            case "天策t":
            case "天策T":
                return TIE_LAO;
            case "剑纯":
                return JIAN_CHUN;
            case "气纯":
                return QI_CHUN;
            case "冰心":
                return BING_XIN;
            case "奶秀":
                return NAI_XIU;
            case "花间":
                return HUA_JIAN;
            case "奶花":
                return NAI_HUA;
            case "易筋":
            case "少林":
            case "和尚":
                return YI_JIN;
            case "洗髓":
            case "和尚t":
            case "和尚T":
                return XI_SUI;
            case "衍天宗":
            case "衍天":
            case "衍员":
            case "演员":
                return YAN_TIAN;
            case "老板":
                return LAO_BAN;
            default:
                return null;
        }
    }
}
