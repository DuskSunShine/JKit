package com.scy.core.interfaces;

import io.reactivex.disposables.Disposable;

/**
 * @author: SCY
 * @date: 2020/12/2   10:17
 * @version: 1.0
 * @desc: http请求的管理接口
 */
public interface IHttpTag<T> {
    /**
     * 添加
     *
     * @param tag        tag
     * @param disposable disposable
     */
    void add(T tag, Disposable disposable);

    /**
     * 移除某个请求
     *
     * @param tag tag
     */
    void remove(T tag);

    /**
     * 取消某个tag的请求
     *
     * @param tag tag
     */
    void cancel(T tag);

    /**
     * 取消某些tag的请求
     * @param tags tags
     */
    void cancel(T... tags);

    /**
     * 取消所有请求
     */
    void cancelAll();
}
