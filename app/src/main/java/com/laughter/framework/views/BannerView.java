package com.laughter.framework.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * created by JH at 2019/4/11
 * des： com.laughter.framework.views
 */

public class BannerView extends FrameLayout implements ViewPager.OnPageChangeListener, View.OnClickListener {

    // 使用ViewPager显示图片
    private ViewPager mViewPager;
    // 指示器外层布局
    private LinearLayout mIndicatorLayout;
    // 指示器小点布局
    private LinearLayout mDotLayout;
    // 指示器小点
    private List<View> viewList = new ArrayList<>();
    // 图片标题
    private TextView tvTitle;

    // 图片地址
    private List<String> imageUrls;

    // 标题
    private List<String> imageTitles;

    // 指示器小点样式
    private int mIndicatorStyle;

    // 轮播图总数
    private int totalCount = 0;
    // 前一个页面
    private int preIndex = 0;
    // 当前页面
    private int curIndex = 0;
    // 滚动消息
    private final int SCROLL_MSG = 1;
    // 滚动间隔时间
    private long mBannerScrollDelayTime = 3000;
    // 点击事件监听
    private OnItemClickListener mOnItemClickListener;

    private Context mContext;

    private BannerAdapter mAdapter;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == SCROLL_MSG){
                mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1, true);
                startBannerScroll();
            }
        }
    };

    public BannerView(Context context) {
        this(context, null);
    }

    public BannerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BannerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        initView();
    }

    private void initView() {
        // 获取焦点
        setFocusable(true);
        setFocusableInTouchMode(true);
        // 动态创建图片ViewPager
        mViewPager = new ViewPager(mContext);
        mViewPager.setLayoutParams(new LayoutParams(ViewPager.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mViewPager.addOnPageChangeListener(this);
        addView(mViewPager);

        // 动态创建指示器 IndicatorLayout
        mIndicatorLayout = new LinearLayout(mContext);
        LayoutParams lp = new LayoutParams(ViewPager.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.BOTTOM;
        mIndicatorLayout.setLayoutParams(lp);
        mIndicatorLayout.setOrientation(LinearLayout.HORIZONTAL);
        mIndicatorLayout.setBackgroundColor(0x4D000000);
        int padding = dip2px(10);
        mIndicatorLayout.setPadding(padding, padding, padding, padding);
        addView(mIndicatorLayout);

        setOnClickListener(this);
    }

    // 设置图片地址
    public void setImageUrl(List<String> mUrls) {
        imageUrls = mUrls;
        totalCount = imageUrls.size();
    }

    // 设置图片标题
    public void setTitle(List<String> mTitles) {
        this.imageTitles = mTitles;
    }

    // 设置指示器 小点 样式
    public void setmIndicaterStyle(int drawable) {
        this.mIndicatorStyle = drawable;
    }

    // 设置滚动时间间隔
    public void setScrollDelayTime(long delayTime) {
        mBannerScrollDelayTime = delayTime;
    }

    // 设置点击事件监听
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public void build() {
        addTitleView();
        addDotView();
        changeIndicaterStyle();
        if (mAdapter == null){
            mAdapter = new BannerAdapter(imageUrls);
            mViewPager.setAdapter(mAdapter);
        }else {
            mAdapter.notifyDataSetChanged();
        }
        startBannerScroll();
    }

    private void addDotView() {
        if (totalCount > 1){
            // 指示器 小点 外层布局
            mDotLayout = new LinearLayout(mContext);
            int dotSize = dip2px(6);
            LayoutParams lp = new LayoutParams(ViewPager.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            mDotLayout.setLayoutParams(lp);
            mDotLayout.setGravity(Gravity.CENTER_VERTICAL | Gravity.END);
            mDotLayout.setOrientation(LinearLayout.HORIZONTAL);
            mIndicatorLayout.addView(mDotLayout);
            // 根据图片数量生成小点
            for (int i=0;i<totalCount;i++){
                View dotView = new View(mContext);
                lp.height = dotSize;
                lp.width = dotSize;
                lp.setMarginStart(dotSize / 2);
                lp.setMarginEnd(dotSize / 2);
                dotView.setLayoutParams(lp);
                dotView.setBackgroundColor(Color.WHITE);
                dotView.setEnabled(false);
                viewList.add(dotView);
                mDotLayout.addView(dotView);
            }
            mDotLayout.getChildAt(curIndex).setEnabled(true);
        }
    }

    private void addTitleView() {
        if (imageTitles != null){
            tvTitle = new TextView(mContext);
            LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            tvTitle.setLayoutParams(lp);
            tvTitle.setTextColor(0xFFFFFFFF);
            tvTitle.setTextSize(13);
            mIndicatorLayout.addView(tvTitle);
            tvTitle.setText(imageTitles.get(0));
        }
    }

    private void changeIndicaterStyle() {
        if (totalCount > 1){
            for (int i=0;i<totalCount; i++){
                viewList.get(i).setBackgroundResource(mIndicatorStyle);
            }
        }
    }

    private int dip2px(float dpValue) {
        float scale = mContext.getResources().getDisplayMetrics().density;
        return (int)(dpValue * scale + 0.5F);
    }

    private void startBannerScroll() {
        mHandler.removeMessages(SCROLL_MSG);
        mHandler.sendEmptyMessageDelayed(SCROLL_MSG, mBannerScrollDelayTime);
    }



    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int index) {
        curIndex = index % totalCount;
        if (totalCount > 1){
            mDotLayout.getChildAt(preIndex).setEnabled(false);
            mDotLayout.getChildAt(curIndex).setEnabled(true);
            preIndex = curIndex;
        }
        if (tvTitle != null){
            tvTitle.setText(imageTitles.get(curIndex));
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    public void onClick(View v) {

    }

    class BannerAdapter extends PagerAdapter implements OnClickListener {
        private List<String> imgs;

        BannerAdapter(List<String> imgs) {
            this.imgs = imgs;
        }

        @Override
        public int getCount() {
            return totalCount > 1 ? Integer.MAX_VALUE : 1;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object obj) {
            return view == obj;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            ImageView img = new ImageView(mContext);
            img.setScaleType(ImageView.ScaleType.FIT_XY);
            Glide.with(mContext).load(imgs.get(position % totalCount)).into(img);
            container.addView(img);
            img.setOnClickListener(this);
            return img;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }

        @Override
        public void onClick(View v) {
            mOnItemClickListener.onItemClick(curIndex);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mHandler.removeMessages(SCROLL_MSG);
        mHandler = null;
    }

    public interface OnItemClickListener {
        void onItemClick(int index);
    }
}
