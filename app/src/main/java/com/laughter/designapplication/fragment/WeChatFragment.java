package com.laughter.designapplication.fragment;

import android.app.Activity;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.laughter.designapplication.HttpCallbackListener;
import com.laughter.designapplication.R;
import com.laughter.designapplication.adapter.TabberAdapter;
import com.laughter.designapplication.model.OfficialAccount;
import com.laughter.designapplication.util.HttpUtil;
import com.laughter.designapplication.util.JsonUtil;
import com.laughter.framework.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class WeChatFragment extends BaseFragment implements HttpCallbackListener {

    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.tabber_wechat) TabLayout mTabLayout;
    @BindView(R.id.vp_wechat) ViewPager mViewPager;

    private List<ArticleListFragment> fragments;
    private List<OfficialAccount> oaList;
    private List<String> tabNames;

    @Override
    public int getLayout() {
        return R.layout.fragment_wechat;
    }

    @Override
    public void initView() {
        mToolbar.setTitle("公众号");
        ((AppCompatActivity)mContext).setSupportActionBar(mToolbar);
    }

    @Override
    public void initData() {
        fragments = new ArrayList<>();
        tabNames = new ArrayList<>();
        HttpUtil.sendHttpRequest("wxarticle/chapters/json", "GET", null, 0, this);
    }

    @Override
    public void onFinish(int requestId, String response, String cookie) {
        try{
            JsonObject jsonObj = new JsonParser().parse(response).getAsJsonObject();
            if (jsonObj.get("errorCode").getAsInt() == 0){
                oaList = JsonUtil.getOfficialAccounts(jsonObj);
            }else {
                ToastUtil.showShortToast(mContext, jsonObj.get("errorMsg").getAsString());
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            initFragments();
        }
    }

    @Override
    public void onFailure(Exception e) {

    }

    private void initFragments() {
        ((Activity)mContext).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for (OfficialAccount oa : oaList){
                    fragments.add(ArticleListFragment.newInstance(String.valueOf(oa.getId())));
                    tabNames.add(oa.getName());
                }
                TabberAdapter adapter = new TabberAdapter(getFragmentManager(), fragments, tabNames);
                mViewPager.setAdapter(adapter);
                mTabLayout.setupWithViewPager(mViewPager);
            }
        });
    }
}
