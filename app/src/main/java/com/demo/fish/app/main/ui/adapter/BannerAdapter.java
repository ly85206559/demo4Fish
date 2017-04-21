package com.demo.fish.app.main.ui.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.demo.fish.R;
import com.demo.fish.app.main.entity.BannerEntity;
import com.demo.fish.databinding.ItemBannerListBinding;

import java.util.List;

/**
 * Created by Ben on 2017/4/9.
 */

public class BannerAdapter extends RecyclerView.Adapter<BannerAdapter.ViewHolder> {

    private Context mContext;
    private List<BannerEntity> mBannerEntities;

    private View.OnClickListener mListener;

    public BannerAdapter(Context mContext, List<BannerEntity> mBannerEntities) {
        this.mContext = mContext;
        this.mBannerEntities = mBannerEntities;
    }

    public void setListener(View.OnClickListener listener) {
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemBannerListBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(mContext),
                R.layout.item_banner_list,
                parent,
                false);
        ViewHolder holder = new ViewHolder(binding);
        if (mListener != null) {
            binding.ivBanner.setOnClickListener(mListener);
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (mBannerEntities.size() <= 0) {
            return;
        }
        BannerEntity entity = mBannerEntities.get(position % mBannerEntities.size());
        holder.mBinding.setEntity(entity);
        holder.mBinding.ivBanner.setTag(entity);
    }

    @Override
    public int getItemCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public void onViewRecycled(ViewHolder holder) {
        super.onViewRecycled(holder);
        holder.mBinding.ivBanner.setOnClickListener(null);
        holder.mBinding.unbind();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ItemBannerListBinding mBinding;

        public ViewHolder(ItemBannerListBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }
    }
}
