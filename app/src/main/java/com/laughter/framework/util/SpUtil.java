package com.laughter.framework.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * 作者： 江浩
 * 创建时间： 2019/3/18
 * 版权： 江苏远大信息股份有限公司
 * 描述： com.laughter.framework
 */
public class SpUtil {

    public static void putString(Context context, String key, String val){
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString(key, val);
        editor.apply();
    }

    public static String getString(Context context, String key, String defVal){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getString(key, defVal);
    }

    public static void putBoolean(Context context, String key, boolean val){
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putBoolean(key, val);
        editor.apply();
    }

    public static Boolean getBoolean(Context context, String key, boolean defVal){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getBoolean(key, defVal);
    }
}
