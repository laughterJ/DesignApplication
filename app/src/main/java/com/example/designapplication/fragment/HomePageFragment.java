package com.example.designapplication.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.designapplication.R;
import com.example.designapplication.adapter.ArticleAdapter;
import com.example.designapplication.model.Article;
import com.example.designapplication.util.HttpUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class HomePageFragment extends Fragment implements Callback {

    @BindView(R.id.rl_view)
    RecyclerView mRecyclerView;

    private List<Article> mArticleList;
    private ArticleAdapter mAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_page, parent, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext() ,DividerItemDecoration.VERTICAL));
        mArticleList = new ArrayList<>();
        mAdapter = new ArticleAdapter(mArticleList, getContext());
        mRecyclerView.setAdapter(mAdapter);

        String address = "http://www.wanandroid.com/article/list/0/json";
        HttpUtil.sendOkHttpRequest(address, this);
    }

    @Override
    public void onFailure(@NonNull Call call, @NonNull IOException e) {

    }

    @Override
    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
        mArticleList.clear();
        try {
            String jsonData = null;
            if (response.body() != null){
                jsonData = response.body().string();
            }
            JSONObject mJsonObject = new JSONObject(jsonData);
            JSONObject data = mJsonObject.getJSONObject("data");
            JSONArray mJsonArray = data.getJSONArray("datas");
            for (int i = 0; i < mJsonArray.length(); i++){
                JSONObject item = mJsonArray.getJSONObject(i);
                String title = item.getString("title");
                String author = item.getString("author");
                JSONArray tags = item.getJSONArray("tags");
                String tag = null;
                if (tags.length() > 0){
                    tag = tags.getJSONObject(0).getString("name");
                }
                String date = item.getString("niceDate");
                String link = item.getString("link");
                mArticleList.add(new Article(title, author, tag, date, link));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }finally {
            initData();
        }
    }

    private void initData() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mAdapter.notifyDataSetChanged();
            }
        });
    }
}
