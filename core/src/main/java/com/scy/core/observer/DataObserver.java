package com.scy.core.observer;

import android.text.TextUtils;
import com.scy.core.base.BaseObserver;
import com.scy.core.base.BaseResponse;
import com.scy.core.base.BaseViewModel;
import com.scy.core.exception.ParamException;
import io.reactivex.annotations.NonNull;

/**
 * @author: SCY
 * @date: 2020/11/26   16:20
 * @version: 1.0
 * @desc: 实体数据观察者
 */
public abstract class DataObserver<T> extends BaseObserver<BaseResponse<T>> {
    

    public DataObserver(BaseViewModel baseViewModel) {
        super(baseViewModel);
    }

    public DataObserver(BaseViewModel baseViewModel, boolean showLoadingUI) {
        super(baseViewModel, showLoadingUI);
    }

    @Override
    public void onNext(@NonNull BaseResponse<T> response) {
        int code = response.getCode();
        String msg = response.getMsg();
        if (!TextUtils.isEmpty(msg) && !msg.equals("success")) {
            onError(new ParamException(code, msg));
        }
        super.onNext(response);
    }
}
