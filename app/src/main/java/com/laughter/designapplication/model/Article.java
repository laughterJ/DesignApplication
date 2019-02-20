package com.laughter.designapplication.model;

/**
 * 作者： 江浩
 * 创建时间： 2018/12/21
 * 描述： com.laughter.network
 */
public class Article {

    private String title;
    private String author;
    private String tagName;
    private String date;
    private String link;

    public Article(){

    }

    public Article(String title, String author, String tagName, String date, String link){
        this.title = title;
        this.author = author;
        this.tagName = tagName;
        this.date = date;
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getTagName() {
        return tagName;
    }

    public String getDate() {
        return date;
    }

    public String getLink() {
        return link;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
