package com.sibo.fastsport.model;

import cn.bmob.v3.BmobObject;

/**
 * Created by zhishan on 2017/2/23.
 */

public class MainAction extends BmobObject {
    private String id;//健身计划Id
    private int dayId;//这个动作是第几天的
    private String mainAction;//健身动作ID

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMainAction() {
        return mainAction;
    }

    public void setMainAction(String mainAction) {
        this.mainAction = mainAction;
    }

    public int getDayId() {
        return dayId;
    }

    public void setDayId(int dayId) {
        this.dayId = dayId;
    }
}
