package com.laughter.designapplication.util;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.laughter.designapplication.model.Article;
import com.laughter.designapplication.model.Banner;
import com.laughter.designapplication.model.OfficialAccount;
import com.laughter.designapplication.model.Project;
import com.laughter.designapplication.model.Tree;

import java.lang.reflect.Type;
import java.util.List;

/**
 * created by JH at 2019/4/11
 * des： com.laughter.designapplication.util
 */

public class JsonUtil {

    public static List<Banner> getBanners(JsonObject jsonObj) {
        List<Banner> banners = null;
        if (jsonObj.get("data").getClass() == JsonArray.class){
            JsonArray data = jsonObj.getAsJsonArray("data");
            banners = new Gson().fromJson(data, new TypeToken<List<Banner>>(){}.getType());
        }
        return banners;
    }

    public static List<Article> getArticles(JsonObject jsonObj) {
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

    public static List<Project> getProjects(JsonObject jsonObj) {
        List<Project> projects = null;
        JsonObject data = jsonObj.getAsJsonObject("data");
        if (data.get("datas").getClass() == JsonArray.class){
            JsonArray datas = data.getAsJsonArray("datas");
            projects = new Gson().fromJson(datas, new TypeToken<List<Project>>(){}.getType());
        }
        return projects;
    }

    public static List<OfficialAccount> getOfficialAccounts(JsonObject jsonObj){
        List<OfficialAccount> oas = null;
        if (jsonObj.get("data").getClass() == JsonArray.class){
            JsonArray data = jsonObj.get("data").getAsJsonArray();
            oas = new Gson().fromJson(data, new TypeToken<List<OfficialAccount>>(){}.getType());
        }
        return oas;
    }
}
