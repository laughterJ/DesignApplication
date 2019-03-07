package com.laughter.designapplication.activity;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.laughter.designapplication.R;
import com.laughter.designapplication.fragment.HomePageFragment;
import com.laughter.designapplication.fragment.KnowledgeFragment;
import com.laughter.designapplication.fragment.LocationFragment;
import com.laughter.designapplication.fragment.ProjectFragment;
import com.laughter.designapplication.fragment.TodoListFragment;
import com.roughike.bottombar.BottomBar;

import butterknife.BindView;

/**
 * 作者： 江浩
 * 创建时间： 2018/12/11
 * 描述： com.example.designapplication.activity
 */

public class MainActivity extends BaseActivity {

    @BindView(R.id.bottomBar) BottomBar mBottomBar;
    @BindView(R.id.toolbar) Toolbar mToolbar;

    private FragmentManager mFragmentManager;
    private FragmentTransaction mTranscation;

    private HomePageFragment mHomePageFragment;
    private KnowledgeFragment mKnowledgeFragment;
    private ProjectFragment mProjectFragment;
    private LocationFragment mLocationFragment;
    private TodoListFragment mTodoListFragment;

    private long lastPressTime = System.currentTimeMillis();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {
        mToolbar.setTitle("首页");
        setSupportActionBar(mToolbar);
        mFragmentManager = getSupportFragmentManager();
        mBottomBar.setOnTabSelectListener(tabId -> {
            mTranscation = mFragmentManager.beginTransaction();
            // 隐藏所有 Fragment
            hideFragments();
            switch (tabId){
                case R.id.tab_homepage:
                    if (mHomePageFragment == null){
                        mHomePageFragment = new HomePageFragment();
                        mTranscation.add(R.id.continer, mHomePageFragment, "HomePage");
                    }else {
                        mTranscation.show(mHomePageFragment);
                    }
                    break;
                case R.id.tab_knowledge:
                    if (mKnowledgeFragment == null){
                        mKnowledgeFragment = new KnowledgeFragment();
                        mTranscation.add(R.id.continer, mKnowledgeFragment, "Knowledge");
                    }else {
                        mTranscation.show(mKnowledgeFragment);
                    }
                    break;
                case R.id.tab_project:
                    if (mProjectFragment == null){
                        mProjectFragment = new ProjectFragment();
                        mTranscation.add(R.id.continer, mProjectFragment, "Project");
                    }else {
                        mTranscation.show(mProjectFragment);
                    }
                    break;
                case R.id.tab_location:
                    if (mLocationFragment == null){
                        mLocationFragment = new LocationFragment();
                        mTranscation.add(R.id.continer, mLocationFragment, "Location");
                    }else {
                        mTranscation.show(mLocationFragment);
                    }
                    break;
                case R.id.tab_todolist:
                    if (mTodoListFragment == null){
                        mTodoListFragment = new TodoListFragment();
                        mTranscation.add(R.id.continer, mTodoListFragment, "TodoList");
                    }else {
                        mTranscation.show(mTodoListFragment);
                    }
                    break;
                default:
                    break;
            }
            mTranscation.commit();
        });
    }

    @Override
    public void initData() {

    }

    private void hideFragments() {
        mHomePageFragment = (HomePageFragment) mFragmentManager.findFragmentByTag("HomePage");
        mKnowledgeFragment = (KnowledgeFragment) mFragmentManager.findFragmentByTag("Knowledge");
        mProjectFragment = (ProjectFragment) mFragmentManager.findFragmentByTag("Project");
        mLocationFragment = (LocationFragment) mFragmentManager.findFragmentByTag("Location");
        mTodoListFragment = (TodoListFragment) mFragmentManager.findFragmentByTag("TodoList");

        if (mHomePageFragment != null){
            mTranscation.hide(mHomePageFragment);
        }
        if (mKnowledgeFragment != null){
            mTranscation.hide(mKnowledgeFragment);
        }
        if (mProjectFragment != null){
            mTranscation.hide(mProjectFragment);
        }
        if (mLocationFragment != null){
            mTranscation.hide(mLocationFragment);
        }
        if (mTodoListFragment != null){
            mTranscation.hide(mTodoListFragment);
        }
    }

    @Override
    public void onBackPressed() {
        long nowTime = System.currentTimeMillis();
        if (nowTime - lastPressTime > 1000) {
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            lastPressTime = nowTime;
        } else{
            finish();
        }
    }
}
