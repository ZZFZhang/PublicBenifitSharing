package com.publicbenifitsharing.android.entityclass;

import java.io.Serializable;

public class Dynamic implements Serializable {
    private int id;
    private String headIconUrl;
    private String userName;
    private String releaseTime;
    private String imageUrl;
    private String contentTitle;

    public int getId(){
        return id;
    }
    public String getHeadIconUrl() {
        return headIconUrl;
    }

    public String getUserName() {
        return userName;
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
