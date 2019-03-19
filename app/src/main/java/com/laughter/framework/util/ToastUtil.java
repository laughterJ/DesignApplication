package com.laughter.framework.util;

import android.content.Context;
import android.widget.Toast;

/**
 * 作者： 江浩
 * 创建时间： 2019/3/18
 * 版权： 江苏远大信息股份有限公司
 * 描述： com.laughter.framework.views
 */
public class ToastUtil {

    public static void showShortToast(Context context, String msg){
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
}
