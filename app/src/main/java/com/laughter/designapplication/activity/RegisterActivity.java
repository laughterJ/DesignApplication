package com.laughter.designapplication.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.laughter.designapplication.HttpCallbackListener;
import com.laughter.designapplication.R;
import com.laughter.designapplication.util.HttpUtil;
import com.laughter.framework.util.SpUtil;
import com.laughter.framework.util.ToastUtil;

import butterknife.BindView;
import butterknife.OnClick;

public class RegisterActivity extends BaseActivity implements HttpCallbackListener {

    @BindView(R.id.edit_name) EditText editUserName;
    @BindView(R.id.edit_pass) EditText editPassword;
    @BindView(R.id.edit_repeat_pass) EditText editRePass;

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
            case R.id.but_register:
                register();
            default:
                break;
        }
    }

    private void register() {
        String userName = editUserName.getText().toString();
        String userPass = editPassword.getText().toString();
        String rePass = editRePass.getText().toString();

        if (TextUtils.isEmpty(userName)){
            ToastUtil.showShortToast(this, "请输入用户名");
            return;
        }
        if (TextUtils.isEmpty(userPass)){
            ToastUtil.showShortToast(this, "请输入密码");
            return;
        }
        if (TextUtils.isEmpty(rePass)){
            ToastUtil.showShortToast(this, "请确认密码");
            return;
        }
        if (!userPass.equals(rePass)){
            ToastUtil.showShortToast(this, "两次输入密码不一致，请重新输入");
            return;
        }

        JsonObject params = new JsonObject();
        params.addProperty("username", userName);
        params.addProperty("password", userPass);
        params.addProperty("repassword", rePass);
        HttpUtil.sendHttpRequest("user/register", "POST", params, 0, this);
    }

    @Override
    public void onFinish(int requestId, String response) {
        try {
            JsonObject jsonObj = new JsonParser().parse(response).getAsJsonObject();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (jsonObj.get("errorCode").getAsInt() == 0){
                        ToastUtil.showShortToast(RegisterActivity.this, "注册成功");
                        SpUtil.putString(RegisterActivity.this, "username", editUserName.getText().toString());
                        SpUtil.putString(RegisterActivity.this, "password", editPassword.getText().toString());
                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                        finish();
                    }else {
                        ToastUtil.showShortToast(RegisterActivity.this, jsonObj.get("errorMsg").getAsString());
                    }
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onFailure(Exception e) {
        Log.e("coder", e.getMessage());
    }
}

