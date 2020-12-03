package com.scy.core.exception;

/**
 * @author: SCY
 * @date: 2020/11/26   16:35
 * @version: 1.0
 * @desc: 参数错误
 */
public class ParamException extends BaseException {


    public ParamException(int errorCode, String errorMessage) {
        super(errorCode, errorMessage);
    }
}
