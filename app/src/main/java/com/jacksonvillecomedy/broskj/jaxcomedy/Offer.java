package com.jacksonvillecomedy.broskj.jaxcomedy;

/**
 * Created by Steve on 2/4/2015.
 */
public class Offer {
    private int pointValue;
    private String offerTitle;
    private String offerDescription;

    Offer(){
        pointValue = 0;
        offerTitle = "";
        offerDescription = "";
    }

    Offer(int pointValue, String offerTitle, String offerDescription){
        this.pointValue = pointValue;
        this.offerTitle = offerTitle;
        this.offerDescription = offerDescription;
    }

    public int getPointValue(){
        return this.pointValue;
    }

    public String getOfferTitle(){
        return this.offerTitle;
    }

    public String getOfferDescription(){
        return this.offerDescription;
    }

    public void setPointValue(int pointValue){
        this.pointValue = pointValue;
    }

    public void setOfferTitle(String offerTitle){
        this.offerTitle = offerTitle;
    }

    public void setOfferDescription(String offerDescription){
        this.offerDescription = offerDescription;
    }
}
