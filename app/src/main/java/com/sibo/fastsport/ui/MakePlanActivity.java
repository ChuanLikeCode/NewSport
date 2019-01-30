package com.sibo.fastsport.ui;

import android.app.Dialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sibo.fastsport.R;
import com.sibo.fastsport.adapter.MyDayFragmentAdapter;
import com.sibo.fastsport.application.Constant;
import com.sibo.fastsport.fragment.BaseDay;
import com.sibo.fastsport.receiver.MakePlanBroadcastReceiver;
import com.sibo.fastsport.utils.CollectPlan;
import com.sibo.fastsport.utils.MakePlanUtils;
import com.sibo.fastsport.utils.MyBombUtils;
import com.sibo.fastsport.utils.StatusBarUtil;
import com.sibo.fastsport.view.WhorlView;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import java.util.ArrayList;
import java.util.List;

public class MakePlanActivity extends FragmentActivity implements View.OnClickListener {

    private boolean click = false;
    //每一个Day的布局ID
    private int[] daysId = {R.id.ll1, R.id.ll2, R.id.ll3, R.id.ll4, R.id.ll5, R.id.ll6, R.id.ll7,};
    private LinearLayout[] days = new LinearLayout[7];
    //ViewPager的适配器
    private MyDayFragmentAdapter adapter;
    private ViewPager viewPager;
    //顶部标题栏
    private Toolbar head;
    //横向ScrollView来显示第一到第七天
    private HorizontalScrollView hs;
    //页面标题与制定计划选择动作按钮
    private TextView title, make;
    //返回与关闭按钮
    private ImageView back, close;
    //第一到第七天的选择图片
    private ImageView[] iv_day = new ImageView[7];
    private int[] iv_dayIds = {R.id.iv1, R.id.iv2, R.id.iv3, R.id.iv4, R.id.iv5, R.id.iv6, R.id.iv7};
    //第一到第七天的Fragment list
    private List<BaseDay> list_day = new ArrayList<>();
    //用来保存选择的ViewPager角标
    private boolean[] isSelected = {false, false, false, false, false, false, false};
    //屏幕的宽
    private int screen_width;
    private int screen_height;
    //显示第一到第七天的控件宽度
    private int day_width;
    //当前选择的控件离屏幕左边的距离
    private int day_left;
    //ViewPager滑动时ScrollView跟随滑动的距离
    private int srcollToDis;

    private MakePlanUtils makePlanUtils;

