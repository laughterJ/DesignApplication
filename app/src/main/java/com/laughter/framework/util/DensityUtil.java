package com.laughter.framework.util;

import android.content.Context;

/**
 * 作者： 王一凡
 * 创建时间： 2017/9/7
 * 版权： 江苏远大信息股份有限公司
 * 描述： com.sinoyd.frame.views 像素-dip转换工具
 */
public class DensityUtil {
    public DensityUtil() {
    }

    public static int dip2px(Context con, float dpValue) {
        float scale = con.getResources().getDisplayMetrics().density;
        return (int)(dpValue * scale + 0.5F);
    }

    public static int px2dip(Context con, float pxValue) {
        float scale = con.getResources().getDisplayMetrics().density;
        return (int)(pxValue / scale + 0.5F);
    }

    public static int px2sp(Context con, float pxValue) {
        float fontScale = con.getResources().getDisplayMetrics().scaledDensity;
        return (int)(pxValue / fontScale + 0.5F);
    }

    public static int sp2px(Context con, float spValue) {
        float fontScale = con.getResources().getDisplayMetrics().scaledDensity;
        return (int)(spValue * fontScale + 0.5F);
    }
}
