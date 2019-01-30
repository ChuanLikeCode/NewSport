package com.sibo.fastsport.model;

import cn.bmob.v3.BmobObject;

/**
 * Created by chaun on 2017/2/23.
 */

public class DayPlan extends BmobObject {
    private String id;//健身计划的ID
    private int dayId; // 健身计划的第几天
    private boolean warmUp; // 热身
    private boolean stretching;// 拉伸
    private boolean mainAction;// 具体
    private boolean relaxAction;// 放松

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getDayId() {
        return dayId;
    }

    public void setDayId(int dayId) {
        this.dayId = dayId;
    }

    public boolean isWarmUp() {
        return warmUp;
    }

    public void setWarmUp(boolean warmUp) {
        this.warmUp = warmUp;
    }

    public boolean isStretching() {
        return stretching;
    }

    public void setStretching(boolean stretching) {
        this.stretching = stretching;
    }

    public boolean isMainAction() {
        return mainAction;
    }

    public void setMainAction(boolean mainAction) {
        this.mainAction = mainAction;
    }

    public boolean isRelaxAction() {
        return relaxAction;
    }

    public void setRelaxAction(boolean relaxAction) {
        this.relaxAction = relaxAction;
    }
}
