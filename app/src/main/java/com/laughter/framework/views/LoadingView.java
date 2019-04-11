package com.laughter.framework.views;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import com.laughter.designapplication.R;

/**
 * created by JH at 2019/4/11
 * des： com.laughter.framework.views
 */

public class LoadingView extends View {

    private Paint bottomPaint, surfacePaint;

    private int bottomLineColor, surfaceLineColor,backgroundColor;

    private int lineWidth, lineNum, mWidth, mHeight, mSize;

    private float mCurProgress = 0f;
    private int mRunContent = 0, mCurContent;

    private ValueAnimator valueAnimator;

    public LoadingView(Context context) {
        this(context, null);
    }

    public LoadingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.LoadingView);
        bottomLineColor = mTypedArray.getColor(R.styleable.LoadingView_bottomLineColor, Color.GRAY);
        surfaceLineColor = mTypedArray.getColor(R.styleable.LoadingView_surfaceLineColor, 0xff34a2da);
        backgroundColor = mTypedArray.getColor(R.styleable.LoadingView_backgroundColor, 0x00ffffff);
        lineWidth = mTypedArray.getDimensionPixelSize(R.styleable.LoadingView_lineWidth, dp2px(5));
        lineNum = mTypedArray.getInteger(R.styleable.LoadingView_lineNum, 10);
        mTypedArray.recycle();

        bottomPaint = new Paint();
        initPaint(bottomPaint);
        bottomPaint.setColor(bottomLineColor);

        surfacePaint = new Paint();
        initPaint(surfacePaint);
        surfacePaint.setColor(surfaceLineColor);

        mWidth = dp2px(100);
        mHeight = dp2px(72);
        mSize = dp2px(42);
    }

    /**
     * 初始化画笔
     * @param mPaint
     */
    private void initPaint(Paint mPaint) {
        mPaint.setStrokeWidth(lineWidth);
        // 抗锯齿
        mPaint.setAntiAlias(true);
        // 防抖动
        mPaint.setDither(true);
        // 线帽样式——圆形线帽
        mPaint.setStrokeCap(Paint.Cap.ROUND);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(backgroundColor);
        canvas.translate(getWidth() / 2, getHeight() / 2);
        int startX = (int)(mSize / 2 * 0.58);
        if (mRunContent % 2 == 0){
            for (int i = 0; i < lineNum ; i++) {
                canvas.drawLine(startX, 0, mSize / 2, 0, bottomPaint);
                canvas.rotate(360 / lineNum);
            }
            mCurContent = (int) (lineNum * mCurProgress);
            for (int i = mCurContent; i > 0; i--) {
                canvas.drawLine(startX, 0, mSize / 2, 0, surfacePaint);
                canvas.rotate(360 / lineNum);
            }
        }else {
            for (int i = 0; i < lineNum; i++) {
                canvas.drawLine(startX, 0, mSize / 2, 0, surfacePaint);
                canvas.rotate(360 / lineNum);
            }
            mCurContent = (int) (lineNum * mCurProgress);
            for (int i = mCurContent; i > 0; i--) {
                canvas.drawLine(startX, 0, mSize / 2, 0, bottomPaint);
                canvas.rotate(360 / lineNum);
            }
        }
    }

    public void start() {
        if (valueAnimator == null){
            valueAnimator = ObjectAnimator.ofFloat(0, 1.0f);
            valueAnimator.setDuration(700);
            valueAnimator.setRepeatCount(-1);
            valueAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationRepeat(Animator animation) {
                    mRunContent ++;
                }
            });
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float value = (float) valueAnimator.getAnimatedValue();
                    setLoadingProgress(value);
                }
            });
            valueAnimator.start();
        }
    }

    private void setLoadingProgress(float progress) {
        this.mCurProgress = progress;
        postInvalidate();
    }

    public void cancle() {
        if (valueAnimator != null){
            valueAnimator.cancel();
            valueAnimator.end();
        }
    }

    private int dp2px(int val) {
        float density = getResources().getDisplayMetrics().density;
        return (int)(density * val + 0.5f);
    }
}
