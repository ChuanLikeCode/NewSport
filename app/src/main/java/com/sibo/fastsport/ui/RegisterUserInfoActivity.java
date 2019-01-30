package com.sibo.fastsport.ui;

import android.os.Bundle;

import com.sibo.fastsport.R;
import com.sibo.fastsport.base.BaseActivity;
import com.sibo.fastsport.utils.StatusBarUtil;

public class RegisterUserInfoActivity extends BaseActivity {

    @Override
    protected void findViewByIDS() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.StatusBarDarkMode(this, StatusBarUtil.StatusBarLightMode(this));
        setContentView(R.layout.activity_register_user_info);
    }
}