    private MyBombUtils myBombUtils;
    //用来处理上传热身拉伸具体放松动作
    public Handler handler2 = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            //Log.e("msg-handler2",msg.what+"");
            if (Constant.SHOW == msg.what) {
                MyBombUtils.addDayPlan++;
                if (MyBombUtils.addDayPlan == 7) {

                    myBombUtils.addWarmUp();
                    myBombUtils.addStretching();
                    myBombUtils.addMainAction();
                    myBombUtils.addRelaxAction();
                }

                //handler.sendEmptyMessage(Constant.SUCCESS);
            }
        }
    };
    //private CollectPlan collectPlan;
    private Dialog dialog;
    private WhorlView whorlView;
    private TextView planName;
    private ImageView showCode;
    //用来显示二维码
    public Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Log.e("msg-handler2",msg.what+"");
            if (msg.what == Constant.SUCCESS){
                whorlView.stop();
                whorlView.setVisibility(View.INVISIBLE);
                planName.setVisibility(View.VISIBLE);
                showCode.setVisibility(View.VISIBLE);
                loadCode();
            }
        }
    };
    private ImageView dialog_close;
    private MakePlanBroadcastReceiver receiver;
    /**
     * 制定计划的按钮监听事件
     */
    private View.OnClickListener okListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            boolean ok = false;
            for (int i = 0 ; i < isSelected.length ; i++) {
                if (isSelected[i]){
                    ok = true;
                }
            }
                if (ok){
                    if ((MakePlanUtils.sp_warmUp.size() != 0)
                            || (MakePlanUtils.sp_stretching.size() != 0)
                            || (MakePlanUtils.sp_mainAction.size() != 0)
                            || (MakePlanUtils.sp_relaxAction.size() != 0)){
                        dialog.show();
                        myBombUtils.addPlan(CollectPlan.userSportPlan);
                    }else {
                        Toast.makeText(MakePlanActivity.this , "请编辑健身计划~~",Toast.LENGTH_SHORT).show();
                    }

                }else {
                    Toast.makeText(MakePlanActivity.this , "请编辑健身计划~~",Toast.LENGTH_SHORT).show();
                }
        }
    };
    /**
     * 返回按钮的监听事件
     */
    private View.OnClickListener backListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            for (BaseDay b :
                    list_day) {
                b.relaxActionList.clear();
                b.warmUpList.clear();
                b.mainActionList.clear();
                b.stretchingList.clear();

            }
            CollectPlan.dayPlan.clear();
            CollectPlan.warmUps.clear();
            CollectPlan.stretchings.clear();
            CollectPlan.mainActions.clear();
            CollectPlan.relaxActions.clear();
            finish();
        }
    };
    /**
     * 当滑动ViewPager时，需要改变选择栏的背景颜色，改变ScrollView滑动的距离
     */
    private ViewPager.OnPageChangeListener listener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int arg0) {
            //计算ScrollView滑动的距离
            day_left = days[arg0].getLeft();
            //滑动的距离是 = 当前控件距离屏幕左边的宽度+控件宽度/2-屏幕宽度/2
            srcollToDis = day_left + day_width / 2 - screen_width / 2;
            //调用smoothScrollTo()滑动ScrollView控件
            hs.smoothScrollTo(srcollToDis, 0);
            //设置选择栏的背景颜色，用来区别哪一个选项被选择了
            resetTextView();
            days[arg0].setBackgroundColor(getResources().getColor(R.color.light_white));
            if (isSelected[arg0]) {
                MakePlanUtils.dayId = arg0;//设置当前选择的是第几天
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
            // TODO Auto-generated method stub

        }
    };
    /**
     * 每一天打钩的那个图片的监听事件
     */
    private View.OnClickListener daysListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            for (int i = 0; i < iv_day.length; i++) {
                if (v.getId() == iv_dayIds[i]) {
                    resetTextView();
                    //设置ViewPager需要显示的页面
                    viewPager.setCurrentItem(i);
                    //改变选择项的背景颜色
                    days[i].setBackgroundColor(getResources().getColor(R.color.light_white));
                    //改变选择项的图标背景
                    if (!isSelected[i]) {
                        isSelected[i] = true;
                        //CollectPlan.dayId.add(i);
                        MakePlanUtils.dayId = i;//标记选择的是第几天
                        iv_day[i].setImageResource(R.mipmap.icon_ok);
                        setVisibility(i, View.VISIBLE);
                        list_day.get(i).tips.setVisibility(View.GONE);//设置提示框消失
                    } else {
                        isSelected[i] = false;
                        //CollectPlan.dayId.remove(i);
                        iv_day[i].setImageResource(R.mipmap.icon_select_default);
                        setVisibility(i, View.GONE);
                        list_day.get(i).tips.setVisibility(View.VISIBLE);//设置提示框显示
                    }

                }

            }
        }
    };

    private void initDialog() {
        dialog = new Dialog(this);
        View view = getLayoutInflater().inflate(R.layout.plan_dialog, null);
        whorlView = (WhorlView) view.findViewById(R.id.loading);
        planName = (TextView) view.findViewById(R.id.dialog_tv_userPlanName);
        showCode = (ImageView) view.findViewById(R.id.dialog_iv_userCode);
        dialog_close = (ImageView) view.findViewById(R.id.dialog_iv_close);
        whorlView.start();
        planName.setVisibility(View.INVISIBLE);
        showCode.setVisibility(View.INVISIBLE);
        Window window = dialog.getWindow();
        window.requestFeature(Window.FEATURE_NO_TITLE);
        window.getDecorView().setPadding(50, 50, 50, 50);
        window.setBackgroundDrawableResource(android.R.color.white);
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.width = 2 * screen_width / 3;
        layoutParams.height = screen_height / 2;
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(view, layoutParams);
        dialog_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                for (BaseDay b :
                        list_day) {
                    b.relaxActionList.clear();
                    b.warmUpList.clear();
                    b.mainActionList.clear();
                    b.stretchingList.clear();
                }
                CollectPlan.dayPlan.clear();
                CollectPlan.warmUps.clear();
                CollectPlan.stretchings.clear();
                CollectPlan.mainActions.clear();
                CollectPlan.relaxActions.clear();
                Intent intent = new Intent(MakePlanActivity.this, MainActivity.class);
                intent.putExtra("finish", 111);
                startActivity(intent);
                finish();
            }
        });
    }

    /**
     * 生产二维码
     */
    public void loadCode() {
        //获取健身计划的ID-计划名字-教练名字
        String str = CollectPlan.userSportPlan.getObjectId();
        planName.setText(CollectPlan.userSportPlan.getPlanName());
        Bitmap bitmap = CodeUtils.createImage(str, 250, 250, BitmapFactory.decodeResource(getResources(), R.mipmap.logo));
        showCode.setImageBitmap(bitmap);
        MyBombUtils.COUNT = 0;
        MyBombUtils.MAKE = 0;
        MyBombUtils.addDayPlan = 0;
        for (int i = 0; i < 7; i++) {
            list_day.get(i).relaxActionList.clear();
            list_day.get(i).mainActionList.clear();
            list_day.get(i).warmUpList.clear();
            list_day.get(i).stretchingList.clear();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    /**
     * 如果今天不休息则显示训练的列表
     * 否则显示提示框
     *
     * @param i
     * @param select
     */
    private void setVisibility(int i, int select) {
        list_day.get(i).warmUpView.setVisibility(select);
        list_day.get(i).warmUpListView.setVisibility(select);
        list_day.get(i).stretchingView.setVisibility(select);
        list_day.get(i).stretchingListView.setVisibility(select);
        list_day.get(i).mainActionView.setVisibility(select);
        list_day.get(i).mainActionListView.setVisibility(select);
        list_day.get(i).relaxActionView.setVisibility(select);
        list_day.get(i).relaxActionListView.setVisibility(select);
    }

    protected void findViewByIDS() {
        for (int i = 0; i < daysId.length; i++) {
            days[i] = (LinearLayout) findViewById(daysId[i]);
        }
        for (int j = 0; j < iv_day.length; j++) {
            iv_day[j] = (ImageView) findViewById(iv_dayIds[j]);
        }
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        head = (Toolbar) findViewById(R.id.makePlanActivity_title);
        title = (TextView) head.findViewById(R.id.tv_title_bar);
        back = (ImageView) head.findViewById(R.id.iv_back_titlebar);
        close = (ImageView) head.findViewById(R.id.iv_close_titlebar);
        make = (TextView) head.findViewById(R.id.tv_complete_titlebar);
        hs = (HorizontalScrollView) findViewById(R.id.makePlanActivity_scrollView);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.StatusBarDarkMode(this, StatusBarUtil.StatusBarLightMode(this));
        setContentView(R.layout.activity_make_plan);
        findViewByIDS();
        init();
        initListener();
        getScreenWH();
        initDialog();
        CollectPlan.initDayPlan();
    }

    /**
     * 当界面显示的时候，判断这个界面是不是第一次显示
     * 是：说明是从选择体型界面跳转过来的，设置isFirst = false
     * 否：说明是从添加动作界面跳转过来的，执行getResult()将选择的健身动作设置在列表中
     */
    @Override
    protected void onResume() {
        super.onResume();
        if (!MakePlanUtils.isFirst) {
            makePlanUtils.getResult();
        }
        MakePlanUtils.isFirst = false;
    }

    /**
     * 设置控件的监听事件
     */
    private void initListener() {
        viewPager.setOnPageChangeListener(listener);
        back.setOnClickListener(backListener);
        make.setOnClickListener(okListener);
        //选择项的监听设置
        for (int i = 0; i < days.length; i++) {
            days[i].setOnClickListener(this);
        }
        //选择图标的监听设置，就是打钩的那个图片
        for (int j = 0; j < iv_day.length; j++) {
            iv_day[j].setOnClickListener(daysListener);
        }

    }


    /**
     * 初始化数据
     */
    private void init() {
        receiver = new MakePlanBroadcastReceiver();
        IntentFilter filter = new IntentFilter("makePlan");
        registerReceiver(receiver, filter);
        MakePlanUtils.context = this;
        //collectPlan = new CollectPlan(this);
        myBombUtils = new MyBombUtils(this);
        for (int i = 0; i < 7; i++) {
            BaseDay day = new BaseDay();
            list_day.add(day);//将第一到第七天的Fragment添加到list中
        }

        resetTextView();
        //初始化Fragment适配器
        adapter = new MyDayFragmentAdapter(getSupportFragmentManager(), list_day);
        //ViewPager设置适配器
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0);//默认显示第一个页面
        //默认选择项为第一个，改变背景颜色
        days[0].setBackgroundColor(getResources().getColor(R.color.light_white));
        //改变标题栏的字符
        title.setText(R.string.makePlan);
        close.setVisibility(View.GONE);
        make.setText(R.string.ok);
        //设置选择动作界面的监听
        makePlanUtils = new MakePlanUtils(this, list_day);
    }


    /**
     * 重置选项卡的背景颜色
     */
    private void resetTextView() {
        // TODO Auto-generated method stub
        for (int i = 0; i < days.length; i++) {
            days[i].setBackgroundColor(Color.WHITE);
        }
    }

    /**
     * 选项卡的点击事件
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        for (int i = 0; i < days.length; i++) {
            if (v.getId() == daysId[i]) {
                resetTextView();
                viewPager.setCurrentItem(i);
                days[i].setBackgroundColor(getResources().getColor(R.color.light_white));
                if (isSelected[i]) {
                    MakePlanUtils.dayId = i;//标记选择的是第几天
                }
                break;
            }
        }
    }

    /**
     * 获得手机屏幕的宽与ScrollView中子控件的宽
     */
    private void getScreenWH() {

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        screen_width = metrics.widthPixels;
        screen_height = metrics.heightPixels;
        day_width = days[0].getWidth();
    }
}
