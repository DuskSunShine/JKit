package com.scy.core.http;

import org.jetbrains.annotations.NotNull;
import java.io.IOException;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author: SCY
 * @date: 2020/11/25   16:03
 * @version: 1.0
 * @desc: 添加公共参数的拦截器
 */
public abstract class GetParametersInterceptor implements Interceptor {

    @NotNull
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request oldRequest = chain.request();
        HttpUrl.Builder builder = oldRequest.url().newBuilder();
        addQueryParameters(builder);
        Request newRequest = oldRequest.newBuilder()
                .url(builder.build())
                .build();
        return chain.proceed(newRequest);
    }

    /***
     * 添加get请求的公共参数
     * @param builder HttpUrl.Builder
     */
    protected abstract void addQueryParameters(HttpUrl.Builder builder);

}
