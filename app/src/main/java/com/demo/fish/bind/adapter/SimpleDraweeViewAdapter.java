package com.demo.fish.bind.adapter;

import android.databinding.BindingAdapter;

import com.facebook.drawee.view.SimpleDraweeView;

/**
 * Created by Ben on 2017/4/9.
 */

public class SimpleDraweeViewAdapter {

    @BindingAdapter(value = {"actualImageUri"})
    public static void setImageUrl(SimpleDraweeView draweeView, String url) {
        draweeView.setImageURI(url);
    }
}
