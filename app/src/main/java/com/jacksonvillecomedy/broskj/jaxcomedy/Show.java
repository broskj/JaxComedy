package com.jacksonvillecomedy.broskj.jaxcomedy;

import java.util.Date;

/**
 * Created by Kyle on 1/28/2015.
 */
public class Show {
    private String comedian;
    private String description;
    private Date showDate;
    private int showTime;
    private boolean soldOut;
    final private int CLEAN = 0, UNCUT = 1, SPECIAL = 2;

    public Show() {
        comedian = "";
        description = "";
        showDate = null;
        showTime = -1;
        soldOut = false;
    }

    public Show(String comedian, String description, Date showDate, int showTime, boolean soldOut) {
        this.comedian = comedian;
        this.description = description;
        this.showDate = showDate;
        this.showTime = showTime;
        this.soldOut = soldOut;
    }

    public void setComedian(String comedian) {
        this.comedian = comedian;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public void setShowDate(Date showDate) {
        this.showDate = showDate;
    }

    public void setShowTime(int showTime) {
        this.showTime = showTime;
    }

    public void setSoldOut(boolean soldOut) {
        this.soldOut = soldOut;
    }

    public String getComedian() {
        return this.comedian;
    }

    public String getDescription(){
        return this.description;
    }

    public Date getShowDate() {
        return this.showDate;
    }

    public int getShowTime() {
        return this.showTime;
    }

    public boolean getSoldOut() {
        return this.soldOut;
    }

}
