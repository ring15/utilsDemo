package com.ring.utilsdemo.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by ring on 2019/11/21.
 */
public class DateUtil {
    /**
     * 获取系统时间戳
     *
     * @return
     */
    public long getCurTimeLong() {
        long time = System.currentTimeMillis();
        return time;
    }

    /**
     * 获取当前时间
     *
     * @param pattern
     * @return
     */
    public static String getCurDate(String pattern) {
        SimpleDateFormat sDateFormat = new SimpleDateFormat(pattern, Locale.CHINA);
        return sDateFormat.format(new Date());
    }

    /**
     * 时间戳转换成字符窜
     *
     * @param milSecond
     * @param pattern
     * @return
     */
    public static String getDateToString(long milSecond, String pattern) {
        Date date = new Date(milSecond);
        SimpleDateFormat format = new SimpleDateFormat(pattern, Locale.CHINA);
        return format.format(date);
    }

    /**
     * 将字符串转为时间戳
     *
     * @param dateString
     * @param pattern
     * @return
     */
    public static long getStringToDate(String dateString, String pattern) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern, Locale.CHINA);
        Date date = new Date();
        try {
            date = dateFormat.parse(dateString);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return date.getTime();
    }

    /**
     * @param time 要转换的毫秒数
     * @return 该毫秒数转换为 * days * hours * minutes * seconds 后的格式
     * @author fy.zhang
     */
    public static String formatDuring(long time) {
        int days = (int) (time / (1000 * 60 * 60 * 24));
        int hours = (int) ((time % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
        int minutes = (int) ((time % (1000 * 60 * 60)) / (1000 * 60));
        int seconds = (int) ((time % (1000 * 60)) / 1000);
        String formatTime;
        if (days != 0) {
            formatTime = days + " 天 " + hours + " 时 " + minutes + " 分钟" + seconds + " 秒 ";
        } else {
            if (hours != 0) {
                formatTime = hours + " 时 " + minutes + " 分 " + seconds + " 秒 ";
            } else if (minutes != 0) {
                formatTime = minutes + " 分 " + seconds + " 秒 ";
            } else {
                formatTime = seconds + " 秒 ";
            }
        }
        return formatTime;
    }
}
