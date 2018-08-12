package com.publicbenifitsharing.android.BannerView;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.publicbenifitsharing.android.GlideApp;
import com.publicbenifitsharing.android.R;

import java.util.ArrayList;
import java.util.List;


public class BannerView extends FrameLayout {
    private List<BannerBean> imageList;//图片
    private List<View> viewList;//ViewPager
    private List<ImageView> subscriptList;//下标

    private ViewPager viewPager;
    private boolean isAuto;
    private long delayTime;
    private int currentItem;
    private Handler handler;
    private Context context;

    public BannerView(Context context) {
        this(context,null);
    }

    private static final String TAG = "BannerView";

    public BannerView(Context context, AttributeSet attrs) {
        super(context,attrs);
        this.context = context;
        imageList=new ArrayList<>();
        viewList=new ArrayList<>();
        subscriptList=new ArrayList<>();
        handler=new Handler(context.getMainLooper());
        delayTime=2000;
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        switch(visibility){
            case View.VISIBLE:
                isAuto=true;
                break;
            default:
                isAuto=false;
                break;
        }
    }

    public void setData(List<BannerBean> list){
        this.imageList=list;
        viewList.clear();
        subscriptList.clear();
        initData();
    }

    private void initData(){
        View view= LayoutInflater.from(context).inflate(R.layout.banner_view,this,true);
        LinearLayout linearLayout=(LinearLayout) view.findViewById(R.id.point);
        viewPager=(ViewPager) view.findViewById(R.id.view_pager);
        linearLayout.removeAllViews();
        LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        layoutParams.leftMargin=5;
        layoutParams.rightMargin=5;
        for (int i=0;i<imageList.size();i++){
            ImageView subscript=new ImageView(context);
            subscript.setLayoutParams(layoutParams);
            if(i==0){
                subscript.setImageResource(R.mipmap.point_red);
            }else{
                subscript.setImageResource(R.mipmap.point_gray);
            }
            linearLayout.addView(subscript);
            subscriptList.add(subscript);

            final ImageView bannerImage=new ImageView(context);
            bannerImage.setScaleType(ImageView.ScaleType.FIT_XY);
            if (imageList.get(i).getType()==0){//加载本地图片
                bannerImage.setImageResource(imageList.get(i).getImageForInt());
            }else if (imageList.get(i).getType()==1){//加载网络图片
                GlideApp.with(context).load(imageList.get(i).getImageForUrl()).into(bannerImage);
                Log.d(TAG, "initData: "+imageList.get(i).getImageForUrl());
            }
            viewList.add(bannerImage);
        }
        viewPager.setAdapter(new MyPagerAdapter());
        viewPager.addOnPageChangeListener(onPageChange);
        isAuto=true;
        handler.postDelayed(runnable,delayTime);
    }

    private Runnable runnable=new Runnable() {
        @Override
        public void run() {
            if (isAuto){
                currentItem=currentItem%(viewList.size());
                if (currentItem==0) {
                    viewPager.setCurrentItem(currentItem);
                }else {
                    viewPager.setCurrentItem(currentItem);
                }
                currentItem++;
                handler.postDelayed(runnable,delayTime);
            }else{
                handler.postDelayed(runnable,delayTime);
            }
        }
    };

    private ViewPager.OnPageChangeListener onPageChange=new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            currentItem=viewPager.getCurrentItem();
            for (int i=0;i<viewList.size();i++){
                if (position==i){
                    subscriptList.get(position).setImageResource(R.mipmap.point_red);
                }else {
                    subscriptList.get(i).setImageResource(R.mipmap.point_gray);
                }
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            switch (state){
                case ViewPager.SCROLL_STATE_IDLE://没有操作
                    isAuto=true;
                    break;
                case ViewPager.SCROLL_STATE_DRAGGING://正在滑动
                    isAuto=false;
                    break;
                case ViewPager.SCROLL_STATE_SETTLING://滑动结束
                    isAuto=true;
                    break;
            }
        }
    };

    private class MyPagerAdapter extends PagerAdapter {
        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view==object;
        }

        @Override
        public int getCount() {
            return viewList.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(viewList.get(position));
            return viewList.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
