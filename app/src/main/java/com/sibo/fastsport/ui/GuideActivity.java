package com.sibo.fastsport.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.sibo.fastsport.R;
import com.sibo.fastsport.adapter.MyViewPagerAdapter;
import com.sibo.fastsport.application.Constant;
import com.sibo.fastsport.application.MyApplication;
import com.sibo.fastsport.base.BaseActivity;
import com.sibo.fastsport.utils.SharepreferencesUtilSystemSettings;

import java.util.ArrayList;
import java.util.List;

public class GuideActivity extends BaseActivity implements View.OnClickListener {

    private ImageView[] iv_point = new ImageView[3];
    private int[] pointIds = {R.id.guide_point1, R.id.guide_point2, R.id.guide_point3};
    private int[] showImgIds = {R.mipmap.y_01, R.mipmap.y_02, R.mipmap.y_03};
    private ViewPager viewPager;
    private List<ImageView> list_image = new ArrayList<>();
    private int screen_width;
    private int screen_height;
    private TextView start;

    @Override
    protected void findViewByIDS() {
        for (int i = 0; i < pointIds.length; i++) {
            iv_point[i] = (ImageView) findViewById(pointIds[i]);
        }
        viewPager = (ViewPager) findViewById(R.id.guide_viewPager);
        start = (TextView) findViewById(R.id.guide_start);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //隐藏通知栏
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getScreenWH();
        checkIsFirst();
        setContentView(R.layout.activity_guide);
        initListener();
        initData();
    }

    /**
     * 检查是不是第一次登录
     */
    private void checkIsFirst() {

        if (!MyApplication.getInstance().isFirstLaucher()) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
    }

    /**
     * 获取屏幕宽高像素
     */
    private void getScreenWH() {

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        screen_width = metrics.widthPixels;
        screen_height = metrics.heightPixels;
    }

    /**
     * 初始化ViewPager数据
     */
    private void initData() {
        for (int i = 0; i < pointIds.length; i++) {
            ImageView imageView = new ImageView(this);
            /** 设置ImageView宽高 */
            ViewPager.LayoutParams params1 = new ViewPager.LayoutParams();
            params1.width = screen_width;
            params1.height = screen_height;

            imageView.setLayoutParams(params1);

            imageView.setBackgroundResource(showImgIds[i]);
            list_image.add(imageView);
        }
        iv_point[0].setImageResource(R.mipmap.yindao_icon_selected);
        MyViewPagerAdapter adapter = new MyViewPagerAdapter(this, list_image);
        viewPager.setAdapter(adapter);
    }

    /**
     * 初始化监听事件
     */
    private void initListener() {
        for (int i = 0; i < pointIds.length; i++) {
            iv_point[i].setOnClickListener(this);
        }
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GuideActivity.this, LoginActivity.class));
                MyApplication.getInstance().updateIsFirstLaucher(false);
                finish();
            }
        });
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            /**
             * 页面滑动
             * @param position
             * @param positionOffset
             * @param positionOffsetPixels
             */
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            /**
             * 页面选择之后
             * @param position
             */
            @Override
            public void onPageSelected(int position) {
                resetImg();
                iv_point[position].setImageResource(R.mipmap.yindao_icon_selected);
                if (position == 2) {
                    start.setVisibility(View.VISIBLE);
                } else {
                    start.setVisibility(View.GONE);
                }
            }

            /**
             * 页面状态变化时
             * @param state
             */
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    @Override
    public void onClick(View v) {
        resetImg();
        for (int i = 0; i < pointIds.length; i++) {
            if (pointIds[i] == v.getId()) {
                iv_point[i].setBackgroundResource(R.mipmap.yindao_icon_selected);
                viewPager.setCurrentItem(i);
                if (i == 2) {
                    start.setVisibility(View.VISIBLE);
                } else {
                    start.setVisibility(View.GONE);
                }
                break;
            }
        }
    }

    /**
     * 重置point背景图片
     */
    private void resetImg() {
        for (int i = 0; i < pointIds.length; i++) {
            iv_point[i].setImageResource(R.mipmap.yindao_icon_default);
        }
    }
}
