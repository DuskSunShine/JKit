package com.scy.core.interfaces;

/**
 * @author: SCY
 * @date: 2020/11/27   11:02
 * @version: 1.0
 * @desc: 可自定义的加载动画 ，需要实现该接口方法
 */
public interface LoadingView {

    void showLoading();

    void hideLoading();
}
