package com.laughter.designapplication.activity;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.laughter.designapplication.R;

import butterknife.BindView;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity {

    @BindView(R.id.edit_name) EditText editUserName;
    @BindView(R.id.edit_pass) EditText editPassword;

    @Override
    public int getLayout() {
        return R.layout.activity_login;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }

    @OnClick({R.id.tv_register, R.id.tv_forgot_pass, R.id.but_sign_in, R.id.ib_close})
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_register:
                startActivity(new Intent(this, RegisterActivity.class));
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
