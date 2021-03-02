package com.rabbit.robot.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author 邢晨旭
 * @date 2021/02/26
 */
public class DateUtil {

    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final String DATE_FORMAT2 = "yyyy-MM-dd-HH-mm-ss";
    private static final String DATE_FORMAT3 = "yyyyMMdd";
    private static final SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
    private static final SimpleDateFormat sdf2 = new SimpleDateFormat(DATE_FORMAT2);
    private static final SimpleDateFormat sdf3= new SimpleDateFormat(DATE_FORMAT3);

    /**
     * 时间戳转时间
     *
     * @param time
     * @return
     */
    public static String stampToDate(int time) {
        return sdf.format(new Date(time * 1000L));
    }

    /**
     * 获取当前时间
     * @return
     */
    public static String getDate() {
        return sdf.format(new Date());
    }

    /**
     * 获取当前时间
     * @return
     */
    public static String getDate2() {
        return sdf2.format(new Date());
    }
    public static String getDay() {
        return sdf3.format(new Date());
    }

    /**
     * 将时间转换为时间戳
     * @param time
     * @return
     */
    public static String dateToStamp(String time) {
        String stamp = "";
        if (!"".equals(time)) {//时间不为空
            try {
                stamp = String.valueOf(sdf.parse(time).getTime() / 1000);
            } catch (Exception e) {
                System.out.println("参数为空！");
            }
        } else {    //时间为空
            long current_time = System.currentTimeMillis();  //获取当前时间
            stamp = String.valueOf(current_time / 1000);
        }
        return stamp;
    }
}
