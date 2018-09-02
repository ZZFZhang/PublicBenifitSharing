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

import com.bumptech.glide.signature.ObjectKey;
import com.publicbenifitsharing.android.GlideApp;
import com.publicbenifitsharing.android.ProjectView;
import com.publicbenifitsharing.android.R;
import com.publicbenifitsharing.android.entityclass.Project;
import com.publicbenifitsharing.android.entityclass.TencentUserInfo;
import com.publicbenifitsharing.android.util.DBService;
import com.publicbenifitsharing.android.viewpager.ProjectPage;


import org.litepal.LitePal;

import java.util.List;

public class MyProjectRecycleViewAdapter extends RecyclerView.Adapter<MyProjectRecycleViewAdapter.ViewHolder> {
    private Context context;
    private List<Project> projectList;
    private static final int DELETE_PROJECT=1;

    static class ViewHolder extends RecyclerView.ViewHolder{
        private CardView cardView;
        private ImageView imageView;
        private TextView titleText;

        public ViewHolder(View view){
            super(view);
            cardView=(CardView) view;
            imageView=(ImageView) view.findViewById(R.id.image_view);
            titleText=(TextView) view.findViewById(R.id.title_text);
        }
    }

    public MyProjectRecycleViewAdapter(List<Project> projects){this.projectList=projects;}

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        if (context==null){
            context=parent.getContext();
        }
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_project_recycleview,parent,false);
        final ViewHolder holder=new ViewHolder(view);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position=holder.getAdapterPosition();
                Intent intent=new Intent(context, ProjectView.class);
                intent.putExtra(ProjectView.PROJECT_DATA,projectList.get(position));
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
                                int result=dbService.deleteMyProjectData(tencentUserInfos.get(0).getUserName());
                                Message message=new Message();
                                message.what=DELETE_PROJECT;
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
        public void handleMessage(Message msg){
            switch (msg.what){
                case DELETE_PROJECT:
                    if (msg.arg1==1){
                        projectList.remove(msg.arg2);
                        notifyItemRemoved(msg.arg2);
                        notifyDataSetChanged();
                        ProjectPage.reLoad=true;
                    }
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Project project=projectList.get(position);
        holder.titleText.setText(project.getTitle());
        GlideApp.with(context).load(project.getImageUrl()).signature(new ObjectKey(project.getReleaseTime())).into(holder.imageView);
    }

    private static final String TAG = "ProjectRecycleViewAdapt";
    @Override
    public int getItemCount() {
        return projectList.size();
    }
}
