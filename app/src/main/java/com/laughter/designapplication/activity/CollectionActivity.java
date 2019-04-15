package com.laughter.designapplication.activity;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.laughter.designapplication.HttpCallbackListener;
import com.laughter.designapplication.R;
import com.laughter.designapplication.adapter.CollectionAdapter;
import com.laughter.designapplication.model.Article;
import com.laughter.designapplication.util.JsonUtil;
import com.laughter.designapplication.util.HttpUtil;
import com.laughter.framework.activity.BaseActivity;
import com.laughter.framework.util.SpUtil;
import com.laughter.framework.util.ToastUtil;
import com.laughter.framework.views.LoadingListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindColor;
import butterknife.BindView;

/**
 * created by JH at 2019/4/11
 * des： com.laughter.designapplication.activity
 */

public class CollectionActivity extends BaseActivity implements LoadingListView.OnLoadMoreListener, SwipeRefreshLayout.OnRefreshListener, HttpCallbackListener {

    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.rlv_collection)
    LoadingListView mListView;
    @BindView(R.id.srl_collection) SwipeRefreshLayout mRefreshLayout;

    @BindColor(R.color.colorPrimary) int primaryColor;

    private List<Article> articles;
    private CollectionAdapter mAdapter;
    private int curPage;

    @Override
    public int getLayout() {
        return R.layout.activity_collection;
    }

    @Override
    public void initView() {
        mToolbar.setTitle("我的收藏");
        setSupportActionBar(mToolbar);

        mRefreshLayout.setColorSchemeColors(primaryColor);
        mRefreshLayout.setOnRefreshListener(this);

        mListView.addFooterView(LayoutInflater.from(this).inflate(R.layout.layout_footer_view, mListView, false));
        mListView.setOnLoadMoreListener(this);
        articles = new ArrayList<>();
        mAdapter = new CollectionAdapter(this, articles);
        mListView.setAdapter(mAdapter);
    }

    @Override
    public void initData() {
        curPage = 0;
        String path = "lg/collect/list/" + curPage + "/json";
        String localCookie = SpUtil.getString(this, "Cookie", null);
        HttpUtil.get(path, 0, localCookie, this);
    }

    @Override
    public void onRefresh() {
        articles.clear();
        curPage = 0;
        String path = "lg/collect/list/" + curPage + "/json";
        String localCookie = SpUtil.getString(this, "Cookie", null);
        HttpUtil.get(path, 0, localCookie, this);
    }

    @Override
    public void onLoadMore() {
        curPage++;
        String path = "lg/collect/list/" + curPage + "/json";
        String localCookie = SpUtil.getString(this, "Cookie", null);
        HttpUtil.get(path, 0, localCookie, this);
    }

    @Override
    public void onFinish(int requestId, String response, String cookie) {
        if (requestId == 0){
            runOnUiThread(() -> {
                if (JsonUtil.getErrorCode(response) == 0){
                    articles.addAll(JsonUtil.getArticles(response));
                    mAdapter.notifyDataSetChanged();
                    mRefreshLayout.setRefreshing(false);
                    mListView.setLoadCompleted();
                }else {
                    ToastUtil.showShortToast(CollectionActivity.this, JsonUtil.getErrorMsg(response));
                }
            });
        }
    }

    @Override
    public void onFailure(Exception e) {

    }
}
