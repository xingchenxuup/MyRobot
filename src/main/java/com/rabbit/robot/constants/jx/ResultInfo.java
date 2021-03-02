package com.rabbit.robot.constants.jx;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 邢晨旭
 * @date 2020/11/30
 */
@NoArgsConstructor
@Data
public class ResultInfo {
    private String text;
    private String server;
    private String region;
    private Integer status;

    private String produceMap;
    /**
     * 来源
     */
    private String source;
    private String name;
    /**
     * 品质等级
     */
    private String qualityLevel;
    /**
     * 颜色
     */
    private String quality;
    /**
     * 价格
     */
    private String architecture;
    /**
     * 观赏
     */
    private String viewScore;
    /**
     * 实用
     */
    private String practicalScore;

    /**
     * 坚固
     */
    private String hardScore;
    /**
     * 风水
     */
    private String geomanticScore;
    /**
     * 趣味
     */
    private String interestingScore;
    private String imagePath;
    /**
     * 家园所需等级
     */
    private String levelLimit;
    private String tip;

    public ResultInfo(String server, String region, Integer status) {
        this.server = server;
        this.region = region;
        this.status = status;
    }
}