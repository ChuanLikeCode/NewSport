package com.sibo.fastsport.ui;

import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.sibo.fastsport.R;
import com.sibo.fastsport.adapter.MyFragmentAdapter;
import com.sibo.fastsport.base.BaseActivity;
import com.sibo.fastsport.fragment.MakePlanFragment;
import com.sibo.fastsport.fragment.MyHomeMenuFragment;
import com.sibo.fastsport.fragment.MyPlanFragment;
import com.sibo.fastsport.fragment.StudentFragment;
import com.sibo.fastsport.receiver.MyBroadcastReceiver;
import com.sibo.fastsport.utils.MakePlanUtils;
import com.sibo.fastsport.utils.MyBombUtils;
import com.sibo.fastsport.utils.StatusBarUtil;
import com.sibo.fastsport.widgets.MetaballMenu;
import com.sibo.fastsport.widgets.MetaballMenuImageView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements MetaballMenu.MetaballMenuClickListener {

    public MakePlanFragment makePlan;//主界面---计划
    public StudentFragment student;//主界面---学员
    public MyHomeMenuFragment myHomeMenu;//主界面--我的
    public MyPlanFragment myPlanFragment;//健身计划列表
    private List<Fragment> list = new ArrayList<Fragment>();//三个主界面的Fragment的list
    private MyFragmentAdapter myFragmentAdapter;//Fragment的适配器
    private MetaballMenu menu;//底部菜单栏
    //底部菜单栏的三个控件 计划、学员、我的
    private MetaballMenuImageView menuMakePlan, menuStudent, menuMyHome;
    private ViewPager viewPager;
    private MyBroadcastReceiver receiver;
    private MyBombUtils bombUtils;
    //viewPager切换时需要做的事情，viewPager监听事件
    private ViewPager.OnPageChangeListener listener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        /**
         * ViewPager选择后需要做的事
         * @param position
         */
        @Override
        public void onPageSelected(int position) {
            switch (position) {
                case 0:
                    menu.selectedView(menuMakePlan);
                    break;
                case 1:
                    menu.selectedView(menuStudent);
                    break;
                case 2:
                    menu.selectedView(menuMyHome);
                    break;
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void findViewByIDS() {
        menu = (MetaballMenu) findViewById(R.id.menu);
        viewPager = (ViewPager) findViewById(R.id.MainActivity_ViewPager);
        menuMakePlan = (MetaballMenuImageView) menu.findViewById(R.id.menuPlan);
        menuStudent = (MetaballMenuImageView) menu.findViewById(R.id.menuStudent);
        menuMyHome = (MetaballMenuImageView) menu.findViewById(R.id.menuMyhome);
    }

    private void setTransparentBar() {
        //透明状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            // Translucent status bar
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
        initListener();
        setTransparentBar();
        StatusBarUtil.StatusBarDarkMode(this, StatusBarUtil.StatusBarLightMode(this));
        MakePlanUtils.isFirst = true;
    }
    /**
     * 初始化数据源
     */
    private void initData() {
        bombUtils = new MyBombUtils(this);
        bombUtils.queryCollect(loginuser);
        makePlan = new MakePlanFragment();
        student = new StudentFragment();
        myHomeMenu = new MyHomeMenuFragment();
        myPlanFragment = new MyPlanFragment();
        String str = loginuser.getType();
        if (str.equals("1")){
            list.add(makePlan);
            menuStudent.setDefaultImage(R.mipmap.student);
            menuStudent.setSelectedImage(R.mipmap.student_focus);
        }else {
            list.add(myPlanFragment);
            menuStudent.setDefaultImage(R.mipmap.teacher);
            menuStudent.setSelectedImage(R.mipmap.teacher_focus);
        }

        list.add(student);
        list.add(myHomeMenu);
        //设置Fragment适配器
        myFragmentAdapter = new MyFragmentAdapter(getSupportFragmentManager(), list);
        viewPager.setAdapter(myFragmentAdapter);
        //使ViewPager默认显示第一个界面
        viewPager.setCurrentItem(0);
        viewPager.setOffscreenPageLimit(3);
        IntentFilter filter = new IntentFilter("scannerFinish");
        receiver = MyBroadcastReceiver.newInstancce();
        registerReceiver(receiver, filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    /**
     * 设置监听事件
     */
    private void initListener() {
        menu.setMenuClickListener(this);
        viewPager.setOnPageChangeListener(listener);
    }


    /**
     * 底部菜单栏的点击效果，跟随着切换viewPager显示界面
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.menuPlan:
                viewPager.setCurrentItem(0);
                StatusBarUtil.StatusBarDarkMode(this, StatusBarUtil.StatusBarLightMode(this));
                break;
            case R.id.menuStudent:
                viewPager.setCurrentItem(1);
                StatusBarUtil.StatusBarDarkMode(this, StatusBarUtil.StatusBarLightMode(this));
                break;
            case R.id.menuMyhome:
                viewPager.setCurrentItem(2);
                StatusBarUtil.StatusBarDarkMode(this, StatusBarUtil.StatusBarLightMode(this));
                break;
        }
    }


}
