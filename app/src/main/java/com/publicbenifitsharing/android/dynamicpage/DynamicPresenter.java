package com.publicbenifitsharing.android.dynamicpage;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.Toast;

import com.publicbenifitsharing.android.base.ValueCallBack;
import com.publicbenifitsharing.android.entityclass.Dynamic;

import java.util.List;

public class DynamicPresenter implements DynamicContact.DynamicPresenter{

    private Context context;
    private DynamicContact.DynamicView view;
    private DynamicContact.DynamicModel model;

    public DynamicPresenter(Context context,DynamicContact.DynamicView view){
        this.context=context;
        this.view=view;
        model=new DynamicModel();
    }

    @Override
    public void initDynamicData() {
        view.openSwipeRefreshLayout();
        model.getDynamicData(new ValueCallBack<List<Dynamic>>() {
            @Override
            public void onSuccess(List<Dynamic> dynamics) {
                view.showDynamicData(dynamics);
                view.cancelSwipeRefreshLayout();
            }

            @Override
            public void onFail() {
                Toast toast=Toast.makeText(context, "连接超时！", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER,0,0);
                toast.show();
                view.cancelSwipeRefreshLayout();
            }
        });
    }
}
