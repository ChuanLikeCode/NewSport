package com.sibo.fastsport.base;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.sibo.fastsport.R;
import com.sibo.fastsport.application.MyApplication;
import com.sibo.fastsport.domain.Pickers;
import com.sibo.fastsport.model.UserInfo;
import com.sibo.fastsport.view.PickerScrollView;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;


public abstract class BaseActivity extends AppCompatActivity {
    protected FragmentManager fragmentManager;
    protected Context mContext;
    protected ProgressDialog dialog;
    protected UserInfo loginuser;
    protected PickerScrollView pickers;
    protected Dialog dialog_info;
    private View scrollViewLayout;
    /**
     * 是否沉浸状态栏
     **/
    private boolean isSetStatusBar = true;
    /**
     * 是否允许全屏
     **/
    private boolean mAllowFullScreen = true;
    /**
     * 是否禁止旋转屏幕
     **/
    private boolean isAllowScreenRoate = false;
    /**
     * 当前Activity渲染的视图View
     **/
    private View mContextView = null;

    public static void StatusBarLightMode(Activity activity, int type) {
        if (type == 1) {
            MIUISetStatusBarLightMode(activity.getWindow(), true);
        } else if (type == 2) {
            FlymeSetStatusBarLightMode(activity.getWindow(), true);
        } else if (type == 3) {
            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

    }

    public static int StatusBarLightMode(Activity activity) {
        int result = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (MIUISetStatusBarLightMode(activity.getWindow(), true)) {
                result = 1;
            } else if (FlymeSetStatusBarLightMode(activity.getWindow(), true)) {
                result = 2;
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                result = 3;
            }
        }
        return result;
    }

    /**
     * 设置状态栏字体图标为深色，需要MIUIV6以上
     *
     * @param window 需要设置的窗口
     * @param dark   是否把状态栏字体及图标颜色设置为深色
     * @return boolean 成功执行返回true
     */
    public static boolean MIUISetStatusBarLightMode(Window window, boolean dark) {
        boolean result = false;
        if (window != null) {
            Class clazz = window.getClass();
            try {
                int darkModeFlag = 0;
                Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
                darkModeFlag = field.getInt(layoutParams);
                Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
                if (dark) {
                    extraFlagField.invoke(window, darkModeFlag, darkModeFlag);//状态栏透明且黑色字体
                } else {
                    extraFlagField.invoke(window, 0, darkModeFlag);//清除黑色字体
                }
                result = true;
            } catch (Exception e) {

            }
        }
        return result;
    }

    /**
     * 设置状态栏图标为深色和魅族特定的文字风格
     * 可以用来判断是否为Flyme用户
     *
     * @param window 需要设置的窗口
     * @param dark   是否把状态栏字体及图标颜色设置为深色
     * @return boolean 成功执行返回true
     */
    public static boolean FlymeSetStatusBarLightMode(Window window, boolean dark) {
        boolean result = false;
        if (window != null) {
            try {
                WindowManager.LayoutParams lp = window.getAttributes();
                Field darkFlag = WindowManager.LayoutParams.class
                        .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field meizuFlags = WindowManager.LayoutParams.class
                        .getDeclaredField("meizuFlags");
                darkFlag.setAccessible(true);
                meizuFlags.setAccessible(true);
                int bit = darkFlag.getInt(null);
                int value = meizuFlags.getInt(lp);
                if (dark) {
                    value |= bit;
                } else {
                    value &= ~bit;
                }
                meizuFlags.setInt(lp, value);
                window.setAttributes(lp);
                result = true;
            } catch (Exception e) {

            }
        }
        return result;
    }

    protected void showDialog(List<Pickers> pickerItems) {
        pickers.setData(pickerItems);
        pickers.setSelected(0);
        if (dialog_info == null) {
            dialog_info = new Dialog(this);
            Window window = dialog_info.getWindow();
            window.setGravity(Gravity.BOTTOM);
            window.setWindowAnimations(R.style.main_menu_animstyle);
            window.requestFeature(Window.FEATURE_NO_TITLE);
            window.setBackgroundDrawableResource(android.R.color.transparent);
            window.getDecorView().setPadding(0, 0, 0, 0);
            Display d = getWindow().getWindowManager().getDefaultDisplay();
            //Display d = window.getWindowManager().getDefaultDisplay();
            WindowManager.LayoutParams p = window.getAttributes();
            p.width = d.getWidth(); //设置dialog的宽度为当前手机屏幕的宽度
            window.setAttributes(p);
            // 设置点击外围解散
            dialog_info.setCanceledOnTouchOutside(true);
            dialog_info.setContentView(scrollViewLayout);
        }
        // 设置显示动画
        dialog_info.show();

    }

