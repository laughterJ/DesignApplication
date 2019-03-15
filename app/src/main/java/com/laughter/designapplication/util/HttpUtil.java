package com.laughter.designapplication.util;

import android.util.Log;

import com.laughter.designapplication.HttpCallbackListener;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * 作者： 江浩
 * 创建时间： 2018/12/21
 * 版权： 江苏远大信息股份有限公司
 * 描述： com.laughter.network.util
 */
public class HttpUtil {

    private static String baseUrl = "https://www.wanandroid.com/";

    public static void sendHttpRequest(final String address, int requestId, final HttpCallbackListener listener){
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try {
                    URL url = new URL(baseUrl + address);
                    Log.e("coder", url.toString());
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    InputStream in = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while((line = reader.readLine()) != null){
                        response.append(line);
                    }
                    if (listener != null){
                        listener.onFinish(requestId, response.toString());
                    }
                    Log.d("coder", response.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                    if (listener != null){
                        listener.onFailure(e);
                    }
                }finally {
                    if (connection != null){
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }

    public static void sendOkHttpRequest(final String address, okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();
        Log.e("coder", baseUrl + address);
        Request request = new Request.Builder().url(baseUrl + address).build();
        client.newCall(request).enqueue(callback);
    }
}
