package com.scy.core.base;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.scy.core.http.HttpTagImpl;
import com.scy.core.interfaces.ViewEvent;
import java.util.HashSet;
import java.util.Set;
import io.reactivex.disposables.Disposable;

/**
 * @author: SCY
 * @date: 2020/11/18   15:37
 * @desc: ViewModel基类
 * @version: 1.0
 */
public abstract class BaseViewModel extends AndroidViewModel implements ViewEvent {

    /**网络请求加载动画*/
    protected final MutableLiveData<Boolean> loadingUI=new MutableLiveData<>();
    /**普通的toast提示*/
    protected final MutableLiveData<String> toastUI=new MutableLiveData<>();
    /**如果是列表，页面的空布局*/
    public final MutableLiveData<Boolean> emptyUI=new MutableLiveData<>();
    /**请求分页*/
    protected int currentPage = 1;
    /**当前页面所包含的请求*/
    private final Set<String> currentHttpTag=new HashSet<>();

    public BaseViewModel(@NonNull Application application) {
        super(application);
        subscribeEvent();
    }

    @Override
    public void showLoadingUI(boolean show) {
        loadingUI.setValue(show);
    }

    @Override
    public void showToastUI(@NonNull String toast) {
        toastUI.setValue(toast);
    }

    @Override
    public void showEmptyUI(boolean show) {
        emptyUI.setValue(show);
    }

    @Override
    public void setCurrentPage(int pageNum) {
        currentPage=pageNum ;
    }

    @Override
    public int getCurrentPage() {
        return currentPage;
    }

    @Override
    public void addDisposable(String tag, Disposable disposable) {
        HttpTagImpl.get().add(tag, disposable);
        currentHttpTag.add(tag);
    }

    @Override
    public void disposeSubscribe(String... tag) {
        HttpTagImpl.get().cancel(tag);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        //当前页面生命周期结束后，取消当前页面所有请求
        disposeSubscribe(currentHttpTag.toArray(new String[currentHttpTag.size()]));
        currentHttpTag.clear();
    }

    public Set<String> getCurrentHttpTag() {
        return currentHttpTag;
    }

    /**
     * 注册交互事件，如列表刷新等
     */
    protected abstract void subscribeEvent();
}
