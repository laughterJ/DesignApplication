package com.laughter.designapplication.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
/**
 * created by JH at 2019/4/11
 * des： com.laughter.designapplication.adapter
 */
public class ArticleAdapterWrapper extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    enum ITEM_TYPE{
        FOOTER,
        NORMAL
    }

    private ArticleAdapter mAdapter;
    private View mFooterView;

    public ArticleAdapterWrapper(ArticleAdapter adapter) {
        this.mAdapter = adapter;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == mAdapter.getItemCount()){
            return ITEM_TYPE.FOOTER.ordinal();
        }else {
            return ITEM_TYPE.NORMAL.ordinal();
        }
    }

    @Override
    public int getItemCount() {
        return mAdapter.getItemCount() + 1;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE.FOOTER.ordinal()){
            return new RecyclerView.ViewHolder(mFooterView){};
        }else {
            return mAdapter.onCreateViewHolder(parent, viewType);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (position != mAdapter.getItemCount()){
            mAdapter.onBindViewHolder((ArticleAdapter.ViewHolder)viewHolder, position);
        }
    }

    public void addFooterView(View view) {
        this.mFooterView = view;
    }
}
