package com.demo.fish.bind.callback;

import android.databinding.ObservableList;
import android.support.v7.widget.RecyclerView;

/**
 * Created by Ben.li on 2016/9/29.
 */

public class RecyclerViewAdapterChangedCallback extends ObservableList.OnListChangedCallback {

    private RecyclerView.Adapter mAdapter;

    public RecyclerViewAdapterChangedCallback(RecyclerView.Adapter adapter) {
        mAdapter = adapter;
    }

    @Override
    public void onChanged(ObservableList sender) {
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemRangeChanged(ObservableList sender, int positionStart, int itemCount) {
        mAdapter.notifyItemRangeChanged(positionStart, itemCount);
    }

    @Override
    public void onItemRangeInserted(ObservableList sender, int positionStart, int itemCount) {
        mAdapter.notifyItemRangeInserted(positionStart, itemCount);
    }

    @Override
    public void onItemRangeMoved(ObservableList sender, int fromPosition, int toPosition, int itemCount) {
        mAdapter.notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onItemRangeRemoved(ObservableList sender, int positionStart, int itemCount) {
        mAdapter.notifyItemRangeRemoved(positionStart, itemCount);
    }
}
