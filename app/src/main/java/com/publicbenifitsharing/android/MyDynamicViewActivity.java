package com.publicbenifitsharing.android;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.publicbenifitsharing.android.adapter.MyDynamicRecyclerViewAdapter;
import com.publicbenifitsharing.android.entityclass.Dynamic;
import com.publicbenifitsharing.android.entityclass.TencentUserInfo;
import com.publicbenifitsharing.android.util.DBService;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

public class MyDynamicViewActivity extends AppCompatActivity {
    private List<Dynamic> dynamicList=new ArrayList<>();
    private MyDynamicRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_dynamic_view);
        Button back=(Button) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        RecyclerView recyclerView=(RecyclerView) findViewById(R.id.recycler_view);
        adapter=new MyDynamicRecyclerViewAdapter(dynamicList);
        LinearLayoutManager manager=new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        getMyDynamicData();
    }

    private void getMyDynamicData(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                dynamicList.clear();
                DBService dbService=DBService.getDbService();
                List<TencentUserInfo> tencentUserInfos= LitePal.findAll(TencentUserInfo.class);
                dynamicList.addAll(dbService.getMyDynamicData(tencentUserInfos.get(0).getUserName()));
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
