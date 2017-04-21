package com.demo.fish.bind.callback;

import android.databinding.ObservableList;

import com.chad.library.adapter.base.BaseQuickAdapter;

/**
 * Created by Ben.li on 2016/9/29.
 */

public class BaseQuickAdapterChangedCallback extends ObservableList.OnListChangedCallback {

    private BaseQuickAdapter mAdapter;

    public BaseQuickAdapterChangedCallback(BaseQuickAdapter adapter) {
        mAdapter = adapter;
    }

    @Override
    public void onChanged(ObservableList sender) {
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemRangeChanged(ObservableList sender, int positionStart, int itemCount) {
        mAdapter.notifyItemRangeChanged(positionStart + mAdapter.getHeaderLayoutCount(), itemCount);
    }

    @Override
    public void onItemRangeInserted(ObservableList sender, int positionStart, int itemCount) {
        mAdapter.notifyItemRangeInserted(positionStart + mAdapter.getHeaderLayoutCount(), itemCount);
    }

    @Override
    public void onItemRangeMoved(ObservableList sender, int fromPosition, int toPosition, int itemCount) {
        mAdapter.notifyItemMoved(fromPosition + mAdapter.getHeaderLayoutCount(), toPosition + mAdapter.getHeaderLayoutCount());
    }

    @Override
    public void onItemRangeRemoved(ObservableList sender, int positionStart, int itemCount) {
        mAdapter.notifyItemRangeRemoved(positionStart + mAdapter.getHeaderLayoutCount(), itemCount);
    }
}
