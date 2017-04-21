package com.demo.fish.app.main.model.impl;

import android.databinding.Observable;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.net.Uri;

import com.chad.library.adapter.base.loadmore.LoadMoreView;
import com.facebook.common.util.UriUtil;
import com.demo.fish.R;
import com.demo.fish.app.main.entity.BannerEntity;
import com.demo.fish.app.main.entity.FunctionItemEntity;
import com.demo.fish.app.main.entity.HomeEntity;
import com.demo.fish.app.main.entity.HomeListEntity;
import com.demo.fish.core.mvvm.ViewLayer;
import com.demo.fish.core.mvvm.ViewModel;
import com.demo.fish.app.main.model.IHomeViewModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Ben on 2017/4/9.
 */

public class HomeViewModel extends ViewModel implements IHomeViewModel {

    private final int PAGE_SIZE = 10;

    private HomeEntity mEntity;

    private ObservableList<HomeListEntity> mHomeList;
    private List<HomeListEntity> mFreshList;

    private List<HomeListEntity> mNearList;
    private ObservableList<BannerEntity> mBannerList;

    private ObservableList<FunctionItemEntity> mFunctionList;

    private int mRefreshListPage = 1;
    private int mNearListPage = 1;

    public HomeViewModel(ViewLayer viewLayer) {
        super(viewLayer);
    }

    @Override
    protected void onAttach() {
        initData();
    }

    @Override
    protected void onDetach() {

    }

    private void initData() {
        mEntity = new HomeEntity();
        mBannerList = new ObservableArrayList<>();
        mHomeList = new ObservableArrayList<>();
        mFreshList = new ArrayList<>();
        mNearList = new ArrayList<>();

        mEntity.setRefreshMoreStatus(LoadMoreView.STATUS_DEFAULT);
        mEntity.setNearMoreStatus(LoadMoreView.STATUS_DEFAULT);
        initFunctionEntities();
    }

    private void initFunctionEntities() {
        mFunctionList = new ObservableArrayList<>();
        String iconUrl = UriUtil.getUriForResourceId(R.mipmap.ic_launcher).toString();
        for (int i = 0; i < 6; i++) {
            FunctionItemEntity entity = new FunctionItemEntity();
            entity.setIconUrl(iconUrl);
            entity.setTitle("测试标题" + i);
            entity.setDesc("测试描述" + i);
            mFunctionList.add(entity);
        }
    }

    @Override
    public void startRefresh(boolean notify) {
        mEntity.setRefreshing(true, notify);
    }

    @Override
    public List<BannerEntity> getBannerList() {
        return mBannerList;
    }

    @Override
    public void refreshData() {
        resetData();
        loadBanner();
        loadHomeData();
    }

    private void resetData() {
        mEntity.setListType(HomeEntity.LIST_TYPE_FRESH);
        mRefreshListPage = 1;
        mNearListPage = 1;
        mEntity.setLoadingMoreStatus(LoadMoreView.STATUS_DEFAULT);
        mEntity.setRefreshMoreStatus(LoadMoreView.STATUS_DEFAULT);
        mEntity.setNearMoreStatus(LoadMoreView.STATUS_DEFAULT);
    }

    @Override
    public void loadBanner() {
        Uri[] bannerList = {
                UriUtil.getUriForResourceId(R.drawable.banner_1),
                UriUtil.getUriForResourceId(R.drawable.banner_2),
                UriUtil.getUriForResourceId(R.drawable.banner_3),
                UriUtil.getUriForResourceId(R.drawable.banner_4)
        };

        List<BannerEntity> banners = new ArrayList<>();
        for (int i = 0; i < bannerList.length; i++) {
            BannerEntity entity = new BannerEntity();
            entity.setImageUrl(bannerList[i].toString());
            banners.add(entity);
        }

        mBannerList.clear();
        mBannerList.addAll(banners);

        mEntity.setBannerCount(mBannerList.size());
    }

    @Override
    public void addBannerListChangedCallback(ObservableList.OnListChangedCallback callback) {
        addObservableListBinding(mBannerList, callback);
    }

    @Override
    public void addHomeEntityChangedCallback(Observable.OnPropertyChangedCallback callback) {
        addObservableBinding(mEntity, callback);
    }

    @Override
    public void onBannerItemClick(BannerEntity entity) {
        //TODO 事件处理
    }

    @Override
    public List<FunctionItemEntity> getFunctionList() {
        return mFunctionList;
    }

    @Override
    public void addFunctionListChangedCallback(ObservableList.OnListChangedCallback callback) {
        addObservableListBinding(mFunctionList, callback);
    }

    @Override
    public void onFunctionItemClick(FunctionItemEntity entity) {
        //TODO 事件处理
    }

