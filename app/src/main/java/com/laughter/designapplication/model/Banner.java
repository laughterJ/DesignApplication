package com.laughter.designapplication.model;

/**
 * 作者： 江浩
 * 创建时间： 2019/4/9
 * 版权： 江苏远大信息股份有限公司
 * 描述： com.laughter.designapplication.model
 */
public class Banner {

    private String title;
    private String imagePath;
    private String url;

    public String getTitle() {
        return title;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getUrl() {
        return url;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
