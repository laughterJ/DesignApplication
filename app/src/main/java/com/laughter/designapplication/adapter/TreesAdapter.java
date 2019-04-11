package com.laughter.designapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.flexbox.FlexboxLayout;
import com.laughter.designapplication.R;
import com.laughter.designapplication.activity.KnowledgeActivity;
import com.laughter.designapplication.model.Tree;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * created by JH at 2019/4/11
 * des： com.laughter.designapplication.adapter
 */
public class TreesAdapter extends RecyclerView.Adapter<TreesAdapter.ViewHolder> {
    private Context mContext;
    private List<Tree> trees;

    public TreesAdapter(Context context, List<Tree> trees) {
        this.mContext = context;
        this.trees = trees;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_trees, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        Tree mTree = trees.get(position);
        viewHolder.tvTitle.setText(mTree.getName());
        viewHolder.fblLabel.removeAllViews();
        int index = 0;
        while (index < mTree.getChildTrees().size() && index < 6){
            TextView tvLabel = new TextView(mContext);
            tvLabel.setText(mTree.getChildTrees().get(index++).getName());
            tvLabel.setTextSize(16);
            tvLabel.setTextColor(viewHolder.colorWeakBlack);
            tvLabel.setPadding(0, 0, 32, 0);
            viewHolder.fblLabel.addView(tvLabel);
        }
        if (index < mTree.getChildTrees().size()){
            TextView tvLabel = new TextView(mContext);
            tvLabel.setText("...");
            tvLabel.setTextSize(16);
            tvLabel.setTextColor(viewHolder.colorWeakBlack);
            viewHolder.fblLabel.addView(tvLabel);
        }

        viewHolder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, KnowledgeActivity.class);
            intent.putParcelableArrayListExtra("items", new ArrayList<>(mTree.getChildTrees()));
            intent.putExtra("title", mTree.getName());
            mContext.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return trees.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_title) TextView tvTitle;
        @BindView(R.id.fbl_label) FlexboxLayout fblLabel;
        @BindColor(R.color.colorWeakBlack) int colorWeakBlack;

        View itemView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            ButterKnife.bind(this, itemView);
        }
    }
}
