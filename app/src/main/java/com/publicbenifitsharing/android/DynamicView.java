package com.publicbenifitsharing.android;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.publicbenifitsharing.android.entityclass.Dynamic;


public class DynamicView extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView toolbarTitle;
    private ImageView headIcon;
    private TextView userName;
    private TextView releaseTime;
    private TextView contentTitle;
    private ImageView imageView;

    public static final String DYNAMIC_DATA="dynamic_data";
    private Dynamic dynamic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamic_view);
        toolbar=(Toolbar) findViewById(R.id.toolbar);
        toolbarTitle=(TextView) findViewById(R.id.toolbar_title);
        headIcon=(ImageView) findViewById(R.id.head_icon);
        userName=(TextView) findViewById(R.id.user_name);
        releaseTime=(TextView) findViewById(R.id.release_time);
        contentTitle=(TextView) findViewById(R.id.content_title);
        imageView=(ImageView) findViewById(R.id.image_view);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        if (actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        dynamic=(Dynamic) getIntent().getSerializableExtra(DYNAMIC_DATA);

        toolbarTitle.setText("详情");
        showDynamic(dynamic);
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

    private void showDynamic(Dynamic dynamic){
        GlideApp.with(getBaseContext()).load(dynamic.getHeadIconUrl()).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).into(headIcon);
        userName.setText(dynamic.getUserName());
        releaseTime.setText(dynamic.getReleaseDate());
        contentTitle.setText(dynamic.getContentTitle());
        if (dynamic.getImageUrl()!=null){
            GlideApp.with(getBaseContext()).load(dynamic.getImageUrl()).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).into(imageView);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(DynamicView.this,PhotoViewerActivity.class);
                    intent.putExtra("image_url",dynamic.getImageUrl());
                    intent.putExtra("release_time",dynamic.getReleaseTime());
                    startActivity(intent);
                }
            });
        }else{
            imageView.setVisibility(View.GONE);
        }
    }
}
