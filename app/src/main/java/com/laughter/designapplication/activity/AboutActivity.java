package com.laughter.designapplication.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.TextView;

import com.laughter.designapplication.R;
import com.laughter.framework.activity.BaseActivity;

import butterknife.BindColor;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.OnClick;

public class AboutActivity extends BaseActivity {

    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.tv_blog) TextView tvBlog;
    @BindView(R.id.tv_github) TextView tvGithub;
    @BindView(R.id.tv_version) TextView tvVersion;

    @BindString(R.string.version) String version;
    @BindColor(R.color.colorPrimary) int primaryColor;

    @Override
    public int getLayout() {
        return R.layout.activity_about;
    }

    @Override
    public void initView() {
        mToolbar.setTitle("关于作者");
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_back);
        mToolbar.setNavigationOnClickListener(v -> finish());
    }

    @Override
    public void initData() {
        try {
            PackageInfo mPackageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            tvVersion.setText(String.format(version, mPackageInfo.versionName));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        SpannableString spanStr = new SpannableString("简书：https://www.jianshu.com/u/0243d7c1921d");
        spanStr.setSpan(new UnderlineSpan(), 3, spanStr.length(), 0);
        spanStr.setSpan(new ForegroundColorSpan(primaryColor), 3, spanStr.length(), 0);
        tvBlog.setText(spanStr);

        spanStr = new SpannableString("Github：https://github.com/laughterJ/DesignApplication");
        spanStr.setSpan(new UnderlineSpan(), 7, spanStr.length(), 1);
        spanStr.setSpan(new ForegroundColorSpan(primaryColor), 7, spanStr.length(), 1);
        tvGithub.setText(spanStr);
    }

    @OnClick({R.id.tv_blog, R.id.tv_github})
    public void onClick(View v) {
        Intent intent = new Intent(this, DetailActivity.class);;
        switch (v.getId()) {
            case R.id.tv_blog:
                intent.putExtra("title", "简书主页");
                intent.putExtra("link", "https://www.jianshu.com/u/0243d7c1921d");
                startActivity(intent);
                break;
            case R.id.tv_github:
                intent.putExtra("title", "项目地址");
                intent.putExtra("link", "https://github.com/laughterJ/DesignApplication");
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}