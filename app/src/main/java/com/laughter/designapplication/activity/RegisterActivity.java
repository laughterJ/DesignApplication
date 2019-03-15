package com.laughter.designapplication.activity;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;

import com.laughter.designapplication.R;

import butterknife.BindView;
import butterknife.OnClick;

public class RegisterActivity extends BaseActivity {

    @BindView(R.id.edit_name) EditText editUserName;
    @BindView(R.id.edit_pass) EditText editPassword;

    @Override
    public int getLayout() {
        return R.layout.activity_register;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }

    @OnClick({R.id.tv_sign_in, R.id.but_register, R.id.ib_close})
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_sign_in:
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                break;
            case R.id.ib_close:
                finish();
                break;
            default:
                break;
        }
    }
}

