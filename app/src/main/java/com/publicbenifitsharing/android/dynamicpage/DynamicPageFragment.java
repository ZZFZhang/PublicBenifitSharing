package com.publicbenifitsharing.android.dynamicpage;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.publicbenifitsharing.android.R;
import com.publicbenifitsharing.android.entityclass.Dynamic;

import java.util.ArrayList;
import java.util.List;

public class DynamicPageFragment extends Fragment implements DynamicContact.DynamicView {
    public SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private DynamicRecyclerViewAdapter adapter;
    private List<Dynamic> dynamicList=new ArrayList<>();

    public boolean isLoad=false;
    public static boolean reLoad=false;

    public DynamicPresenter presenter;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser){
            if (!isLoad){
                presenter.initDynamicData();
            }
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter=new DynamicPresenter(getContext(),this);
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
                presenter.initDynamicData();
            }
        });
    }

    @Override
    public void onResume() {
        if (reLoad){
            presenter.initDynamicData();
            reLoad=false;
        }
        super.onResume();
    }

    @Override
    public void showDynamicData(List<Dynamic> dynamics) {
        dynamicList.clear();
        dynamicList.addAll(dynamics);
        adapter.notifyDataSetChanged();
        isLoad=true;
    }

    @Override
    public void openSwipeRefreshLayout() {
        swipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void cancelSwipeRefreshLayout() {
        swipeRefreshLayout.setRefreshing(false);
    }
}
