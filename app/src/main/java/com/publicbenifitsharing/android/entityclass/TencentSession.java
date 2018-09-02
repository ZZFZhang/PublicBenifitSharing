package com.publicbenifitsharing.android.entityclass;

import org.litepal.crud.LitePalSupport;

public class TencentSession extends LitePalSupport{
    private String accessToken;
    private String openId;
    private String expiresIn;
    private long expiresTime;

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public void setExpiresIn(String expiresIn) {
        this.expiresIn = expiresIn;
    }

    public void setExpiresTime(long expiresTime) {
        this.expiresTime = expiresTime;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getOpenId() {
        return openId;
    }

    public String getExpiresIn() {
        return expiresIn;
    }

    public long getExpiresTime() {
        return expiresTime;
    }
}
