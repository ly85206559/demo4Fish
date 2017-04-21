package com.demo.fish.app.main.entity;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.demo.fish.BR;

import java.util.List;

/**
 * Created by Ben on 2017/4/9.
 */

public class HomeListEntity extends BaseObservable {

    private String iconUrl;
    private String name;
    private boolean followed;
    private String date;
    private String address;
    private String desc;
    private String groupName;
    private int commentCount;
    private int likeCount;
    private List<String> photoList;
    private boolean liked;

    @Bindable
    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
        notifyPropertyChanged(BR.iconUrl);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Bindable
    public boolean isFollowed() {
        return followed;
    }

    public void setFollowed(boolean followed) {
        this.followed = followed;
        notifyPropertyChanged(BR.followed);
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public List<String> getPhotoList() {
        return photoList;
    }

    public void setPhotoList(List<String> photoList) {
        this.photoList = photoList;
    }

    @Bindable
    public boolean isLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
        notifyPropertyChanged(BR.liked);
    }
}
