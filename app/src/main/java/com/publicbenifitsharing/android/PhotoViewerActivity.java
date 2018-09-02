package com.publicbenifitsharing.android;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

import com.bumptech.glide.signature.ObjectKey;
import com.github.chrisbanes.photoview.PhotoView;

public class PhotoViewerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_viewer);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        PhotoView photoView=(PhotoView) findViewById(R.id.photo_view);
        Intent intent=getIntent();
        String imageUrl=intent.getStringExtra("image_url");
        String releaseTime=intent.getStringExtra("release_time");
        GlideApp.with(this).load(imageUrl).signature(new ObjectKey(releaseTime)).into(photoView);
    }
}
