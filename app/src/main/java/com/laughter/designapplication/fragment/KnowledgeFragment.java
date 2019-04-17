package com.laughter.designapplication.fragment;

import android.app.Activity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.laughter.designapplication.R;
import com.laughter.designapplication.adapter.TreesAdapter;
import com.laughter.designapplication.model.Tree;
import com.laughter.designapplication.util.JsonUtil;
import com.laughter.designapplication.util.HttpUtil;
import com.laughter.framework.fragment.BaseFragment;
import com.laughter.framework.views.LoadingView;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindColor;
import butterknife.BindView;

/**
 * created by JH at 2019/4/11
 * des： com.laughter.designapplication.fragment
 */

public class KnowledgeFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, HttpUtil.HttpCallbackListener {

    @BindView(R.id.srl_tree) SwipeRefreshLayout srlTree;
    @BindView(R.id.rl_tree) RecyclerView rlTree;
    @BindView(R.id.loading_trees) LoadingView mLoadingView;
    @BindView(R.id.toolbar) Toolbar mToolbar;

    @BindColor(R.color.colorPrimary) int colorPrimary;
    @BindColor(R.color.colorWhite) int colorWhite;

    private List<Tree> trees;
    private TreesAdapter mAdapter;

    @Override
    public int getLayout() {
        return R.layout.fragment_knowledge;
    }

    @Override
    public void initView() {
        mToolbar.setTitle("体系");
        ((AppCompatActivity)mContext).setSupportActionBar(mToolbar);

        srlTree.setColorSchemeColors(colorPrimary);
        srlTree.setBackgroundColor(colorWhite);
        srlTree.setRefreshing(false);
        srlTree.setOnRefreshListener(this);
    }

    @Override
    public void initData() {
        trees = new ArrayList<>();
        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(mContext);
        layoutManager.setFlexWrap(FlexWrap.WRAP);
        layoutManager.setFlexDirection(FlexDirection.ROW);
        rlTree.setLayoutManager(layoutManager);
        mAdapter = new TreesAdapter(mContext, trees);
        rlTree.setAdapter(mAdapter);

        mLoadingView.start();
        mLoadingView.setVisibility(View.VISIBLE);
        HttpUtil.get("tree/json", 0, null, this);
    }

    @Override
    public void onRefresh() {
        trees.clear();
        mAdapter.notifyDataSetChanged();
        HttpUtil.get("tree/json", 0, null, this);
    }

    @Override
    public void onFinish(int requestId, String response, String cookie) {
        try{
            JsonObject jsonObj = new JsonParser().parse(response).getAsJsonObject();
            if (jsonObj.get("errorCode").getAsInt() == 0){
                trees.addAll(JsonUtil.getTrees(jsonObj));
            }else {
                Toast.makeText(mContext, jsonObj.get("errorMsg").getAsString(), Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            ((Activity)mContext).runOnUiThread(() -> {
                mLoadingView.setVisibility(View.GONE);
                mLoadingView.cancle();
                if (srlTree.isRefreshing()){
                    srlTree.setRefreshing(false);
                }
                mAdapter.notifyDataSetChanged();
            });
        }
    }

    @Override
    public void onFailure(Exception e) {

    }
}
