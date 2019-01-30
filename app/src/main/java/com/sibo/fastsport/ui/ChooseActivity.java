package com.sibo.fastsport.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.sibo.fastsport.R;
import com.sibo.fastsport.application.Constant;
import com.sibo.fastsport.application.MyApplication;
import com.sibo.fastsport.base.BaseActivity;
import com.sibo.fastsport.utils.MyBombUtils;

public class ChooseActivity extends BaseActivity implements View.OnClickListener {

    private TextView trainer,student;
    private MyBombUtils bombUtils;

    @Override
    protected void findViewByIDS() {
        trainer = (TextView) findViewById(R.id.choose_FitnessTrainer);
        student = (TextView) findViewById(R.id.choose_student);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //隐藏通知栏


        Log.e("type", loginuser.getType() + "----");
        if (loginuser.getType() != null) {
            startActivity(MainActivity.class);
            finish();
        }
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_choose);
        trainer.setOnClickListener(this);
        student.setOnClickListener(this);
        bombUtils = new MyBombUtils(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(ChooseActivity.this,MainActivity.class);
        switch (v.getId()){
            case R.id.choose_FitnessTrainer:
                intent.putExtra(Constant.USER_TYPE,"1");
                loginuser.setType("1");
                MyApplication.getInstance().saveUserInfo(loginuser);
                bombUtils.updateUserInfo(loginuser);
                break;
            case R.id.choose_student:
                loginuser.setType("2");
                MyApplication.getInstance().saveUserInfo(loginuser);
                bombUtils.updateUserInfo(loginuser);

                break;
        }
        //Log.e("mUser",MyApp.mUser.getType());
        startActivity(intent);
        finish();
    }


}
