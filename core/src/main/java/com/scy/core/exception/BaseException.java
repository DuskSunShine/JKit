package com.scy.core.exception;

import com.scy.core.BuildConfig;

/**
 * @author: SCY
 * @date: 2020/11/21   14:10
 * @version: 1.0
 * @desc: 自定义运行异常基类
 */
public abstract class BaseException extends RuntimeException {

    private final int errorCode;

    private final String errorMessage;

    public BaseException(int errorCode, String errorMessage) {
        super(BuildConfig.LIBRARY_PACKAGE_NAME +":"+errorMessage);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
