package com.demo.fish.app.main.ui.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.demo.fish.R;
import com.demo.fish.databinding.ItemHomePhotoListBinding;

import java.util.List;

/**
 * Created by Ben on 2017/4/20.
 */

public class HomePhotoListAdapter extends RecyclerView.Adapter<HomePhotoListAdapter.ViewHolder> {

    private Context mContext;
    private List<String> mPhotoUrlList;

    public HomePhotoListAdapter(Context mContext, List<String> mPhotoUrlList) {
        this.mContext = mContext;
        this.mPhotoUrlList = mPhotoUrlList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemHomePhotoListBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(mContext),
                R.layout.item_home_photo_list,
                parent, false);
        ViewHolder holder = new ViewHolder(binding);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String url = mPhotoUrlList.get(position);
        holder.setImageUrl(url);
    }

    @Override
    public int getItemCount() {
        return mPhotoUrlList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ItemHomePhotoListBinding mBinding;

        public ViewHolder(ItemHomePhotoListBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        public void setImageUrl(String url) {
            mBinding.setImageUrl(url);
        }
    }
}
