package com.publicbenifitsharing.android.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.signature.ObjectKey;
import com.publicbenifitsharing.android.DynamicView;
import com.publicbenifitsharing.android.GlideApp;
import com.publicbenifitsharing.android.R;
import com.publicbenifitsharing.android.entityclass.Dynamic;
import com.publicbenifitsharing.android.entityclass.TencentUserInfo;
import com.publicbenifitsharing.android.util.DBService;
import com.publicbenifitsharing.android.viewpager.DynamicPage;

import org.litepal.LitePal;

import java.util.List;

public class MyDynamicRecyclerViewAdapter extends RecyclerView.Adapter<MyDynamicRecyclerViewAdapter.ViewHolder> {
    private Context context;
    private List<Dynamic> dynamicList;

    private static final int DELETE_DYNAMIC=1;

    static class ViewHolder extends RecyclerView.ViewHolder{
        private CardView cardView;
        private ImageView headIcon;
        private TextView userName;
        private TextView releaseTime;
        private TextView contentTitle;
        private ImageView imageView;

        public ViewHolder(View view){
            super(view);
            cardView=(CardView) view;
            headIcon=(ImageView) view.findViewById(R.id.head_icon);
            userName=(TextView) view.findViewById(R.id.user_name);
            releaseTime=(TextView) view.findViewById(R.id.release_time);
            contentTitle=(TextView) view.findViewById(R.id.content_title);
            imageView=(ImageView) view.findViewById(R.id.image_view);
        }
    }

    public MyDynamicRecyclerViewAdapter(List<Dynamic> dynamics){this.dynamicList=dynamics;}

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (context==null){
            context=parent.getContext();
        }
        View view= LayoutInflater.from(context).inflate(R.layout.item_dynamic_recyclerview,parent,false);
        final ViewHolder holder=new ViewHolder(view);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position=holder.getAdapterPosition();
                Intent intent=new Intent(context, DynamicView.class);
                intent.putExtra(DynamicView.DYNAMIC_DATA,dynamicList.get(position));
                context.startActivity(intent);
            }
        });
        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showPopupMenu(v,holder.getAdapterPosition());
                return true;
            }
        });
        return holder;
    }

    private void showPopupMenu(View view,int position){
        PopupMenu menu=new PopupMenu(context,view);
        menu.getMenuInflater().inflate(R.menu.delete_menu,menu.getMenu());
        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.delete:
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                DBService dbService=DBService.getDbService();
                                List<TencentUserInfo> tencentUserInfos= LitePal.findAll(TencentUserInfo.class);
                                int result=dbService.deleteMyDynamicData(tencentUserInfos.get(0).getUserName());
                                Message message=new Message();
                                message.what=DELETE_DYNAMIC;
                                message.arg1=result;
                                message.arg2=position;
                                handler.sendMessage(message);
                            }
                        }).start();
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
        menu.show();
    }

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case DELETE_DYNAMIC:
                    if (msg.arg1==1){
                        dynamicList.remove(msg.arg2);
                        notifyItemRemoved(msg.arg2);
                        notifyDataSetChanged();
                        DynamicPage.reLoad=true;
                    }
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Dynamic dynamic=dynamicList.get(position);
        GlideApp.with(context).load(dynamic.getHeadIconUrl()).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).into(holder.headIcon);
        holder.userName.setText(dynamic.getUserName());
        holder.releaseTime.setText(dynamic.getReleaseDate());
        holder.contentTitle.setText(dynamic.getContentTitle());
        if (dynamic.getImageUrl()!=null){
            holder.imageView.setVisibility(View.VISIBLE);
            GlideApp.with(context).load(dynamic.getImageUrl()).signature(new ObjectKey(dynamic.getReleaseTime())).into(holder.imageView);
            //GlideApp.with(context).load(dynamic.getImageUrl()).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).into(holder.imageView);
        }else {
            holder.imageView.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return dynamicList.size();
    }
}
