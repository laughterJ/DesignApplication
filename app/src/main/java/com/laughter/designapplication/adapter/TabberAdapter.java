package com.laughter.designapplication.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.laughter.designapplication.fragment.ArticleListFragment;

import java.util.List;

/**
 * created by JH at 2019/4/11
 * des： com.laughter.designapplication.adapter
 */
public class TabberAdapter extends FragmentStatePagerAdapter {

    private List<ArticleListFragment> fragments;
    private List<String> tabNames;

    public TabberAdapter(FragmentManager fm, List<ArticleListFragment> fragments, List<String> tabNames) {
        super(fm);
        this.fragments = fragments;
        this.tabNames = tabNames;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return tabNames.get(position);
    }
}
