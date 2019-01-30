/**
 * 文件名称：AppManager.java
 * 内容摘要：应用程序Activity管理类：用于Activity管理和应用程序退出
 * 其它说明：
 * 创建作者：chensong
 * 创建日期：2016-3-7
 */
package com.sibo.fastsport.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.os.Handler;

import com.sibo.fastsport.ui.MainActivity;

import java.util.Stack;


/**
 * 应用程序Activity管理类：用于Activity管理和应用程序退出
 */
public class AppManager {
    private static Stack<Activity> m_activityStack; // activity栈
    private static Stack<Service> m_serviceStack; // service栈
    private static AppManager m_instance; // AppManager对象

    private AppManager() {
    }

    /**
     * 单一实例
     *
     * @param
     * @return CAppManager 返回实例
     */
    public static AppManager getInstance() {
        if (m_instance == null) {
            m_instance = new AppManager();
        }
        return m_instance;
    }

    public int getActivityStackSize() {
        if (m_activityStack != null) {
            return m_activityStack.size();
        }
        return 0;
    }

    /**
     * 添加Activity到堆栈
     *
     * @param
     * @return
     */
    public synchronized void addActivity(Activity activity) {
        if (m_activityStack == null) {
            m_activityStack = new Stack<Activity>();
        }
        LogUtils.i("addActivity-----activity------->" + activity.getClass());
        for (Activity activity2 : m_activityStack) {
            if (activity2.getClass() == activity.getClass()) {
                activity2.finish();
                m_activityStack.remove(activity2);
                break;
            }
        }
        m_activityStack.add(activity);
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     *
     * @param
     * @return 返回当前Activity
     */
    public Activity currentActivity() {
        Activity activity = null;
        if (m_activityStack != null) {
            activity = m_activityStack.lastElement();
        }
        return activity;
    }

    public void finishActivity(String activityName) {
        for (Activity activity : m_activityStack) {
            if (activity.getClass().getSimpleName().equals(activityName)) {
                finishActivity(activity);
                break;
            }
        }
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     *
     * @param
     * @return
     */
    public void finishActivity() {
        if (m_activityStack != null) {
            Activity activity = m_activityStack.lastElement();
            finishActivity(activity);
        }
    }

    /**
     * 结束指定的Activity
     *
     * @return
     */
    public synchronized void finishActivity(final Activity activity) {
        if (activity != null) {
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    if (m_activityStack != null) {
                        m_activityStack.remove(activity);
                        activity.finish();
                    }
                }
            }, 0);
        }
    }

    /**
     * 从栈中移除指定的Activity
     *
     * @return
     */
    public synchronized void removeActivity(final Activity activity) {
        if (activity != null) {
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    if (m_activityStack != null && m_activityStack.size() > 0) {
                        m_activityStack.remove(activity);
                    }
                }
            }, 0);
        }
    }

    /**
     * 结束指定类名的Activity
     *
     * @param  <?> 指定结束的cls
     * @return
     */
    public synchronized void finishSameActivity(final Activity finishActivity) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (finishActivity != null && m_activityStack != null && m_activityStack.size() > 0) {
                    for (Activity activity : m_activityStack) {
                        LogUtils.d("finishSameActivity-----finishActivity---->" + finishActivity.getClass() + ";activity:" + activity.getClass());
                        if (activity != null && activity.getClass().equals(finishActivity.getClass())) {
                            finishActivity(activity);
                        }
                    }
                }
            }
        }, 0);
    }

    public synchronized void finishActivity(final Class<?> activityClass) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (m_activityStack != null && m_activityStack.size() > 0) {
                    for (Activity activity : m_activityStack) {
                        if (activity != null && activity.getClass().equals(activityClass)) {
                            finishActivity(activity);
                        }
                    }
                }
            }
        }, 0);
    }

    /**
     * 结束所有Activity
     *
     * @param
     * @return
     */
    public void finishAllActivity() {
        if (m_activityStack != null && m_activityStack.size() > 0) {
            for (int i = 0, size = m_activityStack.size(); i < size; i++) {
                if (null != m_activityStack.get(i)) {
                    LogUtils.i("finishAllActivity-----activity------->" + m_activityStack.get(i).getClass());
                    m_activityStack.get(i).finish();
                }
            }
            m_activityStack.clear();
        }
    }

    /**
     * 结束除了主界面以外所有的Activity
     *
     * @param
     * @return
     */
    public void finishOtherActivity() {
        if (m_activityStack != null && m_activityStack.size() > 0) {
            for (int i = 0, size = m_activityStack.size(); i < size; i++) {
                if (null != m_activityStack.get(i)) {
                    if (!(m_activityStack.get(i) instanceof MainActivity))
                        m_activityStack.get(i).finish();
                }
            }
            m_activityStack.clear();
        }
    }

    /**
     * 关闭该界面与栈顶之间的所有页面(包含该页面)
     *
     * @param activityClass
     */
    public void finishToTop(final Class<?> activityClass) {
        if (m_activityStack != null && m_activityStack.size() > 0) {
            int size = m_activityStack.size();
            for (int i = 0; i < size; i++) {
                Activity tempActivity = m_activityStack.get(i);
                if (tempActivity == null) {
                    LogUtils.e("tempActivity is null!");
                    continue;
                }

                LogUtils.d("activityClass.getClass():" + activityClass.getClass()
                        + "\n" + "activityClass.getName():" + activityClass.getName()
                        + "\n" + "tempActivity.getClass():" + tempActivity.getClass().getName());

                if (tempActivity.getClass().getName().equals(activityClass.getName())) {

                    for (int j = 0; j < size - i; j++) {
                        finishActivity(m_activityStack.get(i));
                    }
                    break;
                }
            }
        }
    }

    /**
     * 关闭该界面与栈顶之间的所有页面(不包含该页面)
     *
     * @param activityClass
     */
    public void finishToTopAndSelf(final Class<?> activityClass) {
        if (m_activityStack != null && m_activityStack.size() > 0) {
            int size = m_activityStack.size();
            for (int i = 0; i < size; i++) {
                Activity tempActivity = m_activityStack.get(i);
                if (tempActivity == null) {
                    LogUtils.e("tempActivity is null!");
                    continue;
                }

                LogUtils.d("activityClass.getClass():" + activityClass.getClass()
                        + "\n" + "activityClass.getName():" + activityClass.getName()
                        + "\n" + "tempActivity.getClass():" + tempActivity.getClass().getName());

                if (tempActivity.getClass().getName().equals(activityClass.getName())) {

                    for (int j = 0; j < size - (i + 1); j++) {
                        finishActivity(m_activityStack.get(i + 1));
                    }
                    break;
                }
            }
        }
    }

    /**
     * 添加service到堆栈
     *
     * @param service 需要加入的service
     * @return
     */
    public synchronized void addService(Service service) {
        if (m_serviceStack == null) {
            m_serviceStack = new Stack<Service>();
        }
        m_serviceStack.add(service);
    }

    /**
     * 关闭制定Service
     *
     * @param service
     */
    public void stopService(final Service service) {
        if (service != null && m_serviceStack != null && m_serviceStack.size() > 0) {
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    service.stopSelf();
                    m_serviceStack.remove(service);
                }
            }, 50);
        }
    }

    /**
     * 关闭所有service
     */
    public void stopAllService() {
        if (m_serviceStack != null && m_serviceStack.size() > 0) {
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    for (Service service : m_serviceStack) {
                        LogUtils.i("stopAllService------service---->" + service.getClass());
                        service.stopSelf();
                    }
                }
            }, 50);
        }
    }

    /**
     * 退出应用程序
     *
     * @return
     */
    public void AppExit(Context context) {
        if (context != null) {
            try {
                finishAllActivity();
                ActivityManager activityMgr = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
                activityMgr.restartPackage(context.getPackageName());
                System.exit(0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
