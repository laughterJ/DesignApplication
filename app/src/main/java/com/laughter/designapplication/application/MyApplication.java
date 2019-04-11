package com.laughter.designapplication.application;

import android.app.Application;

/**
 * created by JH at 2019/4/11
 * des： com.laughter.designapplication.application
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
