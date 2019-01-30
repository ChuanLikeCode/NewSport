/*
 * Copyright (c) 2013. wyouflf (wyouflf@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sibo.fastsport.utils;


import android.text.TextUtils;
import android.util.Log;

import com.sibo.fastsport.application.Constant;


/**
 * Log工具，类似android.util.Log。
 * tag自动产生，格式: customTagPrefix:className.methodName(L:lineNumber),
 * customTagPrefix为空时只输出：className.methodName(L:lineNumber)。
 * <p/>
 */
public class LogUtils {

    public static String customTagPrefix = "";
    public static String TAG = "lovebaby";
    public static CustomLogger customLogger;
    private static int LOG_MAXLENGTH = 2000;

    private LogUtils() {
    }

    private static String generateTag(StackTraceElement caller) {
        String tag = "%s.%s(L:%d)";
        String callerClazzName = caller.getClassName();
        callerClazzName = callerClazzName.substring(callerClazzName.lastIndexOf(".") + 1);
        tag = String.format(tag, callerClazzName, caller.getMethodName(), caller.getLineNumber());
        tag = TextUtils.isEmpty(customTagPrefix) ? tag : customTagPrefix + ":" + tag;
        return tag;
    }

    public static void d(String content) {
        if (!Constant.Debug) return;
        StackTraceElement caller = CommonTools.getCallerStackTraceElement();
        String tag = generateTag(caller);

        if (customLogger != null) {
            customLogger.d(TAG, tag + ":" + content);
        } else {
            Log.d(TAG, tag + ":" + content);
        }
    }

    public static void d(String content, Throwable tr) {
        if (!Constant.Debug) return;
        StackTraceElement caller = CommonTools.getCallerStackTraceElement();
        String tag = generateTag(caller);

        if (customLogger != null) {
            customLogger.d(tag, content, tr);
        } else {
            Log.d(tag, content, tr);
        }
    }

    public static void e(String content) {
        if (!Constant.Debug) return;
        StackTraceElement caller = CommonTools.getCallerStackTraceElement();
        String tag = generateTag(caller);

        if (customLogger != null) {
            customLogger.e(TAG, tag + ":" + content);
        } else {
            Log.e(TAG, tag + ":" + content);
        }
    }

    public static void e(String content, Throwable tr) {
        if (!Constant.Debug) return;
        StackTraceElement caller = CommonTools.getCallerStackTraceElement();
        String tag = generateTag(caller);

        if (customLogger != null) {
            customLogger.e(tag, content, tr);
        } else {
            Log.e(tag, content, tr);
        }
    }

    public static void i(String content) {
        if (!Constant.Debug) return;
        StackTraceElement caller = CommonTools.getCallerStackTraceElement();
        String tag = generateTag(caller);

        if (customLogger != null) {
            customLogger.i(TAG, tag + ":" + content);
        } else {
            int strLength = content.length();
            int start = 0;
            int end = LOG_MAXLENGTH;
            for (int i = 0; i < 100; i++) {//解决log打印的内容超过Log上限的问题
                if (strLength > end) {
                    Log.i(TAG, tag + ":" + content.substring(start, end));
                    start = end;
                    end = end + LOG_MAXLENGTH;
                } else {
                    Log.i(TAG, tag + ":" + content.substring(start, strLength));
                    break;
                }
            }
        }
    }

    public static void i(String content, Throwable tr) {
        if (!Constant.Debug) return;
        StackTraceElement caller = CommonTools.getCallerStackTraceElement();
        String tag = generateTag(caller);

        if (customLogger != null) {
            customLogger.i(tag, content, tr);
        } else {
            Log.i(tag, content, tr);
        }
    }

    public static void v(String content) {
        if (!Constant.Debug) return;
        StackTraceElement caller = CommonTools.getCallerStackTraceElement();
        String tag = generateTag(caller);

        if (customLogger != null) {
            customLogger.v(TAG, tag + ":" + content);
        } else {
            Log.v(TAG, tag + ":" + content);
        }
    }

    public static void v(String content, Throwable tr) {
        if (!Constant.Debug) return;
        StackTraceElement caller = CommonTools.getCallerStackTraceElement();
        String tag = generateTag(caller);

        if (customLogger != null) {
            customLogger.v(tag, content, tr);
        } else {
            Log.v(tag, content, tr);
        }
    }

    public static void w(String content) {
        if (!Constant.Debug) return;
        StackTraceElement caller = CommonTools.getCallerStackTraceElement();
        String tag = generateTag(caller);

        if (customLogger != null) {
            customLogger.w(tag, content);
        } else {
            Log.w(tag, content);
        }
    }

    public static void w(String content, Throwable tr) {
        if (!Constant.Debug) return;
        StackTraceElement caller = CommonTools.getCallerStackTraceElement();
        String tag = generateTag(caller);

        if (customLogger != null) {
            customLogger.w(tag, content, tr);
        } else {
            Log.w(tag, content, tr);
        }
    }

    public static void w(Throwable tr) {
        if (!Constant.Debug) return;
        StackTraceElement caller = CommonTools.getCallerStackTraceElement();
        String tag = generateTag(caller);

        if (customLogger != null) {
            customLogger.w(TAG, tag + ":" + tr);
        } else {
            Log.w(TAG, tag + ":" + tr);
        }
    }

    public static void wtf(String content) {
        if (!Constant.Debug) return;
        StackTraceElement caller = CommonTools.getCallerStackTraceElement();
        String tag = generateTag(caller);

        if (customLogger != null) {
            customLogger.wtf(tag, content);
        } else {
            Log.wtf(tag, content);
        }
    }

    public static void wtf(String content, Throwable tr) {
        if (!Constant.Debug) return;
        StackTraceElement caller = CommonTools.getCallerStackTraceElement();
        String tag = generateTag(caller);

        if (customLogger != null) {
            customLogger.wtf(tag, content, tr);
        } else {
            Log.wtf(tag, content, tr);
        }
    }

    public static void wtf(Throwable tr) {
        if (!Constant.Debug) return;
        StackTraceElement caller = CommonTools.getCallerStackTraceElement();
        String tag = generateTag(caller);

        if (customLogger != null) {
            customLogger.wtf(TAG, tag + ":" + tr);
        } else {
            Log.wtf(TAG, tag + ":" + tr);
        }
    }

    public interface CustomLogger {
        void d(String tag, String content);

        void d(String tag, String content, Throwable tr);

        void e(String tag, String content);

        void e(String tag, String content, Throwable tr);

        void i(String tag, String content);

        void i(String tag, String content, Throwable tr);

        void v(String tag, String content);

        void v(String tag, String content, Throwable tr);

        void w(String tag, String content);

        void w(String tag, String content, Throwable tr);

        void w(String tag, Throwable tr);

        void wtf(String tag, String content);

        void wtf(String tag, String content, Throwable tr);

        void wtf(String tag, Throwable tr);
    }

}
