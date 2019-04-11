package com.laughter.designapplication.activity;
import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.Toolbar;
import android.webkit.WebSettings;
import android.webkit.WebViewClient;

import com.laughter.designapplication.R;
import com.laughter.framework.views.ProgressWebView;

import butterknife.BindDrawable;
import butterknife.BindView;

/**
 * 作者： 江浩
 * 创建时间： 2019/4/8
 * 版权： 江苏远大信息股份有限公司
 * 描述： com.laughter.designapplication.activity
 */
public class DetailActivity extends BaseActivity {

    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.pwv_detail) ProgressWebView mWebView;

    @BindDrawable(R.drawable.ic_back) Drawable back;

    @Override
    public int getLayout() {
        return R.layout.activity_detail;
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void initView() {
        mToolbar.setTitle(getIntent().getStringExtra("title"));
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(back);
        mToolbar.setNavigationOnClickListener(v -> finish());

        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new WebViewClient());
        mWebView.getSettings().setSupportZoom(true);
        //扩大比例的缩放
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        mWebView.getSettings().setLoadWithOverviewMode(true);
    }

    @Override
    public void initData() {
        mWebView.loadUrl(getIntent().getStringExtra("link"));
    }

    @Override
    public void onBackPressed() {
        if (mWebView.canGoBack()){
            mWebView.goBack();
        }else {
            finish();
        }
    }
}
