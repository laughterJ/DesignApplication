package com.laughter.framework.util;

import android.content.Context;
import android.widget.Toast;

/**
 * created by JH at 2019/4/11
 * des： com.laughter.framework.util
 */

public class ToastUtil {

    public static void showShortToast(Context context, String msg){
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
}
