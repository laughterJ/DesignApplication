package com.laughter.designapplication.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.flexbox.FlexboxLayout;
import com.laughter.designapplication.R;
import com.laughter.designapplication.model.Tree;

import java.util.List;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者： 江浩
 * 创建时间： 2019/3/8
 * 版权： 江苏远大信息股份有限公司
 * 描述： com.laughter.designapplication.adapter
 */
public class TreesAdapter extends RecyclerView.Adapter<TreesAdapter.ViewHolder> {
    private Context mContet;
    private List<Tree> trees;

    public TreesAdapter(Context context, List<Tree> trees) {
        this.mContet = context;
        this.trees = trees;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContet).inflate(R.layout.item_trees, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        Tree mTree = trees.get(position);
        viewHolder.tvTitle.setText(mTree.getName());
        viewHolder.fblLabel.removeAllViews();
        int index = 0;
        while (index < mTree.getChildTrsss().size() && index < 6){
            TextView tvLabel = new TextView(mContet);
            tvLabel.setText(mTree.getChildTrsss().get(index++).getName());
            tvLabel.setTextSize(16);
            tvLabel.setTextColor(viewHolder.colorWeakBlack);
            tvLabel.setPadding(0, 0, 32, 0);
            viewHolder.fblLabel.addView(tvLabel);
        }
        if (index < mTree.getChildTrsss().size()){
            TextView tvLabel = new TextView(mContet);
            tvLabel.setText("...");
            tvLabel.setTextSize(16);
            tvLabel.setTextColor(viewHolder.colorWeakBlack);
            viewHolder.fblLabel.addView(tvLabel);
        }
    }

    @Override
    public int getItemCount() {
        return trees.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_title) TextView tvTitle;
        @BindView(R.id.fbl_label) FlexboxLayout fblLabel;
        @BindColor(R.color.colorWeakBlack) int colorWeakBlack;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
