package com.laughter.designapplication;

/**
 * 作者： 江浩
 * 创建时间： 2018/12/21
 * 版权： 江苏远大信息股份有限公司
 * 描述： com.laughter.network
 */
public interface HttpCallbackListener {
    void onFinish(int requestId, String response);
    void onFailure(Exception e);
}
