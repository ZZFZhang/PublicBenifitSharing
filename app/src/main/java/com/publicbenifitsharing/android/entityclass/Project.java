package com.publicbenifitsharing.android.entityclass;

import java.io.Serializable;

public class Project implements Serializable{
    private int id;
    private String userName;
    private String title;
    private String imageUrl;
    private String contentText;
    private String releaseTime;

    public void setId(int id) {
        this.id = id;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setContentText(String contentText) {
        this.contentText = contentText;
    }

    public void setReleaseTime(String releaseTime) {
        this.releaseTime = releaseTime;
    }

    public int getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public String getTitle() {
        return title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getContentText(){ return contentText; }

    public String getReleaseTime() {
        return releaseTime;
    }
}
