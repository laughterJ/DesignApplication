package com.laughter.designapplication.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.laughter.designapplication.R;

import butterknife.ButterKnife;

public class ProjectFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_project, parent, false);
        ButterKnife.bind(this, view);
        return view;
    }

}
