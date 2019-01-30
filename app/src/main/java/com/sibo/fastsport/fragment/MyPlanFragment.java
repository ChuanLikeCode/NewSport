package com.sibo.fastsport.fragment;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sibo.fastsport.R;
import com.sibo.fastsport.adapter.MakePlanAdapter;
import com.sibo.fastsport.adapter.MyDayFragmentAdapter;
import com.sibo.fastsport.application.Constant;
import com.sibo.fastsport.application.MyApplication;
import com.sibo.fastsport.domain.SportDetail;
import com.sibo.fastsport.domain.SportName;
import com.sibo.fastsport.ui.ScannerActivity;
import com.sibo.fastsport.utils.MakePlanUtils;
import com.sibo.fastsport.utils.MyBombUtils;
import com.sibo.fastsport.utils.MyPlanClickUtils;
import com.sibo.fastsport.view.WhorlView;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import java.util.ArrayList;
import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

/**
 * 扫描二维码得到健身计划
 * Created by zhouchuan on 2017/2/28.
 */

public class MyPlanFragment extends BaseFragment implements View.OnClickListener {
    private static List<SportDetail> list_warmUpDetail = new ArrayList<>();
    private View view;
    private TextView title;//标题和最右边的
    private ImageView scanner;//返回键 关闭键 扫描键
    private WhorlView whorlView;
    private TextView tips;
    private MyBombUtils bombUtils;
    private MakePlanUtils makePlanUtils;
    private RelativeLayout plan_rl, back;
    private MyPlanClickUtils clickUtils;
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
    /**
     * 将扫码之后的健身动作存放在数组里面
     */
    private List<SportName> list_warmUp = new ArrayList<>();
    private List<SportName> list_strthing = new ArrayList<>();
    private List<SportDetail> list_strthingDetail = new ArrayList<>();
    private List<SportName> list_mainAction = new ArrayList<>();
    private List<SportDetail> list_mainActionDetail = new ArrayList<>();
    private List<SportName> list_RelaxAction = new ArrayList<>();
    /**
     * 扫描结果处理
     */
    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constant.SUCCESS:

