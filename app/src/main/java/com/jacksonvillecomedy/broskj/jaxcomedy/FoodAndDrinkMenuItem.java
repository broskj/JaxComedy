package com.jacksonvillecomedy.broskj.jaxcomedy;

/**
 * Created by Kyle on 2/18/2015.
 */
public class FoodAndDrinkMenuItem {
    private String header;
    private String subheader;

    FoodAndDrinkMenuItem(String header, String subheader){
        this.header = header;
        this.subheader = subheader;
    }

    FoodAndDrinkMenuItem(){
        this.header = "";
        this.subheader = "";
    }

    public void setHeader(String header){
        this.header = header;
    }

    public void setSubheader(String subheader){
        this.subheader = subheader;
    }

    public String getHeader(){
        return header;
    }

    public String getSubheader(){
        return subheader;
    }
}
