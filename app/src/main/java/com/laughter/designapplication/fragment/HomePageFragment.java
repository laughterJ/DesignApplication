package com.laughter.designapplication.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.laughter.designapplication.R;
import com.laughter.designapplication.adapter.ArticleAdapter;
import com.laughter.designapplication.adapter.ArticleAdapterWrapper;
import com.laughter.designapplication.model.Article;
import com.laughter.designapplication.util.HttpUtil;
import com.laughter.designapplication.util.JsonUtil;
import com.laughter.framework.views.LoadingView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class HomePageFragment extends BaseFragment implements Callback, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.srl_homepage)
    SwipeRefreshLayout mRefreshLayout;

    @BindView(R.id.rl_view)
    RecyclerView mRecyclerView;

    @BindView(R.id.loading_homepage)
    LoadingView mLoadingView;

    private List<Article> mArticleList;
    private ArticleAdapter mAdapter;
    private ArticleAdapterWrapper wrapperAdapter;

    private int curPage;
    private View mFooterView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
        mFooterView = inflater.inflate(R.layout.layout_footer_view, parent, false);
        return super.onCreateView(inflater, parent, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_home_page;
    }

    @Override
    public void initView() {
        mRefreshLayout.setRefreshing(false);
        mRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        mRefreshLayout.setOnRefreshListener(this);

        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mContext ,DividerItemDecoration.VERTICAL));
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE){
                    ++curPage;
                    HttpUtil.sendOkHttpRequest("article/list/" +curPage + "/json", HomePageFragment.this);
                }
            }
        });
        mArticleList = new ArrayList<>();
        mAdapter = new ArticleAdapter(mContext, mArticleList);
        wrapperAdapter = new ArticleAdapterWrapper(mAdapter);
        wrapperAdapter.addFooterView(mFooterView);
        mRecyclerView.setAdapter(wrapperAdapter);
    }

    @Override
    public void initData() {
        curPage = 0;
        HttpUtil.sendOkHttpRequest("article/list/" +curPage + "/json", this);
        mLoadingView.start();
        mLoadingView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onFailure(@NonNull Call call, @NonNull IOException e) {
        e.printStackTrace();
    }

    @Override
    public void onResponse(@NonNull Call call, @NonNull Response response) {
        mFooterView.setVisibility(View.VISIBLE);
        try {
            if (response.body() != null){
                String jsonData = response.body().string();
                JsonObject jsonObj = new JsonParser().parse(jsonData).getAsJsonObject();
                if (jsonObj.get("errorCode").getAsInt() == 0){
                    mArticleList.addAll(JsonUtil.getHomePageArticle(jsonObj));
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
                    wrapperAdapter.notifyDataSetChanged();
                    mLoadingView.cancle();
                    mLoadingView.setVisibility(View.GONE);
                    if (mRefreshLayout.isRefreshing()){
                        mRefreshLayout.setRefreshing(false);
                    }
                }
            });
        }
    }

    @Override
    public void onRefresh() {
        curPage = 0;
        mArticleList.clear();
        HttpUtil.sendOkHttpRequest("article/list/" +curPage + "/json", this);
    }

}
