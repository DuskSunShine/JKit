package com.scy.core.base;

import android.os.Bundle;
import android.view.View;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewbinding.ViewBinding;
import com.scy.core.common.ActivityManager;
import com.scy.core.common.ClassKit;
import com.scy.core.common.JKitToast;
import com.scy.core.widget.LoadingDialog;
import com.scy.core.interfaces.LoadingView;


/**
 * @author: SCY
 * @date: 2020/11/18   15:50
 * @version: 1.0
 * @desc: activity基类
 */
public abstract class BaseActivity<VB extends ViewBinding, VM extends BaseViewModel> extends AppCompatActivity implements View.OnClickListener {

    protected VM mViewModel;
    protected VB mViewBinding;
    protected LoadingView loadingView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewBinding = ClassKit.getViewBinding(this);
        if (mViewBinding == null) {
            //初始化view错误
            finish();
            return;
        }
        setContentView(mViewBinding.getRoot());
        loadingView=createLoadingView();
        ActivityManager.getInstance().addActivity(this);
        mViewModel = ClassKit.getViewModel(this);
        subscribeBaseEvent();
        preActive();
        subscribeObserver();
        dataObserve();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityManager.getInstance().remove(this);
    }

    /**
     * 订阅基本事件
     */
    private void subscribeBaseEvent() {
        mViewModel.loadingUI.observe(this, aBoolean -> {
            if (aBoolean){
                loadingView.showLoading();
            }else {
                loadingView.hideLoading();
            }
        });

        mViewModel.toastUI.observe(this, JKitToast::error);
    }

    @Deprecated
    protected void setOnClick(int... ids) {
        for (int id : ids)
            findViewById(id).setOnClickListener(this);

    }

    protected void setOnClick(View... views) {
        for (View view : views)
            view.setOnClickListener(this);

    }

    /**
     * 创建加载动画，可重写
     * @return {@link LoadingView}
     */
    protected LoadingView createLoadingView(){
        return new LoadingDialog(this);
    }

    /**
     * 预备交互
     */
    protected abstract void preActive();

    /**
     * 订阅各数据
     */
    protected abstract void subscribeObserver();

    /**
     * 获取数据源等
     */
    protected abstract void dataObserve();


}
