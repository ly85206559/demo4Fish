package com.demo.fish.core.mvvm.binding;

import android.databinding.Observable;
import android.databinding.ObservableList;

/**
 * Created by TimoRD on 2016/3/10.
 */
public final class ObservableBindingUtil {

    private ObservableBindingUtil() {

    }

    public static IBinding bind(Observable observable, Observable.OnPropertyChangedCallback callback) {
        IBinding binding = new PropertyBinding(observable, callback);
        binding.bind();

        return binding;
    }

    public static IBinding bind(ObservableList observableList, ObservableList.OnListChangedCallback callback) {
        IBinding binding = new ListBinding(observableList, callback);
        binding.bind();

        return binding;
    }
}
