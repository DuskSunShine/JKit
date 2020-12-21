### JKit Java版本快速开发框架   [![](https://jitpack.io/v/DuskSunShine/JKit.svg)](https://jitpack.io/#DuskSunShine/JKit)

### 待完善功能 压缩失败监听，上传时的文件类型判断是图片进行压缩，文档补全，cookie持久化等设置

#### 1.使用Rxjava2 + Retrofit2 + ViewBinding + LiveData + ViewModel +AndroidX +Luban封装的轻量MVVM开发框架。

#### 2.主要功能

- 目前主要是封装网络请求，基本满足常规的请求（单图片/图片+参数上传，断点下载/普通下载）
- 利用JKit实现无浸入式获取全局context和application
- 利用`@BaseUrl`注解标识baseUrl
- 可通过HttpTagImpl设置网络请求tag,支持单个取消或全部取消网络请求
- 通过自定义Interceptor实现网络请求公共参数的设置
- 通过自定义Interceptor实现公共请求头添加
- ActivityManager管理Activity回退栈
- 利用LiveData实现LiveBus
- 利用RxJava2实现RxBus
- 利用ViewBinding实现View的绑定，抛弃findViewById和ButterKnife
- 利用LiveData和ViewModel实现简单的MVVM结构
- 利用Luban进行图片的压缩
- 可全局配置的log显示，可全局配置文件路径(默认压缩图片路径为getExternalCacheDir(),
  下载路径为getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
- 支持debug模式下Toast直观提示网络请求错误
- 支持Activity结束后自动取消当前页面所有的网络请求

#### 3.依赖方式

先在项目根目录的 build.gradle 的 repositories 添加:

```
     allprojects {
         repositories {
            ...
            maven { url "https://jitpack.io" }
        }
    }
```

然后在dependencies添加:

```
       dependencies {
       ...
       implementation 'com.github.DuskSunShine:JKit:1.0'
       }
```

#### 4.简单使用

1. 可通过`JKit`进行全局配置，目前只支持设置debug模式，缓存目录，下载目录

```
JKit.setDebug(true);//设置debug模式
JKit.setCacheDir("xxx");//设置图片压缩后缓存目录
JKit.setTargetDir("xxx");//设置下载目录
```

2. 通过`@BaseUrl`注解标识baseUrl

```java
public class Host {
    @BaseUrl
    private static final String HOST="http://www.baidu.com/";
}  
```

3. 通过 `HttpHelper` 设置 `OkHttpClient` 和 `Retrofit`。
   通过构建器设置baseUrl注解的类和初始化默认的okhttp和retrofit
   如果默认的配置不满足需求，可通过HttpHelper的构建器`Builder`中`newBuilder`方法重新构建`HttpHelper`

```
//构建HttpHelper的默认设置
HttpHelper build = new HttpHelper.Builder()
                .setHost(Host.class)
                .initConfig().build();
//重新构建HttpHelper
HttpHelper.Builder builder = build.newBuilder();
//获取HttpHelper的okHttpClient并重新构建
OkHttpClient okHttpClient = builder.getOkHttpClient().newBuilder().build();
//重新构建retrofit
Retrofit retrofit = builder.getRetrofit().newBuilder()
                    .client(okHttpClient)
                    .build();
//重新构建httpHelper
builder.setOkHttpClient(okHttpClient)
                .setRetrofit(retrofit).build();
```

4. 公共参数的添加

- `HeadersInterceptor`添加公共请求头
- `GetParametersInterceptor` 添加GET请求的公共参数
- `PostParametersInterceptor` 添加POST请求的公共参数 (如果是POST表单提交,
  用addFormBody(FormBody.Builder builder)添加公共参数。
  上传图文需要addMultipartBody(MultipartBody.Builder
  builder)添加公共参数。)

5. 接口定义网络请求ApiService

```java
public interface ApiService {
    //普通POST表单请求
    @FormUrlEncoded
    @POST("xxx-xxx/get-xxx-list")
    Observable<BaseResponse<M>> baidu(@Field("pro_id") String pro_id,
                                      @Field("pg") int pg,
                                      @Field("pagesize") int pagesize);
    //单图/图片+参数的POST请求
    @Multipart
    @POST("xxx/xxx-xxx-xxx-info")
    Observable<BaseResponse<Img>> uploadImage(@Part List<MultipartBody.Part> sigle);

    //文件的下载，大文件必须添加@Streaming注解，防止文件加载到内存引起OOM
    //添加Range请求头，实现断点续传
    @Streaming
    @GET
    Observable<ResponseBody> downLoad(@Url String url
            , @Header("Range") String start
    );
    
    //普通的get请求
    @GET
    Observable<String> normal(@Url String url);
}
```

6. 数据观察者

- 提供数据观察者基类 `BaseObserver`
  1. 可控制加载动画，当`onError(@NotNull Throwable
     e)`或`onComplete()`调用时，自动取消加载动画。加载动画可实现`LoadingView`进行自定义更改。
  2. 设置网络请求的tag,可根据tag取消请求。
  3. try catch包裹`onSuccess(T t)`保证接收后的操作不会引起崩溃。
  4. 其中可进行网络请求的tag设置，默认的tag为当前的ViewModel类名，方便统一管理。
  5. 如果需要可继承该类并自定义
- `DataObserver` 继承 `BaseObserver`
  实现特定返回code,msg的处理，如果需要可继承并自定义
- `StringObserver` 继承 `BaseObserver`
  接收String类数据，如果需要可继承并自定义
- `DownLoadObserver` 接收 `ResponseBody`
  数据，可实现下载进度的监听。进行文件的保存操作可通过`supportBreakPoint()`设置是否需要断点续传。***注意如果需要断点续传必须设置请求Range请求头，不需要的话不能设置请求头，否则可能造成文件损坏***

7. ViewModel的封装

- `BaseViewModel`
  提供以下基本的可观测数据，loadingUI控制网络请求加载动画的展示隐藏。
  toastUI用于网络请求的错误提示 emptyUI用于空布局的展示
  currentPage用于分页请求页码的设置
  currentHttpTag保存当前VM中所有请求的tag,当`onCleared()`调用时，统一取消当前页面的所有请求
- `SimpleViewModel`
  如果Activity/Fragment页面很简单，没有多余的业务逻辑，可用该类VM

```
    /**网络请求加载动画*/
    protected final MutableLiveData<Boolean> loadingUI=new MutableLiveData<>();
    /**普通的toast提示*/
    protected final MutableLiveData<String> toastUI=new MutableLiveData<>();
    /**如果是列表，页面的空布局*/
    public final MutableLiveData<Boolean> emptyUI=new MutableLiveData<>();
    /**请求分页*/
    protected int currentPage = 1;
    /**当前页面所包含的请求*/
    private final Set<String> currentHttpTag=new HashSet<>();
```

8. 基本请求操作

- 分页请求

```
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
```

- 图片上传
  可通过Param添加普通参数，图片先通过Luban进行压缩，通过`OnCompleteListener`
  进行监听。***目前1.0版本缺失压缩失败监听，上传时的文件类型判断***

```
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

//方式3原始方式
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
```

- 文件下载 需要注意如果参数添加 `@Header("Range") String start`
  支持断点续传，那么必须设置 `supportBreakPoint()`
  为`true`,反之设置为false,默认为false

```
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
```

- 普通String数据

```
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
```

