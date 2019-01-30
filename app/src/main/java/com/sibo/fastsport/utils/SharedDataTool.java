package com.sibo.fastsport.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class SharedDataTool {

	private final static String SHARED_NAME = "MaintainB";
	public final static String UNKNOWN_STRING = "unknown";
	public final static String APP_DOWN_ID = "APP_DOWN_ID";// 下载安装包id

	public static void setString(Context context, String key, String value) {
		// 实例化SharedPreferences对象（第一步）
		if (context == null) {
			LogUtils.e("context is null");
			return;
		}
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				SHARED_NAME, Activity.MODE_PRIVATE);
		// 实例化SharedPreferences.Editor对象（第二步）
		SharedPreferences.Editor editor = sharedPreferences.edit();
		// 用putString的方法保存数据
		editor.putString(key, value);
		// 提交当前数据
		editor.commit();
	}

	public static void setBoolean(Context context, String key, boolean value) {
		if (context == null) {
			LogUtils.e("context is null");
			return;
		}
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				SHARED_NAME, Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}

	public static boolean getBoolean(Context context, String key) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				SHARED_NAME, Activity.MODE_PRIVATE);
		boolean IsTrue = sharedPreferences.getBoolean(key, false);
		return IsTrue;
	}

	public static void setLong(Context context, String key, long value) {
		if (context == null) {
			LogUtils.e("context is null");
			return;
		}
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				SHARED_NAME, Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putLong(key, value);
		editor.commit();
	}

	public static long getLong(Context context, String key) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				SHARED_NAME, Activity.MODE_PRIVATE);
		long value = sharedPreferences.getLong(key, 0);
		return value;
	}

	public static String getString(Context context, String key) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				SHARED_NAME, Activity.MODE_PRIVATE);
		String data = sharedPreferences.getString(key, null);
		return data;

	}

}
