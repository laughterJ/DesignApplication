package com.laughter.designapplication.application;

import android.app.Application;

/**
 * 作者： 江浩
 * 创建时间： 2019/3/7
 * 版权： 江苏远大信息股份有限公司
 * 描述： com.laughter.designapplication.application
 */
public class MyApplication extends Application {
    private static MyApplication mInstance;

    public static MyApplication getInstance() {
        if (mInstance == null){
            mInstance = new MyApplication();
        }
        return mInstance;
    }
}
