package com.scy.core.observer;

import com.scy.core.base.BaseObserver;
import com.scy.core.base.BaseViewModel;

/**
 * @author: SCY
 * @date: 2020/12/3   14:18
 * @version: 1.0
 * @desc: String类型数据观察者
 */
public abstract class StringObserver extends BaseObserver<String> {


    public StringObserver(BaseViewModel baseViewModel) {
        super(baseViewModel);
    }

    public StringObserver(BaseViewModel baseViewModel, boolean showLoadingUI) {
        super(baseViewModel, showLoadingUI);
    }
}
