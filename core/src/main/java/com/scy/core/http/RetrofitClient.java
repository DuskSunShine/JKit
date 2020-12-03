package com.scy.core.http;

import com.scy.core.common.ClassKit;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author: SCY
 * @date: 2020/11/21   14:51
 * @version: 1.0
 * @desc: retrofit客户端,自己实现比较好。
 */
@Deprecated
public class RetrofitClient {

    private  OkHttpClient okHttpClient;
    private  Class<?> hostClazz;
    private static volatile RetrofitClient retrofitClient;
    private  Retrofit retrofit;
    private static final Object LOCK=new Object();

    private RetrofitClient() {
    }

    private RetrofitClient(Builder builder) {
        this.okHttpClient = builder.okHttpClient;
        this.hostClazz = builder.hostClazz;
        this.retrofit=builder.retrofit;
    }

    /**
     *
     * @param hostClazz {@link BaseUrl}注解的类
     * @return 单例网络请求客户端
     */
    public static RetrofitClient get(Class<?> hostClazz){
        if (retrofitClient==null){
            synchronized (LOCK){
                if (retrofitClient==null){
                    retrofitClient=new RetrofitClient.Builder()
                            .setHostClazz(hostClazz)
                            .configClients()
                            .build();
                }
            }
        }
        return retrofitClient;
    }


    /**
     *
     * @param clazz 相应api service的class
     * @param <T>  api service 的泛型
     * @return  相应的接口api service
     */
    public  <T> T getApiService(Class<T> clazz) {
        return retrofit.create(clazz);
    }



    public static final class Builder{

        private OkHttpClient okHttpClient;
        private Retrofit retrofit;
        private Class<?> hostClazz;

        public OkHttpClient getOkHttpClient() {
            return okHttpClient;
        }

        public Builder setOkHttpClient(OkHttpClient okHttpClient) {
            this.okHttpClient = okHttpClient;
            return this;
        }

        public Class<?> getHostClazz() {
            return hostClazz;
        }

        public Builder setHostClazz(Class<?> hostClazz) {
            this.hostClazz = hostClazz;
            return this;
        }

        public Retrofit getRetrofit() {
            return retrofit;
        }

        public Builder setRetrofit(Retrofit retrofit) {
            this.retrofit = retrofit;
            return this;
        }

        public Builder configClients(){
            if (hostClazz==null){
                throw new NullPointerException("请设置BaseUrl.");
            }
            okHttpClient=defaultHttpClient();
            retrofit=defaultRetrofit();
            return this;
        }

        public RetrofitClient build() {
            return new RetrofitClient(this);
        }


        private OkHttpClient defaultHttpClient() {
            return new OkHttpClient.Builder()
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .retryOnConnectionFailure(true)
                    .build();
        }

        private Retrofit defaultRetrofit(){
            return new Retrofit.Builder()
                    .client(okHttpClient)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(ClassKit.getHost(hostClazz.getName())).build();
        }
    }

}
