package com.sibo.fastsport.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Huangjinfu on 2016/8/6.
 */
public class ToastUtils {
    /**
     * 短时间吐司通知
     *
     * @param activity
     * @param content
     */
    public static void shortToast(Context activity, String content) {
        Toast.makeText(activity, content, Toast.LENGTH_SHORT).show();
    }

    /**
     * 长时间吐司通知
     *
     * @param activity
     * @param content
     */
    public static void longToast(Context activity, String content) {
        Toast.makeText(activity, content, Toast.LENGTH_LONG).show();
    }
}
