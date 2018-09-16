package com.publicbenifitsharing.android.dynamicpage;

import android.os.Handler;
import android.os.Message;

import com.publicbenifitsharing.android.base.ValueCallBack;
import com.publicbenifitsharing.android.entityclass.Dynamic;
import com.publicbenifitsharing.android.util.DBService;

import java.util.ArrayList;
import java.util.List;


public class DynamicModel implements DynamicContact.DynamicModel {

    private List<Dynamic> dynamicList=new ArrayList<>();
    private ValueCallBack<List<Dynamic>> callBack;

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    List<Dynamic> dynamics=(List<Dynamic>) msg.obj;
                    if (dynamics.size()==0){
                        callBack.onFail();
                    }else {
                        callBack.onSuccess(dynamics);
                    }
            }
        }
    };

    @Override
    public void getDynamicData(ValueCallBack<List<Dynamic>> callBack) {
        this.callBack=callBack;
        new Thread(new Runnable() {
            @Override
            public void run() {
                DBService dbService=DBService.getDbService();
                dynamicList.clear();
                dynamicList.addAll(dbService.getDynamicData());
                Message message=new Message();
                message.what=1;
                message.obj=dynamicList;
                handler.sendMessage(message);
            }
        }).start();
    }
}
