package com.scy.core.interfaces;

import androidx.annotation.NonNull;

import io.reactivex.disposables.Disposable;

/**
 * @author: SCY
 * @date: 2020/11/18   17:01
 * @version:
 * @desc:
 */
public interface ViewEvent {

    /**
     * 是否展示loading动画
     * @param show true 展示动画，false关闭动画
     */
    void showLoadingUI(boolean show);

    /**
     * 展示toast
     * @param toast 文字信息
     */
    void showToastUI(@NonNull String toast);

    /**
     * 如果是列表页面，可能展示空布局
     * @param show 是否展示空布局
     */
    void showEmptyUI(boolean show);

    /**
     * 如果分页，请求失败重置页码
     * @param pageNum 页数
     */
    void setCurrentPage(int pageNum);


    int getCurrentPage();

    /**
     * 订阅关系建立
     * @param disposable {@link Disposable}
     * @param tag 该请求的tag
     */
    void addDisposable(String tag,Disposable disposable);

    /**
     * 取消订阅关系
     * @param tag 请求的tag
     */
     void disposeSubscribe(String... tag);
}
