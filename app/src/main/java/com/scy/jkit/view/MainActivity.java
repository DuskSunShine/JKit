package com.scy.jkit.view;


import android.annotation.SuppressLint;
import android.view.View;

import com.scy.core.base.BaseActivity;
import com.scy.core.common.JKitToast;
import com.scy.jkit.databinding.ActivityMainBinding;
import com.scy.jkit.viewmodel.MainViewModel;

public class MainActivity extends BaseActivity<ActivityMainBinding, MainViewModel> {


    @Override
    public void onClick(View view) {

    }


    @SuppressLint("CheckResult")
    @Override
    protected void preActive() {
        mViewBinding.button.setOnClickListener(view -> mViewModel.upImage());
        mViewBinding.text.setOnClickListener(view -> {
            //mViewModel.setCurrentPage(mViewModel.getCurrentPage()+1);
            mViewModel.getData();
        });
        mViewBinding.button2.setOnClickListener(view -> mViewModel.downLoad());
        mViewBinding.button3.setOnClickListener(view -> mViewModel.disposeSubscribe("download"));
        mViewBinding.button4.setOnClickListener(view -> mViewModel.get());
    }

    @Override
    protected void subscribeObserver() {
        mViewModel.mData.observe(this, ms -> mViewBinding.text.setText(ms.toString()));

        mViewModel.image.observe(this, img -> mViewBinding.text.setText(img.toString()));

        mViewModel.emptyUI.observe(this, aBoolean -> {
            if (aBoolean) {
                JKitToast.error("-------------空布局---------------");
            }
        });
        mViewModel.progress.observe(this, aFloat -> mViewBinding.text.setText(aFloat.intValue() + "%"));
        mViewModel.get.observe(this, s -> mViewBinding.text.setText(s));
    }

    @SuppressLint("CheckResult")
    @Override
    protected void dataObserve() {
        mViewModel.getData();
    }


}