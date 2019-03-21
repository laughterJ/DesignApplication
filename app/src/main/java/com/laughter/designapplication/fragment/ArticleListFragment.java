package com.laughter.designapplication.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.laughter.designapplication.R;
import com.laughter.designapplication.adapter.ArticleAdapter;
import com.laughter.designapplication.model.Article;
import com.laughter.designapplication.util.HttpUtil;
import com.laughter.designapplication.util.JsonUtil;
import com.laughter.framework.views.LoadingView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ArticleListFragment extends BaseFragment implements OnRefreshListener, OnLoadMoreListener, Callback {

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
        curPage = 0;
        HttpUtil.sendOkHttpRequest("wxarticle/list/" + id + "/" + curPage + "/json", this);
        mLoadingView.start();
        mLoadingView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        curPage = 1;
        mArticleList.clear();
        mAdapter.notifyDataSetChanged();
        HttpUtil.sendOkHttpRequest("wxarticle/list/" + id + "/" + curPage + "/json", this);
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        ++ curPage;
        HttpUtil.sendOkHttpRequest("wxarticle/list/" + id + "/" + curPage + "/json", this);
    }

    @Override
    public void onFailure(@NonNull Call call, @NonNull IOException e) {

    }

    @Override
    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
        try {
            if (response.body() != null){
                String jsonData = response.body().string();
                JsonObject jsonObj = new JsonParser().parse(jsonData).getAsJsonObject();
                if (jsonObj.get("errorCode").getAsInt() == 0){
                    mArticleList.addAll(JsonUtil.getArticles(jsonObj));
                }else {
                    Toast.makeText(mContext, jsonObj.get("errorMsg").getAsString(), Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            ((Activity)mContext).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mAdapter.notifyDataSetChanged();
                    mLoadingView.cancle();
                    mLoadingView.setVisibility(View.GONE);
                    mRefreshLayout.finishLoadMore();
                    mRefreshLayout.finishRefresh();
                }
            });
        }
    }
}
