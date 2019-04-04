package com.laughter.framework.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import com.laughter.framework.OnLoadMoreListener;

/**
 * 作者： 江浩
 * 创建时间： 2019/4/4
 * 版权： 江苏远大信息股份有限公司
 * 描述： com.laughter.framework.views
 */
public class RefreshListView extends ListView implements AbsListView.OnScrollListener {

    private int mTotalItemCount;//item总数
    private boolean isLoading;
    private OnLoadMoreListener mLoadingListener;

    private View mLoadingView;

    public RefreshListView(Context context) {
        this(context, null);
    }

    public RefreshListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setOverScrollMode(OVER_SCROLL_NEVER);
        setOnScrollListener(this);
    }

    public void setOnLoadMoreListener(OnLoadMoreListener listener){
        this.mLoadingListener = listener;
    }

    public void setLoadCompleted() {
        isLoading = false;
        removeFooterView(mLoadingView);
    }

    @Override
    public void onScrollStateChanged(AbsListView listView, int scrollState) {
        int lastVisibleIndex = listView.getLastVisiblePosition();
        if (!isLoading && scrollState == SCROLL_STATE_IDLE && lastVisibleIndex == mTotalItemCount-1){
            isLoading = true;
            addFooterView(mLoadingView);
            if (mLoadingListener != null){
                mLoadingListener.onLoadMore();
            }
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        mTotalItemCount = totalItemCount;
    }

    @Override
    public void addFooterView(View v) {
        mLoadingView = v;
        super.addFooterView(mLoadingView);
    }
}
