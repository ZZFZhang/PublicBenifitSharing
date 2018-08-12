package com.publicbenifitsharing.android.BannerView;

public class BannerBean {
    private int type;
    private int imageForInt;
    private String imageForUrl;

    public void setType(int type) {
        this.type = type;
    }

    public void setImageForInt(int imageForInt) {
        this.imageForInt = imageForInt;
    }

    public void setImageForUrl(String imageForUrl) {
        this.imageForUrl = imageForUrl;
    }

    public int getType() {
        return type;
    }

    public int getImageForInt() {
        return imageForInt;
    }

    public String getImageForUrl() {
        return imageForUrl;
    }
}
