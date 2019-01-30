package com.sibo.fastsport.ui;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.dalong.library.view.LoopRotarySwitchView;
import com.sibo.fastsport.R;
import com.sibo.fastsport.adapter.MyHomeLabelAdapter;
import com.sibo.fastsport.base.BaseActivity;
import com.sibo.fastsport.model.UserInfo;
import com.sibo.fastsport.utils.ImageLoaderUtils;
import com.sibo.fastsport.utils.LogUtils;
import com.sibo.fastsport.utils.MyBombUtils;
import com.sibo.fastsport.view.DragScaleImageView;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2016/7/26 0026.
 */
public class TeaAndStdDetailActivity extends BaseActivity {
    public int preHeight;//拉伸之前的高度
    public int width;//设置旋转图片的宽度
    private DragScaleImageView mDragScaleImageView;//可拉伸放大的背景图片
    private ImageView change;//修改个人信息
    private ImageView head, sex;//头像 性别
    private ImageView[] level = new ImageView[5];
    private int[] levelIds = {R.id.x1, R.id.x2, R.id.x3, R.id.x4, R.id.x5};
    private TextView name, phone, age, height, weight, jiaoling;//昵称 电话 年龄 身高 体重 教龄
    private RecyclerView recyclerView;//擅长领域
    private MyHomeLabelAdapter adapter;
    private List<String> list_label = new ArrayList<>();
    private LoopRotarySwitchView mLoopRotarySwitchView;//旋转图片
    private MyBombUtils bombUtils;
    private TextView goodAtTip, xiangqingTip;

    public int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    @Override
    protected void findViewByIDS() {
        head = findViewsById(R.id.activity_mylhome_iv_touxiang);
        name = findViewsById(R.id.activity_personalhome_tv_name);
        sex = findViewsById(R.id.activity_personalhome_iv_man);
        for (int i = 0; i < levelIds.length; i++) {
            level[i] = findViewsById(levelIds[i]);
        }
        phone = findViewsById(R.id.textView2);
        age = findViewsById(R.id.activity_personalhome_tv_age);
        height = findViewsById(R.id.activity_personalhome_tv_tall);
        weight = findViewsById(R.id.activity_personalhome_tv_weight);
        jiaoling = findViewsById(R.id.activity_personalhome_tv_teachyear);
        goodAtTip = findViewsById(R.id.goodAt_tip);
        xiangqingTip = findViewsById(R.id.xiangqing_tip);

        change = (ImageView) findViewById(R.id.myhome_iv_xiugai);
        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        mLoopRotarySwitchView = (LoopRotarySwitchView) findViewById(R.id.activity_myhome_loopView);
        mDragScaleImageView = (DragScaleImageView) findViewById(R.id.rl_head);
        initDragScaleImageView();
    }

    private void setTransparentBar() {
        //透明状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            // Translucent status bar
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTransparentBar();
        setContentView(R.layout.activity_myhome);
        initLabel();
        initLinstener();
    }

    /**
     * 初始化个人信息
     */
    @Override
    protected void onResume() {
        super.onResume();

        Bundle bundle = getIntent().getExtras();
        UserInfo info = (UserInfo) bundle.getSerializable("st");
        LogUtils.e(info.getNikeName());
        ImageLoaderUtils.initImage(this, info.getHead().getFileUrl(), head, R.mipmap.logo);
        name.setText(info.getNikeName());
        if (info.getSex().equals("男")) {
            sex.setImageResource(R.mipmap.man);
        } else {
            sex.setImageResource(R.mipmap.girl);
        }
        phone.setText(info.getAccount());
        for (int i = 0; i < info.getLevel(); i++) {
            level[i].setImageResource(R.mipmap.quanxing);
        }
        age.setText(info.getAge());
        height.setText(info.getHeight());
        weight.setText(info.getWeight());
        jiaoling.setText(info.getJiaoling());
        if (info.getGoodAt() != null && info.getGoodAt().size() != 0) {
            list_label.clear();
            list_label.addAll(info.getGoodAt());
            adapter.setList(list_label);
            goodAtTip.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
        if (info.getImg() != null && info.getImg().size() != 0) {

            for (int i = 0; i < info.getImg().size(); i++) {
                View view = LayoutInflater.from(this).inflate(R.layout.myhome_pictureshow, null);
                ImageView image = (ImageView) view.findViewById(R.id.image);
                ImageLoaderUtils.initImage(this, info.getImg().get(i).getFileUrl(), image, R.mipmap.logo);
                mLoopRotarySwitchView.addView(view);
            }
            initLoopRotarySwitchView();
            mLoopRotarySwitchView.setVisibility(View.VISIBLE);
            xiangqingTip.setVisibility(View.GONE);
        }
    }

    /**
     * 初始化拉伸图片
     */
    public void initDragScaleImageView() {
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        int width = metric.widthPixels;     // 屏幕宽度（像素）
        int height = metric.heightPixels;   // 屏幕高度（像素）
        preHeight = dip2px(this, 223);
        mDragScaleImageView.setImageWidthAndHeight(width, preHeight);
    }

    /**
     * 初始化循环图片
     */
    private void initLoopRotarySwitchView() {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(dm);
        width = dm.widthPixels;
        mLoopRotarySwitchView
                .setR(width / 3)//设置R的大小
                .setAutoRotation(true)//是否自动切换
                .setAutoRotationTime(2000);//自动切换的时间  单位毫秒
    }

    /**
     * 初始化擅长领域
     */
    private void initLabel() {
        change.setVisibility(View.GONE);
        bombUtils = new MyBombUtils(this);
        adapter = new MyHomeLabelAdapter(this, list_label);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }


    /**
     * 图片旋转的
     */
    private void initLinstener() {
//        change.setOnClickListener(this);
//        /**
//         * 选中回调
//         */
//        mLoopRotarySwitchView.setOnItemSelectedListener(new OnItemSelectedListener() {
//            @Override
//            public void selected(int position, View view) {
//
//            }
//        });
//
//        /**
//         * 触摸回调
//         */
//        mLoopRotarySwitchView.setOnLoopViewTouchListener(new OnLoopViewTouchListener() {
//            @Override
//            public void onTouch(MotionEvent event) {
//            }
//        });
//        /**
//         * 点击事件
//         */
//        mLoopRotarySwitchView.setOnItemClickListener(new OnItemClickListener() {
//            @Override
//            public void onItemClick(int item, View view) {
//
//
//            }
//        });
    }
}
