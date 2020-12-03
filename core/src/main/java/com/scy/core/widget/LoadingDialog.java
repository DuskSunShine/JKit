package com.scy.core.widget;


import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.scy.core.R;
import com.scy.core.interfaces.LoadingView;

/**
 * @author: SCY
 * @date: 2020/11/27   11:04
 * @version: 1.0
 * @desc: 加载动画
 */
public class LoadingDialog extends AppCompatDialogFragment implements LoadingView {


    private final AppCompatActivity activity;


    public LoadingDialog(AppCompatActivity activity) {
        this.activity = activity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().setCancelable(false);
        getDialog().setCanceledOnTouchOutside(false);
        return inflater.inflate(R.layout.dialog_layout,container,false);
    }


    @Override
    public void showLoading() {
        try {
            if (!isAdded() || isHidden()) {
                show(activity.getSupportFragmentManager(), getClass().getName());
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void hideLoading() {
        dismiss();
    }
}
