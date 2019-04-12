package com.laughter.designapplication.fragment;

import android.animation.ValueAnimator;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;

import com.laughter.designapplication.R;
import com.laughter.framework.fragment.BaseFragment;

import butterknife.BindView;
import butterknife.OnClick;

public class TodoAddFragment extends BaseFragment implements ViewTreeObserver.OnGlobalLayoutListener {

    @BindView(R.id.ll_view)
    LinearLayout llView;

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

    }

    @OnClick({R.id.ll_view})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_view:
                Log.e("coder", "llview");
                break;
            default:
                break;
        }
    }

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
}
