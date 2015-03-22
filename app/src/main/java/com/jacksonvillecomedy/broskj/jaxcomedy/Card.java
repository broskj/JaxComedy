package com.jacksonvillecomedy.broskj.jaxcomedy;

/**
 * Created by Kyle on 3/22/2015.
 */
public class Card {
    private String showsDate;
    private String showsInfo;

    public Card(String showsDate, String showsInfo) {
        this.showsDate = showsDate;
        this.showsInfo = showsInfo;
    }

    public String getShowsDate() {
        return showsDate;
    }

    public String getShowsInfo() {
        return showsInfo;
    }
}
