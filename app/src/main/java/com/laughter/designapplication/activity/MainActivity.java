package com.laughter.designapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.laughter.designapplication.R;
import com.laughter.designapplication.fragment.HomePageFragment;
import com.laughter.designapplication.fragment.KnowledgeFragment;
import com.laughter.designapplication.fragment.OfficialAccountFragment;
import com.laughter.designapplication.fragment.ProjectFragment;
import com.laughter.designapplication.fragment.PersonalFragment;
import com.laughter.designapplication.model.Tree;
import com.laughter.designapplication.util.JsonUtil;
import com.laughter.designapplication.util.HttpUtil;
import com.laughter.framework.activity.BaseActivity;

import org.litepal.LitePal;

import butterknife.BindView;

/**
 * created by JH at 2019/4/11
 * des： com.laughter.designapplication.activity
 */

public class MainActivity extends BaseActivity implements BottomNavigationView.OnNavigationItemSelectedListener, HttpUtil.HttpCallbackListener {

    @BindView(R.id.bottombar)
    BottomNavigationView mBottomBar;

    private static final String TAG_HOMEPAGE = "HomePage";
    private static final String TAG_KNOWLEDGE = "Knowledge";
    private static final String TAG_PROJECT = "Project";
    private static final String TAG_OA = "OfficialAccount";
    private static final String TAG_PERSONAL = "Personal";

    private FragmentManager mFragmentManager;
    private FragmentTransaction mTranscation;

    private HomePageFragment mHomePageFragment;
    private KnowledgeFragment mKnowledgeFragment;
    private ProjectFragment mProjectFragment;
    private OfficialAccountFragment mOfficialAccountFragment;
    private PersonalFragment mPersonalFragment;

    private long lastPressTime = System.currentTimeMillis();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
    }

    @Override
    public int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        mPersonalFragment = (PersonalFragment) mFragmentManager.findFragmentByTag(TAG_PERSONAL);
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
            mTranscation.add(R.id.continer, mHomePageFragment, TAG_HOMEPAGE);
        }else {
            mTranscation.show(mHomePageFragment);
        }
        mTranscation.commit();
    }

    @Override
    public void initData() {
        HttpUtil.get("project/tree/json", 0, null, this);
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
                    mTranscation.add(R.id.continer, mHomePageFragment, TAG_HOMEPAGE);
                }else {
                    mTranscation.show(mHomePageFragment);
                }
                break;
            case R.id.tab_knowledge:
                if (mKnowledgeFragment == null){
                    mKnowledgeFragment = new KnowledgeFragment();
                    mTranscation.add(R.id.continer, mKnowledgeFragment, TAG_KNOWLEDGE);
                }else {
                    mTranscation.show(mKnowledgeFragment);
                }
                break;
            case R.id.tab_project:
                if (mProjectFragment == null){
                    mProjectFragment = new ProjectFragment();
                    mTranscation.add(R.id.continer, mProjectFragment, TAG_PROJECT);
                }else {
                    mTranscation.show(mProjectFragment);
                }
                break;
            case R.id.tab_location:
                if (mOfficialAccountFragment == null){
                    mOfficialAccountFragment = new OfficialAccountFragment();
                    mTranscation.add(R.id.continer, mOfficialAccountFragment, TAG_OA);
                }else {
                    mTranscation.show(mOfficialAccountFragment);
                }
                break;
            case R.id.tab_personal:
                if (mPersonalFragment == null){
                    mPersonalFragment = new PersonalFragment();
                    mTranscation.add(R.id.continer, mPersonalFragment, TAG_PERSONAL);
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
        mHomePageFragment = (HomePageFragment) mFragmentManager.findFragmentByTag(TAG_HOMEPAGE);
        mKnowledgeFragment = (KnowledgeFragment) mFragmentManager.findFragmentByTag(TAG_KNOWLEDGE);
        mProjectFragment = (ProjectFragment) mFragmentManager.findFragmentByTag(TAG_PROJECT);
        mOfficialAccountFragment = (OfficialAccountFragment) mFragmentManager.findFragmentByTag(TAG_OA);
        mPersonalFragment = (PersonalFragment) mFragmentManager.findFragmentByTag(TAG_PERSONAL);

        if (mHomePageFragment != null){
            mTranscation.hide(mHomePageFragment);
        }
        if (mKnowledgeFragment != null){
            mTranscation.hide(mKnowledgeFragment);
        }
        if (mProjectFragment != null){
            mTranscation.hide(mProjectFragment);
        }
        if (mOfficialAccountFragment != null){
            mTranscation.hide(mOfficialAccountFragment);
        }
        if (mPersonalFragment != null){
            mTranscation.hide(mPersonalFragment);
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
    public void onFinish(int requestId, String response, String cookie) {
        LitePal.deleteAll(Tree.class);
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
        e.printStackTrace();
    }
}
