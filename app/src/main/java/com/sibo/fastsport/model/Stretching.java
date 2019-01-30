package com.sibo.fastsport.model;

import cn.bmob.v3.BmobObject;

/**
 * Created by zhishan on 2017/2/23.
 */

public class Stretching extends BmobObject {
    private String id;
    private int dayId;
    private String stretchingId;

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

    public String getStretchingId() {
        return stretchingId;
    }

    public void setStretchingId(String stretchingId) {
        this.stretchingId = stretchingId;
    }
}
