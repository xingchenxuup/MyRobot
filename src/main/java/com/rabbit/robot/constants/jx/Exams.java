package com.rabbit.robot.constants.jx;

import lombok.Data;

import java.util.List;

/**
 * @author 邢晨旭
 * @date 2020/12/22
 */
@Data
public class Exams {
    private List<Exam> data;
    private Integer code = 1;
    private Integer errcode = 0;
}
