package com.publicbenifitsharing.android.viewpager;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.publicbenifitsharing.android.BannerView.BannerBean;
import com.publicbenifitsharing.android.BannerView.BannerView;
import com.publicbenifitsharing.android.MainActivity;
import com.publicbenifitsharing.android.R;

import java.util.ArrayList;
import java.util.List;

public class HomePage extends Fragment {
    private BannerView bannerView;
    private List<BannerBean> bannerBeanList;

    private String[] banners=new String[]{"http://"+ MainActivity.serverId+"/banners/banner_view1.png","http://"+ MainActivity.serverId+"/banners/banner_view2.png","http://"+ MainActivity.serverId+"/banners/banner_view3.png","http://"+ MainActivity.serverId+"/banners/banner_view4.png"};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.home_page,container,false);
        bannerView=(BannerView) view.findViewById(R.id.banner_view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        bannerBeanList=new ArrayList<>();
        for (int i=0;i<banners.length;i++){
            BannerBean bannerBean=new BannerBean();
            bannerBean.setType(1);
            bannerBean.setImageForUrl(banners[i]);
            bannerBeanList.add(bannerBean);
        }
        bannerView.setData(bannerBeanList);
    }
}
