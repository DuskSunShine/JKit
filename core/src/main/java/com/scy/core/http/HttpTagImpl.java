package com.scy.core.http;

import com.scy.core.base.BaseViewModel;
import com.scy.core.interfaces.IHttpTag;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * @author: SCY
 * @date: 2020/12/2   9:53
 * @version: 1.0
 * @desc: tag下的一组或一个请求，用来处理一个页面的所以请求或者某个请求
 *         设置一个相同的tag就行就可以取消当前页面所有请求或者某个请求了
 *         单独封装而不在{@link BaseViewModel}中封装，是因为这样可以全局控制所有的请求。
 */
public class HttpTagImpl implements IHttpTag<String> {

    private final HashMap<String, CompositeDisposable> mMaps;

    private static volatile HttpTagImpl httpTag=null;


    private HttpTagImpl(){
        mMaps=new HashMap<>();
    }

    public static HttpTagImpl get(){
        if (httpTag == null) {
            synchronized (HttpTagImpl.class) {
                if (httpTag == null) {
                    httpTag = new HttpTagImpl();
                }
            }
        }
        return httpTag;
    }


    @Override
    public void add(String tag, Disposable disposable) {
        if (null == tag) {
            return;
        }
        //tag下的一组或一个请求，用来处理一个页面的所以请求或者某个请求
        //设置一个相同的tag就行就可以取消当前页面所有请求或者某个请求了
        CompositeDisposable compositeDisposable = mMaps.get(tag);
        if (compositeDisposable == null) {
            CompositeDisposable compositeDisposableNew = new CompositeDisposable();
            compositeDisposableNew.add(disposable);
            mMaps.put(tag, compositeDisposableNew);
        } else {
            compositeDisposable.add(disposable);
        }
    }

    @Override
    public void remove(String tag) {
        if (null == tag) {
            return;
        }
        if (!mMaps.isEmpty()) {
            mMaps.remove(tag);
        }
    }

    @Override
    public void cancel(String tag) {
        if (null == tag) {
            return;
        }
        if (mMaps.isEmpty()) {
            return;
        }
        if (null == mMaps.get(tag)) {
            return;
        }
        if (!mMaps.get(tag).isDisposed()) {
            mMaps.get(tag).dispose();
            mMaps.remove(tag);
        }
    }

    @Override
    public void cancel(String... tags) {
        if (null == tags) {
            return;
        }
        for (String tag : tags) {
            cancel(tag);
        }
    }

    @Override
    public void cancelAll() {
        if (mMaps.isEmpty()) {
            return;
        }
        Iterator<Map.Entry<String, CompositeDisposable>> it = mMaps.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, CompositeDisposable> entry = it.next();
            CompositeDisposable disposable = entry.getValue();
            //如果直接使用map的remove方法会报这个错误java.util.ConcurrentModificationException
            //所以要使用迭代器的方法remove
            if (null != disposable) {
                if (!disposable.isDisposed()) {
                    disposable.dispose();
                    it.remove();
                }
            }
        }
    }
}
