package com.scy.core.base;

import android.text.TextUtils;

import com.scy.core.common.JKit;
import com.scy.core.common.JKitToast;
import com.scy.core.exception.ParamException;

import org.jetbrains.annotations.NotNull;

import io.reactivex.annotations.NonNull;
import io.reactivex.observers.ResourceObserver;
import retrofit2.HttpException;

/**
 * @author: SCY
 * @date: 2020/12/3   14:07
 * @version: 1.0
 * @desc: 公共数据观察者基类
 */
public abstract class BaseObserver<T> extends ResourceObserver<T> {


    protected final BaseViewModel baseViewModel;

    public BaseObserver(BaseViewModel baseViewModel) {
        this.baseViewModel = baseViewModel;
        baseViewModel.showLoadingUI(false);
    }


    public BaseObserver(BaseViewModel baseViewModel, boolean showLoadingUI) {
        this.baseViewModel = baseViewModel;
        baseViewModel.showLoadingUI(showLoadingUI);
    }

    @Override
    protected void onStart() {
        super.onStart();
        baseViewModel.addDisposable(setHttpTag() == null ? baseViewModel.getClass().getName() : setHttpTag(),this);
    }

    @Override
    public void onComplete() {
        if (baseViewModel != null) {
            baseViewModel.showLoadingUI(false);
        }
    }

    @Override
    public void onNext(@NonNull T t) {
        try {
            //保证接收后的操作不会引起崩溃
            onSuccess(t);
        } catch (Exception e) {
            e.printStackTrace();
            if (JKit.isDebug()) {
                JKitToast.error(e.getMessage());
            }
        }
    }

    @Override
    public void onError(@NotNull Throwable e) {
        if (baseViewModel != null) {
            baseViewModel.showLoadingUI(false);
            if (e.getClass() == HttpException.class) {
                baseViewModel.showToastUI("网络连接异常,请检查网络后重试!");
            } else if (e.getClass() == ParamException.class) {
                ParamException parseException = (ParamException) e;
                baseViewModel.showToastUI(parseException.getErrorMessage());
            } else {
                baseViewModel.showToastUI("加载失败,请稍后重试!");
            }
            int currentPage = baseViewModel.getCurrentPage();
            baseViewModel.setCurrentPage(currentPage > 1 ? currentPage - 1 : 1);
            baseViewModel.showEmptyUI(currentPage == 1);
        }
        e.printStackTrace();
        onFailure(e);
        if (JKit.isDebug()) {
            JKitToast.error(e.getMessage());
        }
    }

    /**
     * 标记网络请求的tag
     * tag下的一组或一个请求，用来处理一个页面的所以请求或者某个请求
     * 设置一个tag就行就可以取消当前页面所有请求或者某个请求了
     *
     * @return string
     */
    protected String setHttpTag() {
        return null;
    }

    /**
     * 成功回调
     * @param t 返回的数据
     */
    protected abstract void onSuccess(T t);

    /**
     * 为了不每次自己写onError,直接定义一个抽象方法
     *
     * @param throwable Throwable
     */
    protected abstract void onFailure(Throwable throwable);
}
