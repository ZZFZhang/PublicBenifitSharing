package com.publicbenifitsharing.android.dynamicpage;

import com.publicbenifitsharing.android.base.ValueCallBack;
import com.publicbenifitsharing.android.entityclass.Dynamic;

import java.util.List;


public class DynamicContact {
    public interface DynamicView{
        void showDynamicData(List<Dynamic> dynamics);
        void openSwipeRefreshLayout();
        void cancelSwipeRefreshLayout();
    }

    public interface DynamicModel{
        void getDynamicData(ValueCallBack<List<Dynamic>> valueCallBack);
    }

    public interface DynamicPresenter{
        void initDynamicData();
    }
}
