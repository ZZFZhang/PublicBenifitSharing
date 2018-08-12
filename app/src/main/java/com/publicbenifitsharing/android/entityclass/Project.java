package com.publicbenifitsharing.android.entityclass;

import java.io.Serializable;

public class Project implements Serializable{
    private int id;
    private String title;
    private String imageUrl;
    private String contentText;

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getContentText(){ return contentText; }
}
