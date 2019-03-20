package com.laughter.designapplication.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.laughter.designapplication.HttpCallbackListener;
import com.laughter.designapplication.R;
import com.laughter.designapplication.fragment.HomePageFragment;
import com.laughter.designapplication.fragment.KnowledgeFragment;
import com.laughter.designapplication.fragment.LocationFragment;
import com.laughter.designapplication.fragment.ProjectFragment;
import com.laughter.designapplication.fragment.PersonalFragment;
import com.laughter.designapplication.util.HttpUtil;
import com.laughter.designapplication.util.JsonUtil;

import org.litepal.LitePal;

import butterknife.BindView;

/**
 * 作者： 江浩
 * 创建时间： 2018/12/11
 * 描述： com.example.designapplication.activity
 */

public class MainActivity extends BaseActivity implements BottomNavigationView.OnNavigationItemSelectedListener, HttpCallbackListener {

    @BindView(R.id.bottombar)
    BottomNavigationView mBottomBar;

    private FragmentManager mFragmentManager;
    private FragmentTransaction mTranscation;

    private HomePageFragment mHomePageFragment;
    private KnowledgeFragment mKnowledgeFragment;
    private ProjectFragment mProjectFragment;
    private LocationFragment mLocationFragment;
    private PersonalFragment mPersonalFragment;

    private long lastPressTime = System.currentTimeMillis();

    @Override
    public int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        mPersonalFragment = (PersonalFragment) mFragmentManager.findFragmentByTag("TodoList");
        if (mPersonalFragment != null){
            mPersonalFragment.initView();
        }
    }

    @Override
    public void initView() {
        mBottomBar.setSelectedItemId(R.id.tab_homepage);
        mFragmentManager = getSupportFragmentManager();
        mBottomBar.setOnNavigationItemSelectedListener(this);
        mTranscation = mFragmentManager.beginTransaction();
        hideFragments();
        if (mHomePageFragment == null){
            mHomePageFragment = new HomePageFragment();
            mTranscation.add(R.id.continer, mHomePageFragment, "HomePage");
        }else {
            mTranscation.show(mHomePageFragment);
        }
        mTranscation.commit();
    }

    @Override
    public void initData() {
        HttpUtil.sendGetRequest("project/tree/json", 0, this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        mTranscation = mFragmentManager.beginTransaction();
        // 隐藏所有 Fragment
        hideFragments();
        switch (menuItem.getItemId()){
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
            case R.id.tab_personal:
                if (mPersonalFragment == null){
                    mPersonalFragment = new PersonalFragment();
                    mTranscation.add(R.id.continer, mPersonalFragment, "TodoList");
                }else {
                    mTranscation.show(mPersonalFragment);
                }
                break;
            default:
                break;
        }
        mTranscation.commit();
        return true;
    }

    private void hideFragments() {
        mHomePageFragment = (HomePageFragment) mFragmentManager.findFragmentByTag("HomePage");
        mKnowledgeFragment = (KnowledgeFragment) mFragmentManager.findFragmentByTag("Knowledge");
        mProjectFragment = (ProjectFragment) mFragmentManager.findFragmentByTag("Project");
        mLocationFragment = (LocationFragment) mFragmentManager.findFragmentByTag("Location");
        mPersonalFragment = (PersonalFragment) mFragmentManager.findFragmentByTag("TodoList");

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
        if (mPersonalFragment != null){
            mTranscation.hide(mPersonalFragment);
        }
    }

    private void removeFragments() {
        mHomePageFragment = (HomePageFragment) mFragmentManager.findFragmentByTag("HomePage");
        mKnowledgeFragment = (KnowledgeFragment) mFragmentManager.findFragmentByTag("Knowledge");
        mProjectFragment = (ProjectFragment) mFragmentManager.findFragmentByTag("Project");
        mLocationFragment = (LocationFragment) mFragmentManager.findFragmentByTag("Location");
        mPersonalFragment = (PersonalFragment) mFragmentManager.findFragmentByTag("TodoList");

        if (mHomePageFragment != null){
            mTranscation.remove(mHomePageFragment);
        }
        if (mKnowledgeFragment != null){
            mTranscation.remove(mKnowledgeFragment);
        }
        if (mProjectFragment != null){
            mTranscation.remove(mProjectFragment);
        }
        if (mLocationFragment != null){
            mTranscation.remove(mLocationFragment);
        }
        if (mPersonalFragment != null){
            mTranscation.remove(mPersonalFragment);
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

    @Override
    public void onFinish(int requestId, String response) {
        try {
            if (response != null){
                JsonObject jsonObj = new JsonParser().parse(response).getAsJsonObject();
                if (jsonObj.get("errorCode").getAsInt() == 0){
                    LitePal.saveAll(JsonUtil.getTrees(jsonObj));
                }else {
                    Toast.makeText(this, jsonObj.get("errorMsg").getAsString(), Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailure(Exception e) {

    }
}
