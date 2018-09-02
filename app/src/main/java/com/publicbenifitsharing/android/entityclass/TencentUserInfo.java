package com.publicbenifitsharing.android.entityclass;

import org.litepal.crud.LitePalSupport;

public class TencentUserInfo extends LitePalSupport{
    private String userName;
    private String headIconUrl;

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setHeadIconUrl(String headIconUrl) {
        this.headIconUrl = headIconUrl;
    }

    public String getUserName() {
        return userName;
    }

    public String getHeadIconUrl() {
        return headIconUrl;
    }
}
