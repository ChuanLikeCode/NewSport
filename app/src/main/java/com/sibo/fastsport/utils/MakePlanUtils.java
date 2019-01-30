package com.sibo.fastsport.utils;

import android.content.Context;
import android.content.Intent;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.sibo.fastsport.adapter.MakePlanAdapter;
import com.sibo.fastsport.domain.SportName;
import com.sibo.fastsport.fragment.BaseDay;
import com.sibo.fastsport.model.DayPlan;
import com.sibo.fastsport.ui.ChooseActionActivity;

import java.util.ArrayList;
import java.util.List;


/**
 * MakePlanActivity界面的工具类
 * Created by Administrator on 2016/11/12.
 */
public class MakePlanUtils {
    public static int dayId;//设置当前是第几天
    public static int preDayId = -1;//
    //设置当前是选择哪一个类型的健身动作 热身--1、拉伸--2、具体--3、放松--4
    public static int typeId;
    public static boolean isFirst = true;//是否第一次执行这个界面
    public static Context context;
    //选择健身动作集合
    public static List<SportName> list = new ArrayList<>();
    //收集第一到第七天的热身、拉伸、具体、放松动作的集合
    public static SparseArray<List<SportName>> sp_warmUp = new SparseArray<>();
    public static SparseArray<List<SportName>> sp_stretching = new SparseArray<>();
    public static SparseArray<List<SportName>> sp_mainAction = new SparseArray<>();
    public static SparseArray<List<SportName>> sp_relaxAction = new SparseArray<>();

    /**
     * 添加具体动作的监听事件
     */
    public static View.OnClickListener mainActionAddListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, ChooseActionActivity.class);
            typeId = 3;
            context.startActivity(intent);
        }
    };
    /**
     * 添加放松动作的监听事件
     */
    public static View.OnClickListener relaxActionAddListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, ChooseActionActivity.class);
            typeId = 4;
            context.startActivity(intent);
        }
    };
    /**
     * 添加拉伸健身动作的监听事件
     */
    public static View.OnClickListener stretchingAddListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, ChooseActionActivity.class);
            typeId = 2;
            context.startActivity(intent);
        }
    };
    /**
     * 添加热身动作的监听事件
     */
    public static View.OnClickListener warmUpAddListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, ChooseActionActivity.class);
            typeId = 1;
            context.startActivity(intent);
        }
    };
    //第一到第七天的Fragment
    public static List<BaseDay> list_day = new ArrayList<>();
    public MakePlanUtils(Context context, List<BaseDay> list_day) {
        MakePlanUtils.context = context;
        MakePlanUtils.list_day = list_day;
    }

    /**
     * 设置返回的健身动作，显示在listView上面
     */
    public void getResult() {
        // Log.e("listSize", list.size() + "");

        switch (typeId) {
            case 1:
                list_day.get(dayId).warmUpList.clear();
                list_day.get(dayId).warmUpList.addAll(list);
                setListViewHeight(list_day.get(dayId).warmUpListView);
                list_day.get(dayId).warmUpAdapter.notifyDataSetChanged();
                list_day.get(dayId).warmUpListView.setVisibility(View.VISIBLE);
                if (preDayId == dayId){
                    sp_warmUp.clear();
                }
                sp_warmUp.put(dayId, list);//收集天数  动作

                //setDayTrueOrFalse();
                ///CollectPlan.typeDayNamePlan.put(typeId,sp_warmUp);//收集健身计划类型 天 动作
                break;
            case 2:
                list_day.get(dayId).stretchingList.clear();
                list_day.get(dayId).stretchingList.addAll(list);
                setListViewHeight(list_day.get(dayId).stretchingListView);
                list_day.get(dayId).stretchingAdapter.notifyDataSetChanged();
                list_day.get(dayId).stretchingListView.setVisibility(View.VISIBLE);
                if (preDayId == dayId){
                    sp_stretching.clear();
                }
                sp_stretching.put(dayId, list);
                //setDayTrueOrFalse();
                //CollectPlan.typeDayNamePlan.put(typeId,sp_stretching);
                break;
            case 3:
                list_day.get(dayId).mainActionList.clear();
                list_day.get(dayId).mainActionList.addAll(list);
                setListViewHeight(list_day.get(dayId).mainActionListView);
                list_day.get(dayId).mainActionAdapter.notifyDataSetChanged();
                list_day.get(dayId).mainActionListView.setVisibility(View.VISIBLE);
                if (preDayId == dayId){
                    sp_mainAction.clear();
                }
                sp_mainAction.put(dayId, list);
                //etDayTrueOrFalse();
                //CollectPlan.typeDayNamePlan.put(typeId,sp_mainAction);
                break;
            case 4:
                list_day.get(dayId).relaxActionList.clear();
                list_day.get(dayId).relaxActionList.addAll(list);
                setListViewHeight(list_day.get(dayId).relaxActionListView);
                list_day.get(dayId).relaxActionAdapter.notifyDataSetChanged();
                list_day.get(dayId).relaxActionListView.setVisibility(View.VISIBLE);
                if (preDayId == dayId){
                    sp_relaxAction.clear();
                }
                sp_relaxAction.put(dayId, list);
                //setDayTrueOrFalse();
                //CollectPlan.typeDayNamePlan.put(typeId,sp_relaxAction);
                break;
        }
        setDayTrueOrFalse();
    }

    /**
     * 由于ScrollView中嵌套ListView显示的时候，高度只显示一行数据的高度
     * 所以应该计算ListView的原本高度，使用getCount()*item.getMeasuredHeight()
     * 计算高度，重新设置ListView的高度
     *
     * @param listView
     */
    public void setListViewHeight(ListView listView) {
        // 获取ListView对应的Adapter
        MakePlanAdapter adapter = (MakePlanAdapter) listView.getAdapter();
        if (adapter == null) {
            return;
        }
        int totalHeight = 0;
        int len = adapter.getCount();
        View item = adapter.getView(0, null, listView);
        item.measure(0, 0);
        totalHeight = item.getMeasuredHeight() * len;
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight;
        listView.setLayoutParams(params);
    }

    /**
     * 收集判断每一天有没有健身计划
     */
    public void setDayTrueOrFalse(){
        //CollectPlan.dayPlan.get(dayId).setId(CollectPlan.id);
        //CollectPlan.dayPlan.get(dayId).setDayId(dayId);
        setType(CollectPlan.dayPlan.get(dayId));
    }

    /**
     * 判断健身类型
     */
    private void setType(DayPlan dayPlan) {
        switch (typeId){
            case 1:
                dayPlan.setWarmUp(true);
                break;
            case 2:
                dayPlan.setStretching(true);
                break;
            case 3:
                dayPlan.setMainAction(true);
                break;
            case 4:
                dayPlan.setRelaxAction(true);
                break;
        }
    }

}
