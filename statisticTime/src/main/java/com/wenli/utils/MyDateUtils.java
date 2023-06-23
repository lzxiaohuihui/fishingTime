package com.wenli.utils;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @description:
 * @date: 2023-06-08 8:51 a.m.
 * @author: lzh
 */
public class MyDateUtils {


    public static Date getDateFromDay(String oneDay){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = dateFormat.parse(oneDay);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return date;

    }
    public static DateTime getDateTimeFromDay(String oneDay){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = dateFormat.parse(oneDay);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        return DateUtil.parseDateTime(DateUtil.formatDateTime(date));
    }

    public static Date[] getDays(String startDay, String endDay){
        Date date1 = getDateFromDay(startDay);
        Date date2 = getDateFromDay(endDay);
        int between = (int) DateUtil.between(date1, date2, DateUnit.DAY);
        Date[] days = new Date[between+1];
        for (int i = 0; i <= between; i++){
            days[i] = DateUtil.offsetDay(date1, i);
        }
        return days;
    }

}
