package com.sibo.fastsport.fragment;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.sibo.fastsport.R;
import com.sibo.fastsport.application.MyApplication;
import com.sibo.fastsport.domain.Pickers;
import com.sibo.fastsport.model.UserInfo;
import com.sibo.fastsport.view.PickerScrollView;

import java.util.List;

/**
 * Created by Administrator on 2016/10/24.
 */
public abstract class BaseFragment extends Fragment {
    protected UserInfo loginuser;
    protected PickerScrollView pickers;
    protected Dialog dialogInfo;
    protected ProgressDialog dialog;
    private View rootView;
    private Context context;
    private Boolean hasInitData = false;
    private View scrollViewLayout;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        context = getActivity();
        scrollViewLayout = getActivity().getLayoutInflater().inflate(R.layout.scrollview_select, null);
        pickers = (PickerScrollView) scrollViewLayout.findViewById(R.id.pickers);
        loginuser = MyApplication.getInstance().readLoginUser();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        if (rootView == null) {
            rootView = initView(inflater, container);
        }
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        if (!hasInitData) {
            initData();
            hasInitData = true;
        }
    }

    @Override
    public void onDestroyView() {
        // TODO Auto-generated method stub
        super.onDestroyView();
        ((ViewGroup) rootView.getParent()).removeView(rootView);
    }

    protected abstract void initData();

    protected abstract View initView(LayoutInflater inflater, ViewGroup container);
    /**
     * 封装从网络下载数据
     */
    /**
     * 获取屏幕宽度
     */
    public int getVmWidth() {
        DisplayMetrics metric = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metric);
        return metric.widthPixels; // 屏幕宽度（像素）

    }

    /**
     * 获取屏幕高度
     */
    public int getVmHeight() {
        DisplayMetrics metric = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metric);
        return metric.heightPixels; // 屏幕高度（像素）
    }

    protected void showDialog(List<Pickers> pickerItems) {
        pickers.setData(pickerItems);
        pickers.setSelected(0);
        if (dialogInfo == null) {
            dialogInfo = new Dialog(getActivity());
            Window window = dialogInfo.getWindow();
            window.setGravity(Gravity.BOTTOM);
            window.setWindowAnimations(R.style.main_menu_animstyle);
            window.requestFeature(Window.FEATURE_NO_TITLE);
            window.setBackgroundDrawableResource(android.R.color.transparent);
            window.getDecorView().setPadding(0, 0, 0, 0);
            Display d = getActivity().getWindow().getWindowManager().getDefaultDisplay();
            //Display d = window.getWindowManager().getDefaultDisplay();
            WindowManager.LayoutParams p = window.getAttributes();
            p.width = d.getWidth(); //设置dialog的宽度为当前手机屏幕的宽度
            window.setAttributes(p);
            // 设置点击外围解散
            dialogInfo.setCanceledOnTouchOutside(true);
            dialogInfo.setContentView(scrollViewLayout);
        }
        // 设置显示动画
        dialogInfo.show();

    }
}

