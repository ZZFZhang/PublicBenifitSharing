package com.publicbenifitsharing.android;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.publicbenifitsharing.android.adapter.MyProjectRecycleViewAdapter;
import com.publicbenifitsharing.android.entityclass.Project;
import com.publicbenifitsharing.android.entityclass.TencentUserInfo;
import com.publicbenifitsharing.android.util.DBService;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

public class MyProjectViewActivity extends AppCompatActivity {
    private List<Project> projectList=new ArrayList<>();
    private MyProjectRecycleViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_project_view);
        Button back=(Button) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        RecyclerView recyclerView=(RecyclerView) findViewById(R.id.recycler_view);
        adapter=new MyProjectRecycleViewAdapter(projectList);
        LinearLayoutManager manager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        getMyProjectData();
    }

    private void getMyProjectData(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                projectList.clear();
                DBService dbService=DBService.getDbService();
                List<TencentUserInfo> tencentUserInfos= LitePal.findAll(TencentUserInfo.class);
                projectList.addAll(dbService.getMyProjectData(tencentUserInfos.get(0).getUserName()));
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        }).start();
    }
}
