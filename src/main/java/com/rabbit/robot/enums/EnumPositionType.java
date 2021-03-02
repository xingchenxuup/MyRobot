package com.rabbit.robot.enums;

import com.rabbit.robot.constants.jx.JxNum;

/**
 * @author 邢晨旭
 * @date 2020/11/16
 */
public enum EnumPositionType {

    /**
     *
     */
    W_DPS(0, JxNum.W_DPS),
    N_DPS(1, JxNum.N_DPS),
    HPS(2, JxNum.HPS),
    MT(3, JxNum.MT),
    LAO_BAN(4, JxNum.LAO_BAN),
    ;
    /**
     * 类型
     * 1\群聊 2\私聊
     */
    public Integer type;
    /**
     * 关键码
     */
    public Integer amount;

    EnumPositionType(Integer type, Integer amount) {
        this.type = type;
        this.amount = amount;
    }

    public static EnumPositionType get(Integer type) {
        for (EnumPositionType value : EnumPositionType.values()) {
            if (value.type.equals(type)) {
                return value;
            }
        }
        return null;
    }

    public static Integer getNum(EnumPositionType type) {
                switch (type) {
                    case W_DPS:
                        return JxNum.W_DPS;
                    case N_DPS:
                        return JxNum.N_DPS;
                    case HPS:
                        return JxNum.HPS;
                    case MT:
                        return JxNum.MT;
                    case LAO_BAN:
                        return JxNum.LAO_BAN;
                    default:
                }
        return 0;
    }

}
