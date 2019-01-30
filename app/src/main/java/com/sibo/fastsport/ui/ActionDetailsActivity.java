package com.sibo.fastsport.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.sibo.fastsport.R;
import com.sibo.fastsport.base.BaseActivity;
import com.sibo.fastsport.domain.SportDetail;
import com.sibo.fastsport.domain.SportName;
import com.sibo.fastsport.utils.StatusBarUtil;

public class ActionDetailsActivity extends BaseActivity {

    //顶部标题栏控件
    private Toolbar toolbar;
    private TextView title, wancheng;
    private ImageView img, back, close;
    //锻炼部位、使用器械、动作详解
    private TextView part, equipment, details;
    //难度星级
    private ImageView[] level = new ImageView[5];
    private int[] levelId = {R.id.action_detail_iv_level1, R.id.action_detail_iv_level2,
            R.id.action_detail_iv_level3, R.id.action_detail_iv_level4,
            R.id.action_detail_iv_level5};

    @Override
    protected void findViewByIDS() {
        toolbar = (Toolbar) findViewById(R.id.action_detail_toolbar);
        back = (ImageView) toolbar.findViewById(R.id.iv_back_titlebar);
        close = (ImageView) toolbar.findViewById(R.id.iv_close_titlebar);
        wancheng = (TextView) toolbar.findViewById(R.id.tv_complete_titlebar);
        title = (TextView) toolbar.findViewById(R.id.tv_title_bar);
        img = (ImageView) findViewById(R.id.action_detail_img);
        part = (TextView) findViewById(R.id.action_detail_part);
        equipment = (TextView) findViewById(R.id.action_detail_equipment);
        details = (TextView) findViewById(R.id.action_detail_details);
        for (int i = 0; i < level.length; i++) {
            level[i] = (ImageView) findViewById(levelId[i]);
            level[i].setVisibility(View.GONE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.StatusBarDarkMode(this, StatusBarUtil.StatusBarLightMode(this));
        setContentView(R.layout.activity_action_details);
        initData();
        initListener();
    }

    /**
     * 监听事件初始化
     */
    private void initListener() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * 初始化数据
     */
    private void initData() {
        title.setText(R.string.actionDetails);
        wancheng.setVisibility(View.GONE);
        close.setVisibility(View.GONE);
        setDetails();
    }

    /**
     * 将传递过来的数据提取显示在界面上
     */
    private void setDetails() {
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("details");
        SportName sn = (SportName) bundle.getSerializable("sportName");
        SportDetail sd = (SportDetail) bundle.getSerializable("sportDetail");
        Glide.with(this).load(sn.getIcon().getFileUrl())
                .asGif().diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.mipmap.loading)
                .centerCrop()
                .error(R.drawable.failed).into(img);
        part.setText(sd.getExercise_part());
        equipment.setText(sd.getNeed_equipment());
        for (int j = 0; j < sn.getLevel(); j++) {
            level[j].setVisibility(View.VISIBLE);
        }
        details.setText(sd.getDetail());
    }
}
