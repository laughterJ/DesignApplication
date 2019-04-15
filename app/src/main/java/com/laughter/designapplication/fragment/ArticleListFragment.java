package com.laughter.designapplication.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.laughter.designapplication.HttpCallbackListener;
import com.laughter.designapplication.R;
import com.laughter.designapplication.adapter.ArticleAdapter;
import com.laughter.designapplication.model.Article;
import com.laughter.designapplication.util.JsonUtil;
import com.laughter.designapplication.util.HttpUtil;
import com.laughter.framework.fragment.BaseFragment;
import com.laughter.framework.util.ToastUtil;
import com.laughter.framework.views.LoadingView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * created by JH at 2019/4/11
 * des： com.laughter.designapplication.fragment
 */

public class ArticleListFragment extends BaseFragment implements OnRefreshListener,
        OnLoadMoreListener, HttpCallbackListener {

    @BindView(R.id.srl)
    SmartRefreshLayout mRefreshLayout;

    @BindView(R.id.rv)
    RecyclerView mRecyclerView;

    @BindView(R.id.loading_view)
    LoadingView mLoadingView;

    private List<Article> mArticleList;
    private ArticleAdapter mAdapter;

    private int curPage;
    private String id;

    public static ArticleListFragment newInstance(String id) {
        ArticleListFragment fragment = new ArticleListFragment();
        Bundle args = new Bundle();
        args.putString("id", id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_article_list;
    }

    @Override
    public void initView() {
        if (getArguments() != null){
            id = getArguments().getString("id", "");
        }

        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setOnLoadMoreListener(this);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mArticleList = new ArrayList<>();
        mAdapter = new ArticleAdapter(mContext, mArticleList);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void initData() {
        curPage = 1;
        HttpUtil.get("wxarticle/list/" + id + "/" + curPage + "/json", 0, null, this);
        mLoadingView.start();
        mLoadingView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        curPage = 1;
        mArticleList.clear();
        mAdapter.notifyDataSetChanged();
        HttpUtil.get("wxarticle/list/" + id + "/" + curPage + "/json", 0, null, this);
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        ++ curPage;
        HttpUtil.get("wxarticle/list/" + id + "/" + curPage + "/json", 0, null, this);
    }

    @Override
    public void onFinish(int requestId, String response, String cookie) {
        ((Activity)mContext).runOnUiThread(() -> {
            if (JsonUtil.getErrorCode(response) == 0){
                mArticleList.addAll(JsonUtil.getArticles(response));
                mAdapter.notifyDataSetChanged();
                mLoadingView.cancle();
                mLoadingView.setVisibility(View.GONE);
                mRefreshLayout.finishLoadMore();
                mRefreshLayout.finishRefresh();
            }else {
                ToastUtil.showShortToast(mContext, JsonUtil.getErrorMsg(response));
            }
        });
    }

    @Override
    public void onFailure(Exception e) {

    }
}