    @Override
    public List<HomeListEntity> getHomeList() {
        return mHomeList;
    }

    @Override
    public void addHomeListChangedCallback(ObservableList.OnListChangedCallback callback) {
        addObservableListBinding(mHomeList, callback);
    }

    @Override
    public void loadHomeData() {
        mHomeList.clear();
        mEntity.setRefreshLoading(true);

        io.reactivex.Observable
                .create(new ObservableOnSubscribe<List<HomeListEntity>>() {
                    @Override
                    public void subscribe(@NonNull ObservableEmitter<List<HomeListEntity>> e) throws Exception {
                        List<HomeListEntity> list = new ArrayList<>();
                        String message = "新鲜的 ";
                        for (int i = 1; i <= PAGE_SIZE; i++) {
                            HomeListEntity entity = new HomeListEntity();
                            entity.setName(message + i);
                            entity.setDesc(message + i);
                            entity.setCommentCount(i);
                            entity.setLikeCount(i);
                            entity.setDate(new Date().toString());
                            entity.setAddress(message + i);
                            entity.setGroupName(message + i);
                            entity.setIconUrl(UriUtil.getUriForResourceId(R.drawable.ic_default_icon).toString());
                            List<String> list1 = new ArrayList<>();
                            for (int j = 0; j < 7; j++) {
                                list1.add(UriUtil.getUriForResourceId(R.drawable.ic_test1).toString());
                            }
                            entity.setPhotoList(list1);
                            entity.setLiked(i % 2 == 0 ? true : false);
                            list.add(entity);
                        }

                        Thread.sleep(2000);
                        e.onNext(list);
                        e.onComplete();
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<List<HomeListEntity>>() {
                    @Override
                    public void onNext(List<HomeListEntity> homeListEntities) {
                        mFreshList.clear();
                        mNearList.clear();

                        mFreshList.addAll(homeListEntities);
                        mHomeList.clear();
                        mHomeList.addAll(mFreshList);
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (mEntity.isRefreshLoading()) {
                            mEntity.setRefreshLoading(false);
                        }
                        mEntity.setRefreshing(false);
                    }

                    @Override
                    public void onComplete() {
                        if (mEntity.isRefreshLoading()) {
                            mEntity.setRefreshLoading(false);
                        }
                        mEntity.setRefreshing(false);
                    }
                });
    }

    @Override
    public void changeHomeData(int position) {
        int type = position;
        if (type == mEntity.getListType()) {
            return;
        }

        mEntity.setNearLoading(false);
        mEntity.setRefreshLoading(false);
        mEntity.setListType(type, false);
        if (type == HomeEntity.LIST_TYPE_FRESH) {
            if (mFreshList.size() > 0) {
                mHomeList.clear();
                mHomeList.addAll(mFreshList);
                mEntity.setLoadingMoreStatus(mEntity.getRefreshMoreStatus());
            } else {
                loadRefreshList();
            }
        } else {
            if (mNearList.size() > 0) {
                mHomeList.clear();
                mHomeList.addAll(mNearList);
                mEntity.setLoadingMoreStatus(mEntity.getNearMoreStatus());
            } else {
                loadNearList();
            }
        }
    }

    @Override
    public void loadMore() {
        if (HomeEntity.LIST_TYPE_FRESH == mEntity.getListType()) {
            loadMoreRefreshList();
        } else {
            loadMoreNearList();
        }
    }

    private void loadRefreshList() {
        if (mEntity.isRefreshLoading()) {
            return;
        }
        mHomeList.clear();
        mEntity.setRefreshLoading(true);

        io.reactivex.Observable
                .create(new ObservableOnSubscribe<List<HomeListEntity>>() {
                    @Override
                    public void subscribe(@NonNull ObservableEmitter<List<HomeListEntity>> e) throws Exception {
                        List<HomeListEntity> list = new ArrayList<>();
                        String message = "新鲜的 ";
                        for (int i = 1; i <= PAGE_SIZE; i++) {
                            HomeListEntity entity = new HomeListEntity();
                            entity.setName(message + i);
                            entity.setDesc(message + i);
                            entity.setCommentCount(i);
                            entity.setLikeCount(i);
                            entity.setDate(new Date().toString());
                            entity.setAddress(message + i);
                            entity.setGroupName(message + i);
                            entity.setIconUrl(UriUtil.getUriForResourceId(R.drawable.ic_default_icon).toString());
                            List<String> list1 = new ArrayList<>();
                            for (int j = 0; j < 7; j++) {
                                list1.add(UriUtil.getUriForResourceId(R.drawable.ic_test1).toString());
                            }
                            entity.setPhotoList(list1);
                            entity.setLiked(i % 2 == 0 ? true : false);
                            list.add(entity);
                        }

                        Thread.sleep(1000);
                        e.onNext(list);
                        e.onComplete();
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<List<HomeListEntity>>() {
                    @Override
                    public void onNext(List<HomeListEntity> homeListEntities) {
                        mFreshList.clear();
                        mFreshList.addAll(homeListEntities);

                        if (HomeEntity.LIST_TYPE_FRESH == mEntity.getListType()) {
                            mHomeList.addAll(mFreshList);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (mEntity.isRefreshLoading()) {
                            mEntity.setRefreshLoading(false);
                        }
                    }

                    @Override
                    public void onComplete() {
                        if (mEntity.isRefreshLoading()) {
                            mEntity.setRefreshLoading(false);
                        }
                    }
                });
    }

    private void loadNearList() {
        if (mEntity.isNearLoading()) {
            return;
        }
        mHomeList.clear();
        mEntity.setNearLoading(true);

        io.reactivex.Observable
                .create(new ObservableOnSubscribe<List<HomeListEntity>>() {
                    @Override
                    public void subscribe(@NonNull ObservableEmitter<List<HomeListEntity>> e) throws Exception {
                        List<HomeListEntity> list = new ArrayList<>();
                        String message = "附近的 ";
                        for (int i = 1; i <= PAGE_SIZE; i++) {
                            HomeListEntity entity = new HomeListEntity();
                            entity.setName(message + i);
                            entity.setDesc(message + i);
                            entity.setCommentCount(i);
                            entity.setLikeCount(i);
                            entity.setDate(new Date().toString());
                            entity.setAddress(message + i);
                            entity.setGroupName(message + i);
                            entity.setIconUrl(UriUtil.getUriForResourceId(R.drawable.ic_default_icon).toString());
                            List<String> list1 = new ArrayList<>();
                            for (int j = 0; j < 7; j++) {
                                list1.add(UriUtil.getUriForResourceId(R.drawable.ic_test1).toString());
                            }
                            entity.setLiked(i % 2 == 0 ? true : false);
                            entity.setPhotoList(list1);
                            list.add(entity);
                        }

                        Thread.sleep(1000);
                        e.onNext(list);
                        e.onComplete();
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<List<HomeListEntity>>() {
                    @Override
                    public void onNext(List<HomeListEntity> homeListEntities) {
                        mNearList.clear();
                        mNearList.addAll(homeListEntities);

                        if (HomeEntity.LIST_TYPE_NEAR == mEntity.getListType()) {
                            mHomeList.clear();
                            mHomeList.addAll(mNearList);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (mEntity.isNearLoading()) {
                            mEntity.setNearLoading(false);
                        }
                    }

                    @Override
                    public void onComplete() {
                        if (mEntity.isNearLoading()) {
                            mEntity.setNearLoading(false);
                        }
                    }
                });
    }

    private void loadMoreRefreshList() {
        mEntity.setRefreshMoreStatus(LoadMoreView.STATUS_LOADING);
        mEntity.setLoadingMoreStatus(mEntity.getRefreshMoreStatus(), false);

        io.reactivex.Observable
                .create(new ObservableOnSubscribe<List<HomeListEntity>>() {
                    @Override
                    public void subscribe(@NonNull ObservableEmitter<List<HomeListEntity>> e) throws Exception {
                        List<HomeListEntity> list = new ArrayList<>();
                        String message = "新鲜的 ";
                        int size;
                        if (mRefreshListPage > 2) {
                            size = mRefreshListPage * PAGE_SIZE + 5;
                        } else {
                            size = (mRefreshListPage + 1) * PAGE_SIZE;
                        }
                        for (int i = mRefreshListPage * PAGE_SIZE + 1; i <= size; i++) {
                            HomeListEntity entity = new HomeListEntity();
                            entity.setName(message + i);
                            entity.setDesc(message + i);
                            entity.setCommentCount(i);
                            entity.setLikeCount(i);
                            entity.setDate(new Date().toString());
                            entity.setAddress(message + i);
                            entity.setGroupName(message + i);
                            entity.setIconUrl(UriUtil.getUriForResourceId(R.drawable.ic_default_icon).toString());
                            List<String> list1 = new ArrayList<>();
                            for (int j = 0; j < 7; j++) {
                                list1.add(UriUtil.getUriForResourceId(R.drawable.ic_test1).toString());
                            }
                            entity.setLiked(i % 2 == 0 ? true : false);
                            entity.setPhotoList(list1);
                            list.add(entity);
                        }

                        Thread.sleep(1000);
                        e.onNext(list);
                        e.onComplete();
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<List<HomeListEntity>>() {
                    @Override
                    public void onNext(List<HomeListEntity> homeListEntities) {
                        if (null != homeListEntities && homeListEntities.size() > 0) {
                            mFreshList.addAll(homeListEntities);

                            if (HomeEntity.LIST_TYPE_FRESH == mEntity.getListType()) {
                                mHomeList.addAll(homeListEntities);
                            }
                        }

                        if (null == homeListEntities
                                || homeListEntities.size() < PAGE_SIZE) {
                            mEntity.setRefreshMoreStatus(LoadMoreView.STATUS_END);
                            if (HomeEntity.LIST_TYPE_FRESH == mEntity.getListType()) {
                                mEntity.setLoadingMoreStatus(mEntity.getRefreshMoreStatus());
                            }
                        } else {
                            mEntity.setRefreshMoreStatus(LoadMoreView.STATUS_DEFAULT);
                            if (HomeEntity.LIST_TYPE_FRESH == mEntity.getListType()) {
                                mEntity.setLoadingMoreStatus(mEntity.getRefreshMoreStatus());
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        mEntity.setRefreshMoreStatus(LoadMoreView.STATUS_FAIL);
                        if (HomeEntity.LIST_TYPE_FRESH == mEntity.getListType()) {
                            mEntity.setLoadingMoreStatus(mEntity.getRefreshMoreStatus());
                        }
                    }

                    @Override
                    public void onComplete() {
                        mRefreshListPage++;
                    }
                });
    }

    private void loadMoreNearList() {
        mEntity.setNearMoreStatus(LoadMoreView.STATUS_LOADING);
        mEntity.setLoadingMoreStatus(mEntity.getNearMoreStatus(), false);

        io.reactivex.Observable
                .create(new ObservableOnSubscribe<List<HomeListEntity>>() {
                    @Override
                    public void subscribe(@NonNull ObservableEmitter<List<HomeListEntity>> e) throws Exception {
                        List<HomeListEntity> list = new ArrayList<>();
                        String message = "附近的 ";
                        int size;
                        if (mNearListPage > 2) {
                            size = mNearListPage * PAGE_SIZE + 5;
                        } else {
                            size = (mNearListPage + 1) * PAGE_SIZE;
                        }
                        for (int i = mNearListPage * PAGE_SIZE + 1; i <= size; i++) {
                            HomeListEntity entity = new HomeListEntity();
                            entity.setName(message + i);
                            entity.setDesc(message + i);
                            entity.setCommentCount(i);
                            entity.setLikeCount(i);
                            entity.setDate(new Date().toString());
                            entity.setAddress(message + i);
                            entity.setGroupName(message + i);
                            entity.setIconUrl(UriUtil.getUriForResourceId(R.drawable.ic_default_icon).toString());
                            List<String> list1 = new ArrayList<>();
                            for (int j = 0; j < 7; j++) {
                                list1.add(UriUtil.getUriForResourceId(R.drawable.ic_test1).toString());
                            }
                            entity.setLiked(i % 2 == 0 ? true : false);
                            entity.setPhotoList(list1);
                            list.add(entity);
                        }

                        Thread.sleep(1000);
                        e.onNext(list);
                        e.onComplete();
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<List<HomeListEntity>>() {
                    @Override
                    public void onNext(List<HomeListEntity> homeListEntities) {
                        if (null != homeListEntities && homeListEntities.size() > 0) {
                            mNearList.addAll(homeListEntities);

                            if (HomeEntity.LIST_TYPE_NEAR == mEntity.getListType()) {
                                mHomeList.addAll(homeListEntities);
                            }
                        }

                        if (null == homeListEntities
                                || homeListEntities.size() < PAGE_SIZE) {
                            mEntity.setNearMoreStatus(LoadMoreView.STATUS_END);
                            if (HomeEntity.LIST_TYPE_NEAR == mEntity.getListType()) {
                                mEntity.setLoadingMoreStatus(mEntity.getNearMoreStatus());
                            }
                        } else {
                            mEntity.setNearMoreStatus(LoadMoreView.STATUS_DEFAULT);
                            if (HomeEntity.LIST_TYPE_NEAR == mEntity.getListType()) {
                                mEntity.setLoadingMoreStatus(mEntity.getNearMoreStatus());
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        mEntity.setNearMoreStatus(LoadMoreView.STATUS_FAIL);
                        if (HomeEntity.LIST_TYPE_NEAR == mEntity.getListType()) {
                            mEntity.setLoadingMoreStatus(mEntity.getNearMoreStatus());
                        }
                    }

                    @Override
                    public void onComplete() {
                        mNearListPage++;
                    }
                });
    }
}
