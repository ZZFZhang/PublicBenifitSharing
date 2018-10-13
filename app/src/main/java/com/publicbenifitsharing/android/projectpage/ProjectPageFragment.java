package com.publicbenifitsharing.android.projectpage;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.publicbenifitsharing.android.R;
import com.publicbenifitsharing.android.adapter.ProjectRecycleViewAdapter;
import com.publicbenifitsharing.android.entityclass.Project;
import com.publicbenifitsharing.android.util.DBService;

import java.util.ArrayList;
import java.util.List;

public class ProjectPageFragment extends Fragment implements ProjectContact.ProjectView{
    public SwipeRefreshLayout swipeRefresh;
    private RecyclerView recyclerView;
    private ProjectRecycleViewAdapter adapter;

    private List<Project> projectList=new ArrayList<>();

    public boolean isLoad=false;
    public static boolean reLoad=false;

    public ProjectPresenter presenter;
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser){
            if (!isLoad){
                presenter.initProjectData();
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.project_page,container,false);
        swipeRefresh=(SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
        recyclerView=(RecyclerView) view.findViewById(R.id.recycle_view);
        adapter=new ProjectRecycleViewAdapter(projectList);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        presenter=new ProjectPresenter(getContext(),this);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.initProjectData();
            }
        });
    }

    @Override
    public void onResume() {
        if (reLoad){
            presenter.initProjectData();
            reLoad=false;
        }
        super.onResume();
    }

    @Override
    public void openSwipeRefreshLayout() {
        swipeRefresh.setRefreshing(true);
    }

    @Override
    public void cancelSwipeRefreshLayout() {
        swipeRefresh.setRefreshing(false);
    }

    @Override
    public void showProjectData(List<Project> projects) {
        projectList.clear();
        projectList.addAll(projects);
        adapter.notifyDataSetChanged();
        isLoad=true;
    }
}
