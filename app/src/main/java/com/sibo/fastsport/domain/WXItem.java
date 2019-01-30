package com.sibo.fastsport.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/11/13.
 */
public class WXItem {
    private String title;
    private List<String> img = new ArrayList<>();
    private String updateTime;
    private String author;
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public List<String> getImg() {
        return img;
    }

    public void setImg(List<String> img) {
        this.img.clear();
        this.img.addAll(img);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "WXItem{" +
                "title='" + title + '\'' +
                ", img=" + img.get(1) +
                ", updateTime='" + updateTime + '\'' +
                ", author='" + author + '\'' +
                '}';
    }
}
