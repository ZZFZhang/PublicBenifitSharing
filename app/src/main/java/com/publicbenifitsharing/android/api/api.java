package com.publicbenifitsharing.android.api;

public class api {
    public interface DBApi{
        int getProjectNumber();
        void getProjectData();
        void getMyProjectData();
        void uploadProjectData();
        void deleteMyProjectData();

        int getDynamicNumber();
        void getDynamicData();
        void getMyDynamicData();
        void uploadDynamicData();
        void deleteMyDaynamicData();
    }
}
