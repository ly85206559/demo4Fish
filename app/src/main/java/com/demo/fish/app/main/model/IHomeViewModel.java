package com.demo.fish.app.main.model;

import android.databinding.Observable;
import android.databinding.ObservableList;

import com.demo.fish.app.main.entity.BannerEntity;
import com.demo.fish.app.main.entity.FunctionItemEntity;
import com.demo.fish.app.main.entity.HomeListEntity;
import com.demo.fish.core.mvvm.binding.IBinding;

import java.util.List;

/**
 * Created by Ben on 2017/4/9.
 */

public interface IHomeViewModel extends IBinding {
    /**
     * 页面刷新
     */
    void startRefresh(boolean notify);

    /**
     * 获取BannerList
     *
     * @return
     */
    List<BannerEntity> getBannerList();

    /**
     * 刷新数据
     */
    void refreshData();

    /**
     * 加载Banner
     */
    void loadBanner();

    /**
     * 为BannerList添加监听器
     *
     * @param callback
     */
    void addBannerListChangedCallback(ObservableList.OnListChangedCallback callback);

    /**
     * 监听HomeFragment的数据
     *
     * @param callback
     */
    void addHomeEntityChangedCallback(Observable.OnPropertyChangedCallback callback);

    /**
     * 处理Banner点击事件
     *
     * @param entity
     */
    void onBannerItemClick(BannerEntity entity);

    /**
     * 获取功能区域列表
     *
     * @return
     */
    List<FunctionItemEntity> getFunctionList();

    /**
     * 监听功能列表数据
     *
     * @param callback
     */
    void addFunctionListChangedCallback(ObservableList.OnListChangedCallback callback);

    /**
     * 处理功能区域点击事件
     *
     * @param entity
     */
    void onFunctionItemClick(FunctionItemEntity entity);

    /**
     * 获取首页列表
     *
     * @return
     */
    List<HomeListEntity> getHomeList();

    /**
     * 对首页列表数据监听
     *
     * @param callback
     */
    void addHomeListChangedCallback(ObservableList.OnListChangedCallback callback);

    /**
     * 刷新首页主列表数据
     */
    void loadHomeData();

    /**
     * 新鲜的和附近的列表切换
     *
     * @param position
     */
    void changeHomeData(int position);

    /**
     * 加载更多数据
     */
    void loadMore();
}
