package com.itep.mt.common.util;

import android.app.AlarmManager;
import android.content.Context;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by xianghui on 2019/11/5.
 */

public class DateUtil {
    public static final String DATE_FORMAT="yyyy-MM-dd HH:mm:ss";
    public static final String DATE_FORMAT2="yyyyMMddHHmmss";
    public static final String DATE_FORMAT3="MMddHHmmyyyy.ss";

    /**
     * 获取指定格式当前时间的字符串
     *
     * @param format
     * @return
     */
    public static String getCurrentTimeStrByFormat(String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        Date tDate = new Date();
        String dateString = formatter.format(tDate);
        return dateString;
    }

    public static String getTimeFormat(String time, String format1, String format2){
        SimpleDateFormat formatter1 = new SimpleDateFormat(format1);
        SimpleDateFormat formatter2 = new SimpleDateFormat(format2);
        Date tDate = null;
        try {
            tDate = formatter1.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String dateString = formatter2.format(tDate);
        return dateString;
    }

    public static String getTimeFormat(String time, String format1,Locale locale1, String format2){
        SimpleDateFormat formatter1 = new SimpleDateFormat(format1, locale1);
        SimpleDateFormat formatter2 = new SimpleDateFormat(format2);
        Date tDate = null;
        try {
            tDate = formatter1.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String dateString = formatter2.format(tDate);
        return dateString;
    }

    /**
     * 获取默认格式当前时间的字符串
     *
     * @return
     */
    public static String getCurrentTimeStrByFormat() {
        return getCurrentTimeStrByFormat(DATE_FORMAT);
    }

    public void setAndroidSystemTime(Context mContext, String time) {
//        String time = "2019-01-17 17:30:54";
        SimpleDateFormat simpleDateFormat = null;
        if (simpleDateFormat == null) {
            simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        }
        Date date = null;
        try {
            date = simpleDateFormat.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        long when = calendar.getTimeInMillis();
        ((AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE)).setTime(when);
    }

    //    设置系统时间
    static void setTime(Context context, int hourOfDay, int minute) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        long when = c.getTimeInMillis();
        if (when / 1000 < Integer.MAX_VALUE) {
            ((AlarmManager) context.getSystemService(Context.ALARM_SERVICE)).setTime(when);
        }
    }

    //    设置系统日期
    static void setDate(Context context, int year, int month, int day) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, day);
        long when = c.getTimeInMillis();
        if (when / 1000 < Integer.MAX_VALUE) {
            ((AlarmManager) context.getSystemService(Context.ALARM_SERVICE)).setTime(when);
        }
    }


    /**
     * 获取指定格式指定时间的字符串
     *
     * @param dateStr
     * @param format
     * @return
     */
    public static Date getDateTimeByFormat(String dateStr, String format) {
        Date date = null;
        if (null == dateStr) {
            return date;
        }
        SimpleDateFormat sFormat = new SimpleDateFormat(format);
        try {
            date = sFormat.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

}
