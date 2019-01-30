package com.sibo.fastsport.base;


import android.support.multidex.MultiDexApplication;

public class BaseApplication extends MultiDexApplication {

    private static BaseApplication instance;
    private boolean networkAvailable ;
    private BaseActivity currentActivity;


    /***/
    @Override
    public void onCreate()
    {
        super.onCreate();
        instance = this;



    }




    /** 网络断开时调用 */
    public void onDisconnect()
    {
        networkAvailable = false;
        if (null != currentActivity)
            currentActivity.onDisconnect();
    }

    /**
     * 取到应用程序实例
     *
     * @return
     */
    public static BaseApplication getInstance()
    {
        if (null == instance)
            throw new RuntimeException("BaseApplication cannot registered!");

        return instance;
    }


    public BaseActivity getCurrentActivity() {
        return currentActivity;
    }

    public void setCurrentActivity(BaseActivity currentActivity) {
        this.currentActivity = currentActivity;
    }



}
