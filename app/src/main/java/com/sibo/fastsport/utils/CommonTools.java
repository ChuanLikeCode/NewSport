package com.sibo.fastsport.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class CommonTools {
    private static ExecutorService cachedThreadPool = null;
    private static ExecutorService fixedThreadPool = null;
    private static ScheduledExecutorService scheduledThreadPool = null;
    private static ExecutorService singleThreadExecutor = null;
    private static String mSharedPreferencesName = "lovebaby";
    /**
     * 检查点击是否太过频繁
     *
     * @return
     */
    private static long lastClickTime;

    //获取当前线程的堆栈信息
    public static StackTraceElement getCurrentStackTraceElement() {
        return Thread.currentThread().getStackTrace()[3];
    }

    public static StackTraceElement getCallerStackTraceElement() {
        return Thread.currentThread().getStackTrace()[4];
    }

    /**
     * 获取随机整数
     *
     * @param max
     * @param min
     * @return
     */
    public static int getRandomInt(int max, int min) {
        int tempMax = max;
        int temMin = min;
        if (max < min) {
            tempMax = min;
            temMin = max;
        }
        Random random = new Random();
        int randomInt = random.nextInt(tempMax) % (tempMax - temMin + 1) + temMin;
        return randomInt;
    }

    public static String getStringFromObj(Object paramObj) {
        if (paramObj != null) {
            try {
                String tempStr = String.valueOf(paramObj);
                if (tempStr.equals("null")) {
                    return "";
                }
                return tempStr;
            } catch (Exception e) {
                e.printStackTrace();
                return "";
            }
        }
        return "";
    }

    public static long getlongFromObj(Object paramObj) {
        if (paramObj != null) {
            try {
                return getlongFromString(String.valueOf(paramObj));
            } catch (Exception e) {
                e.printStackTrace();
                return 0;
            }
        }
        return 0;
    }

    public static long getlongFromString(String paramStr) {
        long lReturn = 0;
        try {
            lReturn = Long.valueOf(paramStr);
        } catch (Exception e) {
            e.printStackTrace();
            return lReturn;
        }
        return lReturn;
    }

    public static int getIntFromObj(Object paramObj) {
        if (paramObj != null) {
            try {
                return getIntFromString(String.valueOf(paramObj));
            } catch (Exception e) {
                e.printStackTrace();
                return 0;
            }
        }
        return 0;
    }

    public static int getIntFromString(String paramStr) {
        int lReturn = 0;
        try {
            lReturn = Integer.valueOf(paramStr);
        } catch (Exception e) {
            e.printStackTrace();
            return lReturn;
        }
        return lReturn;
    }

    public static float getFloatFromObj(Object paramObj) {
        if (paramObj != null) {
            try {
                return getFloatFromString(String.valueOf(paramObj));
            } catch (Exception e) {
                e.printStackTrace();
                return 0;
            }
        }
        return 0;
    }

    public static float getFloatFromString(String paramStr) {
        float lReturn = 0;
        try {
            lReturn = Float.valueOf(paramStr);
        } catch (Exception e) {
            e.printStackTrace();
            return lReturn;
        }
        return lReturn;
    }

    public static double getDoubleFromObj(Object paramObj) {
        if (paramObj != null) {
            try {
                return getDoubleFromString(String.valueOf(paramObj));
            } catch (Exception e) {
                e.printStackTrace();
                return 0;
            }
        }
        return 0;
    }

    public static double getDoubleFromString(String paramStr) {
        double lReturn = 0;
        try {
            lReturn = Double.valueOf(paramStr);
        } catch (Exception e) {
            e.printStackTrace();
            return lReturn;
        }
        return lReturn;
    }

    /**
     * 获取屏幕宽度
     *
     * @param context
     */
    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay().getWidth();//屏幕宽度
    }

    /**
     * 获取屏幕高度
     *
     * @param context
     */
    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay().getHeight();//屏幕宽度
    }

    /**
     * 将dip或dp值转换为px值，保证尺寸大小不变
     *
     * @param dipValue
     * @param （DisplayMetrics类中属性density）
     * @return
     */
    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param pxValue
     * @param （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue
     * @param （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 获取view的坐标
     *
     * @param view
     * @return int[]{0:x;1:y}
     */
    public static int[] getViewLocation(View view) {
        int[] location = new int[2];
        view.getLocationInWindow(location); //获取在当前窗口内的绝对坐标
//		view.getLocationOnScreen(location);//获取在整个屏幕内的绝对坐标,包括了通知栏的高度
//		getLeft , getTop, getBottom, getRight,  这一组是获取相对在它父亲里的坐标。
        LogUtils.i("CommonTools----getViewLocation-----x---->" + location[0] + ";y:" + location[1]);
        return location;
    }

    /**
     * @param paramSecond 时间(s)
     * @return 获取时间 yyyy.MM.dd
     */
    public static String getDateStringBySecond(long paramSecond) {
        return getDateStringByMillSecond(paramSecond * 1000);
    }

    /**
     * @param ()
     * @return 获取时间 yyyy.MM.dd
     */
    public static String getDateStringByMillSecond(long paramMillS) {
        String strDate = "";
        Date currentTime = new Date(paramMillS);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd");//修改时间格式时请查看有哪些地方调用了该方法，修改相应的隔断符(".")
        strDate = formatter.format(currentTime);
        return strDate;
    }

    /**
     * 获取当前年
     */
    public static int getCurrentYear() {
        int year = 0;
        try {
//    		URL urlTime=new URL("http://www.bjtime.cn");//取得资源对象
//    		URLConnection uc=urlTime.openConnection();//生成连接对象
//    		uc.connect(); //发出连接
//    		long ld=uc.getDate(); //取得网站日期时间
//    		Date date=new Date(ld); //转换为标准时间对象
//    		year = date.getYear();
            LogUtils.i("CommonTools-----getCurrentYear------year-------->" + year);
        } catch (Exception e) {
            e.printStackTrace();
            year = 0;
        }
        if (year == 0) {
            Calendar c = Calendar.getInstance();
            year = c.get(Calendar.YEAR);
        }
        return year;
    }

    /**
     * 获取当前月
     *
     * @return month
     */
    public static int getCurrentMounth() {
        int month = 0;
        try {
//    		URL urlTime=new URL("http://www.bjtime.cn");//取得资源对象
//    		URLConnection uc=urlTime.openConnection();//生成连接对象
//    		uc.connect(); //发出连接
//    		long ld=uc.getDate(); //取得网站日期时间
//    		Date date=new Date(ld); //转换为标准时间对象
//    		month = date.getMonth();
            LogUtils.i("CommonTools-----getCurrentMounth------month-------->" + month);
        } catch (Exception e) {
            e.printStackTrace();
            month = 0;
        }
        if (month == 0) {
            Calendar calendar = Calendar.getInstance();
            month = calendar.get(Calendar.MONTH) + 1;
            LogUtils.i("CommonTools-----getCurrentMounth------month--11------>" + month);
        }

        return month;
    }

    /**
     * 描述：获取milliseconds表示的日期时间的字符串.
     *
     * @param milliseconds the milliseconds
     * @param format       格式化字符串，如："yyyy-MM-dd HH:mm:ss"
     * @return String 日期时间字符串
     */
    public static String getStringByFormat(long milliseconds, String format) {
        String thisDateTime = null;
        try {
            SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(format);
            thisDateTime = mSimpleDateFormat.format(milliseconds);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return thisDateTime;
    }

    /**
     * 获取时间戳
     *
     * @return 返回时间戳 yyyyMMddHHmmss
     */
    public static String getTimeStamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String sd = sdf.format(new Date());
        return sd;
    }

    public static Date getDateByFormat(String strDate, String format) {
        SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(format);
        Date date = null;
        try {
            date = mSimpleDateFormat.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 键盘關閉
     *
     * @param
     * @param
     */
    public static void closeKeyboard(Activity activity) {
        try {
            InputMethodManager imm = (InputMethodManager) activity
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            // 得到InputMethodManager的实例
            if (imm.isActive()) {
                imm.hideSoftInputFromWindow(activity.getCurrentFocus()
                        .getWindowToken(), 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 显示键盘
     *
     * @param
     * @param paramEditText 编辑框
     */
    public static void showKeyboard(EditText paramEditText) {
        if (paramEditText == null) {
            LogUtils.e("input param>error!");
            return;
        }
        paramEditText.requestFocus();
        InputMethodManager inputManager =
                (InputMethodManager) paramEditText.getContext().getSystemService(
                        Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(paramEditText, 0);
    }

    /**
     * 显示键盘
     *
     * @param activity      上下文
     * @param paramEditText 编辑框
     */
    public static void showKeyboard(Activity activity, EditText paramEditText) {
        if (activity == null || paramEditText == null) {
            LogUtils.e("input param>error!");
            return;
        }
        try {
            paramEditText.requestFocus();
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(paramEditText, InputMethodManager.SHOW_FORCED);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断字符串是否为空
     *
     * @param paramStr 字符串
     * @return true则为空，false为非空
     */
    public static boolean isStringNull(String paramStr) {
        if (paramStr == null || "".equals(paramStr)) {
            return true;
        } else {
            return false;
        }
    }

    // 检测服务是否正在运行
    public static boolean isServiceRunning(Context context, String service_Name) {
        ActivityManager manager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (service_Name.equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 创建一个可缓存线程池，如果线程池长度超过处理需要，可灵活回收空闲线程，若无可回收，则新建线程;线程池为无限大，当执行第二个任务时第一个任务已经完成，会复用执行第一个任务的线程，而不用每次新建线程。
     *
     * @param runnable
     * @author chensong
     */
    public static synchronized void openCachedThread(Runnable runnable) {
        if (runnable != null) {
            if (cachedThreadPool == null) {
                cachedThreadPool = Executors.newCachedThreadPool();
            }
            cachedThreadPool.execute(runnable);
        }
    }

    /**
     * 限制线程启动数量
     *
     * @param runnable
     * @author chensong
     * @date 2012/12/2
     */
    public static synchronized void openFixedThread(Runnable runnable) {
        if (runnable != null) {
            if (fixedThreadPool == null) {
                fixedThreadPool = Executors.newFixedThreadPool(3);//创建一个定长线程池，可控制线程最大并发数，超出的线程会在队列中等待
            }
            fixedThreadPool.execute(runnable);
        }
    }

    /**
     * 创建一个定长线程池，支持定时及周期性任务执行
     *
     * @param runnable
     * @author chensong
     */
    public static synchronized void openScheduledThread(Runnable runnable) {
        if (runnable != null) {
            if (scheduledThreadPool == null) {
                scheduledThreadPool = Executors.newScheduledThreadPool(5);
            }
            scheduledThreadPool.scheduleAtFixedRate(runnable, 1, 3, TimeUnit.SECONDS);//表示延迟1秒后每3秒执行一次
        }
    }

    /**
     * 创建一个单线程化的线程池，它只会用唯一的工作线程来执行任务，保证所有任务按照指定顺序(FIFO, LIFO, 优先级)执行
     *
     * @param runnable
     * @author chensong
     * @date 2015/12/2
     */
    public static synchronized void openSingleThread(Runnable runnable) {
        if (runnable != null) {
            if (singleThreadExecutor == null) {
                singleThreadExecutor = Executors.newSingleThreadExecutor();
            }
            singleThreadExecutor.execute(runnable);
        }
    }

    /**
     * 网络是否可用
     *
     * @param context
     * @return
     * @author chensong
     * @date 2016年5月21日
     */
    public static boolean isNetworkOk(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeInfo = connectivityManager.getActiveNetworkInfo();
        if (activeInfo == null) {
            return false;
        } else {
            return activeInfo.isConnected();
        }
    }

    public static boolean updatePicFileToSystem(Context context, String absPath) {
        // 其次把文件插入到系统图库
        /*  try {
	        MediaStore.Images.Media.insertImage(context.getContentResolver(),
	        		absPath, paramName, null);
	    } catch (FileNotFoundException e) {
	        e.printStackTrace();
	        return false;
	    }*/
        // 最后通知图库更新
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.parse(absPath);
        intent.setData(uri);
        context.sendBroadcast(intent);
        return true;
    }

    /**
     * 更新图片到图库
     *
     * @param context
     * @param absPath 绝对路径
     *                操作的实际路径格式应为：file://+absPath"
     *                上一个接口弃用
     * @return
     */
    public static boolean updatePicFileToSystemV2(Context context, String absPath) {
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.parse("file://" + absPath);
        intent.setData(uri);
        context.sendBroadcast(intent);
        return true;
    }

    public static boolean SDCardIsExists() {
        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
            return true;
        }
        return false;
    }

    public static boolean isFastDoubleClick(int duration) {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < duration) {       //500毫秒内按钮无效，这样可以控制快速点击，自己调整频率
            return true;
        }
        lastClickTime = time;
        return false;
    }

    /**
     * 设置多种颜色的文本
     *
     * @param paramTv          目标TextView
     * @param paramWholeStr    目标全字符串
     * @param paramSpeStr      需要更改颜色的字符串
     * @param paramCommonColor 普通颜色值
     * @param paramSpeColor    特殊颜色
     */
    public static void setMultiColorText(TextView paramTv, String paramWholeStr, String paramSpeStr, int paramCommonColor, int paramSpeColor) {
        if (paramTv == null || paramWholeStr == null || paramSpeStr == null || paramCommonColor == 0 ||
                paramSpeColor == 0) {
            LogUtils.e("input param>error!");
            return;
        }
        paramTv.setTextColor(paramCommonColor);
        int bstart = paramWholeStr.indexOf(paramSpeStr);
        int bend = bstart + paramSpeStr.length();
        SpannableStringBuilder style = new SpannableStringBuilder(paramWholeStr);
        style.setSpan(new ForegroundColorSpan(paramSpeColor), bstart, bend, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        paramTv.setText(style);
    }


//    /**
//     * base64加密
//     * @param paramSrc
//     * @return
//     */
//    public static String Base64Encoder(String paramSrc)
//    {
//    	String tempDest = "";
//    	if( StringUtils.isBlank(paramSrc))
//    		return tempDest;
//    	try{
//    		//Base64.encode(paramSrc.getBytes(), Base64.DEFAULT);
//    		//String tempStr = (new BASE64Encoder()).encode(paramSrc.getBytes());
//    		//String tempStr = Base64.encode(tempDest.getBytes());
//    		//return tempStr;
//    		return new String(Base64.encode(paramSrc.getBytes(), Base64.DEFAULT));
//    	}catch(Exception e)
//    	{
//    		e.printStackTrace();
//    	}
//    	return paramSrc;
//    }

//    /**
//     * base64解密
//     * @param paramSrc
//     * @return
//     */
//    public static String Base64Decoder(String paramSrc)
//    {
//    	String tempDest = "";
//
//      	if( StringUtils.isBlank(paramSrc))
//    		return tempDest;
//    	try{
//    		//return new String(Base64.decode(paramSrc));
//    		//return new String(new BASE64Decoder().decodeBuffer(paramSrc));
//    		//byte[] tempByte = (new BASE64Decoder().decodeBuffer(paramSrc.getBytes()));
//    		return new String(Base64.decode(paramSrc.getBytes(), Base64.DEFAULT));
//    	}catch(Exception e)
//    	{
//    		e.printStackTrace();
//    	}
//
//    	return paramSrc;
//    }

    public static Bitmap drawable2Bitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        } else if (drawable instanceof NinePatchDrawable) {
            Bitmap bitmap = Bitmap
                    .createBitmap(
                            drawable.getIntrinsicWidth(),
                            drawable.getIntrinsicHeight(),
                            drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                                    : Bitmap.Config.RGB_565);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                    drawable.getIntrinsicHeight());
            drawable.draw(canvas);
            return bitmap;
        } else {
            return null;
        }
    }


    /**
     * list是否为空
     *
     * @param paramList
     * @return
     */
    public static boolean isListAvailable(List<?> paramList) {
        if (paramList == null || paramList.size() == 0) {
            LogUtils.e("paramList is null!");
            return false;
        }
        return true;
    }


    /**
     * index在list中是否有效
     *
     * @param paramIndex
     * @param paramList
     * @return
     */
    public static boolean isIndexAvailableInList(int paramIndex, List<?> paramList) {
        if (!isListAvailable(paramList)) {
            LogUtils.e("paramList is null!");
            return false;
        }
        if (paramIndex >= paramList.size()) {
            LogUtils.e("paramIndex is oversize!");
            return false;
        }
        return true;
    }

    /**
     * 获取application名字
     *
     * @param context
     * @return
     */
    public static String getApplicationName(Context context) {
        PackageManager packageManager = null;
        ApplicationInfo applicationInfo = null;
        try {
            packageManager = context.getApplicationContext()
                    .getPackageManager();
            applicationInfo = packageManager.getApplicationInfo(
                    context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            applicationInfo = null;
        }
        String applicationName = (String) packageManager
                .getApplicationLabel(applicationInfo);
        return applicationName;
    }


    /**
     * 打开wifi设置界面
     *
     * @param context
     */
    public static void openWifiSettingPage(Context context) {
        context.startActivity(new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS));
    }

    /**
     * 四舍五入保留小数点后两位
     *
     * @param value
     * @return
     */
    public static int convert(double value) {
        int ret = (int) Math.rint(value); // 四舍五入
        return ret;
    }


    /**
     * 语音权限是否被允许 ，false为不允许
     *
     * @param context
     * @return
     */
    public static boolean checkAudioPermissionIsGranted(Context context) {
        //int bRet1 = context.checkCallingOrSelfPermission("android.permission.RECORD_AUDIO");
        int bRet2 = context.getApplicationContext().checkCallingPermission("android.permission.RECORD_AUDIO");
        {
            if (bRet2 == PackageManager.PERMISSION_DENIED)
                return false;
        }
        return true;
    	/*PackageManager pm = context.getPackageManager();
    	boolean permission = (PackageManager.PERMISSION_DENIED == pm.checkPermission("android.permission.RECORD_AUDIO", "com.lovebaby"));
    	
    	boolean permission2 = (PackageManager.PERMISSION_GRANTED == pm.checkPermission("android.permission.RECORD_AUDIO", "com.lovebaby"));
    	return permission;*/
    }

}
