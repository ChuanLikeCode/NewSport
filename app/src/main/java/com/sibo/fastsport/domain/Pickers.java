package com.sibo.fastsport.domain;

import java.io.Serializable;

/**
 * @author zengtao 2015年5月20日下午7:18:14
 */
public class Pickers implements Serializable {

    private static final long serialVersionUID = 1L;

    private String showConetnt;
    private String showId;

    public Pickers(String showConetnt, String showId) {
        super();
        this.showConetnt = showConetnt;
        this.showId = showId;
    }

    public Pickers() {
        super();
    }

    public String getShowConetnt() {
        return showConetnt;
    }

    public void setShowConetnt(String showConetnt) {
        this.showConetnt = showConetnt;
    }

    public String getShowId() {
        return showId;
    }

    public void setShowId(String showId) {
        this.showId = showId;
    }

}
