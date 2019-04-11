package com.laughter.designapplication.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import com.laughter.designapplication.R;
import com.laughter.designapplication.adapter.TabberAdapter;
import com.laughter.designapplication.fragment.ArticleListFragment;
import com.laughter.designapplication.model.Tree;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class KnowledgeActivity extends BaseActivity {

    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.tabber_knowledge) TabLayout mTabLayout;
    @BindView(R.id.vp_knowledge) ViewPager mViewPager;

    private List<ArticleListFragment> fragments;
    private List<String> tabNames;

    @Override
    public int getLayout() {
        return R.layout.activity_knowledge;
    }

    @Override
    public void initView() {
        mToolbar.setTitle(getIntent().getStringExtra("title"));
        setSupportActionBar(mToolbar);
    }

    @Override
    public void initData() {
        fragments = new ArrayList<>();
        tabNames = new ArrayList<>();
        List<Tree> trees = getIntent().getParcelableArrayListExtra("items");
        for (int i =0;i<trees.size(); i++){
            fragments.add(ArticleListFragment.newInstance(trees.get(i).getCid()));
            tabNames.add(trees.get(i).getName());
        }
        TabberAdapter adapter = new TabberAdapter(getSupportFragmentManager(), fragments, tabNames);
        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }
}
