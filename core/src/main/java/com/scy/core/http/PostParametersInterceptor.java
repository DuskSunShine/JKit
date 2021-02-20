package com.scy.core.http;

import android.text.TextUtils;
import org.jetbrains.annotations.NotNull;
import java.io.IOException;
import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * @author: SCY
 * @date: 2020/11/25   18:58
 * @version: 1.0
 * @desc: 如果是POST表单提交, 用addFormBody(FormBody.Builder builder)添加公共参数.
 * 上传图文需要{@link okhttp3.MultipartBody}添加公共参数.
 */
public abstract class PostParametersInterceptor implements Interceptor {
    @NotNull
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request oldRequest = chain.request();
        String method = oldRequest.method();
        Request newRequest = null;
        if (!TextUtils.isEmpty(method) && method.equals("POST")) {
            RequestBody body = oldRequest.body();

            if (body instanceof FormBody) {
                FormBody.Builder newFormBody = new FormBody.Builder();
                //添加公共参数
                addFormBody(newFormBody);
                FormBody formBody = (FormBody) body;
                //保留接口定义中的注解参数
                for (int i = 0; i < formBody.size(); i++) {
                    newFormBody.add(formBody.name(i), formBody.value(i));
                }
                newRequest = oldRequest.newBuilder()
                        .post(newFormBody.build())
                        .build();
            } else if (body instanceof MultipartBody) {
                //上传图片
                MultipartBody.Builder newMultipartBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM);
                //添加公共参数
                addMultipartBody(newMultipartBody);
                MultipartBody multipartBody = (MultipartBody) body;
                //保留接口定义中的注解参数
                for (int i = 0; i < multipartBody.size(); i++) {
                    MultipartBody.Part part = multipartBody.part(i);
                    newMultipartBody.addPart(part);
                }

                newRequest = oldRequest.newBuilder()
                        .post(newMultipartBody.build())
                        .build();
            }else {
                //没有@FormUrlEncoded注解，不需要额外参数，只需要公共参数
                FormBody.Builder newFormBody = new FormBody.Builder();
                //添加公共参数
                addFormBody(newFormBody);
                newRequest = oldRequest.newBuilder()
                        .post(newFormBody.build())
                        .build();
            }
        }
        return chain.proceed(newRequest == null ? oldRequest : newRequest);
    }

    /**
     * 添加公共表单参数
     * @param builder FormBody.Builder
     */
    protected abstract void addFormBody(FormBody.Builder builder);

    /**
     * 添加上传图片等公共参数
     * @param builder MultipartBody.Builder
     */
    protected abstract void addMultipartBody(MultipartBody.Builder builder);
}
