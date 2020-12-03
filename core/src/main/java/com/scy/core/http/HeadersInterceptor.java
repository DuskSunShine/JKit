package com.scy.core.http;

import org.jetbrains.annotations.NotNull;
import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author: SCY
 * @date: 2020/11/24   18:51
 * @version: 1.0
 * @desc: 拦截器添加公共的请求头
 */
public abstract class HeadersInterceptor implements Interceptor {

    @NotNull
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Request.Builder builder = request.newBuilder();
        addHeaders(builder);
        return chain.proceed(builder.build());
    }

    /**
     * 添加公共请求头
     * @param builder Request.Builder
     */
    protected abstract void addHeaders(Request.Builder builder);
}
