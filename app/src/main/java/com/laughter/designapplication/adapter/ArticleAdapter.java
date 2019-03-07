package com.laughter.designapplication.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.laughter.designapplication.R;
import com.laughter.designapplication.model.Article;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者： 江浩
 * 创建时间： 2018/12/21
 * 版权： 江苏远大信息股份有限公司
 * 描述： com.laughter.network.adapter
 */
public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ViewHolder> {

    private List<Article> articleList;
    private Context mContext;

    public ArticleAdapter(Context mContext, List<Article> articleList) {
        this.articleList = articleList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
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
    }

    @Override
    public int getItemCount() {
        return articleList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_title) TextView tvTitle;
        @BindView(R.id.tv_author) TextView tvAuthor;
        @BindView(R.id.tv_tag) TextView tvTag;
        @BindView(R.id.tv_date) TextView tvDate;

        View itemView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            ButterKnife.bind(this, itemView);
        }
    }
}
