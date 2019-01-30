package com.sibo.fastsport.utils;

import android.content.Context;
import android.util.SparseArray;

import com.sibo.fastsport.domain.SportName;
import com.sibo.fastsport.model.DayPlan;
import com.sibo.fastsport.model.MainAction;
import com.sibo.fastsport.model.RelaxAction;
import com.sibo.fastsport.model.Stretching;
import com.sibo.fastsport.model.UserSportPlan;
import com.sibo.fastsport.model.WarmUp;

import java.util.ArrayList;
import java.util.List;

/**
 * 收集健身计划
 * Created by chuan on 2017/2/23.
 */

public class CollectPlan {

    //public static List<UserSportPlan> userSportPlan = new ArrayList<>();//教练的健身计划 名字 教练
    public static List<Integer> dayId = new ArrayList<>();//选择天数的集合
    public static String id;
    public static UserSportPlan userSportPlan = new UserSportPlan();//教练的健身计划 名字 教练
    public static List<DayPlan> dayPlan = new ArrayList<>();//计划的具体内容 第几天的计划 动作类型的有无
    public static List<WarmUp> warmUps = new ArrayList<>();//热身动作
    public static List<Stretching> stretchings = new ArrayList<>();//拉伸动作
    public static List<MainAction> mainActions = new ArrayList<>();//具体动作
    public static List<RelaxAction> relaxActions = new ArrayList<>();//放松动作
    //动作类别 天数 名字 这样的存储方案
    // 热身--1、拉伸--2、具体--3、放松--4
    public static SparseArray<SparseArray<List<SportName>>> typeDayNamePlan = new SparseArray<>();
    private Context context;

    public CollectPlan(Context context){
        this.context = context;
    }
    /**
     * 收集健身计划
     */
    public static void initDayPlan(){
        for (int i = 0;i<7;i++){
            DayPlan d = new DayPlan();
            d.setDayId(i);
            dayPlan.add(d);
        }
    }

    public static void prepareToPush(){

        for (int i = 0;i < 7 ; i++){
            if (MakePlanUtils.list_day.get(i).warmUpList.size() != 0){
                for (SportName s :
                        MakePlanUtils.list_day.get(i).warmUpList) {
                    WarmUp w = new WarmUp();
                    w.setDayId(i);
                    w.setId(id);
                    w.setWarmId(s.getObjectId());
                    warmUps.add(w);
                }
            }

        }
        for (int i = 0;i < 7 ; i++){
            if (MakePlanUtils.list_day.get(i).stretchingList.size() != 0){
                for (SportName s :
                        MakePlanUtils.list_day.get(i).stretchingList) {
                    Stretching stre = new Stretching();
                    stre.setDayId(i);
                    stre.setId(id);
                    stre.setStretchingId(s.getObjectId());
                    stretchings.add(stre);
                }
            }

        }
        for (int i = 0;i < 7 ; i++){
            if (MakePlanUtils.list_day.get(i).mainActionList.size() != 0){
                for (SportName s :
                        MakePlanUtils.list_day.get(i).mainActionList) {
                    MainAction m = new MainAction();
                    m.setDayId(i);
                    m.setId(id);
                    m.setMainAction(s.getObjectId());
                    mainActions.add(m);
                }
            }

        }
        for (int i = 0;i < 7 ; i++){
            if (MakePlanUtils.list_day.get(i).relaxActionList.size() != 0){
                for (SportName s :
                        MakePlanUtils.list_day.get(i).relaxActionList) {
                    RelaxAction r = new RelaxAction();
                    r.setDayId(i);
                    r.setId(id);
                    r.setRelaxAction(s.getObjectId());
                    relaxActions.add(r);
                }
            }

        }
    }
}