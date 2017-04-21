package com.demo.fish.app.main.ui.layer;

import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.android.databinding.library.baseAdapters.BR;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.loadmore.LoadMoreView;
import com.demo.fish.R;
import com.demo.fish.app.main.entity.BannerEntity;
import com.demo.fish.app.main.entity.FunctionItemEntity;
import com.demo.fish.app.main.entity.HomeEntity;
import com.demo.fish.app.main.ui.adapter.BannerAdapter;
import com.demo.fish.app.main.ui.adapter.FunctionListAdapter;
import com.demo.fish.app.main.ui.adapter.HomeListAdapter;
import com.demo.fish.app.main.model.IHomeViewModel;
import com.demo.fish.bind.callback.BaseQuickAdapterChangedCallback;
import com.demo.fish.bind.callback.RecyclerViewAdapterChangedCallback;
import com.demo.fish.core.mvvm.DataBindingViewLayer;
import com.demo.fish.databinding.FragmentHomeBinding;
import com.demo.fish.databinding.LayoutHomeBannerBinding;
import com.demo.fish.databinding.LayoutHomeFunctionBinding;
import com.demo.fish.databinding.LayoutHomeLoadingBinding;
import com.demo.fish.utils.DisplayUtil;

/**
 * Created by Ben on 2017/4/9.
 */

public class HomeViewLayer extends DataBindingViewLayer<FragmentHomeBinding, IHomeViewModel, Fragment> implements SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {

    private IHomeViewModel mViewModel;
    private LayoutHomeBannerBinding mBannerBinding;
    private LayoutHomeFunctionBinding mFunctionBinding;
    private LayoutHomeLoadingBinding mLoadingBinding;

    private FrameLayout mViewContainer;
    private RecyclerView mHomeList;
    private RecyclerView mBannerListView;
    private RecyclerView mFunctionListView;
    private LinearLayout mDotLayout;
    private TabLayout mTabLayout;
    private TabLayout mStickyTabLayout;
    private View mLoadingView;

    private HomeListAdapter mAdapter;
    private BannerAdapter mBannerAdapter;
    private FunctionListAdapter mFunctionAdapter;

    private int mStickyPositionY;
    private int mHomeListPositionY;
    private int mInitPositionY = -1;//初始状态下RecycleView的Y坐标

    private int mOffsetY;//Tab切换时需要记录下之前的偏移量
    //Tab切换的时候需要记录下之前所处的列表位置
    private int mRefreshPosition = 0;
    private int mNearPosition = 0;

    public HomeViewLayer(FragmentHomeBinding binding, Fragment fragment) {
        super(binding, fragment);
    }

    @Override
    protected void onAttach(IHomeViewModel viewModel) {
        mViewModel = viewModel;

        initView();
        initDataBinding();
        initData();
    }

    @Override
    protected void onDetach() {
        mBannerListView.removeCallbacks(mSlideRunnable);
        mBannerBinding.unbind();
        mFunctionBinding.unbind();
        mLoadingBinding.unbind();
        super.onDetach();
    }

    private void initView() {
        initHomeListView();
        initBannerListView();
        initFunctionListView();
        initTabLayout();
        initLoadingLayout();

        addHeaderView();
    }

