package com.scy.core.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.viewbinding.ViewBinding;
import com.scy.core.common.ClassKit;

/**
 * @author: SCY
 * @date: 2020/11/19   9:47
 * @version: 1.0
 * @desc: fragment基类
 */
public abstract class BaseFragment<VB extends ViewBinding, VM extends BaseViewModel> extends Fragment implements View.OnClickListener {

    protected VM mViewModel;
    protected VB mViewBinding;
    protected LifecycleOwner mLifecycleOwner;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mViewBinding = ClassKit.getViewBinding(this);
        if (mViewBinding == null && getActivity() != null) {
            //初始化view错误
            getActivity().finish();
            return null;
        }
        return mViewBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ClassKit.getViewModel(this);
        mLifecycleOwner = getViewLifecycleOwner();
        preActive();
        subscribeObserver();
        dataObserve();

    }


    @Deprecated
    protected void setOnClick(int... ids) {
        for (int id : ids)
            mViewBinding.getRoot().findViewById(id).setOnClickListener(this);

    }

    protected void setOnClick(View... views) {
        for (View view : views)
            view.setOnClickListener(this);

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
