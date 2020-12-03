package com.scy.core.base;

/**
 * @author: SCY
 * @date: 2020/11/23   14:04
 * @version: 1.0
 * @desc: 通用的能api返回实体类
 */
public final class BaseResponse<T> {
    private int code;
    private String msg;
    private T result;
    private String time;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "BaseResponse{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", result=" + result +
                ", time='" + time + '\'' +
                '}';
    }
}
