package com.laughter.designapplication.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.laughter.designapplication.HttpCallbackListener;
import com.laughter.designapplication.R;
import com.laughter.designapplication.adapter.ProjectAdapter;
import com.laughter.designapplication.model.Project;
import com.laughter.designapplication.model.Tree;
import com.laughter.designapplication.util.HttpUtil;
import com.laughter.designapplication.util.JsonUtil;
import com.laughter.framework.views.LoadingView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class ProjectFragment extends BaseFragment implements HttpCallbackListener, OnRefreshListener, OnLoadMoreListener {

    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.srl) SmartRefreshLayout mRefreshLayout;
    @BindView(R.id.rv) RecyclerView mRecyclerView;
    @BindView(R.id.loading_project) LoadingView mLoadingView;

    List<Tree> menuItems;
    private List<Project> mProjects;
    private ProjectAdapter mAdapter;
    private int curPage = 1;
    private String cid;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_project;
    }

    @Override
    public void initView() {
        mToolbar.setTitle("项目");
        ((AppCompatActivity)mContext).setSupportActionBar(mToolbar);

        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setOnLoadMoreListener(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mProjects = new ArrayList<>();
        mAdapter = new ProjectAdapter(mContext, mProjects);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void initData() {
        menuItems = LitePal.findAll(Tree.class);
        cid = menuItems.get(0).getCid();
        HttpUtil.sendGetRequest("project/list/" + curPage + "/json?cid=" + cid, 1, this);
        mLoadingView.start();
        mLoadingView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        for (int i=0;i<menuItems.size();i++){
            menu.add(0, i, 0, menuItems.get(i).getName());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.e("coder", item.getOrder()+"");
        cid = menuItems.get(item.getItemId()).getCid();
        mLoadingView.start();
        mLoadingView.setVisibility(View.VISIBLE);
        onRefresh(mRefreshLayout);
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        curPage = 1;
        mProjects.clear();
        mAdapter.notifyDataSetChanged();
        HttpUtil.sendGetRequest("project/list/" + curPage + "/json?cid=" + cid, 1, this);
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        ++curPage;
        HttpUtil.sendGetRequest("project/list/" + curPage + "/json?cid=" + cid, 1, this);
    }

    @Override
    public void onFinish(int requestId, String response, String cookie) {
        try {
            if (response != null){
                JsonObject jsonObj = new JsonParser().parse(response).getAsJsonObject();
                if (jsonObj.get("errorCode").getAsInt() == 0){
                    mProjects.addAll(JsonUtil.getProjects(jsonObj));
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
                    mRefreshLayout.finishRefresh();
                    mRefreshLayout.finishLoadMore();
                    mLoadingView.setVisibility(View.GONE);
                    mLoadingView.cancle();
                }
            });
        }
    }

    @Override
    public void onFailure(Exception e) {

    }
}
