package com.scy.core.common;

import android.annotation.SuppressLint;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class LiveBus {

    private static volatile LiveBus instance;

    private static final Object lock=new Object();

    private final Map<Object, MutableLiveData<Object>> mLiveBus;

    private LiveBus() {
        mLiveBus = new HashMap<>();
    }

    public static LiveBus get() {
        if (instance == null) {
            synchronized (lock) {
                if (instance == null) {
                    instance = new LiveBus();
                }
            }
        }
        return instance;
    }

    @SuppressLint("CheckResult")
    public <T,K> MutableLiveData<T> subscribe(K eventKey) {
        Objects.requireNonNull(eventKey, "所订阅的事件不能为null.");
        if (!mLiveBus.containsKey(eventKey)) {
            mLiveBus.put(eventKey, new LiveBusData<>(true));
        } else {
            LiveBusData<T> liveBusData = (LiveBusData<T>) mLiveBus.get(eventKey);
            if (liveBusData!=null){
                liveBusData.isFirstSubscribe = false;
            }
        }
        return (MutableLiveData<T>) mLiveBus.get(eventKey);
    }

    public <T,K> void postEvent(K eventKey, T value) {
        Objects.requireNonNull(eventKey, "所订阅的事件不能为null.");
        MutableLiveData<T> mutableLiveData = subscribe(eventKey);
        mutableLiveData.postValue(value);
    }

    public <T,K> void sendEvent(K eventKey, T value) {
        Objects.requireNonNull(eventKey, "所订阅的事件不能为null.");
        MutableLiveData<T> mutableLiveData = subscribe(eventKey);
        mutableLiveData.setValue(value);
    }

    public static class LiveBusData<T> extends MutableLiveData<T> {

        private boolean isFirstSubscribe;

        public LiveBusData(boolean isFirstSubscribe) {
            this.isFirstSubscribe = isFirstSubscribe;
        }

        @Override
        public void observe(@NonNull LifecycleOwner owner, @NonNull Observer observer) {
            super.observe(owner, new ObserverWrapper<T>(observer, isFirstSubscribe));
        }
    }

    private static class ObserverWrapper<T> implements Observer<T> {
        private final Observer<T> observer;

        private boolean isChanged;

        private ObserverWrapper(Observer<T> observer, boolean isFirstSubscribe) {
            this.observer = observer;
            isChanged = isFirstSubscribe;
        }

        @Override
        public void onChanged(@Nullable T t) {
            if (isChanged) {
                if (observer != null) {
                    observer.onChanged(t);
                }
            } else {
                isChanged = true;
            }
        }
    }
}
