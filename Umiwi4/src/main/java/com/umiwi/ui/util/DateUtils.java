package com.umiwi.ui.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by gjj on 2016/8/10.
 * 根据日期获取星期
 */
public class DateUtils {
    private static SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd");
    private static SimpleDateFormat format = new SimpleDateFormat("HH:mm");
    private static SimpleDateFormat fMat = new SimpleDateFormat("HH:mm:ss");
    private static SimpleDateFormat mat = new SimpleDateFormat("MM-dd");
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private static SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static SimpleDateFormat sdf1 = new SimpleDateFormat("mm:ss");

    /**
     * 根据日期获得星期
     *
     * @param date
     * @return
     */
    public static String getWeekOfDate(Date date) {
        String[] weekDaysName = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        String[] weekDaysCode = {"0", "1", "2", "3", "4", "5", "6"};
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int intWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        return weekDaysName[intWeek];
    }

    /**
     * 格式日期
     *
     * @param date
     * @return
     */
    public static String formatDate(Date date) {
        return mFormat.format(date);
    }

    public static String formatmmss(long time){
       return sdf1.format(new Date(time));
    }
    /**
     * 获取HH:mm:ss
     *
     * @param time
     * @return
     */
    public static String formatHHmmss(long time) {
        return fMat.format(new Date(time));
    }

    /**
     * 获取 MM-dd
     *
     * @param time
     * @return
     */
    public static String mmDD(long time) {
        return mat.format(new Date(time));
    }

    /**
     * 将一个long类型转成yyyy-MM-dd
     *
     * @param dateLong
     * @return
     */
    public static String formatDate(long dateLong) {
        return mFormat.format(new Date(dateLong));
    }


    /**
     * 格式化日期 yyyy-MM-dd HH:mm:ss
     *
     * @param dateLong
     * @return
     */
    public static String yMdHms(long dateLong) {
        return sdf2.format(new Date(dateLong));
    }

    /**
     * 将一个long类型转成HH:mm
     *
     * @param dateLong
     * @return
     */
    public static String hhMM(long dateLong) {
        return format.format(new Date(dateLong));
    }

    public static long date2Long(String date) {
        try {
            return mFormat.parse(date).getTime();
        } catch (ParseException e) {
        }
        return 0;
    }

    /**
     * 格式日期 2016-12-12 10：08
     *
     * @param date
     * @return
     */
    public static String yMdHm(long date) {
        return sdf.format(new Date(date));
    }
}
