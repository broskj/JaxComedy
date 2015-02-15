package com.jacksonvillecomedy.broskj.jaxcomedy;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Kyle on 2/4/2015. 
 */
public class Offer implements Parcelable{
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(pointValue);
        dest.writeString(offerTitle);
        dest.writeString(offerDescription);
    }

    @SuppressWarnings("unchecked")
    public static final Parcelable.Creator<Offer> CREATOR = new Parcelable.Creator<Offer>(){

        @Override
        public Offer createFromParcel(Parcel in) {
            return new Offer(in);
        }

        @Override
        public Offer[] newArray(int size) {
            return new Offer[size];
        }
    };

    private Offer(Parcel in){
        pointValue = in.readInt();
        offerTitle = in.readString();
        offerDescription = in.readString();
    }
}
