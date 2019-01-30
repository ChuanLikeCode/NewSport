package com.sibo.fastsport.model;

import cn.bmob.v3.BmobObject;

/**
 * Created by zhishan on 2017/2/23.
 */

public class WarmUp extends BmobObject {
    private String id;//健身计划的id
    private int dayId;//标识第几天的健身动作
    private String warmId;//健身动作的SportName  objectId

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

    public String getWarmId() {
        return warmId;
    }

    public void setWarmId(String warmId) {
        this.warmId = warmId;
    }
}
