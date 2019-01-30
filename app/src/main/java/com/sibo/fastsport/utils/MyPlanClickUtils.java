package com.sibo.fastsport.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.sibo.fastsport.domain.SportDetail;
import com.sibo.fastsport.domain.SportName;
import com.sibo.fastsport.fragment.BaseDay;
import com.sibo.fastsport.ui.ActionDetailsActivity;

import java.util.List;

/**
 * Created by chuan on 2017/3/25.
 */

public class MyPlanClickUtils {
    public static int dayId;//标记这是第几天
    private static Context context;
    private static List<BaseDay> list_day;
    /**
     * 点击热身运动
     */
    public static AdapterView.OnItemClickListener warmUp = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(context, ActionDetailsActivity.class);
            SportName sportName = list_day.get(dayId).warmUpList.get(position);
            SportDetail sportDetail = null;
            //筛选出需要传递的SportDetail
            for (SportDetail sd :
                    MyBombUtils.list_sportDetail) {
                if (sd.getName().equals(sportName.getName())) {
                    sportDetail = sd;
                    break;
                }
            }
            //传递数据
            Bundle bundle = new Bundle();
            bundle.putSerializable("sportName", sportName);
            bundle.putSerializable("sportDetail", sportDetail);
            intent.putExtra("details", bundle);
            context.startActivity(intent);
        }
    };
    /**
     * 点击热身运动
     */
    public static AdapterView.OnItemClickListener Strething = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(context, ActionDetailsActivity.class);
            SportName sportName = list_day.get(dayId).stretchingList.get(position);
            SportDetail sportDetail = null;
            //筛选出需要传递的SportDetail
            for (SportDetail sd :
                    MyBombUtils.list_sportDetail) {
                if (sd.getName().equals(sportName.getName())) {
                    sportDetail = sd;
                    break;
                }
            }
            //传递数据
            Bundle bundle = new Bundle();
            bundle.putSerializable("sportName", sportName);
            bundle.putSerializable("sportDetail", sportDetail);
            intent.putExtra("details", bundle);
            context.startActivity(intent);
        }
    };
    /**
     * 点击热身运动
     */
    public static AdapterView.OnItemClickListener mainAction = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(context, ActionDetailsActivity.class);
            SportName sportName = list_day.get(dayId).mainActionList.get(position);
            SportDetail sportDetail = null;
            //筛选出需要传递的SportDetail
            for (SportDetail sd :
                    MyBombUtils.list_sportDetail) {
                if (sd.getName().equals(sportName.getName())) {
                    sportDetail = sd;
                    break;
                }
            }
            //传递数据
            Bundle bundle = new Bundle();
            bundle.putSerializable("sportName", sportName);
            bundle.putSerializable("sportDetail", sportDetail);
            intent.putExtra("details", bundle);
            context.startActivity(intent);
        }
    };
    /**
     * 点击热身运动
     */
    public static AdapterView.OnItemClickListener relaxAction = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(context, ActionDetailsActivity.class);
            SportName sportName = list_day.get(dayId).relaxActionList.get(position);
            SportDetail sportDetail = null;
            //筛选出需要传递的SportDetail
            for (SportDetail sd :
                    MyBombUtils.list_sportDetail) {
                if (sd.getName().equals(sportName.getName())) {
                    sportDetail = sd;
                    break;
                }
            }
            //传递数据
            Bundle bundle = new Bundle();
            bundle.putSerializable("sportName", sportName);
            bundle.putSerializable("sportDetail", sportDetail);
            intent.putExtra("details", bundle);
            context.startActivity(intent);
        }
    };

    public MyPlanClickUtils(Context cont, List<BaseDay> list) {
        context = cont;
        list_day = list;
    }

}
