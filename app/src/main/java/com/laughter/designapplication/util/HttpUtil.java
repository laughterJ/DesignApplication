package com.laughter.designapplication.util;

import android.util.Log;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * created by JH at 2019/4/11
 * des： com.laughter.designapplication.util
 */

public class HttpUtil {
    private static final String baseUrl = "https://www.wanandroid.com/";

    public static void get(final String path, final int requestId, final String localCookie, final HttpCallbackListener listener) {
        new Thread(() -> {
            HttpURLConnection connection = null;
            try {
                URL url = new URL(baseUrl + path);
                Log.e("coder", url.toString());
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(8000);
                connection.setReadTimeout(8000);
                if (localCookie != null) {
                    connection.setRequestProperty("Cookie", localCookie);
                }

                InputStreamReader in = new InputStreamReader(connection.getInputStream());
                BufferedReader reader = new BufferedReader(in);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null){
                    sb.append(line);
                }
                if (listener != null){
                    listener.onFinish(requestId, sb.toString(), null);
                    Log.d("coder", "【返回数据】" + sb.toString());
                }
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
        }).start();
    }

    public static void post(final String path, final int requestId, final JsonObject params, final String localCookie, final boolean saveCookie, final HttpCallbackListener listener) {
        new Thread(() -> {
            HttpURLConnection connection = null;
            try {
                URL url = new URL(baseUrl + path);
                Log.e("coder", url.toString());
                connection = (HttpURLConnection) url.openConnection();
                connection.setReadTimeout(8000);
                connection.setConnectTimeout(8000);
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                connection.setDoInput(true);
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                if (localCookie != null) {
                    connection.setRequestProperty("Cookie", localCookie);
                }

                StringBuilder param = new StringBuilder();
                if (params != null){
                    String[] keys = params.keySet().toArray(new String[0]);
                    for (int i = 0; i < keys.length; i++) {
                        param.append(keys[i]).append("=").append(params.get(keys[i]).getAsString());
                        if (i < keys.length - 1) {
                            param.append("&");
                        }
                    }
                    Log.d("coder", "【参数】" + param.toString());
                }

                //建立输入流，向指向的URL传入参数
                DataOutputStream dos = new DataOutputStream(connection.getOutputStream());
                dos.write(param.toString().getBytes());
                dos.flush();
                dos.close();

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                StringBuilder cookie = new StringBuilder();
                if (saveCookie) {
                    List<String> cookies = connection.getHeaderFields().get("Set-Cookie");
                    if (cookies != null){
                        for(String ele : cookies){
                            cookie.append(ele).append(";");
                        }
                    }
                }
                if (listener != null) {
                    listener.onFinish(requestId, sb.toString(), cookie.toString());
                    Log.d("coder", "【返回数据】" + sb.toString());
                }
            } catch (Exception e) {
                e.printStackTrace();
                if (listener != null) {
                    listener.onFailure(e);
                }
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        }).start();
    }

    public interface HttpCallbackListener {
        void onFinish(int requestId, String response, String cookie);
        void onFailure(Exception e);
    }
}
