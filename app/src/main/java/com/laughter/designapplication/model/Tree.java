package com.laughter.designapplication.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 作者： 江浩
 * 创建时间： 2019/3/8
 * 版权： 江苏远大信息股份有限公司
 * 描述： com.laughter.designapplication.model
 */
public class Tree {

    private String name;
    @SerializedName("id")
    private String cid;
    @SerializedName("children")
    private List<Tree> childTrsss;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public List<Tree> getChildTrsss() {
        return childTrsss;
    }

    public void setChildTrsss(List<Tree> childTrsss) {
        this.childTrsss = childTrsss;
    }
}
