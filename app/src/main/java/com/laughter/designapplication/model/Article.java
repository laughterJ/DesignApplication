package com.laughter.designapplication.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 作者： 江浩
 * 创建时间： 2018/12/21
 * 描述： com.laughter.network
 */
public class Article {

    private String title;
    private String author;
    private List<Tag> tags;
    @SerializedName("niceDate")
    private String date;
    private String link;
    private String id;

    public Article(){

    }

    public Article(String title, String author, List<Tag> tags, String date, String link){
        this.title = title;
        this.author = author;
        this.tags = tags;
        this.date = date;
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public String getDate() {
        return date;
    }

    public String getLink() {
        return link;
    }

    public String getId() {
        return id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setId(String id) {
        this.id = id;
    }

    public class Tag {
        String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
