package com.sibo.fastsport.model;

import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by Administrator on 2016/11/20.
 */
public class UserInfo extends BmobObject{
    private String planObjectId;
    private String type;//1---教练  2----学员
    private String account;
    private String password;
    private String id;//用户唯一标识
    private String nikeName;//用户昵称
    private String sex;//性别
    private int level;//用户星级
    private String age;//用户年龄
    private String height;//用户身高
    private String weight;//用户体重
    private String jiaoling;//用户教龄
    private List<String> goodAt;//用户的擅长领域
    private BmobFile head;//用户头像
    private List<BmobFile> img;//用户详情图片集
    private String newInfo;//是不是新用户

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getNewInfo() {
        return newInfo;
    }

    public void setNewInfo(String newInfo) {
        this.newInfo = newInfo;
    }

    public String getNikeName() {
        return nikeName;
    }

    public void setNikeName(String nikeName) {
        this.nikeName = nikeName;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }


    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getJiaoling() {
        return jiaoling;
    }

    public void setJiaoling(String jiaoling) {
        this.jiaoling = jiaoling;
    }

    public BmobFile getHead() {
        return head;
    }

    public void setHead(BmobFile head) {
        this.head = head;
    }

    public List<String> getGoodAt() {
        return goodAt;
    }

    public void setGoodAt(List<String> goodAt) {
        this.goodAt = goodAt;
    }

    public List<BmobFile> getImg() {
        return img;
    }

    public void setImg(List<BmobFile> img) {
        this.img = img;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPlanObjectId() {
        return planObjectId;
    }

    public void setPlanObjectId(String planObjectId) {
        this.planObjectId = planObjectId;
    }

}
