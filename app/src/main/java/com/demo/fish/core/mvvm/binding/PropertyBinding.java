package com.demo.fish.core.mvvm.binding;

import android.databinding.Observable;

/**
 * Created by TimoRD on 2016/3/10.
 */
public class PropertyBinding implements IBinding {

    private Observable mObservable;
    private Observable.OnPropertyChangedCallback mCallback;

    public PropertyBinding(Observable observable, Observable.OnPropertyChangedCallback callback) {
        mObservable = observable;
        mCallback = callback;
    }

    @Override
    public void bind() {
        mObservable.addOnPropertyChangedCallback(mCallback);
    }

    @Override
    public void unbind() {
        mObservable.removeOnPropertyChangedCallback(mCallback);
        mCallback = null;
        mObservable = null;
    }
}
