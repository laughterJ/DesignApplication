package com.laughter.designapplication.model;

/**
 * created by JH at 2019/4/11
 * des： com.laughter.designapplication.model
 */
public class Project {
    private String author;
    private String title;
    private String desc;
    private String envelopePic;
    private String link;
    private String projectLink;
    private String niceDate;
    private String id;

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAuthor() {
        return author;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getLink() {
        return link;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDesc() {
        return desc;
    }

    public void setEnvelopePic(String envelopePic) {
        this.envelopePic = envelopePic;
    }

    public String getEnvelopePic() {
        return envelopePic;
    }

    public void setNiceDate(String niceDate) {
        this.niceDate = niceDate;
    }

    public String getNiceDate() {
        return niceDate;
    }

    public void setProjectLink(String projectLink) {
        this.projectLink = projectLink;
    }

    public String getProjectLink() {
        return projectLink;
    }

    public String getId() {
        return id;
    }
}