                    MyApplication.getInstance().saveUserInfo(loginuser);
                    bombUtils.updateAccountInfo(loginuser);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            setMyPlan();//处理扫描得到的健身计划
                        }
                    }).start();

                    break;
                case Constant.FAILED:
                    Log.e("scanner", "failed");
                    Toast.makeText(getActivity(), "获取失败", Toast.LENGTH_SHORT).show();
                    break;
                case Constant.UPLOAD_SUCCESS:
                    initializePlan();
                    break;
                case Constant.SHOW:
                    whorlView.stop();
                    whorlView.setVisibility(View.GONE);
                    plan_rl.setVisibility(View.VISIBLE);
                    break;
            }
        }
    };
    private List<SportDetail> list_RelaxActionDetail = new ArrayList<>();
    private View.OnClickListener dayListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            for (int i = 0; i < days.length; i++) {
                if (v.getId() == daysId[i]) {
                    resetTextView();
                    viewPager.setCurrentItem(i);
                    days[i].setBackgroundColor(getResources().getColor(R.color.light_white));
                    if (isSelected[i]) {
                        MyPlanClickUtils.dayId = i;//标记选择的是第几天
                    }
                    break;
                }
            }
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
            MyPlanClickUtils.dayId = arg0;//标记选择的是第几天
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
     * 初始化健身模块 七天的。。。。。
     */
    private void initializePlan() {
        /**
         * 这个i并不是第几天，因为上传到服务器的数据并不是顺序排列的
         * 需要取出来看看到底是第几天
         */
        for (int i = 0; i < list_day.size(); i++) {
            /**
             * 判断每一天有没有健身计划
             */
            list_day.get(i).warmUpAdd.setVisibility(View.GONE);
            list_day.get(i).stretchingAdd.setVisibility(View.GONE);
            list_day.get(i).mainActionAdd.setVisibility(View.GONE);
            list_day.get(i).relaxActionAdd.setVisibility(View.GONE);
            int Id = MyBombUtils.list_userDayPlan.get(i).getDayId();

            if (MyBombUtils.list_userDayPlan.get(i).isWarmUp()
                    || MyBombUtils.list_userDayPlan.get(i).isStretching()
                    || MyBombUtils.list_userDayPlan.get(i).isMainAction()
                    || MyBombUtils.list_userDayPlan.get(i).isRelaxAction()) {

                list_day.get(Id).tips.setVisibility(View.GONE);
                Log.e("MyBombUtils", "---" + Id);
                //判断这一天有没有热身动作
                setWarmUp(Id, i);
                //判断这一天有没有拉伸动作
                setStretching(Id, i);
                //判断这一天有没有具体动作
                setMainAction(Id, i);
                //判断这一天有没有放松动作
                setRelaxAction(Id, i);
                setVisibility(Id, View.VISIBLE);
            } else {
                list_day.get(Id).tips.setText("今天可以休息哦~~");
                list_day.get(Id).tips.setVisibility(View.VISIBLE);
            }

        }
        handler.sendEmptyMessage(Constant.SHOW);
    }

    private void setMyPlan() {
        /**
         * 将健身动作归类
         */
        list_warmUp.clear();
        list_strthing.clear();
        list_mainAction.clear();
        list_RelaxAction.clear();
        for (int k = 0; k < MyBombUtils.list_sportName.size(); k++) {
            for (int m = 0; m < MyBombUtils.list_warmUp.size(); m++) {
                if (MyBombUtils.list_warmUp.get(m).getWarmId().equals(
                        MyBombUtils.list_sportName.get(k).getObjectId())) {
                    list_warmUp.add(MyBombUtils.list_sportName.get(k));
                }
            }
            for (int m = 0; m < MyBombUtils.list_stretching.size(); m++) {
                if (MyBombUtils.list_stretching.get(m).getStretchingId().equals(
                        MyBombUtils.list_sportName.get(k).getObjectId())) {
                    list_strthing.add(MyBombUtils.list_sportName.get(k));
                }
            }
            for (int m = 0; m < MyBombUtils.list_mainAction.size(); m++) {
                if (MyBombUtils.list_mainAction.get(m).getMainAction().equals(
                        MyBombUtils.list_sportName.get(k).getObjectId())) {
                    list_mainAction.add(MyBombUtils.list_sportName.get(k));
                }
            }
            for (int m = 0; m < MyBombUtils.list_relaxAction.size(); m++) {
                if (MyBombUtils.list_relaxAction.get(m).getRelaxAction().equals(
                        MyBombUtils.list_sportName.get(k).getObjectId())) {
                    list_RelaxAction.add(MyBombUtils.list_sportName.get(k));
                }
            }

        }
        handler.sendEmptyMessage(Constant.UPLOAD_SUCCESS);

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

    /**
     * 设置每一天的热身动作
     */
    public void setWarmUp(int Id, int i) {
        list_day.get(Id).warmUpList.clear();
        if (MyBombUtils.list_userDayPlan.get(i).isWarmUp()) {
            for (int j = 0; j < MyBombUtils.list_warmUp.size(); j++) {
                if (MyBombUtils.list_warmUp.get(j).getDayId() == Id) {
                    list_day.get(Id).warmUpList.add(list_warmUp.get(j));
                }
            }
        }

        Log.e("warmUpList", "---" + list_day.get(Id).warmUpList.size());
        setListViewHeight(list_day.get(Id).warmUpListView);
        list_day.get(Id).warmUpAdapter.notifyDataSetChanged();
        list_day.get(Id).warmUpListView.setVisibility(View.VISIBLE);
    }

    /**
     * 设置每一天的拉伸动作
     */
    public void setStretching(int Id, int i) {
        list_day.get(Id).stretchingList.clear();
        if (MyBombUtils.list_userDayPlan.get(i).isStretching()) {
            for (int j = 0; j < MyBombUtils.list_stretching.size(); j++) {
                if (MyBombUtils.list_stretching.get(j).getDayId() == Id) {
                    list_day.get(Id).stretchingList.add(list_strthing.get(j));
                }
            }
        }
        setListViewHeight(list_day.get(Id).stretchingListView);
        list_day.get(Id).stretchingAdapter.notifyDataSetChanged();
        list_day.get(Id).stretchingListView.setVisibility(View.VISIBLE);
    }

    /**
     * 设置每一天的具体动作
     */
    public void setMainAction(int Id, int i) {
        list_day.get(Id).mainActionList.clear();
        if (MyBombUtils.list_userDayPlan.get(i).isMainAction()) {
            for (int j = 0; j < MyBombUtils.list_mainAction.size(); j++) {
                if (MyBombUtils.list_mainAction.get(j).getDayId() == Id) {
                    list_day.get(Id).mainActionList.add(list_mainAction.get(j));
                }
            }
        }
        setListViewHeight(list_day.get(Id).mainActionListView);
        list_day.get(Id).mainActionAdapter.notifyDataSetChanged();
        list_day.get(Id).mainActionListView.setVisibility(View.VISIBLE);
    }

    /**
     * 设置每一天的放松动作
     */
    public void setRelaxAction(int Id, int i) {
        list_day.get(Id).relaxActionList.clear();
        if (MyBombUtils.list_userDayPlan.get(i).isRelaxAction()) {
            for (int j = 0; j < MyBombUtils.list_relaxAction.size(); j++) {
                if (MyBombUtils.list_relaxAction.get(j).getDayId() == Id) {
                    list_day.get(Id).relaxActionList.add(list_RelaxAction.get(j));
                }
            }
        }
//        Log.e("warmUpList","---"+list_day.get(Id).warmUpList.size());
        setListViewHeight(list_day.get(Id).relaxActionListView);
        list_day.get(Id).relaxActionAdapter.notifyDataSetChanged();
        list_day.get(Id).relaxActionListView.setVisibility(View.VISIBLE);
    }

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container) {
        view = inflater.inflate(R.layout.fragment_plan, container, false);
        findView(view);
        getScreenWH();
        bind();
        return view;
    }

    private void bind() {
        viewPager.setOnPageChangeListener(listener);
        //选择项的监听设置
        for (int i = 0; i < days.length; i++) {

            days[i].setOnClickListener(dayListener);
            iv_day[i].setVisibility(View.GONE);
        }
    }

    /**
     * 获得手机屏幕的宽与ScrollView中子控件的宽
     */
    private void getScreenWH() {
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        screen_width = metrics.widthPixels;
        screen_height = metrics.heightPixels;
        day_width = days[0].getWidth();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 456) {
            if (data!=null){
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    String scanner_id = bundle.getString(CodeUtils.RESULT_STRING);
                    Log.e("id", scanner_id);
                    Toast.makeText(getActivity(), "获取成功", Toast.LENGTH_SHORT).show();
                    loginuser.setPlanObjectId(scanner_id);
                    tips.setVisibility(View.GONE);//隐藏提示框
                    whorlView.setVisibility(View.VISIBLE);//显示进度条
                    whorlView.start();//使进度条动起来
                    getPlanDetail(scanner_id);
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    Toast.makeText(getActivity(), "获取失败", Toast.LENGTH_SHORT).show();

                }
            }

        }
    }

    private void getPlanDetail(String scanner_id) {
        bombUtils = new MyBombUtils(getActivity());
        bombUtils.getUserSportPlan(scanner_id);
        bombUtils.getDayPlan(scanner_id);
        bombUtils.getWarmUp(scanner_id);
        bombUtils.getStretching(scanner_id);
        bombUtils.getMainAction(scanner_id);
        bombUtils.getRelaxAction(scanner_id);
        bombUtils.getPlanAllDetail();
        bombUtils.getPlanAllName();
    }

    private void findView(View view) {
        back = (RelativeLayout) view.findViewById(R.id.top_rl_back);
        back.setVisibility(View.GONE);
        plan_rl = (RelativeLayout) view.findViewById(R.id.plan_icd);
        viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        hs = (HorizontalScrollView) view.findViewById(R.id.makePlanActivity_scrollView);
        tips = (TextView) view.findViewById(R.id.makePlanFragment_tips);
        whorlView = (WhorlView) view.findViewById(R.id.loading);
        title = (TextView) view.findViewById(R.id.top_tv_title);
        scanner = (ImageView) view.findViewById(R.id.scanner);
        for (int i = 0; i < daysId.length; i++) {
            days[i] = (LinearLayout) view.findViewById(daysId[i]);
        }
        for (int j = 0; j < iv_day.length; j++) {
            iv_day[j] = (ImageView) view.findViewById(iv_dayIds[j]);
        }
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

    @Override
    protected void initData() {
        plan_rl.setVisibility(View.GONE);
        title.setText("我的健身计划");
        whorlView.setVisibility(View.GONE);//进度条
        scanner.setVisibility(View.VISIBLE);
        scanner.setOnClickListener(this);
        MakePlanUtils.context = getActivity();
        bombUtils = new MyBombUtils(getActivity());
        BaseDay.select = true;
        for (int i = 0; i < 7; i++) {
            BaseDay day = new BaseDay();
            list_day.add(day);//将第一到第七天的Fragment添加到list中
        }
        //健身计划点击事件查看详情
        clickUtils = new MyPlanClickUtils(getActivity(), list_day);
        resetTextView();
        //初始化Fragment适配器
        adapter = new MyDayFragmentAdapter(getChildFragmentManager(), list_day);
        //ViewPager设置适配器
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0);//默认显示第一个页面
        viewPager.setOffscreenPageLimit(7);
        //默认选择项为第一个，改变背景颜色
        days[0].setBackgroundColor(getResources().getColor(R.color.light_white));
        makePlanUtils = new MakePlanUtils(getActivity(), list_day);
        if (loginuser.getPlanObjectId() != null) {
            tips.setVisibility(View.GONE);//隐藏提示框
            whorlView.setVisibility(View.VISIBLE);//显示进度条
            whorlView.start();//使进度条动起来
            getPlanDetail(loginuser.getPlanObjectId());
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.scanner:
                if (EasyPermissions.hasPermissions(getActivity(), Manifest.permission.CAMERA)){
                    Intent intent = new Intent(getActivity(), ScannerActivity.class);
                    MyBombUtils.COUNT = 0;
                    plan_rl.setVisibility(View.GONE);
                    startActivityForResult(intent, 456);
                }
                break;
        }
    }


    /**
     * 由于ScrollView中嵌套ListView显示的时候，高度只显示一行数据的高度
     * 所以应该计算ListView的原本高度，使用getCount()*item.getMeasuredHeight()
     * 计算高度，重新设置ListView的高度
     *
     * @param listView
     */
    public void setListViewHeight(ListView listView) {
        // 获取ListView对应的Adapter
        MakePlanAdapter adapter = (MakePlanAdapter) listView.getAdapter();
        if (adapter == null) {
            return;
        }
        int totalHeight = 0;
        int len = adapter.getCount();
        View item = adapter.getView(0, null, listView);
        item.measure(0, 0);
        totalHeight = item.getMeasuredHeight() * len;
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight;
        listView.setLayoutParams(params);
    }
}
