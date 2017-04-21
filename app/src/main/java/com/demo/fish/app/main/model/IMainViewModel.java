package com.demo.fish.app.main.model;

import android.databinding.Observable;
import android.databinding.ObservableList;

import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.demo.fish.core.mvvm.binding.IBinding;

import java.util.List;

/**
 * Created by Ben on 2017/4/5.
 */

public interface IMainViewModel extends IBinding {

    List<IDrawerItem> getDrawerItems();

    void addDrawerItemsChangedCallback(ObservableList.OnListChangedCallback callback);

    void addCurrentIndexChangedCallback(Observable.OnPropertyChangedCallback callback);

    /**
     * 设置侧边栏头部信息
     */
    void loadNavHeaderData();

    /**
     * 设置侧边栏条目信息
     */
    void loadDrawerItemData();

    /**
     * 设置当前页
     *
     * @param index
     */
    void setIndex(int index);
}
