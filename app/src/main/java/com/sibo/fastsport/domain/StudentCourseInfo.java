package com.sibo.fastsport.domain;

/**
 * Created by Administrator on 2016/10/10 0010.
 */
public class StudentCourseInfo {
    private int touxiang;
    private String name;
    private String lastClass;
    private String completeClass;

    public StudentCourseInfo() {

    }

    public StudentCourseInfo(int touxiang, String name, String lastClass, String completeClass) {
        this.touxiang = touxiang;
        this.name = name;
        this.lastClass = lastClass;
        this.completeClass = completeClass;
    }

    public int getTouxiang() {
        return touxiang;
    }

    public void setTouxiang(int touxiang) {
        this.touxiang = touxiang;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastClass() {
        return lastClass;
    }

    public void setLastClass(String lastClass) {
        this.lastClass = lastClass;
    }

    public String getCompleteClass() {
        return completeClass;
    }

    public void setCompleteClass(String completeClass) {
        this.completeClass = completeClass;
    }
}
