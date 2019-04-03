package com.laughter.designapplication.adapter;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.laughter.designapplication.HttpCallbackListener;
import com.laughter.designapplication.R;
import com.laughter.designapplication.model.Article;
import com.laughter.designapplication.util.HttpUtil;
import com.laughter.designapplication.util.NewHttpUtil;
import com.laughter.framework.util.SpUtil;
import com.laughter.framework.util.ToastUtil;

import java.util.List;

import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者： 江浩
 * 创建时间： 2018/12/21
 * 版权： 江苏远大信息股份有限公司
 * 描述： com.laughter.network.adapter
 */
public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ViewHolder> implements HttpCallbackListener {

    private List<Article> articleList;
    private Context mContext;

    public ArticleAdapter(Context mContext, List<Article> articleList) {
        this.articleList = articleList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_article, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        Article article = articleList.get(position);
        viewHolder.tvTitle.setText(article.getTitle());
        viewHolder.tvAuthor.setText(article.getAuthor());
        if (article.getTags().size() > 0 && article.getTags().get(0).getName() != null){
            viewHolder.tvTag.setText(article.getTags().get(0).getName());
            viewHolder.tvTag.setVisibility(View.VISIBLE);
        }else {
            viewHolder.tvTag.setVisibility(View.GONE);
        }
        viewHolder.tvDate.setText(article.getDate());

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        viewHolder.ibLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SpUtil.getBoolean(mContext, "isLogin", false)){
                    viewHolder.ibLike.setBackground(viewHolder.icCollected);
                    String path = "lg/collect/" + article.getId() + "/json";
                    String localCookie = SpUtil.getString(mContext, "Cookie", null);
                    NewHttpUtil.post(path, 0, null, localCookie, false, ArticleAdapter.this);
                }else {
                    ToastUtil.showShortToast(mContext, "请先登录");
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return articleList.size();
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
        e.printStackTrace();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_title) TextView tvTitle;
        @BindView(R.id.tv_author) TextView tvAuthor;
        @BindView(R.id.tv_tag) TextView tvTag;
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
