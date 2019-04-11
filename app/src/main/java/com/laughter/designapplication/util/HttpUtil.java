package com.laughter.designapplication.util;

import android.util.Log;

import com.google.gson.JsonObject;
import com.laughter.designapplication.HttpCallbackListener;
import com.laughter.designapplication.activity.LoginActivity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Set;

import javax.net.ssl.HttpsURLConnection;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * created by JH at 2019/4/11
 * des： com.laughter.designapplication.util
 */

public class HttpUtil {

    private static String baseUrl = "https://www.wanandroid.com/";

    public static void sendGetRequest(final String address, int requestId, final HttpCallbackListener listener) {
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
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    if (listener != null) {
                        listener.onFinish(requestId, response.toString(), null);
                    }
                    Log.d("coder", response.toString());
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
            }
        }).start();
    }

    public static void sendHttpRequest(String address, String method, JsonObject paramsObj, int requestId, HttpCallbackListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try {
                    URL url = new URL(baseUrl + address);
                    Log.e("coder", url.toString());
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod(method);
                    connection.setReadTimeout(8000);
                    connection.setReadTimeout(8000);
                    if (method.equals("POST")) {
                        Log.e("coder", "【" + paramsObj.toString() + "】");
                        connection.setDoInput(true);
                        connection.setDoOutput(true);

                        StringBuilder params = new StringBuilder();
                        String[] keys = paramsObj.keySet().toArray(new String[0]);
                        for (int i = 0; i < keys.length; i++) {
                            params.append(keys[i]).append("=").append(paramsObj.get(keys[i]).getAsString());
                            if (i < keys.length - 1) {
                                params.append("&");
                            }
                        }
                        Log.e("coder", params.toString());
//                        PrintWriter out = new PrintWriter(connection.getOutputStream());
//                        // 发送请求参数
//                        out.print(params.toString());
//                        // flush输出流的缓冲
//                        out.flush();
                        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream(), "UTF-8"));
                        writer.write(params.toString());
                    }

                    InputStream in = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    if (listener != null) {
                        listener.onFinish(requestId, response.toString(), null);
                    }
                    Log.d("coder", response.toString());
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
            }
        }).start();
    }

    public static void sendOkHttpRequest(final String address, okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        Log.e("coder", baseUrl + address);
        Request request = new Request.Builder().url(baseUrl + address).build();
        client.newCall(request).enqueue(callback);
    }
}