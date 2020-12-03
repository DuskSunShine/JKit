### JKit Java版本快速开发框架   [![](https://jitpack.io/v/DuskSunShine/JKit.svg)](https://jitpack.io/#DuskSunShine/JKit)
##### 1.使用Rxjava2 + Retrofit2 + ViewBinding + LiveData + ViewModel +AndroidX +Luban封装的快速开发框架。
##### 2.主要功能
- 目前主要是封装网络请求，基本满足常规的请求（单图片/图片+参数上传，断点下载/普通下载）
- 利用JKit实现无浸入式获取全局context和application
- 利用`@BaseUrl`注解标识baseUrl
- 可通过HttpTagImpl设置网络请求tag,支持单个取消或全部取消网络请求
- 通过Interceptor实现网络请求公共参数的设置
- 通过Interceptor实现公共请求头添加
- ActivityManager管理Activity回退栈
- 利用LiveData实现LiveBus
- 利用RxJava2实现RxBus
- 利用ViewBinding实现View的绑定，抛弃findViewById和ButterKnife
- 利用LiveData和ViewModel实现简单的MVVM结构
- 利用Luban进行图片的压缩
- 可全局配置的log显示，可全局配置的文件路径
- 支持debug模式下Toast直观提示网络请求错误
```
public class Host {
    @BaseUrl
    private static final String HOST="http://www.baidu.com/";
}  
```