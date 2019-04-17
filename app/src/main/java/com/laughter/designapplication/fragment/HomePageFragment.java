package com.laughter.designapplication.fragment;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.laughter.designapplication.R;
import com.laughter.designapplication.activity.DetailActivity;
import com.laughter.designapplication.adapter.ArticleAdapter;
import com.laughter.designapplication.model.Article;
import com.laughter.designapplication.model.Banner;
import com.laughter.designapplication.util.JsonUtil;
import com.laughter.designapplication.util.HttpUtil;
import com.laughter.framework.fragment.BaseFragment;
import com.laughter.framework.util.ToastUtil;
import com.laughter.framework.views.BannerView;
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

public class HomePageFragment extends BaseFragment implements OnRefreshListener, OnLoadMoreListener,
        HttpUtil.HttpCallbackListener, BannerView.OnItemClickListener {

    @BindView(R.id.banner_view)
    BannerView mBannerView;

    @BindView(R.id.srl)
    SmartRefreshLayout mRefreshLayout;

    @BindView(R.id.rv)
    RecyclerView mRecyclerView;

    @BindView(R.id.loading_homepage)
    LoadingView mLoadingView;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    private final static int BANNER_REQUEST_ID = 0;
    private final static int ARTICLE_REQUEST_ID = 1;

    private List<Banner> banners;
    private List<String> imgs;
    private List<String> titles;
    private List<Article> mArticleList;
    private ArticleAdapter mAdapter;

    private int curPage;

    @Override
    public int getLayout() {
        return R.layout.fragment_home_page;
    }

    @Override
    public void initView() {
        mToolbar.setTitle("首页");
        ((AppCompatActivity)mContext).setSupportActionBar(mToolbar);

        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setOnLoadMoreListener(this);

        mBannerView.setOnItemClickListener(this);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mArticleList = new ArrayList<>();
        mAdapter = new ArticleAdapter(mContext, mArticleList);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void initData() {
        banners = new ArrayList<>();
        imgs = new ArrayList<>();
        titles = new ArrayList<>();
        HttpUtil.get("banner/json", BANNER_REQUEST_ID, null, this);
        curPage = 1;
        HttpUtil.get("article/list/" +curPage + "/json", ARTICLE_REQUEST_ID, null, this);
        mLoadingView.start();
        mLoadingView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onFinish(int requestId, String response, String cookie) {
        ((Activity) mContext).runOnUiThread(() -> {
            if (JsonUtil.getErrorCode(response) == 0){
                if (requestId == BANNER_REQUEST_ID){
                    banners.addAll(JsonUtil.getBanners(response));
                    for(int i=0;i<banners.size();i++){
                        imgs.add(banners.get(i).getImagePath());
                        titles.add(banners.get(i).getTitle());
                    }
                    mBannerView.setImageUrl(imgs);
                    mBannerView.setTitle(titles);
                    mBannerView.setmIndicaterStyle(R.drawable.dot_bg_selector);
                    mBannerView.build();
                }
                if (requestId == ARTICLE_REQUEST_ID){
                    mArticleList.addAll(JsonUtil.getArticles(response));
                    mAdapter.notifyDataSetChanged();
                    mBannerView.setVisibility(View.VISIBLE);
                }
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
        e.printStackTrace();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        mArticleList.clear();
        mAdapter.notifyDataSetChanged();
        curPage = 1;
        HttpUtil.get("article/list/" +curPage + "/json", ARTICLE_REQUEST_ID, null, this);
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        ++curPage;
        HttpUtil.get("article/list/" +curPage + "/json", ARTICLE_REQUEST_ID, null, HomePageFragment.this);
    }

    @Override
    public void onItemClick(int index) {
        Intent intent = new Intent(mContext, DetailActivity.class);
        intent.putExtra("title", titles.get(index));
        intent.putExtra("link", banners.get(index).getUrl());
        mContext.startActivity(intent);
    }
}
