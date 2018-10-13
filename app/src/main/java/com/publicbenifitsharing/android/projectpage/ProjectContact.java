package com.publicbenifitsharing.android.projectpage;

import com.publicbenifitsharing.android.base.ValueCallBack;
import com.publicbenifitsharing.android.entityclass.Project;

import java.util.List;

public class ProjectContact {
    public interface ProjectView{
        void showProjectData(List<Project> projects);
        void openSwipeRefreshLayout();
        void cancelSwipeRefreshLayout();
    }

    public interface ProjectModel{
        void getProjectData(ValueCallBack<List<Project>> callBack);
    }

    public interface ProjectPresenter{
        void initProjectData();
    }
}
