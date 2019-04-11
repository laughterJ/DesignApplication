package com.laughter.framework.util;

import android.content.Context;

/**
 * created by JH at 2019/4/11
 * des： com.laughter.framework.util
 */

public class DensityUtil {

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
