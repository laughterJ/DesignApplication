package com.laughter.designapplication.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import butterknife.ButterKnife;

/**
 * 作者： 江浩
 * 创建时间： 2018/12/11
 * 版权： 江苏远大信息股份有限公司
 * 描述： com.example.designapplication.activity
 */
public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        }
        setContentView(getLayout());
        ButterKnife.bind(this);
        initView();
        initData();
    }

    /**
     * 获取布局文件
     */
    public abstract int getLayout();

    /**
     * 初始化控件
     */
    public abstract void initView();

    /**
     * 初始化数据
     */
    public abstract void initData();
}
