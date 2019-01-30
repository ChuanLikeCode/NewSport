package com.sibo.fastsport.ui;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.sibo.fastsport.R;
import com.sibo.fastsport.base.BaseActivity;
import com.sibo.fastsport.model.UserInfo;
import com.sibo.fastsport.utils.MyBombUtils;
import com.sibo.fastsport.utils.StatusBarUtil;

import java.util.regex.Pattern;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

import static com.sibo.fastsport.application.Constant.REGISTER_FAILED;
import static com.sibo.fastsport.application.Constant.REGISTER_SUCCESS;
import static com.sibo.fastsport.application.Constant.SMSDDK_HANDLER;
import static com.sibo.fastsport.application.Constant.SUCCESS;

/**
 *
 * Created by Administrator on 2016/9/21 0021.
 */
public class RegisterActivity extends BaseActivity {
    public UserInfo userInfo = new UserInfo();
    private TextView title;
    private Button btn_register;//注册按钮
    private Button takeIdentify;//获取验证码按钮
    private TextView goLogin;//有账号直接登录
    //private TextView sendIdentify, receiverSecond, remainSecond;
    private EditText phone;
    //输入密码
    private EditText password;
    //输入验证码
    private EditText identify;
    private EventHandler eventHandler;
    private MyBombUtils bmobUtils;
    private String userPhone;//用户注册的手机号
    private String userPassword;//用户密码
    private String code;//用户的验证码
    private int type;
    /**
     * 获取验证码倒计时用处
     */
    private CountDownTimer timer = new CountDownTimer(1000 * 60, 1000) {

        @Override
        public void onTick(long millisUntilFinished) {

            takeIdentify.setText("重新发送 " + (millisUntilFinished / 1000) + "s");
        }

        @Override
        public void onFinish() {
            takeIdentify.setEnabled(true);
            takeIdentify.setText("获取验证码");
        }
    };

