package com.publicbenifitsharing.android;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.publicbenifitsharing.android.adapter.MyFragmentPagerAdapter;
import com.publicbenifitsharing.android.entityclass.TencentSession;
import com.publicbenifitsharing.android.entityclass.TencentUserInfo;
import com.publicbenifitsharing.android.viewpager.DynamicPage;
import com.publicbenifitsharing.android.viewpager.HomePage;
import com.publicbenifitsharing.android.viewpager.ProjectPage;
import com.tencent.connect.UserInfo;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONObject;
import org.litepal.LitePal;
import org.litepal.crud.LitePalSupport;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ImageView headIconSmall;
    private TextView titleText;
    private Button upload;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private NavigationView navigationView;
    private ImageView headIconBig;
    private TextView userName;
    private TextView describe;

    private HomePage homePageFragment;
    private ProjectPage projectPageFragment;
    private DynamicPage dynamicPageFragment;
    private List<Fragment> fragmentList;
    private MyFragmentPagerAdapter myFragmentPagerAdapter;

    private String[] titleActionBar=new String[]{"益享公益","一起志愿","动态"};
    private String[] titleTabLayout=new String[]{"首页","公益项目","动态"};
    private int[] iconTabLayout=new int[]{R.drawable.icon_home_page,R.drawable.icon_project_page,R.drawable.icon_dynamic_page};
    private int[] iconPressTabLayout=new int[]{R.drawable.icon_home_page_press,R.drawable.icon_project_page_press,R.drawable.icon_dynamic_page_press};

    private boolean isLogin=false;
    private Tencent mTencent;
    private String TencentAppId="1107469874";

    public static String serverId="172.27.35.1";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawerLayout=(DrawerLayout) findViewById(R.id.drawer_layout);
        headIconSmall=(ImageView) findViewById(R.id.head_icon);
        titleText=(TextView) findViewById(R.id.title_text);
        upload=(Button) findViewById(R.id.upload);
        viewPager=(ViewPager) findViewById(R.id.view_pager);
        tabLayout=(TabLayout) findViewById(R.id.tab_layout);
        navigationView=(NavigationView) findViewById(R.id.nav_view);
        View headerLayout=navigationView.inflateHeaderView(R.layout.nav_header);
        headIconBig=(ImageView) headerLayout.findViewById(R.id.icon_image);
        userName=(TextView) headerLayout.findViewById(R.id.username);
        describe=(TextView) headerLayout.findViewById(R.id.describe);
        titleText.setText("益享公益");

        homePageFragment=new HomePage();
        projectPageFragment=new ProjectPage();
        dynamicPageFragment=new DynamicPage();
        fragmentList=new ArrayList<>();
        fragmentList.add(homePageFragment);
        fragmentList.add(projectPageFragment);
        fragmentList.add(dynamicPageFragment);
        myFragmentPagerAdapter=new MyFragmentPagerAdapter(getSupportFragmentManager(),fragmentList);
        viewPager.setAdapter(myFragmentPagerAdapter);
        viewPager.setOffscreenPageLimit(3);
        tabLayout.setupWithViewPager(viewPager);//此方法会清除所有tabs
        //初始化TabLayout
        for (int i=0;i<titleTabLayout.length;i++){
            TabLayout.Tab tab=tabLayout.getTabAt(i);
            tab.setCustomView(getTabView(i));
        }

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                titleText.setText(titleActionBar[position]);
                for (int i=0;i<titleTabLayout.length;i++){
                    TabLayout.Tab tab=tabLayout.getTabAt(i);
                    View view=tab.getCustomView();
                    ImageView imageView=(ImageView) view.findViewById(R.id.icon_tablayout);
                    TextView textView=(TextView) view.findViewById(R.id.title_talayout);
                    if (position==i){
                        imageView.setImageResource(iconPressTabLayout[i]);
                        textView.setTextColor(getResources().getColor(R.color.colorTitleTabLayoutPressed));
                    }else{
                        imageView.setImageResource(iconTabLayout[i]);
                        textView.setTextColor(getResources().getColor(R.color.colorTitleTabLayout));
                    }
                }

                if (isLogin){
                    switch (position){
                        case 0:
                            upload.setVisibility(View.INVISIBLE);
                            break;
                        case 1:
                            upload.setVisibility(View.VISIBLE);
                            if (isLogin){
                                upload.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent startUploadProject=new Intent(MainActivity.this,UploadProjectActivity.class);
                                        startActivityForResult(startUploadProject,2);
                                    }
                                });
                            }
                            break;
                        case 2:
                            upload.setVisibility(View.VISIBLE);
                            if (isLogin){
                                upload.setOnClickListener(new View.OnClickListener(){
                                    @Override
                                    public void onClick(View v) {
                                        Intent startUploadDynamic=new Intent(MainActivity.this,UploadDynamicActivity.class);
                                        startActivityForResult(startUploadDynamic,1);
                                    }
                                });
                            }
                            break;
                        default:
                            break;
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.login_out:
                        mTencent.logout(getApplicationContext());
                        updateUserInfo();
                        LitePal.deleteAll(TencentSession.class);
                        LitePal.deleteAll(TencentUserInfo.class);
                        break;
                    case R.id.my_project:
                        Intent startMyProjectView=new Intent(MainActivity.this,MyProjectViewActivity.class);
                        startActivity(startMyProjectView);
                        break;
                    case R.id.my_dynamic:
                        Intent startMyDynamicView=new Intent(MainActivity.this,MyDynamicViewActivity.class);
                        startActivity(startMyDynamicView);
                        break;
                    default:
                        break;
                }
                return true;
            }
        });

        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        mTencent=Tencent.createInstance(TencentAppId,this.getApplicationContext());

        /*if (mTencent.checkSessionValid(TencentAppId)){
            mTencent.initSessionCache(mTencent.loadSession(TencentAppId));//源码显示验证mTencent.setAccessToken  mTencent.setOpenId
            updateUserInfo();
        }else {
            Toast.makeText(this, "Token已过期，请重新登录！", Toast.LENGTH_SHORT).show();
        }*/
        List<TencentSession> tencentSessionList= LitePal.findAll(TencentSession.class);
        if (tencentSessionList.size()!=0){
            long currentTime=System.currentTimeMillis();
            if (currentTime<tencentSessionList.get(0).getExpiresTime()){
                mTencent.setAccessToken(tencentSessionList.get(0).getAccessToken(), tencentSessionList.get(0).getExpiresIn());
                mTencent.setOpenId(tencentSessionList.get(0).getOpenId());
                updateUserInfo();
            }else{
                Toast.makeText(this,"Token已过期,请重新登录!",Toast.LENGTH_SHORT).show();

            }
        }

        headIconSmall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLogin){
                    drawerLayout.openDrawer(GravityCompat.START);
                }else{
                    if (!mTencent.isSessionValid()){
                        mTencent.login(MainActivity.this, "all",loginListener);
                    }
                }
            }
        });
    }

    private static final String TAG = "MainActivity";

    //自定义TabLayout布局
    private View getTabView(int position){
        LayoutInflater inflater=LayoutInflater.from(this);
        View view=inflater.inflate(R.layout.item_tablayout,null);
        ImageView imageView=(ImageView) view.findViewById(R.id.icon_tablayout);
        TextView textView=(TextView) view.findViewById(R.id.title_talayout);
        textView.setText(titleTabLayout[position]);
        if (position==0){
            imageView.setImageResource(iconPressTabLayout[position]);
            textView.setTextColor(getResources().getColor(R.color.colorTitleTabLayoutPressed));
        }else{
            imageView.setImageResource(iconTabLayout[position]);
            textView.setTextColor(getResources().getColor(R.color.colorTitleTabLayout));
        }
        return view;
    }

    IUiListener loginListener=new IUiListener() {
        @Override
        public void onComplete(Object o) {
            Toast.makeText(MainActivity.this, "授权成功！", Toast.LENGTH_SHORT).show();
            JSONObject jsonObject=(JSONObject) o;
            try{
                String openId=jsonObject.getString("openid");
                String accessToken=jsonObject.getString("access_token");
                String expires=jsonObject.getString("expires_in");
                String expiresTime=jsonObject.getString("expires_time");
                mTencent.setOpenId(openId);
                mTencent.setAccessToken(accessToken,expires);
                updateUserInfo();

                TencentSession tencentSession=new TencentSession();
                tencentSession.setAccessToken(accessToken);
                tencentSession.setOpenId(openId);
                tencentSession.setExpiresIn(expires);
                tencentSession.setExpiresTime(Long.parseLong(expiresTime));
                tencentSession.save();
            }catch (Exception e){
                e.printStackTrace();
            }

        }

        @Override
        public void onError(UiError uiError) {
            Toast.makeText(MainActivity.this, "授权出错！", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel() {
            Toast.makeText(MainActivity.this, "授权取消！", Toast.LENGTH_SHORT).show();
        }
    };

    private void updateUserInfo(){
        if (mTencent!=null && mTencent.isSessionValid()){
            IUiListener listener = new IUiListener() {
                @Override
                public void onComplete(Object o) {
                    JSONObject jsonObject=(JSONObject) o;
                    try{
                        String nickName=jsonObject.getString("nickname");
                        Log.d(TAG, "onComplete: "+nickName);
                        String figureUrl;
                        if (jsonObject.getString("figureurl_qq_2")!=null){
                            figureUrl=jsonObject.getString("figureurl_qq_2");
                            Log.d(TAG, "onComplete: "+figureUrl);
                        }else{
                            figureUrl=jsonObject.getString("figureurl_qq_1");
                        }
                        String describeText=jsonObject.getString("province")+" "+jsonObject.getString("city")+" "+jsonObject.getString("gender")+" "+jsonObject.getString("year");
                        GlideApp.with(getApplicationContext()).load(figureUrl).into(headIconSmall);
                        GlideApp.with(getApplicationContext()).load(figureUrl).into(headIconBig);
                        userName.setText(nickName);
                        describe.setText(describeText);

                        TencentUserInfo tencentUserInfo=LitePal.find(TencentUserInfo.class,1);
                        if (tencentUserInfo==null){
                            TencentUserInfo tencentUserInfo1=new TencentUserInfo();
                            tencentUserInfo1.setUserName(nickName);
                            tencentUserInfo1.setHeadIconUrl(figureUrl);
                            tencentUserInfo1.save();
                        }else {
                            tencentUserInfo.setUserName(nickName);
                            tencentUserInfo.setHeadIconUrl(figureUrl);
                            tencentUserInfo.save();
                        }
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError(UiError uiError) {

                }

                @Override
                public void onCancel() {

                }
            };
            UserInfo userInfo=new UserInfo(this,mTencent.getQQToken());
            userInfo.getUserInfo(listener);
            isLogin=true;
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        }else{
            headIconSmall.setImageResource(R.drawable.head_icon);
            headIconBig.setImageResource(R.drawable.head_icon);
            userName.setText("");
            describe.setText("");
            isLogin=false;
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "-->onActivityResult " + requestCode  + " resultCode=" + resultCode);
        if (requestCode == Constants.REQUEST_LOGIN ||
                requestCode == Constants.REQUEST_APPBAR) {
            Tencent.onActivityResultData(requestCode,resultCode,data,loginListener);
        }

        switch(requestCode){
            case 1:
                if (resultCode==RESULT_OK){
                    boolean returnedData=data.getBooleanExtra("data_return",false);
                    if (returnedData){
                        dynamicPageFragment.swipeRefreshLayout.setRefreshing(true);
                        dynamicPageFragment.getDataFromServer();
                        Toast.makeText(this, "发表成功", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case 2:
                if (resultCode==RESULT_OK){
                    boolean returnedData=data.getBooleanExtra("data_return",false);
                    if (returnedData){
                        projectPageFragment.swipeRefresh.setRefreshing(true);
                        projectPageFragment.getDataFromServer();
                        Toast.makeText(this, "发表成功", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
