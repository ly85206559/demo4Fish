package com.demo.fish.app.main.ui;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.demo.fish.R;
import com.demo.fish.app.main.ui.layer.HomeViewLayer;
import com.demo.fish.app.main.model.IHomeViewModel;
import com.demo.fish.app.main.model.impl.HomeViewModel;
import com.demo.fish.databinding.FragmentHomeBinding;

/**
 * Created by Ben on 2017/4/7.
 */

public class HomeFragment extends Fragment {

    private IHomeViewModel mViewModel;

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentHomeBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        mViewModel = new HomeViewModel(new HomeViewLayer(binding, this));
        mViewModel.bind();
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel.startRefresh(true);
    }

    @Override
    public void onDestroyView() {
        mViewModel.unbind();
        super.onDestroyView();
    }
}
