package com.sibo.fastsport.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.sibo.fastsport.R;
import com.sibo.fastsport.base.BaseActivity;
import com.sibo.fastsport.utils.MakePlanUtils;
import com.sibo.fastsport.utils.StatusBarUtil;

public class BodyjudgmentActivity extends BaseActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    private RelativeLayout bodyLable, bodyLabelByYourself;
    private Switch selectBody_switch;
    private Toolbar head;
    private TextView title, zhiding;
    private ImageView back, close;
    private TextView[] bodyLabels = new TextView[8];
    private int[] labels = {R.id.tv_bodyLabel1, R.id.tv_bodyLabel2,
            R.id.tv_bodyLabel3, R.id.tv_bodyLabel4,
            R.id.tv_bodyLabel5, R.id.tv_bodyLabel6,
            R.id.tv_bodyLabel7, R.id.tv_bodyLabel8};

    @Override
    protected void findViewByIDS() {
        head = (Toolbar) findViewById(R.id.bodyTitle);
        title = (TextView) head.findViewById(R.id.tv_title_bar);
        back = (ImageView) head.findViewById(R.id.iv_back_titlebar);
        close = (ImageView) head.findViewById(R.id.iv_close_titlebar);

        zhiding = (TextView) head.findViewById(R.id.tv_complete_titlebar);
        selectBody_switch = (Switch) findViewById(R.id.selectBody_switch);
        bodyLabelByYourself = (RelativeLayout) findViewById(R.id.rl_bodyLabelByYourself);
        bodyLable = (RelativeLayout) findViewById(R.id.rl_bodyLabel);

        for (int i = 0; i < labels.length; i++) {
            bodyLabels[i] = (TextView) findViewById(labels[i]);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.StatusBarDarkMode(this, StatusBarUtil.StatusBarLightMode(this));
        setContentView(R.layout.activity_bodyjudgment);
        initData();
        initListener();

    }

    @Override
    protected void onResume() {
        super.onResume();
        MakePlanUtils.isFirst = true;//修改选择动作界面为第一次执行
        MakePlanUtils.sp_relaxAction.clear();
        MakePlanUtils.sp_warmUp.clear();
        MakePlanUtils.sp_mainAction.clear();
        MakePlanUtils.sp_stretching.clear();
    }

    private void initData() {
        title.setText(R.string.judgeBody);
        close.setVisibility(View.GONE);
        zhiding.setText(R.string.zhiding);

    }

    private void initListener() {
        selectBody_switch.setOnCheckedChangeListener(this);
        back.setOnClickListener(this);
        zhiding.setOnClickListener(this);
        for (int i = 0; i < labels.length; i++) {
            bodyLabels[i].setOnClickListener(this);
        }
    }


    @Override
    public void onClick(View v) {
        for (int i = 0; i < labels.length; i++) {
            if (v.getId() == labels[i]) {
                bodyLabels[i].setBackgroundResource(R.drawable.bodylabel_selected_bg);
                bodyLabels[i].setTextColor(getResources().getColor(R.color.white));
            } else {
                bodyLabels[i].setBackgroundResource(R.drawable.bodylabel_nomal_bg);
                bodyLabels[i].setTextColor(getResources().getColor(R.color.prompt_gray));
            }
        }
        switch (v.getId()) {
            case R.id.tv_complete_titlebar:
                startActivity(new Intent(BodyjudgmentActivity.this, MakePlanActivity.class));
                break;
            case R.id.iv_back_titlebar:
                finish();
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {

            bodyLable.startAnimation(StartAnimation(0, 1.0f));
            bodyLabelByYourself.startAnimation(StartAnimation(0, 1.0f));

        } else {
            bodyLable.startAnimation(StartAnimation(1.0f, 0));
            bodyLabelByYourself.startAnimation(StartAnimation(1.0f, 0));
        }
    }

    private Animation StartAnimation(float x, float y) {
        AlphaAnimation animation = new AlphaAnimation(x, y);
        animation.setFillAfter(true);
        animation.setDuration(500);
        return animation;
    }
}
