package com.demo.fish.app.main.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.demo.fish.R;
import com.demo.fish.app.main.ui.layer.MainViewLayer;
import com.demo.fish.app.main.model.IMainViewModel;
import com.demo.fish.app.main.model.impl.MainViewModel;

public class MainActivity extends AppCompatActivity {

    private IMainViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new MainViewModel(new MainViewLayer(this));
        mViewModel.bind();

        mViewModel.loadDrawerItemData();
        mViewModel.setIndex(MainViewLayer.TAB_INDEX_HOME);
    }

    @Override
    protected void onDestroy() {
        mViewModel.unbind();
        super.onDestroy();
    }

    public void onClick(View v) {
        if (v.getId() == R.id.home) {
            mViewModel.setIndex(MainViewLayer.TAB_INDEX_HOME);
        } else if (v.getId() == R.id.find) {
            mViewModel.setIndex(MainViewLayer.TAB_INDEX_FIND);
        } else if (v.getId() == R.id.message) {
            mViewModel.setIndex(MainViewLayer.TAB_INDEX_MESSAGE);
        } else if (v.getId() == R.id.mine) {
            mViewModel.setIndex(MainViewLayer.TAB_INDEX_MINE);
        }
    }
}