    abstract protected void findViewByIDS();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
//        steepStatusBar();
        initStart();
//        ZsOkHttpUtils.myInstant().setContext(this);
        loginuser = MyApplication.getInstance().readLoginUser();
        MyApplication.getInstance().getActivityManager().addActivity(this);
        // 设置所有Activity禁止横屏展示
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        scrollViewLayout = getLayoutInflater().inflate(R.layout.scrollview_select, null);
        pickers = (PickerScrollView) scrollViewLayout.findViewById(R.id.pickers);
//        setStatusBarColor(R.color.title);
        StatusBarLightMode(this, StatusBarLightMode(this));

    }

    public void backClick(View view) {
        finish();
    }

    /**
     * [沉浸状态栏]
     */
    protected void steepStatusBar() {
        ViewGroup contentFrameLayout = (ViewGroup) findViewById(Window.ID_ANDROID_CONTENT);
        View parentView = contentFrameLayout.getChildAt(0);
        if (parentView != null && Build.VERSION.SDK_INT >= 14) {
            parentView.setFitsSystemWindows(true);
        }
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);//初始化UI界面
        findViewByIDS();

    }

    private void initStart() {
        mContext = this;
        fragmentManager = getSupportFragmentManager();//fragment管理者
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//设置为竖屏
    }

    /**
     * 跳转Activity
     */
    public void startActivity(Class<?> cls) {
        Intent intent = new Intent(BaseActivity.this, cls);
        startActivity(intent);
    }

    /**
     * 跳转Activity
     * 包含数据传送
     */
    public void startActivity(Class<?> cls, Bundle bundle) {
        Intent intent = new Intent(BaseActivity.this, cls);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    /**
     * 跳转Activity
     * 包含数据传送
     * 包含过度动画的实现
     */
    public void startActivity(Class<?> cls, Bundle bundle, int enterAnim, int exitAnim) {
        startActivity(cls, bundle);
        overridePendingTransition(enterAnim, exitAnim);
    }

    /**
     * 跳转Activity
     * 包含过度动画的实现
     *
     * @param cls       跳转的类
     * @param enterAnim 进入的动画
     * @param exitAnim  退出的动画
     */
    public void startActivity(Class<?> cls, int enterAnim, int exitAnim) {
        startActivity(cls);
        overridePendingTransition(enterAnim, exitAnim);
    }

    /**
     * 通过控件的Id获取对于的控件
     */
    protected <T extends View> T findViewsById(int viewId) {
        View view = findViewById(viewId);
        return (T) view;
    }

    /**
     * 通过控件的Id获取对于的控件
     */
    protected <T extends View> T findViewsById(View parent, int viewId) {
        View view = parent.findViewById(viewId);
        return (T) view;
    }

    /**
     * 退出加动画
     *
     * @param exitAnim 退出动画
     */
    public void finish(int exitAnim) {
        finish();
        overridePendingTransition(0, exitAnim);
    }

    /**
     * 获取Bundle
     */
    protected Bundle getBundle() {
        Bundle extras = getIntent().getExtras();
        return extras;
    }

    /**
     * 销毁Activity
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.getInstance().getActivityManager().removeActivity(this);
    }

    /**
     * 获取屏幕宽度
     */
    public int getVmWidth() {
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        return metric.widthPixels; // 屏幕宽度（像素）

    }

    /**
     * 获取屏幕高度
     */
    public int getVmHeight() {
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        return metric.heightPixels; // 屏幕高度（像素）

    }

    /**
     * 网络断开
     */
    public void onDisconnect() {

    }

    // 设置有颜色得状态栏
    public void setStatusBarColor(int color) {
        if (MIUISetStatusBarLightMode(getWindow(), true) || FlymeSetStatusBarLightMode(getWindow(), true)) {
            return;
        }
        Window window = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN  | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);//使其变成全屏
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(color));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }

}
