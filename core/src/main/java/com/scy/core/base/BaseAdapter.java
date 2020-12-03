package com.scy.core.base;

import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewbinding.ViewBinding;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.scy.core.common.ClassKit;
import org.jetbrains.annotations.NotNull;

/**
 * @author: SCY
 * @date: 2020/11/21   14:21
 * @version: 1.0
 * @desc: 自带ViewBinding的适配器基类
 */
public abstract class BaseAdapter<T,VB extends ViewBinding> extends BaseQuickAdapter<T, BaseViewHolder> {

    protected VB mViewBinding;

    public BaseAdapter(int layoutResId) {
        super(layoutResId);
    }

    @NotNull
    @Override
    protected BaseViewHolder createBaseViewHolder(@NotNull View view) {
        mViewBinding = ClassKit.getViewBinding((AppCompatActivity) getContext());
        return super.createBaseViewHolder(mViewBinding != null ? mViewBinding.getRoot() : view);
    }

}
