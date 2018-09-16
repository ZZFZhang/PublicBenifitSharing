package com.publicbenifitsharing.android.dynamicpage;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.signature.ObjectKey;
import com.publicbenifitsharing.android.DynamicView;
import com.publicbenifitsharing.android.GlideApp;
import com.publicbenifitsharing.android.R;
import com.publicbenifitsharing.android.entityclass.Dynamic;

import java.util.List;

public class DynamicRecyclerViewAdapter extends RecyclerView.Adapter<DynamicRecyclerViewAdapter.ViewHolder> {
    private Context context;
    private List<Dynamic> dynamicList;

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

    public DynamicRecyclerViewAdapter(List<Dynamic> dynamics){this.dynamicList=dynamics;}

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
        return holder;
    }

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
