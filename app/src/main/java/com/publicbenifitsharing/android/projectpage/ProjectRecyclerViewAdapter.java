package com.publicbenifitsharing.android.projectpage;

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

import com.bumptech.glide.signature.ObjectKey;
import com.publicbenifitsharing.android.GlideApp;
import com.publicbenifitsharing.android.ProjectView;
import com.publicbenifitsharing.android.R;
import com.publicbenifitsharing.android.adapter.ProjectRecycleViewAdapter;
import com.publicbenifitsharing.android.entityclass.Project;

import java.util.List;

public class ProjectRecyclerViewAdapter extends RecyclerView.Adapter<ProjectRecyclerViewAdapter.ViewHolder> {
    private Context context;
    private List<Project> projectList;

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

    public ProjectRecyclerViewAdapter(List<Project> projects){this.projectList=projects;}

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
        return holder;
    }

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
