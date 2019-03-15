package com.laughter.designapplication.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.laughter.designapplication.application.MyApplication;

import butterknife.ButterKnife;


/**
 * 作者： 江浩
 * 创建时间： 2019/3/7
 * 描述： com.laughter.designapplication.fragment
 */

public abstract class BaseFragment extends Fragment {

    public Context mContext;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayout(), parent, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mContext = getContext() != null ? getContext() : MyApplication.getInstance();
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
