package com.demo.fish;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.backends.okhttp3.OkHttpImagePipelineConfigFactory;
import com.facebook.imagepipeline.core.ImagePipelineConfig;

import okhttp3.OkHttpClient;

/**
 * Created by Ben on 2017/3/31.
 */

public class DemoApplication extends Application {

    private static DemoApplication application;

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;

        init();
    }

    private void init() {
        OkHttpClient mOkHttpClient = new OkHttpClient();
        ImagePipelineConfig imagePipelineConfig = OkHttpImagePipelineConfigFactory
                .newBuilder(this, mOkHttpClient)
                .setDownsampleEnabled(true)
                .build();
        Fresco.initialize(this, imagePipelineConfig);
    }

    public static DemoApplication getApplication() {
        return application;
    }
}
