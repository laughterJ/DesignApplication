package com.laughter.designapplication.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.laughter.designapplication.R;
import com.laughter.designapplication.util.HttpUtil;
import com.laughter.framework.activity.BaseActivity;
import com.laughter.framework.util.SpUtil;
import com.laughter.framework.util.ToastUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * created by JH at 2019/4/11
 * des： com.laughter.designapplication.activity
 */

public class LoginActivity extends BaseActivity implements HttpUtil.HttpCallbackListener {

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
        String username = SpUtil.getString(this, "username", "");
        String password = SpUtil.getString(this, "password", "");
        editUserName.setText(username);
        editPassword.setText(password);
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
            case R.id.tv_forgot_pass:
                ToastUtil.showShortToast(this, "开发中...");
                break;
            case R.id.but_sign_in:
                login();
                break;
            default:
                break;
        }
    }

    private void login() {
        String username = editUserName.getText().toString();
        String password = editPassword.getText().toString();

        if (TextUtils.isEmpty(username)){
            ToastUtil.showShortToast(this, "请输入用户名");
            return;
        }
        if (TextUtils.isEmpty(password)){
            ToastUtil.showShortToast(this, "请输入密码");
            return;
        }

        JsonObject params = new JsonObject();
        params.addProperty("username", username);
        params.addProperty("password", password);
        HttpUtil.post("user/login", 0, params, null, true, this);
    }

    @Override
    public void onFinish(int requestId, String response, String cookie) {
        JsonObject jsonObj = new JsonParser().parse(response).getAsJsonObject();
        runOnUiThread(() -> {
            if (jsonObj.get("errorCode").getAsInt() == 0){
                ToastUtil.showShortToast(LoginActivity.this, "登陆成功");
                SpUtil.putString(LoginActivity.this, "username", editUserName.getText().toString());
                SpUtil.putString(LoginActivity.this, "password", editPassword.getText().toString());
                SpUtil.putBoolean(LoginActivity.this, "isLogin", true);
                SpUtil.putString(LoginActivity.this, "Cookie", cookie);
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            }else{
                ToastUtil.showShortToast(LoginActivity.this, jsonObj.get("errorMsg").getAsString());
            }
        });
    }

    @Override
    public void onFailure(Exception e) {
        e.printStackTrace();
    }
}
