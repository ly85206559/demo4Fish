package com.demo.fish.bind.adapter;

import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.widget.TextView;

/**
 * Created by Ben on 2017/4/21.
 */

public class TextViewAdapter {
    @BindingAdapter(value = {"android:drawableLeft"})
    public static void setDrawableLeft(TextView textView, int resID) {
        Drawable leftDrawable = textView.getContext().getResources().getDrawable(resID);
        leftDrawable.setBounds(0, 0, leftDrawable.getMinimumWidth(), leftDrawable.getMinimumHeight());
        textView.setCompoundDrawables(
                leftDrawable,
                null,
                null,
                null);
    }
}
