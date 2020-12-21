package com.scy.jkit.viewmodel;

import android.annotation.SuppressLint;
import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.scy.core.observer.DataObserver;
import com.scy.core.base.BaseResponse;
import com.scy.core.base.BaseViewModel;
import com.scy.core.observer.DownLoadObserver;
import com.scy.core.common.JKit;
import com.scy.core.common.Transformer;
import com.scy.core.http.Param;
import com.scy.core.interfaces.OnCompleteListener;
import com.scy.core.observer.StringObserver;
import com.scy.jkit.model.Api;
import com.scy.jkit.model.ApiService;
import com.scy.jkit.model.Img;
import com.scy.jkit.model.M;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;

/**
 * @author: SCY
 * @date: 2020/11/23   11:16
 * @version:
 * @desc:
 */
public class MainViewModel extends BaseViewModel {

    public MainViewModel(@NonNull Application application) {
        super(application);
    }

    public final MutableLiveData<M> mData = new MutableLiveData<>();
    public final MutableLiveData<Img> image = new MutableLiveData<>();
    public final MutableLiveData<Float> progress = new MutableLiveData<>();
    public final MutableLiveData<String> get = new MutableLiveData<>();

    @SuppressLint("CheckResult")
    public void getData() {
        Api.get().createApi(ApiService.class)
                .baidu("4800736", getCurrentPage(), 20)
                .compose(Transformer.schedulersMain())
                .subscribe(new DataObserver<M>(this,true) {
                    @Override
                    protected void onSuccess(BaseResponse<M> mBaseResponse) {
                        if (mBaseResponse.getResult().getList() == null) {
                            showEmptyUI(true);
                            return;
                        }
                        mData.setValue(mBaseResponse.getResult());
                    }

                    @Override
                    public void onFailure(Throwable throwable) {

                    }
                });
    }

    public void upImage() {
        //方式1
//        HashMap<String,String> map=new HashMap<>();
//        map.put("pro_id", "4800736");
//        map.put("group_id", "1325");
//        map.put("class_type", "laborGroup");
//        map.put("operate_date", "20201130");
//        map.put("price", "1288");
//        map.put("operate_type", "2");
//        List<String> files=new ArrayList<>();
//        files.add("/storage/emulated/0/wxr/5b12de26-8180-49fd-a149-eb5e91f2ab51.jpeg");
//        files.add("/storage/emulated/0/temp.jpg");
//        Param.addParams(map, null, files, new Param.OnCompleteListener() {
//            @Override
//            public void complete(List<MultipartBody.Part> parts) {
//                addDisposable(Api.get().createApi(ApiService.class)
//                                .uploadImage(
////                        4800736,1325,
////                "laborGroup","20201130",
////                "1288",2,
//                                        parts)
//                                .compose(Transformer.schedulersMain())
//                                .subscribeWith(new BaseObserver<Img>(MainViewModel.this) {
//                                    @Override
//                                    protected void onSuccess(BaseResponse<Img> rBaseResponse) {
//                                        image.setValue(rBaseResponse.getResult());
//                                    }
//
//                                    @Override
//                                    protected void onFailure(Throwable throwable) {
//
//                                    }
//                                })
//                        ,true);
//            }
//        });


        //方式2
        List<String> files = new ArrayList<>();
        files.add("/storage/emulated/0/wxr/5b12de26-8180-49fd-a149-eb5e91f2ab51.jpeg");
        files.add("/storage/emulated/0/temp.jpg");

        Param.builder()
                .addParam("pro_id", "4800736")
                .addParam("group_id", "1325")
                .addParam("class_type", "laborGroup")
                .addParam("operate_date", "20201130")
                .addParam("price", "1288")
                .addParam("contents", "contents")
                .addParam("operate_type", "2")
                .addFileParams(null, files)
                .setOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void complete(List<MultipartBody.Part> parts) {
                        Api.get().createApi(ApiService.class)
                                        .uploadImage(parts)
                                        .compose(Transformer.schedulersMain())
                                        .subscribe(new DataObserver<Img>(MainViewModel.this) {
                                            @Override
                                            protected void onSuccess(BaseResponse<Img> rBaseResponse) {
                                                image.setValue(rBaseResponse.getResult());
                                            }

                                            @Override
                                            protected void onFailure(Throwable throwable) {

                                            }
                                        });
                    }
                });


//            File file = new File("/storage/emulated/0/wxr/5b12de26-8180-49fd-a149-eb5e91f2ab51.jpeg");
//            RequestBody imageBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
//            //"fileName"+i 后台接收图片流的参数名
//        builder.addFormDataPart("pro_id", "4800736");
//        builder.addFormDataPart("group_id", "1325");
//        builder.addFormDataPart("class_type", "laborGroup");
//        builder.addFormDataPart("operate_date", "20201130");
//        builder.addFormDataPart("price", "1288");
//        builder.addFormDataPart("operate_type", "2");
//        builder.addFormDataPart("file[1]", file.getName(), imageBody);
//        List<MultipartBody.Part> parts = builder.build().parts();
//        addDisposable(Api.get().createApi(ApiService.class)
//                .uploadImage(
////                        4800736,1325,
////                "laborGroup","20201130",
////                "1288",2,
//                        parts)
//                .compose(Transformer.schedulersMain())
//                .subscribeWith(new BaseObserver<Img>(this) {
//                    @Override
//                    protected void onSuccess(BaseResponse<Img> rBaseResponse) {
//                        image.setValue(rBaseResponse.getResult());
//                    }
//
//                    @Override
//                    protected void onFailure(Throwable throwable) {
//
//                    }
//                })
//                ,true);
    }



    public void downLoad(){
        File file = new File(JKit.getTargetDir(), "5.pdf");
        long length = file.length();
        Api.get().createApi(ApiService.class)
                .downLoad("http://api.test.jgjapp.com/download/standard_information/20200913/15_1618525453.pdf"
                        , "bytes="+length+"-"
                )
                .compose(Transformer.schedulersMain())
                .subscribe(new DownLoadObserver(this,"5.pdf") {
                    @Override
                    protected void start() {
                        JKit.log("start");
                    }

                    @Override
                    protected void progress(long bytesRead, long contentLength, float progress) {
                        MainViewModel.this.progress.setValue(progress);
                        JKit.log("总长："+contentLength+",写入："+bytesRead+",进度："+progress);
                    }

                    @Override
                    protected String setHttpTag() {
                        JKit.log("setHttpTag");
                        return "download";
                    }

                    @Override
                    protected boolean supportBreakPoint() {
                        return true;
                    }

                    @Override
                    protected void complete(String filePath) {
                        JKit.log("complete  "+filePath);
                    }

                    @Override
                    protected void failed(Throwable e) {
                        JKit.log("failed"+e.getMessage());
                    }
                });
    }


    public void get(){
        Api.get().createApi(ApiService.class)
                .normal("http://www.baidu.com")
                .compose(Transformer.schedulersMain())
                .subscribe(new StringObserver(this, true) {
                    @Override
                    protected void onSuccess(String s) {
                        JKit.log(s);
                        get.setValue(s);
                    }

                    @Override
                    protected void onFailure(Throwable throwable) {

                    }
                });
    }
}
