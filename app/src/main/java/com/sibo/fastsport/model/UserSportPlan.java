package com.sibo.fastsport.model;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 *
 * Created by chuan on 2017/2/23.
 */

public class UserSportPlan extends BmobObject {
    private String planName; //健身计划名字
    private String account;  //标识健身教练
    private String body;//体型
    private String yundong;//运动量
    private String studentId;//学员ID
    private String name;//学员名字
    private BmobFile img;//学员头像
    private String jirou;//肌肉
    private String zhifang;//脂肪
    private BmobFile teacherImg;//教练头像
    private String teacherName;//教练名字

    public BmobFile getTeacherImg() {
        return teacherImg;
    }

    public void setTeacherImg(BmobFile teacherImg) {
        this.teacherImg = teacherImg;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getJirou() {
        return jirou;
    }

    public void setJirou(String jirou) {
        this.jirou = jirou;
    }

    public String getZhifang() {
        return zhifang;
    }

    public void setZhifang(String zhifang) {
        this.zhifang = zhifang;
    }

    public BmobFile getImg() {
        return img;
    }

    public void setImg(BmobFile img) {
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getYundong() {
        return yundong;
    }

    public void setYundong(String yundong) {
        this.yundong = yundong;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }
}
