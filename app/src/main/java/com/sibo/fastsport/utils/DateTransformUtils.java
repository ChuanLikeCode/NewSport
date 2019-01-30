package com.sibo.fastsport.utils;

import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by yyuand on 2016.11.29.
 */

public class DateTransformUtils {
    // 长日期格式
    public static String TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static String transfromDate(String date){
        long tim = Long.parseLong(date);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(tim);
        DateFormat sdf = new SimpleDateFormat(TIME_FORMAT);
//        SimpleDateFormat sdf = new SimpleDateFormat(TIME_FORMAT);
//        Date date1 = new Date(tim);
        Log.e("(calendar.getTime())", sdf.format(calendar.getTime()));
        return sdf.format(calendar.getTime());
    }
}
