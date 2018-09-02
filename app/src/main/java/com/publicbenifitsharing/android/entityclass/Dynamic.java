package com.publicbenifitsharing.android.entityclass;

import java.io.Serializable;

public class Dynamic implements Serializable {
    private int id;
    private String headIconUrl;
    private String userName;
    private String releaseDate;
    private String releaseTime;
    private String imageUrl;
    private String contentTitle;

    public void setId(int id) {
        this.id = id;
    }

    public void setHeadIconUrl(String headIconUrl) {
        this.headIconUrl = headIconUrl;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public void setReleaseTime(String releaseTime) {
        this.releaseTime = releaseTime;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setContentTitle(String contentTitle) {
        this.contentTitle = contentTitle;
    }

    public int getId(){
        return id;
    }
    public String getHeadIconUrl() {
        return headIconUrl;
    }

    public String getUserName() {
        return userName;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getReleaseTime() {
        return releaseTime;
    }

    public String getImageUrl(){
        return imageUrl;
    }

    public String getContentTitle() {
        return contentTitle;
    }
}
