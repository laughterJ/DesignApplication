package com.laughter.designapplication.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.laughter.designapplication.HttpCallbackListener;
import com.laughter.designapplication.R;
import com.laughter.designapplication.activity.CollectionActivity;
import com.laughter.designapplication.activity.LoginActivity;
import com.laughter.designapplication.activity.TodoListActivity;
import com.laughter.designapplication.util.HttpUtil;
import com.laughter.framework.fragment.BaseFragment;
import com.laughter.framework.util.SpUtil;
import com.laughter.framework.util.ToastUtil;

import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * created by JH at 2019/4/11
 * des： com.laughter.designapplication.fragment
 */

public class PersonalFragment extends BaseFragment implements HttpCallbackListener,
        DialogInterface.OnClickListener {

    @BindView(R.id.img_profile_pic) ImageView imgProfilePic;
    @BindView(R.id.tv_username) TextView tvUsername;
    @BindView(R.id.tv_tag) TextView tvTag;

    @BindDrawable(R.drawable.ic_def) Drawable defPrifilePic;

    private static final int LOG_OFF = 0;

    @Override
    public int getLayout() {
        return R.layout.fragment_personal;
    }

    @Override
    public void initView() {
        RequestOptions mRequestOptions = RequestOptions.circleCropTransform()
                .diskCacheStrategy(DiskCacheStrategy.NONE)//不做磁盘缓存
                .skipMemoryCache(true);//不做内存缓存
        Glide.with(mContext).load(defPrifilePic).apply(mRequestOptions).into(imgProfilePic);
        if (SpUtil.getBoolean(mContext, "isLogin", false)){
            tvUsername.getPaint().setFlags(0);
            tvUsername.setText(SpUtil.getString(mContext, "username", getString(R.string.sign)));
        }else {
            tvUsername.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
            tvUsername.setText(getString(R.string.sign));
        }
    }

    @Override
    public void initData() {

    }

    @OnClick({R.id.ll_collection, R.id.ll_todo, R.id.ll_author, R.id.ll_log_off,
            R.id.tv_username, R.id.tv_tag, R.id.img_profile_pic})
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_collection:
                if (SpUtil.getBoolean(mContext, "isLogin", false)){
                    startActivity(new Intent(mContext, CollectionActivity.class));
                }else {
                    startActivity(new Intent(mContext, LoginActivity.class));
                }
                break;
            case R.id.ll_todo:
                if (SpUtil.getBoolean(mContext, "isLogin", false)){
                    startActivity(new Intent(mContext, TodoListActivity.class));
                }else {
                    startActivity(new Intent(mContext, LoginActivity.class));
                }
                break;
            case R.id.ll_log_off:
                if (SpUtil.getBoolean(mContext, "isLogin", false)){
                    new AlertDialog.Builder(mContext)
                            .setTitle("确认退出？")
                            .setPositiveButton("确认", this)
                            .setCancelable(true)
                            .create()
                            .show();
                }else {
                    ToastUtil.showShortToast(mContext, "你还没有登陆噢");
                }
                break;
            case R.id.tv_username:
                if (SpUtil.getBoolean(mContext, "isLogin", false)){
                    ToastUtil.showShortToast(mContext, "暂时还不支持修改用户名噢");
                }else {
                    startActivity(new Intent(mContext, LoginActivity.class));
                }
                break;
            case R.id.tv_tag:
                if (SpUtil.getBoolean(mContext, "isLogin", false)){
                    ToastUtil.showShortToast(mContext, "暂时还不支持修改签名噢");
                }
                break;
            case R.id.img_profile_pic:
                if (SpUtil.getBoolean(mContext, "isLogin", false)){
                    ToastUtil.showShortToast(mContext, "暂时还不支持修改头像噢");
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onFinish(int requestId, String response, String cookie) {
        switch (requestId){
            case LOG_OFF:
                ((Activity)mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        SpUtil.putBoolean(mContext, "isLogin", false);
                        SpUtil.putString(mContext,"Cookie", "");
                        initView();
                    }
                });
                break;
            default:
                break;
        }
    }

    @Override
    public void onFailure(Exception e) {

    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        HttpUtil.get("user/logout/json", LOG_OFF, null, this);
    }
}
