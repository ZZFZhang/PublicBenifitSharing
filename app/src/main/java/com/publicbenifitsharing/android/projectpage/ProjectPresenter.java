package com.publicbenifitsharing.android.projectpage;

import android.content.Context;

import com.publicbenifitsharing.android.base.ValueCallBack;
import com.publicbenifitsharing.android.entityclass.Project;
import com.publicbenifitsharing.android.util.ToastUtil;

import java.util.List;

public class ProjectPresenter implements ProjectContact.ProjectPresenter {
    private Context context;
    private ProjectContact.ProjectModel model;
    private ProjectContact.ProjectView view;

    public ProjectPresenter(Context context,ProjectContact.ProjectView view){
        this.context=context;
        this.view=view;
        model=new ProjectModel();
    }

    @Override
    public void initProjectData() {
        view.openSwipeRefreshLayout();
        model.getProjectData(new ValueCallBack<List<Project>>() {
            @Override
            public void onSuccess(List<Project> projects) {
                view.showProjectData(projects);
                view.cancelSwipeRefreshLayout();
            }

            @Override
            public void onFail() {
                ToastUtil.toastCenter(context,"连接超时！");
                view.cancelSwipeRefreshLayout();
            }
        });
    }
}
