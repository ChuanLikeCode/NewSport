package com.sibo.fastsport.application;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.sibo.fastsport.base.BaseApplication;
import com.sibo.fastsport.model.UserInfo;
import com.sibo.fastsport.utils.AppManager;
import com.sibo.fastsport.utils.SharedDataTool;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;

import org.apache.commons.codec.binary.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import cn.bmob.v3.Bmob;
import cn.smssdk.SMSSDK;

/**
 * Created by Huangjinfu on 2016/7/26.
 */
public class MyApplication extends BaseApplication {
    public static Context applicationContext;
    /**
     * 当前用户nickname,为了苹果推送不是userid而是昵称
     */
    public static String currentUserNick = "";
    private static MyApplication mInstance = null;
    public int count = 0;
    public Vibrator mVibrator;
    private AppManager mAppManager = null;

    public static MyApplication getInstance() {
        return mInstance;
    }

    public static Context getContext() {
        return applicationContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        applicationContext = this;
        mInstance = this;
        ZXingLibrary.initDisplayOpinion(this);
        SMSSDK.initSDK(this, "193141f4621c1", "b393e87172c18c5069feaf2a4286bc15");
        Bmob.initialize(this, "f79d34f38040f7e7512a4228ea4d0c7a");
        frontOrBack();
        //initOkhttp();

    }

    /**
     * 网络请求相关初始化操作
     */
//    private void initOkhttp() {
//        OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
//                .readTimeout(10000L, TimeUnit.MILLISECONDS)
//                .writeTimeout(10000L, TimeUnit.MILLISECONDS)
//                .build();
//        OkHttpUtils.initClient(okHttpClient);
//    }

    /**
     * 判断在前台还是后台
     */
    private void frontOrBack() {
        //前后台切换判断
        registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

            }

            @Override
            public void onActivityStarted(Activity activity) {
                if (count == 0) {
                    Log.v("vergo", "*   *********切到前台**********");
                    SharedDataTool.setBoolean(activity, "isBackGround", false);
                }
                count++;
            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {
                count--;
                if (count == 0) {
                    Log.v("vergo", "**********切到后台**********");
                    SharedDataTool.setBoolean(activity, "isBackGround", true);
                }
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });
    }

    /**
     * 获取系统版本号
     *
     * @return
     */
    public String getVersionCode() {
        String version = "";
        try {
            version = this.getPackageManager().getPackageInfo(
                    this.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return version;
    }

    /**
     * 读取保存的登陆用户
     *
     * @return
     */
    public UserInfo readLoginUser() {
        SharedPreferences preferences = getSharedPreferences("base64",
                MODE_PRIVATE);
        String productBase64 = preferences.getString("user", "");
        if (productBase64 == "") {
            return null;
        }

        // 读取字节
        byte[] base64 = Base64.decodeBase64(productBase64.getBytes());

        // 封装到字节流
        ByteArrayInputStream bais = new ByteArrayInputStream(base64);
        try {
            // 再次封装
            ObjectInputStream bis = new ObjectInputStream(bais);
            // 读取对象
            return (UserInfo) bis.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public void saveUserInfo(UserInfo storeInfo) {
        SharedPreferences preferences = getSharedPreferences("base64",
                MODE_PRIVATE);
        // 创建字节输出流
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            // 创建对象输出流，并封装字节流
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            // 将对象写入字节流
            oos.writeObject(storeInfo);
            // 将字节流编码成base64的字符窜
            String oAuth_Base64 = new String(Base64.encodeBase64(baos
                    .toByteArray()));
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("user", oAuth_Base64);

            editor.commit();
        } catch (IOException e) {
            // TODO Auto-generated
        }
        Log.i("ok", "存储成功");
    }

    /**
     * 判断是不是第一次登陆
     */
    public boolean isFirstLogin() {//
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(this); // 获得Preferences
        boolean isfirst = sp.getBoolean("isfirst", true);
        return isfirst;
    }

    /**
     * 更新第一次登陆
     */
    public void updateIsFirstLogin(Boolean isfirst) {//
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(this); // 获得Preferences
        SharedPreferences.Editor editor = sp.edit(); // 获得Editor
        editor.putBoolean("isfirst", isfirst); // 将密码存入Preferences
        editor.commit();
    }

    /**
     * 判断是不是第一次启动
     */
    public boolean isFirstLaucher() {//
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this); // 获得Preferences
        boolean isfirst = sp.getBoolean("isfirstlaucher", true);
        return isfirst;
    }

    /**
     * 更新第一次启动
     */
    public void updateIsFirstLaucher(Boolean isfirst) {//
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this); // 获得Preferences
        SharedPreferences.Editor editor = sp.edit(); // 获得Editor
        editor.putBoolean("isfirstlaucher", isfirst); // 将密码存入Preferences
        editor.commit();
    }

    /**
     * activity栈管理
     *
     * @author chensong
     * @date 2016-5-25
     */
    public AppManager getActivityManager() {
        if (mAppManager == null) {
            mAppManager = AppManager.getInstance();
        }
        return mAppManager;
    }


    /**
     * 图片处理
     */
    //图片上传相关 选择图片后的操作
    public File createimagefile(Uri imageUri, int vmWidth, int vmHeight) {
        File imageFile;
        BitmapFactory.Options factory = new BitmapFactory.Options();
        Bitmap bmp;
        Cursor cursor = getContentResolver().query(imageUri, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex("_data");
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            cursor = null;
            if (picturePath == null || picturePath.equals("null")) {
                Toast.makeText(this, "无法获取该图片的路径1", Toast.LENGTH_SHORT).show();
                return null;
            }
            imageFile = new File(picturePath);
        } else {
            File file = new File(imageUri.getPath());
            if (!file.exists()) {
                Toast.makeText(this, "无法获取该图片的路径2", Toast.LENGTH_SHORT).show();
                return null;

            }
            imageFile = new File(file.getAbsolutePath());
        }
        return imageFile;
    }

    /**
     * 获取系统版本号
     *
     * @return
     */
    public String getVesion() {
        String version = "";
        try {
            version = this.getPackageManager().getPackageInfo(this.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return version;
    }



}
