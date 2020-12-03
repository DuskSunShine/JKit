package com.scy.core.http;


import com.scy.core.interfaces.ApiCreator;

/**
 * @author: SCY
 * @date: 2020/11/26   14:52
 * @version: 1.0
 * @desc: 创建API需要继承ApiFactory,方便
 *        {@link com.scy.core.base.BaseViewModel}直接获取API
 */
public abstract class ApiFactory implements ApiCreator {


    protected static ApiFactory apiFactory;

    protected static HttpHelper httpHelper;


    protected ApiFactory(){
        httpHelper=getHttpHelper();
    }

    /**
     * 创建Http帮助类
     * @return HttpHelper
     */
    protected abstract HttpHelper getHttpHelper();


}
