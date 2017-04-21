package com.demo.fish.app.main.entity;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.demo.fish.BR;

/**
 * Created by Ben on 2017/4/9.
 */

public class HomeEntity extends BaseObservable {

    //列表类型 0:新鲜的 1:附近的
    public static final int LIST_TYPE_FRESH = 0;
    public static final int LIST_TYPE_NEAR = 1;

    private int bannerCount;
    private int listType = LIST_TYPE_FRESH;
    private boolean refreshLoading;
    private boolean nearLoading;
    private boolean refreshing;
    private int refreshMoreStatus;
    private int nearMoreStatus;
    private int loadingMoreStatus;

    @Bindable
    public int getBannerCount() {
        return bannerCount;
    }

    public void setBannerCount(int bannerCount) {
        this.bannerCount = bannerCount;
        notifyPropertyChanged(BR.bannerCount);
    }

    @Bindable
    public int getListType() {
        return listType;
    }

    public void setListType(int listType) {
        setListType(listType, true);
    }

    public void setListType(int listType, boolean notify) {
        this.listType = listType;
        if (notify) {
            notifyPropertyChanged(BR.listType);
        }
    }

    @Bindable
    public boolean isRefreshLoading() {
        return refreshLoading;
    }

    public void setRefreshLoading(boolean refreshLoading) {
        if (this.refreshLoading != refreshLoading) {
            this.refreshLoading = refreshLoading;
            notifyPropertyChanged(BR.refreshLoading);
        }
    }

    @Bindable
    public boolean isNearLoading() {
        return nearLoading;
    }

    public void setNearLoading(boolean nearLoading) {
        if (this.nearLoading != nearLoading) {
            this.nearLoading = nearLoading;
            notifyPropertyChanged(BR.nearLoading);
        }
    }

    @Bindable
    public boolean isRefreshing() {
        return refreshing;
    }

    public void setRefreshing(boolean refreshing) {
        setRefreshing(refreshing, true);
    }

    public void setRefreshing(boolean refreshing, boolean notify) {
        this.refreshing = refreshing;
        if (notify) {
            notifyPropertyChanged(BR.refreshing);
        }
    }

    public int getRefreshMoreStatus() {
        return refreshMoreStatus;
    }

    public void setRefreshMoreStatus(int refreshMoreStatus) {
        this.refreshMoreStatus = refreshMoreStatus;
    }

    public int getNearMoreStatus() {
        return nearMoreStatus;
    }

    public void setNearMoreStatus(int nearMoreStatus) {
        this.nearMoreStatus = nearMoreStatus;
    }

    @Bindable
    public int getLoadingMoreStatus() {
        return loadingMoreStatus;
    }

    public void setLoadingMoreStatus(int loadingMoreStatus) {
        setLoadingMoreStatus(loadingMoreStatus, true);
    }

    public void setLoadingMoreStatus(int loadingMoreStatus, boolean notify) {
        this.loadingMoreStatus = loadingMoreStatus;
        if (notify) {
            notifyPropertyChanged(BR.loadingMoreStatus);
        }
    }
}
