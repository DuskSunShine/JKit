package com.scy.core.common;

import io.reactivex.FlowableTransformer;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 *线程切换
 */

public class Transformer {

    /**
     * 统一线程处理
     * @param <T> 指定的泛型类型
     * @return FlowableTransformer
     */
    public static <T> FlowableTransformer<T, T> FlowableSchedulersMain() {
        return flowable -> flowable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static <T> FlowableTransformer<T, T> FlowableSchedulersIO() {
        return flowable -> flowable.subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io());
    }

    /**
     * 统一线程处理
     * @param <T> 指定的泛型类型
     * @return ObservableTransformer
     */
    public static <T> ObservableTransformer<T, T> schedulersMain() {
        return observable -> observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 把文件直接下载到本地
     * @param <T> 
     * @return
     */
    public static <T> ObservableTransformer<T, T> schedulersFile() {
        return observable -> observable.subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static <T> ObservableTransformer<T, T> schedulersIO() {
        return observable -> observable.subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io());
    }

}
