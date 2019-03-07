package com.laughter.designapplication.util;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.laughter.designapplication.model.Article;

import java.util.List;

/**
 * 作者： 江浩
 * 创建时间： 2019/3/7
 * 版权： 江苏远大信息股份有限公司
 * 描述： com.laughter.designapplication.util
 */
public class JsonUtil {

    public static List<Article> getHomePageArticle(JsonObject jsonObj) {
        List<Article> articles = null;
        JsonObject data = jsonObj.getAsJsonObject("data");
        if (data.get("datas").getClass() == JsonArray.class){
            JsonArray datas = data.getAsJsonArray("datas");
            articles = new Gson().fromJson(datas, new TypeToken<List<Article>>(){}.getType());
        }
        return articles;
    }
}
