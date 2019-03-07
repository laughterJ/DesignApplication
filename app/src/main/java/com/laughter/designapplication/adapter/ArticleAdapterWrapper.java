package com.laughter.designapplication.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * 作者： 江浩
 * 创建时间： 2019/2/27
 * 版权： 江苏远大信息股份有限公司
 * 描述： com.laughter.test
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
