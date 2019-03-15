package com.laughter.designapplication.model;

import org.litepal.crud.LitePalSupport;

/**
 * 作者： 江浩
 * 创建时间： 2019/3/14
 * 版权： 江苏远大信息股份有限公司
 * 描述： com.laughter.designapplication.model
 */
public class Project {
    private String author;
    private String title;
    private String desc;
    private String envelopePic;
    private String link;
    private String projectLink;
    private String niceDate;

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
}
