package com.sibo.fastsport.model;

import cn.bmob.v3.BmobObject;

/**
 * Created by zhishan on 2017/2/23.
 */

public class RelaxAction extends BmobObject {
    private String id;
    private int dayId;
    private String relaxAction;

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

    public String getRelaxAction() {
        return relaxAction;
    }

    public void setRelaxAction(String relaxAction) {
        this.relaxAction = relaxAction;
    }
}
