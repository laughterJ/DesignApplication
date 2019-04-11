package com.laughter.designapplication.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import org.litepal.crud.LitePalSupport;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 作者： 江浩
 * 创建时间： 2019/3/8
 * 版权： 江苏远大信息股份有限公司
 * 描述： com.laughter.designapplication.model
 */
public class Tree extends LitePalSupport implements Parcelable {

    private String name;
    @SerializedName("id")
    private String cid;
    @SerializedName("children")
    private List<Tree> childTrees;

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

    public List<Tree> getChildTrees() {
        return childTrees;
    }

    public void setChildTrees(List<Tree> childTrees) {
        this.childTrees = childTrees;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(cid);
        dest.writeParcelableArray(childTrees.toArray(new Tree[0]), flags);
    }

    public static final Creator<Tree> CREATOR = new Creator<Tree>() {
        @Override
        public Tree createFromParcel(Parcel source) {
            return new Tree(source);
        }

        @Override
        public Tree[] newArray(int size) {
            return new Tree[size];
        }
    };

    private Tree(Parcel in) {
        name = in.readString();
        cid = in.readString();
        childTrees = new ArrayList<>();
        in.readTypedList(childTrees, Tree.CREATOR);
    }
}
