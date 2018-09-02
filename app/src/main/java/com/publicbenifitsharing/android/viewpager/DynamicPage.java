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
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.publicbenifitsharing.android.MainActivity;
import com.publicbenifitsharing.android.R;
import com.publicbenifitsharing.android.adapter.DynamicRecyclerViewAdapter;
import com.publicbenifitsharing.android.entityclass.Dynamic;
import com.publicbenifitsharing.android.util.DBService;
import com.publicbenifitsharing.android.util.HttpUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class DynamicPage extends Fragment {
    public SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private DynamicRecyclerViewAdapter adapter;
    private List<Dynamic> dynamicList=new ArrayList<>();

    public boolean isLoad=false;
    public static boolean reLoad=false;
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser){
            if (!isLoad){
                getDataFromServer();
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.dynamic_page,container,false);
        swipeRefreshLayout=(SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
        recyclerView=(RecyclerView) view.findViewById(R.id.recycle_view);
        adapter=new DynamicRecyclerViewAdapter(dynamicList);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getDataFromServer();
            }
        });
    }

    @Override
    public void onResume() {
        if (reLoad){
            getDataFromServer();
            reLoad=false;
        }
        super.onResume();
    }

    public void getDataFromServer(){
        swipeRefreshLayout.setRefreshing(true);
        new Thread(new Runnable() {
            @Override
            public void run() {
                dynamicList.clear();
                DBService dbService=DBService.getDbService();
                dynamicList.addAll(dbService.getDynamicData());
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                        swipeRefreshLayout.setRefreshing(false);
                        if (dynamicList.size()==0){
                            Toast.makeText(getContext(), "连接超时！", Toast.LENGTH_SHORT).show();
                        }
                        isLoad=true;
                    }
                });
            }
        }).start();
    }
}
