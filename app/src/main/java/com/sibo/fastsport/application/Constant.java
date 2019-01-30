package com.sibo.fastsport.application;

/**
 * Created by Administrator on 2016/11/20.
 */
public class Constant {

    //4dccdd3999204da7cc4df5a06906d8a0
    public static final String AppID = "wx970434db9a75a8b4";
    public static final String AppSecret = "8e146e7798ae7a445731c9944d406480";
    public static final String getAccessToken = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + AppID + "&secret=" + AppSecret;
    public static final String getMaterial = "https://api.weixin.qq.com/cgi-bin/material/batchget_material?access_token=";
    public final static int SUCCESS = 0;
    public final static int FAILED = 88;
    public final static int SHOW = 3;
    public final static int NO_MORE = 1;
    public static final boolean Debug = true;
    public static String PATH = "config";
    public static String USERACCOUNTCOOKIE = "account";
    public static String USERPASSWORDCOOKIE = "password";
    public static String ISFIRSTSTART = "isFirst";
    public static String ISLOGIN = "isLogin";
    public static String ACCESSTOKEN = "access_token";
    public static String PLANOBJECTID ="plan";
    public static String USER_TYPE ="plan";
    public static final int RESULT_SUCCESS = 666;
    public static final int RESULT_ERROR = 999;
    public static final int CODE_REPEAT = 333;  //重新发送
    public static final int SMSDDK_HANDLER = 23;  //短信回调
    public static final int REGISTER_SUCCESS = 200;  //用户名不存在
    public static final int REGISTER_FAILED = 404;  //用户名已存在
    public static final int UPLOAD_SUCCESS = 520;  //用户名已存在
}
