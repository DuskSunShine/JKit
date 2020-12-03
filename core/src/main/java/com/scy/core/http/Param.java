package com.scy.core.http;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import com.scy.core.common.JKit;
import com.scy.core.interfaces.OnCompleteListener;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import top.zibin.luban.CompressionPredicate;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;
import top.zibin.luban.OnRenameListener;

/**
 * @author: SCY
 * @date: 2020/11/30   14:15
 * @version: 1.0
 * @desc: 参数
 */
public class Param {

    private static MultipartBody.Builder addFormDataPart;

    private OnCompleteListener onCompleteListener;
    private Luban.Builder luban;

    private Param(){
    }

    private static MultipartBody.Builder getBuilder() {
        return new MultipartBody.Builder()
                .setType(MultipartBody.FORM);
    }

    public OnCompleteListener getOnCompleteListener() {
        return onCompleteListener;
    }

    /**
     * 如果异步压缩，必须添加监听
     * @param onCompleteListener {@link OnCompleteListener}
     */
    public void setOnCompleteListener(OnCompleteListener onCompleteListener) {
        this.onCompleteListener = onCompleteListener;
        if (luban!=null){
            //设置监听后，立即开始压缩
            luban.launch();
        }
    }

    /**
     * 以{@link Param#addParam(String, String)}或者
     * {@link Param#addFileParam(String, String)}添加参数
     *
     * @return Param
     */
    public static Param builder() {
        addFormDataPart = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);
        return new Param();
    }

    /**
     * @return List<MultipartBody.Part>
     */
    public List<MultipartBody.Part> build() {
        Objects.requireNonNull(addFormDataPart, "addFormDataPart为空.");
        return addFormDataPart.build().parts();
    }


    /**
     * 添加图文参数等
     *
     * @param params    普通参数
     * @param fileName  服务端接收的参数名
     * @param filePaths 文件路径
     * @return List<MultipartBody.Part>
     */
    public static void addParams(Map<String, String> params, String fileName, List<String> filePaths, OnCompleteListener onCompleteListener) {
        MultipartBody.Builder builder = getBuilder();
        if (null != params) {
            for (String key : params.keySet()) {
                String v = params.get(key);
                if (v != null) {
                    builder.addFormDataPart(key, v);
                }
            }
        }
        compressImage(fileName, filePaths, builder, onCompleteListener);
    }

    /**
     * 添加图片参数等
     *
     * @param filePaths 文件路径
     * @return List<MultipartBody.Part>
     */
    public static void addParams(List<String> filePaths, OnCompleteListener onCompleteListener) {
        MultipartBody.Builder builder = getBuilder();
        compressImage(null, filePaths, builder, onCompleteListener);
    }

    /**
     * 单个参数的添加
     *
     * @param key   参数名
     * @param value 参数值
     * @return Param
     */
    public Param addParam(String key, String value) {
        if (!TextUtils.isEmpty(value)) {
            addFormDataPart.addFormDataPart(key, value);
        }
        return this;
    }

    /**
     * 单个文件参数添加
     *
     * @param fileName 服务端接收的参数名
     * @param filePath 文件路径
     * @return Param
     */
    public Param addFileParam(String fileName, String filePath) {
        Objects.requireNonNull(fileName, "参数名fileName不能为空.");
        if (TextUtils.isEmpty(filePath)) {
            return this;
        }
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                return this;
            }
            Luban.with(JKit.getmApplication())
                    .setTargetDir(JKit.getCacheDir())
                    .ignoreBy(100).putGear(3).get();

            RequestBody imageBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            //"fileName"+i 后台接收图片流的参数名
            addFormDataPart.addFormDataPart(fileName, file.getName(), imageBody);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this;
    }

    /**
     * 多个文件添加
     *
     * @param fileName 服务端接收的参数名
     * @param filePath 文件路径
     * @return Param
     */
    public Param addFileParams(String fileName, List<String> filePath) {
        luban = compressImage2(fileName, filePath, addFormDataPart);
        return this;
    }

    /**
     * @param fileName 服务端接收的参数名
     * @param filePath 文件路径
     * @param builder  MultipartBody.Builder
     */
    @SuppressLint("CheckResult")
    private static void compressImage(String fileName, List<String> filePath, MultipartBody.Builder builder, OnCompleteListener compressListener) {
        Objects.requireNonNull(compressListener,"compressListener压缩结束监听为空，导致不能发起网络请求.");
        final int[] index = {1};
        final int[] size = {0};
        Luban.with(JKit.getmApplication())
                .setTargetDir(JKit.getCacheDir())
                .ignoreBy(100).putGear(3)
                .load(filePath)
                .filter(new CompressionPredicate() {
                    @Override
                    public boolean apply(String path) {
                        File file = new File(path);
                        return !TextUtils.isEmpty(path) && file.exists();
                    }
                })
                .setRenameListener(new OnRenameListener() {
                    @Override
                    public String rename(String filePath) {
                        String[] split = filePath.split("\\.");
                        return "images" + index[0] + "." + split[split.length - 1];
                    }
                }).setCompressListener(new OnCompressListener() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(File file) {
                RequestBody imageBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                //"fileName"+i 后台接收图片流的参数名
                builder.addFormDataPart(TextUtils.isEmpty(fileName) ? "file[" + index[0] + "]" : fileName, file.getName(), imageBody);
                index[0]++;
                size[0]++;
                JKit.log("压缩：" + file.toString());
                if (compressListener != null && size[0] == filePath.size()) {
                    compressListener.complete(builder.build().parts());
                }
            }

            @Override
            public void onError(Throwable e) {
                size[0]++;
                if (compressListener != null && size[0] == filePath.size()) {
                    compressListener.complete(builder.build().parts());
                }
            }
        }).launch();
    } /**


     * @param fileName 服务端接收的参数名
     * @param filePath 文件路径
     * @param builder  MultipartBody.Builder
     */
    @SuppressLint("CheckResult")
    private Luban.Builder compressImage2(String fileName, List<String> filePath, MultipartBody.Builder builder) {
        final int[] index = {1};
        final int[] size = {0};
       return Luban.with(JKit.getmApplication())
                .setTargetDir(JKit.getCacheDir())
                .ignoreBy(100).putGear(3)
                .load(filePath)
                .filter(new CompressionPredicate() {
                    @Override
                    public boolean apply(String path) {
                        File file = new File(path);
                        return !TextUtils.isEmpty(path) && file.exists();
                    }
                })
                .setRenameListener(new OnRenameListener() {
                    @Override
                    public String rename(String filePath) {
                        String[] split = filePath.split("\\.");
                        return "images" + index[0] + "." + split[split.length - 1];
                    }
                }).setCompressListener(new OnCompressListener() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(File file) {
                RequestBody imageBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                //"fileName"+i 后台接收图片流的参数名
                builder.addFormDataPart(TextUtils.isEmpty(fileName) ? "file[" + index[0] + "]" : fileName, file.getName(), imageBody);
                index[0]++;
                size[0]++;
                JKit.log("压缩：" + file.toString());
                if (onCompleteListener != null && size[0] == filePath.size()) {
                    onCompleteListener.complete(builder.build().parts());
                }
            }

            @Override
            public void onError(Throwable e) {
                size[0]++;
                if (onCompleteListener != null && size[0] == filePath.size()) {
                    onCompleteListener.complete(builder.build().parts());
                }
            }
        });
    }
}
