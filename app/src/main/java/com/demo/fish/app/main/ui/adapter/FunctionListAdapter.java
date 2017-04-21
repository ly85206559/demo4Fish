package com.demo.fish.app.main.ui.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.demo.fish.R;
import com.demo.fish.app.main.entity.FunctionItemEntity;
import com.demo.fish.databinding.ItemBannerListBinding;
import com.demo.fish.databinding.ItemFunctionListBinding;

import java.util.List;

/**
 * Created by Ben on 2017/4/9.
 */

public class FunctionListAdapter extends RecyclerView.Adapter<FunctionListAdapter.ViewHolder> {

    private Context mContext;
    private List<FunctionItemEntity> mFunctionEntities;

    private View.OnClickListener mListener;

    public FunctionListAdapter(Context mContext, List<FunctionItemEntity> mFunctionEntities) {
        this.mContext = mContext;
        this.mFunctionEntities = mFunctionEntities;
    }

    public void setListener(View.OnClickListener listener) {
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemFunctionListBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(mContext),
                R.layout.item_function_list,
                parent,
                false);
        ViewHolder holder = new ViewHolder(binding);
        if (mListener != null) {
            binding.getRoot().setOnClickListener(mListener);
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        FunctionItemEntity entity = mFunctionEntities.get(position);
        holder.mBinding.setEntity(entity);
        holder.mBinding.getRoot().setTag(entity);
    }

    @Override
    public int getItemCount() {
        return mFunctionEntities.size();
    }

    @Override
    public void onViewRecycled(ViewHolder holder) {
        super.onViewRecycled(holder);
        holder.mBinding.getRoot().setOnClickListener(null);
        holder.mBinding.unbind();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ItemFunctionListBinding mBinding;

        public ViewHolder(ItemFunctionListBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }
    }
}
