package com.publicbenifitsharing.android.projectpage;

import android.os.Handler;
import android.os.Message;

import com.publicbenifitsharing.android.base.ValueCallBack;
import com.publicbenifitsharing.android.entityclass.Project;
import com.publicbenifitsharing.android.util.DBService;

import java.util.ArrayList;
import java.util.List;

public class ProjectModel implements ProjectContact.ProjectModel{
    private ValueCallBack<List<Project>> callBack;
    private List<Project> projectList=new ArrayList<>();

    public Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what){
                case 1:
                    List<Project> projects=(List<Project>) msg.obj;
                    if (projects.size()>0){
                        callBack.onSuccess(projects);
                    }else{
                        callBack.onFail();
                    }
                    break;
                default:
                    break;
            }
        }
    };
    @Override
    public void getProjectData(ValueCallBack<List<Project>> callBack) {
        this.callBack=callBack;
        new Thread(new Runnable() {
            @Override
            public void run() {
                projectList.clear();
                DBService dbService=DBService.getDbService();
                projectList.addAll(dbService.getProjectData());
                Message message=new Message();
                message.what=1;
                message.obj=projectList;
                handler.sendMessage(message);
            }
        }).start();
    }
}