    public Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case REGISTER_SUCCESS://发送短信
                    sendSMS();
                    break;
                case REGISTER_FAILED:
                    if (type == 1){
                        sendSMS();
                    }else {
                        Toast.makeText(RegisterActivity.this, "用户名已被注册", Toast.LENGTH_LONG).show();

                    }
                    break;
                case SUCCESS://上传用户信息完成
                    dialog.dismiss();
                    //bmobUtils.updateAccountInfo(account);
                    startActivity(LoginActivity.class);
                    finish();
                    break;
                case SMSDDK_HANDLER://短信发送完成处理信息回调
                    getSMS(msg);
                    break;
            }
        }
    };

    /**
     * 处理收到短信之后
     * @param msg
     */
    private void getSMS(Message msg) {

        int event = msg.arg1;
        int result = msg.arg2;
        Object data = msg.obj;
        //回调完成
        if (result == SMSSDK.RESULT_COMPLETE) {
            //验证码验证成功
            if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                if (type == 1){//找回密码
                    Toast.makeText(RegisterActivity.this, "找回成功", Toast.LENGTH_LONG).show();
                    userPassword = password.getText().toString();
                    userPhone = phone.getText().toString();
                    bmobUtils.getBackYourAccount(userPhone,userPassword);
                    //bmobUtils.findPassword(account);
                }else {
                    Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_LONG).show();
                    userPassword = password.getText().toString();
                    userPhone = phone.getText().toString();
                    userInfo.setAccount(userPhone);
                    userInfo.setPassword(userPassword);
                    bmobUtils.addUserInfo(userInfo);
                }

            }
            //已发送验证码
            else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                dialog.dismiss();
                Toast.makeText(RegisterActivity.this, "验证码已经发送",
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            dialog.dismiss();
            Toast.makeText(RegisterActivity.this, "验证码错误请重新输入！",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void findViewByIDS() {
        goLogin = (TextView) findViewById(R.id.tv_gotoLogin);
        btn_register = (Button) findViewById(R.id.btn_register);
        takeIdentify = (Button) findViewById(R.id.takeIndentify);
        phone = (EditText) findViewById(R.id.register_et_account);
        password = (EditText) findViewById(R.id.register_et_password);
        identify = (EditText) findViewById(R.id.et_identify);
        title = findViewsById(R.id.top_tv_title);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.StatusBarDarkMode(this, StatusBarUtil.StatusBarLightMode(this));
        setContentView(R.layout.activity_register);
        initData();
        initSMS();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterAllEventHandler();
    }

    /**
     * 初始化短信注册回调方法
     * 获得验证码时会调用这个方法
     * 注册时也会调用这个方法
     */
    private void initSMS() {
        eventHandler = new EventHandler() {
            /**
             * EVENT	                                  DATA类型	                          说明
             * EVENT_GET_SUPPORTED_COUNTRIES	ArrayList<HashMap<String,Object>>	返回支持发送验证码的国家列表
             * EVENT_GET_VERIFICATION_CODE	    Boolean	                            true为智能验证，false为普通下发短信
             * EVENT_SUBMIT_VERIFICATION_CODE	HashMap<String,Object>	校验验证码，返回校验的手机和国家代码
             * EVENT_GET_CONTACTS	            ArrayList<HashMap<String,Object>>	获取手机内部的通信录列表
             * EVENT_SUBMIT_USER_INFO	                null	                    提交应用内的用户资料
             * EVENT_GET_FRIENDS_IN_APP	        ArrayList<HashMap<String,Object>>	获取手机通信录在当前应用内的用户列表
             * EVENT_GET_VOICE_VERIFICATION_CODE	    null	                    请求发送语音验证码，无返回
             * afterEvent在操作结束时被触发，同样具备event和data参数，但是data是事件操作结果
             *
             * @param event  事件返回的类型
             * @param result 事件返回的结果
             * @param data   事件执行结束返回的操作结果
             */
            @Override
            public void afterEvent(int event, int result, Object data) {
                Message message = new Message();
                message.arg1 = event;
                message.arg2 = result;
                message.obj = data;
                message.what = SMSDDK_HANDLER;
                handler.sendMessage(message);
            }
        };
        SMSSDK.registerEventHandler(eventHandler);//短信回调注册
    }

    private void initData() {
        title.setText(R.string.Register);
        //complete.setVisibility(View.GONE);
        bmobUtils = new MyBombUtils(this);

        Intent intent = getIntent();
        if (intent != null){
            String str = intent.getStringExtra("type");
            if (!str.equals("")){
                if (str.equals("register")){
                    type = 0;//注册
                    btn_register.setText("注册");
                }else if (str.equals("find")){
                    type = 1;//找回密码
                    btn_register.setText("确定找回");
                    goLogin.setVisibility(View.GONE);
                }
            }
        }
    }
    /**
     * 前往登录
     *
     * @param view
     */
    public void goLogin(View view) {
        Intent intent =new Intent(this,LoginActivity.class);
        startActivity(intent);
    }

    /**
     * 发送手机验证码的处理
     */
    private void prepareSendSMS() {
        userPhone = phone.getText().toString();
        userPassword = password.getText().toString();
        if (!(userPhone.equals("") || userPassword.equals(""))) {
            if (check()) {
                bmobUtils.registerChecked(userPhone);
            } else {
                Toast.makeText(RegisterActivity.this, "用户名不对或者密码只能为数字/字母(6-16位)",
                        Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(RegisterActivity.this, "用户名或者密码不能为空",
                    Toast.LENGTH_SHORT).show();
        }
    }
    /**
     * 验证手机号与密码（6-16位数字字母）
     *
     * @return
     */
    public boolean check() {
        //验证手机号码与密码（6-16数字字母）
        String regPhone = "^1[3|4|5|7|8][0-9]\\d{8}$";
        String regPassword = "^[0-9a-zA-z]{6,16}";
        boolean phoneBoolean = Pattern.matches(regPhone, userPhone);
        boolean pswBoolean = Pattern.matches(regPassword, userPassword);
        if (phoneBoolean && pswBoolean) {
            return true;
        }
        return false;
    }

    /**
     * 点击注册
     *
     * @param view
     */
    public void goRegister(View view) {
        if (type == 0){
            dialog = ProgressDialog.show(this, null, "正在注册...");
        }else {
            dialog = ProgressDialog.show(this, null, "正在找回...");
        }
        code = identify.getText().toString();
        userPhone = phone.getText().toString();
        userPassword = password.getText().toString();
        if (!(userPhone.equals("") || userPassword.equals(""))) {
            if (!code.equals("")) {
                SMSSDK.submitVerificationCode("86", userPhone, code);
            } else {
                dialog.dismiss();
                Toast.makeText(RegisterActivity.this, "验证码不能为空",
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            dialog.dismiss();
            Toast.makeText(RegisterActivity.this, "用户名或者密码不能为空",
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 获取验证码
     *
     * @param view
     */

    public void getTestCode(View view) {
        prepareSendSMS();
    }
    /**
     * 发送短信
     */
    private void sendSMS() {
        SMSSDK.getVerificationCode("86", userPhone);
        takeIdentify.setEnabled(false);
        timer.start();
        dialog = ProgressDialog.show(this, null, "正在发送...");
    }
}
