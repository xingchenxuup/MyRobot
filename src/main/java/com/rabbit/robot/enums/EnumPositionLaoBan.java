package com.rabbit.robot.enums;

/**
 * @author 邢晨旭
 * @date 2020/7/22 下午8:28
 * <p>
 * 报名职业枚举
 */
public enum EnumPositionLaoBan {

    /**
     *
     */
    QIXIU("七秀"),
    WANHUA("万花"),
    WUDU("五毒"),
    CHANGGE("长歌"),
    TIANCE("天策"),
    HESHANG("和尚"),
    MINGJIAO("明教"),
    CANGYUN("苍云"),
    CHUN_YANG("纯阳"),
    TANGMEN("唐门"),
    CANGJIAN("藏剑"),
    GAIBANG("丐帮"),
    BADAO("霸刀"),
    PENGLAI("蓬莱"),
    LINGXUE("凌雪"),
    YANTIAN("衍天"),


    ;

    /**
     * 职业
     */
    public String position;

    /**
     * 位置
     */


    EnumPositionLaoBan(String position) {
        this.position = position;
    }

    public static EnumPositionLaoBan get(String position) {
        switch (position) {
            case "凌雪":
                return LINGXUE;
            case "蓬莱":
                return PENGLAI;
            case "霸刀":
                return BADAO;
            case "莫问":
            case "奶歌":
            case "相知":
            case "长歌":
                return CHANGGE;
            case "分山":
            case "苍云":
            case "铁骨":
            case "苍云t":
            case "苍云T":
                return CANGYUN;
            case "丐帮":
                return GAIBANG;
            case "焚影":
            case "明教T":
            case "明教t":
            case "明尊":
            case "明教":
                return MINGJIAO;
            case "田螺":
            case "天罗":
            case "鲸鱼":
            case "惊羽":
            case "唐门":
                return TANGMEN;
            case "毒经":
            case "奶毒":
            case "五毒":
                return WUDU;
            case "藏剑":
                return CANGJIAN;
            case "傲雪":
            case "傲血":
            case "铁牢":
            case "天策t":
            case "天策T":
            case "天策":
                return TIANCE;
            case "剑纯":
            case "气纯":
            case "纯阳":
                return CHUN_YANG;
            case "冰心":
            case "奶秀":
            case "七秀":
                return QIXIU;
            case "花间":
            case "奶花":
            case "万花":
                return WANHUA;
            case "易筋":
            case "少林":
            case "和尚":
            case "洗髓":
            case "和尚t":
            case "和尚T":
                return HESHANG;
            case "衍天宗":
            case "衍天":
            case "衍员":
            case "演员":
                return YANTIAN;
            default:
                return null;
        }
    }
}
