package com.scy.core.http;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.scy.core.common.ClassKit;
import com.scy.core.interfaces.ApiCreator;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * @author: SCY
 * @date: 2020/11/26   10:14
 * @version: 1.0
 * @desc: 发送Http请求的类
 */
public class HttpHelper implements ApiCreator {

    private HttpHelper() {
    }

    /**
     * 缓存retrofit针对同一个相同的ApiService不会重复创建retrofit对象
     */
    private HashMap<String, Object> apiServiceCache;
    private Retrofit retrofit;
    private OkHttpClient okHttpClient;
    /**
     * {@link BaseUrl}注解的类名
     */
    private Class<?> hostClass;

    private HttpHelper(Builder builder) {
        this.retrofit = builder.retrofit;
        this.okHttpClient = builder.okHttpClient;
        this.hostClass = builder.hostClass;
        apiServiceCache=new HashMap<>();
    }


    @Override
    public <T> T createApi(Class<T> clazz) {
        Objects.requireNonNull(retrofit, "Retrofit未创建.");
        T apiCache= (T) apiServiceCache.get(clazz.getName());
        if (apiCache==null){
            T api = retrofit.create(clazz);
            apiServiceCache.put(clazz.getName(),api);
            return api;
        }
        return apiCache;
    }

    @Override
    public <T> T createApi(Class<T> clazz,@NonNull String baseUrl) {
        Objects.requireNonNull(retrofit, "Retrofit未创建.");
        T apiCache = (T) apiServiceCache.get(baseUrl);
        if (apiCache==null) {
            Retrofit retrofit = this.retrofit.newBuilder().baseUrl(baseUrl).build();
            T api = retrofit.create(clazz);
            apiServiceCache.put(baseUrl, api);
            return api;
        }
        return apiCache;
    }

    public Builder newBuilder() {
        return new Builder(this);
    }

    public static final class Builder {
        private Retrofit retrofit;
        private OkHttpClient okHttpClient;
        private Class<?> hostClass;

        public Retrofit getRetrofit() {
            return retrofit;
        }

        public Builder setRetrofit(Retrofit retrofit) {
            this.retrofit = retrofit;
            return this;
        }

        public OkHttpClient getOkHttpClient() {
            return okHttpClient;
        }

        public Builder setOkHttpClient(OkHttpClient okHttpClient) {
            this.okHttpClient = okHttpClient;
            return this;
        }

        public Class<?> getHost() {
            return hostClass;
        }

        public Builder setHost(Class<?> hostClass) {
            this.hostClass = hostClass;
            return this;
        }

        public Builder initConfig() {
            Objects.requireNonNull(getHost(),"Host未设置.");
            okHttpClient=new OkHttpClient.Builder()
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .retryOnConnectionFailure(true)
                    .build();
            retrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(ClassKit.getHost(getHost().getName())).build();
            return this;
        }

        public Builder() {
        }

        Builder(HttpHelper httpHelper) {
            this.retrofit = httpHelper.retrofit;
            this.okHttpClient = httpHelper.okHttpClient;
            this.hostClass = httpHelper.hostClass;
        }

        public HttpHelper build() {
            return new HttpHelper(this);
        }
    }
}
