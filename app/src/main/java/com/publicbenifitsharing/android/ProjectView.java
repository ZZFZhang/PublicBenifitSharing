package com.publicbenifitsharing.android;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.signature.ObjectKey;
import com.publicbenifitsharing.android.entityclass.Project;

import java.util.Random;

public class ProjectView extends AppCompatActivity {
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private Toolbar toolbar;
    private ImageView imageView;
    private TextView contentText;

    public static final String PROJECT_DATA="project_data";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置状态栏透明
        if (Build.VERSION.SDK_INT>=21){
            View decorView=getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);//透明状态栏，布局与状态栏重叠
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_project_view);
        collapsingToolbarLayout=(CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        toolbar=(Toolbar) findViewById(R.id.toolbar);
        imageView=(ImageView) findViewById(R.id.image_view);
        contentText=(TextView) findViewById(R.id.content_text);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        if (actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        Project project=(Project) getIntent().getSerializableExtra(PROJECT_DATA);
        collapsingToolbarLayout.setTitle("详情");
        GlideApp.with(getBaseContext()).load(project.getImageUrl()).signature(new ObjectKey(project.getReleaseTime())).into(imageView);
        contentText.setText(project.getContentText());

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ProjectView.this,PhotoViewerActivity.class);
                intent.putExtra("image_url",project.getImageUrl());
                intent.putExtra("release_time",project.getReleaseTime());
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
