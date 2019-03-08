package com.laughter.designapplication.util;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.laughter.designapplication.model.Article;
import com.laughter.designapplication.model.Tree;

import java.lang.reflect.Type;
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

    public static List<Tree> getTrees(JsonObject jsonObj) {
        List<Tree> trees = null;
        if (jsonObj.get("data").getClass() == JsonArray.class){
            JsonArray datas = jsonObj.getAsJsonArray("data");
            trees = new Gson().fromJson(datas, new TypeToken<List<Tree>>(){}.getType());
        }
        return trees;
    }
}