    private void initData() {
        int statusBarHeight = -1;
        int resourceId = mContainer.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = mContainer.getResources().getDimensionPixelSize(resourceId);
        }
        mStickyPositionY = statusBarHeight + DisplayUtil.dip2px(mContainer.getContext(), 46);
        mOffsetY = DisplayUtil.dip2px(mContainer.getContext(), 46);
        mRefreshPosition = mAdapter.getHeaderLayoutCount();
        mNearPosition = mAdapter.getHeaderLayoutCount();
    }

    private void initHomeListView() {
        //设置SwipeRefresh布局
        mBinding.layoutRefresh.setColorSchemeResources(R.color.colorPrimary);
        mBinding.layoutRefresh.setOnRefreshListener(this);

        mViewContainer = mBinding.container;
        mHomeList = mBinding.list;

        mAdapter = new HomeListAdapter(mContainer.getContext(), R.layout.item_home_list, mViewModel.getHomeList());
        mAdapter.setOnLoadMoreListener(this, mHomeList);
        LinearLayoutManager manager = new LinearLayoutManager(mContainer.getContext()) {
            @Override
            public boolean onRequestChildFocus(RecyclerView parent, RecyclerView.State state, View child, View focused) {
                //TODO 暂时处理View焦点问题
                return true;
            }
        };
        mHomeList.setLayoutManager(manager);
        mHomeList.setAdapter(mAdapter);

        mHomeList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int[] location = new int[2];
                mTabLayout.getLocationInWindow(location);
                int count = mViewContainer.getChildCount();
                if (location[1] <= mStickyPositionY) {
                    if (count == 1) {
                        mViewContainer.addView(mStickyTabLayout);
                        mBinding.layoutRefresh.setEnabled(false);
                    }
                } else {
                    if (count > 1) {
                        mViewContainer.removeView(mStickyTabLayout);
                        mOffsetY = DisplayUtil.dip2px(mContainer.getContext(), 46);
                        mRefreshPosition = mAdapter.getHeaderLayoutCount();
                        mNearPosition = mAdapter.getHeaderLayoutCount();
                        mBinding.layoutRefresh.setEnabled(true);
                    }
                }

                if (mInitPositionY == -1) {
                    mInitPositionY = location[1];
                }
                mHomeListPositionY = location[1];
            }
        });
    }

    private void initBannerListView() {
        mBannerBinding = DataBindingUtil.inflate(
                LayoutInflater.from(mContainer.getContext()),
                R.layout.layout_home_banner,
                null, false);

        mBannerListView = mBannerBinding.list;
        mDotLayout = mBannerBinding.layoutDot;

        int screenWidth = DisplayUtil.getScreenWidth(mContainer.getContext());
        int height = (int) (screenWidth / 2.13);
        RelativeLayout.LayoutParams params =
                new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, height);
        mBannerListView.setLayoutParams(params);

        mBannerAdapter = new BannerAdapter(mContainer.getContext(), mViewModel.getBannerList());
        mBannerAdapter.setListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BannerEntity entity = (BannerEntity) v.getTag();
                mViewModel.onBannerItemClick(entity);
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContainer.getActivity(), LinearLayoutManager.HORIZONTAL, false);
        mBannerListView.setLayoutManager(layoutManager);
        mBannerListView.setAdapter(mBannerAdapter);
        mBannerListView.addOnScrollListener(mBannerScrollListener);
        mBannerListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEvent.ACTION_DOWN == event.getAction()) {
                    stopSlideBanner();
                } else if (MotionEvent.ACTION_UP == event.getAction()) {
                    startSlideBanner();
                }
                return false;
            }
        });

        PagerSnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(mBannerListView);
    }

    private void initFunctionListView() {
        mFunctionBinding = DataBindingUtil.inflate(
                LayoutInflater.from(mContainer.getContext()),
                R.layout.layout_home_function,
                null, false);
        mFunctionListView = mFunctionBinding.listFunction;
        mFunctionAdapter = new FunctionListAdapter(mContainer.getContext(), mViewModel.getFunctionList());
        mFunctionAdapter.setListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FunctionItemEntity entity = (FunctionItemEntity) v.getTag();
                mViewModel.onFunctionItemClick(entity);
            }
        });

        GridLayoutManager manager = new GridLayoutManager(mContainer.getContext(), 2) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        mFunctionListView.setLayoutManager(manager);
        mFunctionListView.setAdapter(mFunctionAdapter);
    }

    private void initTabLayout() {
        mTabLayout = new TabLayout(mContainer.getContext());
        mTabLayout.setLayoutParams(
                new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        DisplayUtil.dip2px(mContainer.getContext(), 46))
        );
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
        mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        mTabLayout.setSelectedTabIndicatorColor(mContainer.getResources().getColor(R.color.colorPrimary));
        mTabLayout.setSelectedTabIndicatorHeight(DisplayUtil.dip2px(mContainer.getContext(), 3));
        mTabLayout.setTabTextColors(
                mContainer.getResources().getColor(R.color.colorTextGary),
                mContainer.getResources().getColor(R.color.colorTextNormal)
        );
        mTabLayout.setBackgroundResource(R.drawable.shape_tab_layout_bg);
        TabLayout.Tab tab = mTabLayout.newTab().setText("新鲜的");
        mTabLayout.addTab(tab, true);
        tab = mTabLayout.newTab().setText("附近的");
        mTabLayout.addTab(tab);
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                //设置粘贴TabLayout的选中Tab
                if (!mStickyTabLayout.getTabAt(position).isSelected()) {
                    mStickyTabLayout.getTabAt(position).select();
                    mViewModel.changeHomeData(position);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        mStickyTabLayout = new TabLayout(mContainer.getContext());
        mStickyTabLayout.setLayoutParams(
                new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        DisplayUtil.dip2px(mContainer.getContext(), 46))
        );
        mStickyTabLayout.setTabMode(TabLayout.MODE_FIXED);
        mStickyTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        mStickyTabLayout.setSelectedTabIndicatorColor(mContainer.getResources().getColor(R.color.colorPrimary));
        mStickyTabLayout.setSelectedTabIndicatorHeight(DisplayUtil.dip2px(mContainer.getContext(), 3));
        mStickyTabLayout.setTabTextColors(
                mContainer.getResources().getColor(R.color.colorTextGary),
                mContainer.getResources().getColor(R.color.colorTextNormal)
        );
        mStickyTabLayout.setBackgroundResource(R.drawable.shape_tab_layout_bg);
        tab = mStickyTabLayout.newTab().setText("新鲜的");
        mStickyTabLayout.addTab(tab, true);
        tab = mStickyTabLayout.newTab().setText("附近的");
        mStickyTabLayout.addTab(tab);
        mStickyTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                if (!mTabLayout.getTabAt(position).isSelected()) {
                    mTabLayout.getTabAt(position).select();
                    mHomeList.stopScroll();

                    mAdapter.setEnableLoadMore(false);
                    mViewModel.changeHomeData(position);

                    LinearLayoutManager manager = (LinearLayoutManager) mHomeList.getLayoutManager();
                    if (mLoadingView.getVisibility() != View.VISIBLE) {
                        if (0 == position) {
                            manager.scrollToPositionWithOffset(mRefreshPosition, mOffsetY);
                        } else {
                            manager.scrollToPositionWithOffset(mNearPosition, mOffsetY);
                        }
                    }
                    int firstPosition = manager.findFirstVisibleItemPosition() + mAdapter.getHeaderLayoutCount();
                    View firstVisibleView = manager.findViewByPosition(firstPosition);
                    if (0 == position) {
                        mNearPosition = firstPosition;
                    } else {
                        mRefreshPosition = firstPosition;
                    }
                    if (null != firstVisibleView) {
                        mOffsetY = (int) firstVisibleView.getY();
                    }
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void initLoadingLayout() {
        mLoadingBinding = DataBindingUtil.inflate(
                LayoutInflater.from(mContainer.getContext()),
                R.layout.layout_home_loading,
                null, false);
        int width = DisplayUtil.getScreenWidth(mContainer.getContext());
        int height = DisplayUtil.getScreenHeight(mContainer.getContext())
                - DisplayUtil.dip2px(mContainer.getContext(), 46 * 3 + 18);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);
        mLoadingView = mLoadingBinding.getRoot();
        mLoadingView.setLayoutParams(params);
        mLoadingView.setVisibility(View.GONE);
    }

    private void addHeaderView() {
        mAdapter.addHeaderView(mBannerBinding.getRoot());
        mAdapter.addHeaderView(mFunctionBinding.getRoot());

        View view = new View(mContainer.getContext());
        view.setLayoutParams(
                new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        DisplayUtil.dip2px(mContainer.getContext(), 12))
        );
        view.setBackgroundColor(mContainer.getResources().getColor(R.color.colorBackground));
        mAdapter.addHeaderView(view);

        mAdapter.addHeaderView(mTabLayout);

        mAdapter.addHeaderView(mLoadingBinding.getRoot());
    }

    private void initDataBinding() {
        mViewModel.addHomeEntityChangedCallback(mCallback);
        mViewModel.addHomeListChangedCallback(new BaseQuickAdapterChangedCallback(mAdapter));
        mViewModel.addBannerListChangedCallback(new RecyclerViewAdapterChangedCallback(mBannerAdapter));
        mViewModel.addFunctionListChangedCallback(new RecyclerViewAdapterChangedCallback(mFunctionAdapter));
    }

    private void initBannerListPosition(int count) {
        if (count <= 0) {
            return;
        }
        mBannerListView.scrollToPosition(count * 1000);
    }

    private void initDotLayout(int count) {
        mDotLayout.removeAllViews();

        if (count <= 0) {
            return;
        }
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(
                DisplayUtil.dip2px(mContainer.getContext(), 4),
                0,
                DisplayUtil.dip2px(mContainer.getContext(), 4),
                0
        );
        for (int i = 0; i < count; i++) {
            ImageView dotView = new ImageView(mContainer.getContext());
            dotView.setImageResource(R.drawable.selector_indicator);
            dotView.setLayoutParams(params);
            mDotLayout.addView(dotView);
        }
        mDotLayout.getChildAt(0).setSelected(true);
    }

    private void startSlideBanner() {
        mBannerListView.removeCallbacks(mSlideRunnable);
        mBannerListView.postDelayed(mSlideRunnable, 5000);
    }

    private void stopSlideBanner() {
        mBannerListView.removeCallbacks(mSlideRunnable);
    }

    private Observable.OnPropertyChangedCallback mCallback = new Observable.OnPropertyChangedCallback() {
        @Override
        public void onPropertyChanged(Observable sender, int propertyId) {
            HomeEntity entity = (HomeEntity) sender;
            if (propertyId == BR.bannerCount) {
                int bannerCount = entity.getBannerCount();
                initBannerListPosition(bannerCount);
                initDotLayout(bannerCount);
                startSlideBanner();
            } else if (propertyId == BR.refreshLoading) {
                if (HomeEntity.LIST_TYPE_FRESH != entity.getListType()) {
                    return;
                }

                setLoadingView(entity.isRefreshLoading(), entity.getListType());
                if (mLoadingView.getVisibility() == View.GONE) {
                    mAdapter.setEnableLoadMore(true);
                }
            } else if (propertyId == BR.nearLoading) {
                if (HomeEntity.LIST_TYPE_NEAR != entity.getListType()) {
                    return;
                }

                setLoadingView(entity.isNearLoading(), entity.getListType());
                if (mLoadingView.getVisibility() == View.GONE) {
                    mAdapter.setEnableLoadMore(true);
                }
            } else if (propertyId == BR.refreshing) {
                if (entity.isRefreshing()) {
                    mBinding.layoutRefresh.post(new Runnable() {
                        @Override
                        public void run() {
                            mBinding.layoutRefresh.setRefreshing(true);
                            mViewModel.refreshData();
                        }
                    });
                } else {
                    mBinding.layoutRefresh.setRefreshing(false);
                }
            } else if (propertyId == BR.listType) {
                mTabLayout.getTabAt(entity.getListType()).select();
                mStickyTabLayout.getTabAt(entity.getListType()).select();
            } else if (propertyId == BR.loadingMoreStatus) {
                int status = entity.getLoadingMoreStatus();
                mAdapter.setEnableLoadMore(true);
                if (LoadMoreView.STATUS_DEFAULT == status) {
                    mAdapter.loadMoreComplete();
                } else if (LoadMoreView.STATUS_END == status) {
                    mAdapter.loadMoreEnd();
                } else if (LoadMoreView.STATUS_FAIL == status) {
                    mAdapter.loadMoreFail();
                }
            }
        }
    };

    private RecyclerView.OnScrollListener mBannerScrollListener = new RecyclerView.OnScrollListener() {

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                int count = mDotLayout.getChildCount();
                if (0 == count) {
                    return;
                }

                LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int position = manager.findFirstCompletelyVisibleItemPosition() % count;
                for (int i = 0; i < count; i++) {
                    View view = mDotLayout.getChildAt(i);
                    if (i == position) {
                        if (!view.isSelected()) {
                            view.setSelected(true);
                        }
                    } else {
                        if (view.isSelected()) {
                            view.setSelected(false);
                        }
                    }
                }
            }
        }
    };

    private Runnable mSlideRunnable = new Runnable() {
        @Override
        public void run() {
            LinearLayoutManager manager = (LinearLayoutManager) mBannerListView.getLayoutManager();
            int position = manager.findFirstVisibleItemPosition();
            mBannerListView.smoothScrollToPosition(position + 1);
            startSlideBanner();
        }
    };

    private void setLoadingView(boolean visible, int type) {
        int position;
        if (type == HomeEntity.LIST_TYPE_FRESH) {
            position = mRefreshPosition;
        } else {
            position = mNearPosition;
        }
        if (visible) {
            mLoadingView.setVisibility(View.VISIBLE);
            if (mStickyTabLayout.getVisibility() == View.VISIBLE) {
                LinearLayoutManager layoutManager = (LinearLayoutManager) mHomeList.getLayoutManager();
                layoutManager.scrollToPositionWithOffset(0, mHomeListPositionY - mInitPositionY);
            }
        } else {
            mLoadingView.setVisibility(View.GONE);
            LinearLayoutManager layoutManager = (LinearLayoutManager) mHomeList.getLayoutManager();
            if (mViewContainer.getChildCount() > 1) {
                layoutManager.scrollToPositionWithOffset(position, mStickyTabLayout.getHeight());
            } else {
                layoutManager.scrollToPositionWithOffset(0, mHomeListPositionY - mInitPositionY);
            }
        }
    }

    @Override
    public void onRefresh() {
        mViewModel.startRefresh(false);
        mViewModel.refreshData();
    }

    @Override
    public void onLoadMoreRequested() {
        mViewModel.loadMore();
    }
}
