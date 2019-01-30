package com.sibo.fastsport.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.sibo.fastsport.application.Constant;

import java.net.ConnectException;


/**
 * Created by Administrator on 2016/11/21.
 */
public class SharepreferencesUtilSystemSettings {
    public final static String SETTING = Constant.PATH;

    public static void clear(Context context){
        SharedPreferences.Editor sp = context.getSharedPreferences(SETTING,Context.MODE_PRIVATE).edit();
        sp.clear();
        sp.commit();
    }

    public static void putValue(Context context, String key, int value) {
        SharedPreferences.Editor sp = context.getSharedPreferences(SETTING, Context.MODE_PRIVATE)
                .edit();
        sp.putInt(key, value);
        sp.commit();
    }

    public static void putValue(Context context, String key, boolean value) {
        SharedPreferences.Editor sp = context.getSharedPreferences(SETTING, Context.MODE_PRIVATE)
                .edit();
        sp.putBoolean(key, value);
        sp.commit();
    }

    public static void putValue(Context context, String key, String value) {
        SharedPreferences.Editor sp = context.getSharedPreferences(SETTING, Context.MODE_PRIVATE)
                .edit();
        sp.putString(key, value);
        sp.commit();
    }

    public static int getValue(Context context, String key, int defValue) {
        SharedPreferences sp = context.getSharedPreferences(SETTING,
                Context.MODE_PRIVATE);
        int value = sp.getInt(key, defValue);
        return value;
    }

    public static boolean getValue(Context context, String key, boolean defValue) {
        SharedPreferences sp = context.getSharedPreferences(SETTING,
                Context.MODE_PRIVATE);
        boolean value = sp.getBoolean(key, defValue);
        return value;
    }

    public static String getValue(Context context, String key, String defValue) {
        SharedPreferences sp = context.getSharedPreferences(SETTING,
                Context.MODE_PRIVATE);
        String value = sp.getString(key, defValue);
        return value;
    }
    public static void saveFirstTime(Context context,boolean isFirstTime){
        SharedPreferences sp = context.getSharedPreferences("SharePreferences", Context.MODE_PRIVATE);
        sp.edit().putBoolean("share_isFirstTime",isFirstTime).commit();
    }

    public static boolean readFirstTime(Context context) {
        SharedPreferences sp = context.getSharedPreferences("SharePreferences", Context.MODE_PRIVATE);
        return sp.getBoolean("share_isFirstTime", true);
    }
}
