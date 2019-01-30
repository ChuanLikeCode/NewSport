package com.sibo.fastsport.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sibo.fastsport.R;
import com.sibo.fastsport.adapter.MakePlanAdapter;
import com.sibo.fastsport.domain.SportName;
import com.sibo.fastsport.utils.MakePlanUtils;
import com.sibo.fastsport.utils.MyPlanClickUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/28.
 */
public class BaseDay extends BaseFragment {
    public static boolean select = false;
    public RelativeLayout warmUpView, stretchingView, mainActionView, relaxActionView;
    public TextView tv_warmUp, tv_stretching, tv_mainAction, tv_relaxAction, tips;
    public ImageView warmUpAdd, stretchingAdd, mainActionAdd, relaxActionAdd;
    public ListView warmUpListView, stretchingListView, mainActionListView, relaxActionListView;
    public List<SportName> warmUpList = new ArrayList<>();
    public List<SportName> stretchingList = new ArrayList<>();
    public List<SportName> mainActionList = new ArrayList<>();
    public List<SportName> relaxActionList = new ArrayList<>();
    //创建适配器
    public MakePlanAdapter warmUpAdapter;
    public MakePlanAdapter stretchingAdapter;
    public MakePlanAdapter mainActionAdapter;
    public MakePlanAdapter relaxActionAdapter;
    private View view;//主界面布局

    @Override
    protected void initData() {
        tv_warmUp.setText(R.string.warm_up);
        tv_stretching.setText(R.string.stretching);
        tv_mainAction.setText(R.string.main_action);
        tv_relaxAction.setText(R.string.relax_action);
        warmUpView.setVisibility(View.GONE);
        stretchingView.setVisibility(View.GONE);
        mainActionView.setVisibility(View.GONE);
        relaxActionView.setVisibility(View.GONE);
        warmUpAdapter = new MakePlanAdapter(getActivity(), warmUpList);
        stretchingAdapter = new MakePlanAdapter(getActivity(), stretchingList);
        mainActionAdapter = new MakePlanAdapter(getActivity(), mainActionList);
        relaxActionAdapter = new MakePlanAdapter(getActivity(), relaxActionList);
        warmUpListView.setAdapter(warmUpAdapter);
        stretchingListView.setAdapter(stretchingAdapter);
        mainActionListView.setAdapter(mainActionAdapter);
        relaxActionListView.setAdapter(relaxActionAdapter);
    }

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container) {

        view = inflater.inflate(R.layout.base_day, container, false);
        findById();
        initListener();
        //Log.e("initView","initView");
        return view;
    }

    /**
     * 设置监听事件，调用MakePlanUtils工具类里面定义的事件
     */
    private void initListener() {
        warmUpAdd.setOnClickListener(MakePlanUtils.warmUpAddListener);
        stretchingAdd.setOnClickListener(MakePlanUtils.stretchingAddListener);
        mainActionAdd.setOnClickListener(MakePlanUtils.mainActionAddListener);
        relaxActionAdd.setOnClickListener(MakePlanUtils.relaxActionAddListener);
        if (select) {
            warmUpListView.setOnItemClickListener(MyPlanClickUtils.warmUp);
            stretchingListView.setOnItemClickListener(MyPlanClickUtils.Strething);
            mainActionListView.setOnItemClickListener(MyPlanClickUtils.mainAction);
            relaxActionListView.setOnItemClickListener(MyPlanClickUtils.relaxAction);
        }

    }

    private void findById() {

        warmUpView = (RelativeLayout) view.findViewById(R.id.base_day_rl_warmUp);
        stretchingView = (RelativeLayout) view.findViewById(R.id.base_day_rl_stretching);
        mainActionView = (RelativeLayout) view.findViewById(R.id.base_day_rl_mainAction);
        relaxActionView = (RelativeLayout) view.findViewById(R.id.base_day_rl_relaxAction);
        tips = (TextView) view.findViewById(R.id.makePlan_tip);

        tv_warmUp = (TextView) view.findViewById(R.id.makePlan_tv_warmUp);
        warmUpAdd = (ImageView) view.findViewById(R.id.makePlan_iv_warmUpAdd);
        warmUpListView = (ListView) view.findViewById(R.id.makePlan_listView_warmUp);

        tv_stretching = (TextView) view.findViewById(R.id.makePlan_tv_stretching);
        stretchingAdd = (ImageView) view.findViewById(R.id.makePlan_iv_stretchingAdd);
        stretchingListView = (ListView) view.findViewById(R.id.makePlan_listView_stretching);

        tv_mainAction = (TextView) view.findViewById(R.id.makePlan_tv_mainAction);
        mainActionAdd = (ImageView) view.findViewById(R.id.makePlan_iv_mainActionAdd);
        mainActionListView = (ListView) view.findViewById(R.id.makePlan_listView_mainAction);

        tv_relaxAction = (TextView) view.findViewById(R.id.makePlan_tv_relaxAction);
        relaxActionAdd = (ImageView) view.findViewById(R.id.makePlan_iv_relaxActionAdd);
        relaxActionListView = (ListView) view.findViewById(R.id.makePlan_listView_relaxAction);

    }

}
