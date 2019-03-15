package com.laughter.designapplication.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.laughter.designapplication.R;
import com.laughter.designapplication.model.Project;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者： 江浩
 * 创建时间： 2019/3/14
 * 版权： 江苏远大信息股份有限公司
 * 描述： com.laughter.designapplication.adapter
 */
public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.ViewHolder> {

    private Context mContext;
    private List<Project> projects;

    public ProjectAdapter(Context context, List<Project> projects){
        this.mContext = context;
        this.projects = projects;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_project, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        Log.e("coder", projects.get(position).getAuthor());
        Project project = projects.get(position);
        viewHolder.tvTitle.setText(project.getTitle());
        viewHolder.tvDesc.setText(project.getDesc());
        viewHolder.tvAuthor.setText(project.getAuthor());
        viewHolder.tvDate.setText(project.getNiceDate());
        Glide.with(mContext).load(project.getEnvelopePic()).into(viewHolder.imgPic);
    }

    @Override
    public int getItemCount() {
        Log.d("coder", projects.size()+"");
        return projects.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_title) TextView tvTitle;
        @BindView(R.id.tv_desc) TextView tvDesc;
        @BindView(R.id.img_pic) ImageView imgPic;
        @BindView(R.id.tv_author) TextView tvAuthor;
        @BindView(R.id.tv_date) TextView tvDate;
        @BindView(R.id.ib_like) ImageButton ibLike;

        public View itemView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            ButterKnife.bind(this, itemView);
        }
    }
}
