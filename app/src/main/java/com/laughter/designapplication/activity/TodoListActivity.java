package com.laughter.designapplication.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.laughter.designapplication.R;
import com.laughter.designapplication.fragment.TodoAddFragment;
import com.laughter.framework.activity.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class TodoListActivity extends BaseActivity {

    @BindView(R.id.toolbar) Toolbar mToolbar;

    private Fragment mTodoAddFragment;

    @Override
    public int getLayout() {
        return R.layout.activity_todo_list;
    }

    @Override
    public void initView() {
        mToolbar.setTitle("TodoList");
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_back);
        mToolbar.setNavigationOnClickListener(v -> finish());
    }

    @Override
    public void initData() {

    }

    @OnClick({R.id.fab_add})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_add:
                FragmentManager manager = getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.setCustomAnimations(R.anim.anim_enter, R.anim.anim_exit);
                mTodoAddFragment = manager.findFragmentByTag("todo");
                if (mTodoAddFragment == null){
                    mTodoAddFragment = new TodoAddFragment();
                    transaction.add(R.id.fl_add, mTodoAddFragment, "todo");
                }
                transaction.show(mTodoAddFragment);
                transaction.commit();
                break;
            default:
                break;
        }
    }
}
