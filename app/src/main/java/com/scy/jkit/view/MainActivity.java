package com.scy.jkit.view;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.View;

import androidx.lifecycle.Observer;

import com.scy.core.base.BaseActivity;
import com.scy.core.common.JKit;
import com.scy.core.common.JKitToast;
import com.scy.core.common.LiveBus;
import com.scy.jkit.databinding.ActivityMainBinding;
import com.scy.jkit.viewmodel.MainViewModel;

public class MainActivity extends BaseActivity<ActivityMainBinding, MainViewModel> {

    private int value;
    @Override
    public void onClick(View view) {
        JKit.log("onClick");
        value++;
        LiveBus.get().sendEvent("BUS",value);

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
        mViewBinding.main2.setOnClickListener(view -> startActivity(new Intent(this,MainActivity2.class)));
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
        LiveBus.get().subscribe("BUS")
                .observe(this, o -> {
                    int v= (int) o;
                    JKit.log("接收11111:"+v);
                });
        LiveBus.get().subscribe("BUS")
                .observe(this, o -> {
                    int v= (int) o;
                    JKit.log("接收22222:"+v);
                });
    }

    @SuppressLint("CheckResult")
    @Override
    protected void dataObserve() {
        mViewModel.getData();
    }


}