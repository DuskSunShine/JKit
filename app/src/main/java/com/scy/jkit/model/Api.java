package com.scy.jkit.model;

import com.scy.core.http.ApiFactory;
import com.scy.core.http.GetParametersInterceptor;
import com.scy.core.http.HeadersInterceptor;
import com.scy.core.http.HttpHelper;
import com.scy.core.http.PostParametersInterceptor;
import com.scy.jkit.Host;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;

/**
 * @author: SCY
 * @date: 2020/11/24   18:05
 * @version:
 * @desc:
 */
public class Api extends ApiFactory {


    @Override
    protected HttpHelper getHttpHelper() {
                HttpHelper build = new HttpHelper.Builder()
                .setHost(Host.class)
                .initConfig().build();
        //重新构建http并保留原属性
        HttpHelper.Builder builder = build.newBuilder();
        OkHttpClient okHttpClient = builder.getOkHttpClient().newBuilder()
                .addInterceptor(new HeadersInterceptor() {
                    @Override
                    protected void addHeaders(Request.Builder builder) {
                        builder.addHeader("Authorization","A 557c26e8964cd6cf4ec9e55a292a1b9c");
                    }
                }).addInterceptor(new PostParametersInterceptor() {
                    @Override
                    protected void addFormBody(FormBody.Builder builder) {
                        builder.add("os", "A")
                                .add("sign", "60b8814fa0db2f676812a827d2dcce384578d79f")
                                .add("timestamp", "1606460999")
                                .add("client_type", "manage")
                                .add("ver", "4.6.0")
                                .add("channel", "yzgong");
                    }

                    @Override
                    protected void addMultipartBody(MultipartBody.Builder builder) {
                        builder.addFormDataPart("os", "A")
                                .addFormDataPart("sign", "60b8814fa0db2f676812a827d2dcce384578d79f")
                                .addFormDataPart("timestamp", "1606460999")
                                .addFormDataPart("client_type", "manage")
                                .addFormDataPart("ver", "4.6.0")
                                .addFormDataPart("channel", "yzgong");
                    }
                })
//                .addInterceptor(new GetParametersInterceptor() {
//                    @Override
//                    protected void addQueryParameters(HttpUrl.Builder builder) {
//                        builder.addQueryParameter("os", "A")
//                                .addQueryParameter("sign", "60b8814fa0db2f676812a827d2dcce384578d79f")
//                                .addQueryParameter("timestamp", "1606460999")
//                                .addQueryParameter("client_type", "manage")
//                                .addQueryParameter("ver", "4.6.0")
//                                .addQueryParameter("channel", "yzgong");
//                    }
//                })
                .build();
        //重新构建retrofit并保留原属性
        Retrofit retrofit = builder.getRetrofit().newBuilder()
                .client(okHttpClient)
                .build();
        //重新设置httpAgent属性
        return builder.setOkHttpClient(okHttpClient)
                .setRetrofit(retrofit).build();
    }

    @Override
    public <T> T createApi(Class<T> clazz) {
        return httpHelper.createApi(clazz);
    }


    public static ApiFactory get(){
        if (apiFactory==null){
            synchronized (Api.class){
                if (apiFactory==null){
                    apiFactory=new Api();

                }
            }
        }
        return apiFactory;
    }
}
