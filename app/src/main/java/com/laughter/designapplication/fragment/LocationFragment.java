package com.laughter.designapplication.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.laughter.designapplication.R;

import butterknife.ButterKnife;

public class LocationFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_location, parent, false);
        ButterKnife.bind(this ,view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        MaterialHeader mHeader = new MaterialHeader(mContext);
//        mHeader.setColorSchemeColors(colorAccent);
//        mHeader.setBackgroundColor(colorWhite);
//        srlTree.setRefreshHeader(mHeader);
//        BallPulseFooter mFooter = new BallPulseFooter(mContext);
//        srlTree.setRefreshFooter(mFooter);
//        srlTree.setOnRefreshListener(this);
//        srlTree.setOnLoadMoreListener(this);
    }
}
