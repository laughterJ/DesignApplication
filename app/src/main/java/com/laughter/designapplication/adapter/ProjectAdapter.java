package com.laughter.designapplication.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
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
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.laughter.designapplication.HttpCallbackListener;
import com.laughter.designapplication.R;
import com.laughter.designapplication.activity.DetailActivity;
import com.laughter.designapplication.model.Project;
import com.laughter.designapplication.util.NewHttpUtil;
import com.laughter.framework.util.SpUtil;
import com.laughter.framework.util.ToastUtil;

import java.util.List;

import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者： 江浩
 * 创建时间： 2019/3/14
 * 版权： 江苏远大信息股份有限公司
 * 描述： com.laughter.designapplication.adapter
 */
public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.ViewHolder> implements HttpCallbackListener {

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

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, DetailActivity.class);
                intent.putExtra("title", project.getTitle());
                intent.putExtra("link", project.getLink());
                mContext.startActivity(intent);
            }
        });

        viewHolder.ibLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SpUtil.getBoolean(mContext, "isLogin", false)){
                    viewHolder.ibLike.setBackground(viewHolder.icCollected);
                    String path = "lg/collect/" + project.getId() + "/json";
                    String localCookie = SpUtil.getString(mContext, "Cookie", null);
                    NewHttpUtil.post(path, 0, null, localCookie, false, ProjectAdapter.this);
                }else {
                    ToastUtil.showShortToast(mContext, "请先登录");
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        Log.d("coder", projects.size()+"");
        return projects.size();
    }

    @Override
    public void onFinish(int requestId, String response, String cookie) {
        if (requestId == 0){
            JsonObject jsonObj = new JsonParser().parse(response).getAsJsonObject();
            ((Activity)mContext).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (jsonObj.get("errorCode").getAsInt() == 0){
                        ToastUtil.showShortToast(mContext, "收藏成功");
                    }else {
                        ToastUtil.showShortToast(mContext, jsonObj.get("errorMsg").getAsString());
                    }
                }
            });
        }
    }

    @Override
    public void onFailure(Exception e) {

    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_title) TextView tvTitle;
        @BindView(R.id.tv_desc) TextView tvDesc;
        @BindView(R.id.img_pic) ImageView imgPic;
        @BindView(R.id.tv_author) TextView tvAuthor;
        @BindView(R.id.tv_date) TextView tvDate;
        @BindView(R.id.ib_like) ImageButton ibLike;
        @BindDrawable(R.drawable.ic_collected) Drawable icCollected;

        View itemView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            ButterKnife.bind(this, itemView);
        }
    }
}
