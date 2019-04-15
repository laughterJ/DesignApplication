package com.laughter.designapplication.model;

/**
 * created by JH at 2019/4/15
 * des： com.laughter.designapplication.model
 */

public class TodoItem {

    private String title;
    private String content;
    private String dateStr;
    private int priority;
    private int status;
    private int id;

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setDateStr(String dateStr) {
        this.dateStr = dateStr;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getDateStr() {
        return dateStr;
    }

    public int getPriority() {
        return priority;
    }

    public int getStatus() {
        return status;
    }

    public int getId() {
        return id;
    }
}
