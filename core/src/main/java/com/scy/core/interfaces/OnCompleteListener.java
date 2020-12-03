package com.scy.core.interfaces;

import java.util.List;

import okhttp3.MultipartBody;

/**
 * @author: SCY
 * @date: 2020/12/1   10:03
 * @version: 1.0
 * @desc: 图片压缩完成回调
 */
public interface OnCompleteListener {

    void complete(List<MultipartBody.Part> parts);
}
