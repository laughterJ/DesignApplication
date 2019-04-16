package com.laughter.designapplication.fragment;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.google.gson.JsonObject;
import com.laughter.designapplication.HttpCallbackListener;
import com.laughter.designapplication.R;
import com.laughter.designapplication.activity.TodoListActivity;
import com.laughter.designapplication.util.HttpUtil;
import com.laughter.designapplication.util.JsonUtil;
import com.laughter.framework.fragment.BaseFragment;
import com.laughter.framework.util.DateUtil;
import com.laughter.framework.util.SpUtil;
import com.laughter.framework.util.ToastUtil;

import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;

public class TodoAddFragment extends BaseFragment implements ViewTreeObserver.OnGlobalLayoutListener, HttpCallbackListener {

    @BindView(R.id.ll_view) LinearLayout llView;
    @BindView(R.id.et_todo_des) EditText etTodoDes;
    @BindView(R.id.rg_priority) RadioGroup rgPriority;
    @BindView(R.id.rb_first) RadioButton rbFirst;
    @BindView(R.id.rb_second) RadioButton rbSecond;
    @BindView(R.id.rb_third) RadioButton rbThird;

    private String todoDes;
    private String deadline;
    private int priority;
    private boolean isAdded;
    private int id;

    public static TodoAddFragment newInstance(Bundle args) {
        TodoAddFragment fragment = new TodoAddFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_todo_add;
    }

    @Override
    public void initView() {
        llView.getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    @Override
    public void initData() {
        // 默认优先级和时间
        priority = 3;
        deadline = DateUtil.date2String(new Date(System.currentTimeMillis()), "yyyy-MM-dd");

        // 判断是修改还是创建
        Bundle args = getArguments();
        if (args != null && args.getBoolean("isAdded", false)){
            isAdded = args.getBoolean("isAdded", false);
            id = args.getInt("id", id);
            todoDes = args.getString("title", todoDes);
            etTodoDes.setText(todoDes);
            priority = args.getInt("priority", priority);
            switch (priority){
                case 1:
                    rbFirst.setChecked(true);
                    break;
                case 2:
                    rbSecond.setChecked(true);
                    break;
                case 3:
                    rbThird.setChecked(true);
                    break;
                default:
                    break;
            }
            deadline = args.getString("deadline", deadline);
        }
    }

    /**
     * 监听软键盘弹出与收起
     * 控制底部View跟随软键盘移动
     */
    @Override
    public void onGlobalLayout() {
        // 获取 顶级View(包括toolbar和contentView)
        View decorView = llView.getRootView();
        // 获取顶级View的高度
        int screenHeight = decorView.getHeight();
        // 获取当前视图可见高度
        Rect mRect = new Rect();
        decorView.getWindowVisibleDisplayFrame(mRect);
        int visiableHeight = mRect.bottom;
        // 计算软键盘遮挡区域高度
        int keyboardHeight = screenHeight - visiableHeight;
        Log.e("coder", llView.getTranslationY()+"");
        ValueAnimator animator = ValueAnimator.ofFloat(llView.getTranslationY(), -keyboardHeight);
        animator.setDuration(240);
        animator.addUpdateListener(animation -> llView.setTranslationY((float)animation.getAnimatedValue()));
        animator.start();
    }

    @OnClick({R.id.tv_cancle, R.id.tv_save, R.id.iv_date})
    public void onClick(View v) {
        InputMethodManager manager = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        // 收起软键盘
        manager.hideSoftInputFromWindow(llView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        switch (v.getId()) {
            case R.id.iv_date:
                // 弹出时间选择控件
                new TimePickerBuilder(mContext, (date, v1) -> {
                    deadline = DateUtil.date2String(date, "yyyy-MM-dd");
                }).setType(new boolean[]{true, true, true, false, false, false}).build().show();
                break;
            case R.id.tv_cancle:
                ((TodoListActivity)mContext).hideFragment();
                break;
            case R.id.tv_save:
                if (!TextUtils.isEmpty(etTodoDes.getText())){
                    saveData();
                    ((TodoListActivity)mContext).hideFragment();
                }else {
                    ToastUtil.showShortToast(mContext, "输入不能为空");
                }
                break;
            default:
                break;
        }
    }

    private void saveData() {
        todoDes = etTodoDes.getText().toString();
        switch (rgPriority.getCheckedRadioButtonId()){
            case R.id.rb_first:
                priority = 1;
                break;
            case R.id.rb_second:
                priority = 2;
                break;
            case R.id.rb_third:
                priority = 3;
                break;
            default:
                break;
        }
        JsonObject params = new JsonObject();
        params.addProperty("title", todoDes);
        params.addProperty("content", todoDes);
        params.addProperty("date", deadline);
        params.addProperty("priority", priority);
        String localCookie = SpUtil.getString(mContext, "Cookie", null);
        HttpUtil.post(getPath(), 0, params, localCookie, false, this);
        ((TodoListActivity)mContext).showLoading();
    }

    private String getPath() {
        return isAdded ? "lg/todo/update/" + id + "/json" : "lg/todo/add/json";
    }

    @Override
    public void onFinish(int requestId, String response, String cookie) {
        ((Activity)mContext).runOnUiThread(() -> {
            if (JsonUtil.getErrorCode(response) == 0){
                ((TodoListActivity)mContext).updateList();
            }else {
                ToastUtil.showShortToast(mContext, JsonUtil.getErrorMsg(response));
            }
        });
    }

    @Override
    public void onFailure(Exception e) {
        e.printStackTrace();
    }
}
