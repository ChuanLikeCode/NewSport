package com.sibo.fastsport.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sibo.fastsport.R;
import com.sibo.fastsport.model.UserInfo;
import com.sibo.fastsport.ui.MyCodeActivity;
import com.sibo.fastsport.ui.MyHomeActivity;
import com.sibo.fastsport.ui.NewsActivity;
import com.sibo.fastsport.ui.SettingActivity;
import com.sibo.fastsport.ui.WxCollectedActivity;
import com.sibo.fastsport.utils.ImageLoaderUtils;
import com.sibo.fastsport.utils.MyBombUtils;
import com.sibo.fastsport.view.CircleImageView;

/**
 * Created by Administrator on 2016/10/31.
 */
public class MyHomeMenuFragment extends BaseFragment implements View.OnClickListener {
    private View myhomemenu;
    private RelativeLayout myHome;//我的主页
    private RelativeLayout news;//新闻资讯
    private RelativeLayout collection;//我的收藏
    private RelativeLayout code;//我的二维码
    private ImageView setting, sex;
    private MyBombUtils myBombUtils;
    private CircleImageView head;
    private TextView name, phone, age, height, weight, jiaoling;
    private ImageView[] level = new ImageView[5];
    private int[] level_ids = {R.id.level1, R.id.level2, R.id.level3, R.id.level4, R.id.level5};
    @Override
    protected void initData() {
        myBombUtils = new MyBombUtils(getActivity());
        setInfo(loginuser);
    }

    public void setInfo(UserInfo info) {
        Log.e("setInfo", info.getNikeName());
        name.setText(info.getNikeName());
        phone.setText(info.getAccount());
        age.setText(info.getAge());
        height.setText(info.getHeight());
        weight.setText(info.getWeight());
        jiaoling.setText(info.getJiaoling());
        ImageLoaderUtils.initImage(getActivity(), info.getHead().getFileUrl(), head, R.mipmap.loading);
        if (info.getSex().equals("男")) {
            sex.setImageResource(R.mipmap.man);
        } else {
            sex.setImageResource(R.mipmap.girl);
        }
        for (int i = 0; i < info.getLevel(); i++) {
            level[i].setImageResource(R.mipmap.quanxing);
        }
    }

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container) {
        myhomemenu = inflater.inflate(R.layout.fragment_myhomemenu, container, false);
        findById();
        initListener();
        return myhomemenu;
    }

    private void initListener() {
        myHome.setOnClickListener(this);
        news.setOnClickListener(this);
        collection.setOnClickListener(this);
        setting.setOnClickListener(this);
        code.setOnClickListener(this);
    }

    private void findById() {
        for (int i = 0; i < level_ids.length; i++) {
            level[i] = (ImageView) myhomemenu.findViewById(level_ids[i]);
        }
        name = (TextView) myhomemenu.findViewById(R.id.myhomemenu_tv_name);
        phone = (TextView) myhomemenu.findViewById(R.id.myhomemenu_tv_phonenumber);
        age = (TextView) myhomemenu.findViewById(R.id.myhomemenu_tv_age);
        height = (TextView) myhomemenu.findViewById(R.id.myhomemenu_tv_tall);
        weight = (TextView) myhomemenu.findViewById(R.id.myhomemenu_tv_weight);
        jiaoling = (TextView) myhomemenu.findViewById(R.id.myhomemenu_tv_teachyear);
        head = (CircleImageView) myhomemenu.findViewById(R.id.myhomemenu_iv_touxiang);

        setting = (ImageView) myhomemenu.findViewById(R.id.myhomemenu_iv_setting);
        myHome = (RelativeLayout) myhomemenu.findViewById(R.id.myhomemenu_rl_myhome);
        news = (RelativeLayout) myhomemenu.findViewById(R.id.myhomemenu_rl_news);
        collection = (RelativeLayout) myhomemenu.findViewById(R.id.myhomemenu_rl_shoucang);
        sex = (ImageView) myhomemenu.findViewById(R.id.myhomemenu_iv_man);
        code = (RelativeLayout) myhomemenu.findViewById(R.id.myhomemenu_rl_code);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.myhomemenu_rl_news:
                startActivity(new Intent(getActivity(), NewsActivity.class));
                break;
            case R.id.myhomemenu_rl_myhome:
                startActivity(new Intent(getActivity(), MyHomeActivity.class));
                break;
            case R.id.myhomemenu_rl_shoucang:
                startActivity(new Intent(getActivity(), WxCollectedActivity.class));
                break;
            case R.id.myhomemenu_iv_setting:
                Intent intent = new Intent(getActivity(), SettingActivity.class);
                startActivityForResult(intent, 456);
                break;
            case R.id.myhomemenu_rl_code:
                startActivity(new Intent(getActivity(), MyCodeActivity.class));
                break;
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 456) {
            if (data != null) {
                Bundle bundle = data.getExtras();
                UserInfo info = (UserInfo) bundle.getSerializable("save");
                setInfo(info);
            }

        }
    }
}
