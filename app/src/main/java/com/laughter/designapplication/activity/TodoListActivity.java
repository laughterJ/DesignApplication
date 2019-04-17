package com.laughter.designapplication.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.laughter.designapplication.R;
import com.laughter.designapplication.adapter.TodoAdapter;
import com.laughter.designapplication.fragment.TodoAddFragment;
import com.laughter.designapplication.model.TodoItem;
import com.laughter.designapplication.util.HttpUtil;
import com.laughter.designapplication.util.JsonUtil;
import com.laughter.framework.activity.BaseActivity;
import com.laughter.framework.util.SpUtil;
import com.laughter.framework.util.ToastUtil;
import com.laughter.framework.views.LoadingListView;
import com.laughter.framework.views.LoadingView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * created by JH at 2019/4/16
 * des： com.laughter.designapplication.activity
 */

public class TodoListActivity extends BaseActivity implements HttpUtil.HttpCallbackListener,
        LoadingListView.OnLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.loading_view) LoadingView mLoadingView;
    @BindView(R.id.llv_list) LoadingListView mListView;
    @BindView(R.id.srl_todo) SwipeRefreshLayout srlTodo;
    @BindView(R.id.tv_empty) TextView tvEmpty;

    @BindColor(R.color.colorPrimary) int primaryColor;

    private Fragment mTodoAddFragment;

    private List<TodoItem> todoItems;
    private TodoAdapter mAdapter;

    private int curPage = 1;
    private int status = 0;
    private int orderby = 3;

    @Override
    public int getLayout() {
        return R.layout.activity_todo_list;
    }

    @Override
    public void initView() {
        mToolbar.setTitle("TodoList");
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_back);
        mToolbar.setNavigationOnClickListener(v -> finish());

        srlTodo.setColorSchemeColors(primaryColor);
        srlTodo.setOnRefreshListener(this);
        mListView.addFooterView(LayoutInflater.from(this).inflate(R.layout.layout_footer_view, mListView, false));
        mListView.setOnLoadMoreListener(this);
    }

    @Override
    public void initData() {
        todoItems = new ArrayList<>();
        curPage = 1;
        String path = getPath();
        String localCookie = SpUtil.getString(this, "Cookie", null);
        HttpUtil.get(path, 0, localCookie, this);
    }

    @OnClick({R.id.fab_add})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_add:
                Bundle args = new Bundle();
                args.putBoolean("isAdded", false);
                showFragment(args);
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.todo_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        status = (status == 0 ? 1: 0);
        orderby = (orderby == 3 ? 2 : 3);
        showLoading();
        updateList();
        return true;
    }

    public void showFragment(Bundle args) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.setCustomAnimations(R.anim.anim_enter, R.anim.anim_exit);
        mTodoAddFragment = manager.findFragmentByTag("todo");
        if (mTodoAddFragment == null){
            mTodoAddFragment = TodoAddFragment.newInstance(args);
            transaction.add(R.id.fl_add, mTodoAddFragment, "todo");
        }
        transaction.show(mTodoAddFragment);
        transaction.commit();
    }

    public void hideFragment() {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.setCustomAnimations(R.anim.anim_enter, R.anim.anim_exit);
        transaction.remove(mTodoAddFragment);
        transaction.commit();
    }

    public void updateList() {
        todoItems.clear();
        curPage = 1;
        String path = getPath();
        String localCookie = SpUtil.getString(this, "Cookie", null);
        HttpUtil.get(path, 0, localCookie, this);
    }

    public void showLoading() {
        mLoadingView.start();
        mLoadingView.setVisibility(View.VISIBLE);
    }

    public void hideLoading() {
        mLoadingView.setVisibility(View.GONE);
        mLoadingView.cancle();
    }

    @Override
    public void onRefresh() {
        srlTodo.setRefreshing(true);
        todoItems.clear();
        curPage = 1;
        String path = getPath();
        String localCookie = SpUtil.getString(this, "Cookie", null);
        HttpUtil.get(path, 0, localCookie, this);
    }

    @Override
    public void onLoadMore() {
        ++ curPage;
        String path = getPath();
        String localCookie = SpUtil.getString(this, "Cookie", null);
        HttpUtil.get(path, 0, localCookie, this);
    }

    private String getPath() {
        return "lg/todo/v2/list/" + curPage + "/json?" + "status=" + status + "&orderby=" + orderby;
    }

    @Override
    public void onFinish(int requestId, String response, String cookie) {
        switch (requestId) {
            case 0:
                runOnUiThread(() -> {
                    if (JsonUtil.getErrorCode(response) == 0){
                        todoItems.addAll(JsonUtil.getTodoList(response));
                        if (todoItems.size() == 0){
                            tvEmpty.setVisibility(View.VISIBLE);
                        }else {
                            tvEmpty.setVisibility(View.GONE);
                        }
                        if (mAdapter == null){
                            mAdapter = new TodoAdapter(this, todoItems);
                            mListView.setAdapter(mAdapter);
                        }else {
                            mAdapter.notifyDataSetChanged();
                        }
                        if (srlTodo.isRefreshing()) {
                            srlTodo.setRefreshing(false);
                        }
                        mListView.setLoadCompleted();
                        hideLoading();
                    }else {
                        ToastUtil.showShortToast(this, JsonUtil.getErrorMsg(response));
                    }
                });
                break;
            default:
                break;
        }
    }

    @Override
    public void onFailure(Exception e) {
        e.printStackTrace();
    }

    @Override
    public void onBackPressed() {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.setCustomAnimations(R.anim.anim_enter, R.anim.anim_exit);
        if (mTodoAddFragment != null && mTodoAddFragment.isVisible()){
            transaction.hide(mTodoAddFragment);
            transaction.commit();
        }else {
            finish();
        }
    }
}
