package com.scy.core.common;

import android.app.Application;
import androidx.annotation.NonNull;
import com.scy.core.base.BaseViewModel;

/**
 * @author: SCY
 * @date: 2020/11/18   15:42
 * @version: 1.0
 * @desc: 如果Activity很简单，不需要额外的ViewModel开销，使用SimpleViewModel.
 */
public class SimpleViewModel extends BaseViewModel {

    public SimpleViewModel(@NonNull Application application) {
        super(application);
    }
}
