package com.publicbenifitsharing.android.viewpager;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.app.ProgressDialog;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.publicbenifitsharing.android.GlideApp;
import com.publicbenifitsharing.android.MainActivity;
import com.publicbenifitsharing.android.R;
import com.publicbenifitsharing.android.adapter.ProjectRecycleViewAdapter;
import com.publicbenifitsharing.android.entityclass.Project;
import com.publicbenifitsharing.android.util.HttpUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ProjectPage extends Fragment {
    private SwipeRefreshLayout swipeRefresh;
    private RecyclerView recyclerView;
    private ProjectRecycleViewAdapter adapter;
    private ProgressDialog progressDialog;

    private List<Project> projectList=new ArrayList<>();

    private static final String TAG = "ProjectPage";

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
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getDataFromServer();
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getDataFromServer();
            }
        });
    }

    private void getDataFromServer(){
        HttpUtil.sendOkHttpRequest("http://" + MainActivity.serverId + "/project.json", new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                byte[] bytes=response.body().bytes();
                final String responseData=new String(bytes,"GB2312");
                Log.d(TAG, "onResponse: "+responseData);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        projectList.clear();
                        List<Project> list=new Gson().fromJson(responseData,new TypeToken<List<Project>>(){}.getType());
                        projectList.addAll(list);
                        adapter.notifyDataSetChanged();
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        });
    }
}
