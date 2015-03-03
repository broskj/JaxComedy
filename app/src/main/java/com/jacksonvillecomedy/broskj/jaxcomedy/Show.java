package com.jacksonvillecomedy.broskj.jaxcomedy;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import java.net.URI;

/**
 * Created by Kyle on 1/28/2015.
 */
public class Show implements Parcelable {
    private String comedian;
    private String description;
    private String showDate;
    private int showTime;
    private String videoID;

    public Show() {
        comedian = "";
        description = "";
        showDate = "";
        showTime = -1;
        videoID = "";
    }

    public Show(String comedian, String description, String showDate, int showTime, String videoID) {
        this.comedian = comedian;
        this.description = description;
        this.showDate = showDate;
        this.showTime = showTime;
        this.videoID = videoID;
    }

    public void setComedian(String comedian) {
        this.comedian = comedian;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public void setShowDate(String showDate) {
        this.showDate = showDate;
    }

    public void setShowTime(int showTime) {
        this.showTime = showTime;
    }

    public void setVideoID(String videoID) { this.videoID = videoID; }

    public String getComedian() {
        return this.comedian;
    }

    public String getDescription(){
        return this.description;
    }

    public String getShowDate() {
        return this.showDate;
    }

    public int getShowTime() {
        return this.showTime;
    }

    public String getVideoID() { return this.videoID; }

    public boolean equals(Show show) {
        return (this.comedian.equals(show.getComedian()) &&
                this.description.equals(show.getDescription()) &&
                this.showDate.equals(show.getShowDate()) &&
                this.showTime == show.getShowTime() &&
                this.videoID.equals(show.getVideoID()));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(comedian);
        dest.writeString(description);
        dest.writeString(showDate);
        dest.writeInt(showTime);
        dest.writeString(videoID);
    }

    @SuppressWarnings("unchecked")
    public static final Parcelable.Creator<Show> CREATOR = new Parcelable.Creator<Show>(){

        @Override
        public Show createFromParcel(Parcel in) {
            return new Show(in);
        }

        @Override
        public Show[] newArray(int size) {
            return new Show[size];
        }
    };

    private Show(Parcel in){
        comedian = in.readString();
        description = in.readString();
        showDate = in.readString();
        showTime = in.readInt();
        videoID = in.readString();
    }
}
