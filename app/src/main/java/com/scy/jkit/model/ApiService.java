package com.scy.jkit.model;

import com.scy.core.base.BaseResponse;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * @author: SCY
 * @date: 2020/11/23   11:18
 * @version:
 * @desc:
 */
public interface ApiService {

    @FormUrlEncoded
    @POST("jgb-education/get-education-list")
    Observable<BaseResponse<M>> baidu(@Field("pro_id") String pro_id,
                                      @Field("pg") int pg,
                                      @Field("pagesize") int pagesize);

    @Multipart
    @POST("jgb/edit-reward-punishment-info")
    Observable<BaseResponse<Img>> uploadImage(@Part List<MultipartBody.Part> sigle);


    @Streaming
    @GET
    Observable<ResponseBody> downLoad(@Url String url
            , @Header("Range") String start
    );

    @GET
    Observable<String> normal(@Url String url);


}
