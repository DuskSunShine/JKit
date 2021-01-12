package com.scy.core.observer;

import android.text.TextUtils;
import com.scy.core.base.BaseViewModel;
import com.scy.core.common.JKit;
import com.scy.core.common.JKitToast;
import com.scy.core.common.Transformer;
import com.scy.core.exception.ParamException;
import org.jetbrains.annotations.NotNull;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.Objects;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.ResourceObserver;
import okhttp3.ResponseBody;
import retrofit2.HttpException;

/**
 * @author: SCY
 * @date: 2020/12/1   14:28
 * @version: 1.0
 * @desc: 文件下载观察者, 如果请求中有@Header("Range") String start 注解,
 * 必须设置{@link #supportBreakPoint()}为true
 * 因为该注解会让服务器只返回剩下的文件字节，如果不设置可能会造成文件损坏。
 */
public abstract class DownLoadObserver extends ResourceObserver<ResponseBody> {

    private final BaseViewModel baseViewModel;
    private final String targetDir;
    private final String fileName;

    public DownLoadObserver(BaseViewModel baseViewModel, String fileName) {
        this(baseViewModel, JKit.getTargetDir(), fileName);
    }

    public DownLoadObserver(BaseViewModel baseViewModel, String targetDir, String fileName) {
        Objects.requireNonNull(fileName, "fileName为空,不能下载.");
        this.baseViewModel = baseViewModel;
        this.targetDir = TextUtils.isEmpty(targetDir) ?
                JKit.getTargetDir() : targetDir;
        this.fileName = fileName;
    }

    @Override
    protected void onStart() {
        super.onStart();
        baseViewModel.addDisposable(setHttpTag() == null ? baseViewModel.getClass().getName() : setHttpTag(), this);
    }

    @Override
    public void onComplete() {

    }

    @Override
    public void onNext(@NonNull ResponseBody responseBody) {
        Observable.create(new ObservableOnSubscribe<Progress>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Progress> emitter) {
                if (supportBreakPoint()) {
                    breakPointSaveFile(responseBody, emitter);
                } else {
                    normalSaveFile(responseBody, emitter);
                }
            }
        }).compose(Transformer.schedulersFile())
                .subscribe(new Observer<Progress>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        start();
                    }

                    @Override
                    public void onNext(@NonNull Progress progress) {
                        progress(progress.getBytesRead(), progress.getContentLength(), progress.getProgress());
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        if (baseViewModel != null) {
                            baseViewModel.showLoadingUI(false);
                        }
                        e.printStackTrace();
                        if (JKit.isDebug()) {
                            JKitToast.error(e.getMessage(),4000);
                        }
                        failed(e);
                    }

                    @Override
                    public void onComplete() {
                        if (baseViewModel != null) {
                            baseViewModel.showLoadingUI(false);
                        }
                        complete(targetDir + File.separator + fileName);
                    }
                });
    }

    @Override
    public void onError(@NotNull Throwable e) {
        if (baseViewModel != null) {
            baseViewModel.showLoadingUI(false);
            if (e.getClass() == HttpException.class) {
                baseViewModel.showToastUI("网络连接异常,请检查网络后重试!");
            } else if (e.getClass() == ParamException.class) {
                ParamException parseException = (ParamException) e;
                baseViewModel.showToastUI(parseException.getErrorMessage());
            } else {
                baseViewModel.showToastUI("加载失败,请稍后重试!");
            }
        }
        e.printStackTrace();
        if (JKit.isDebug()) {
            JKitToast.error(e.getMessage(),4000);
        }
    }

    /**
     * 标记网络请求的tag
     * tag下的一组或一个请求，用来处理一个页面的所以请求或者某个请求
     * 设置一个tag就行就可以取消当前页面所有请求或者某个请求了
     *
     * @return string
     */
    protected String setHttpTag() {
        return null;
    }

    /**
     * 断点保存文件
     *
     * @param response ResponseBody
     */
    private void breakPointSaveFile(ResponseBody response, ObservableEmitter<Progress> emitter) {
        long contentLength;
        InputStream is = null;
        byte[] buf = new byte[2048];
        RandomAccessFile fos = null;
        try {
            is = response.byteStream();
            File dir = new File(targetDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File file = new File(dir, fileName);
            fos = new RandomAccessFile(file, "rw");
            long fileLength = file.length();//已写入到文件的长度
            if (fileLength==0){
                //表示没开始写文件
                contentLength=response.contentLength();
            }else {
                //文件总长=已写长度+剩下的长度
                contentLength=fileLength+response.contentLength();
            }
            fos.seek(fileLength);//可以将指针移动到某个位置开始读写
            Progress progress = new Progress();
            progress.setContentLength(contentLength);
            int len;
            int readLength = (int) fileLength;
            while ((len = is.read(buf)) != -1) {
                fos.write(buf, 0, len);
                readLength += len;
                progress.setBytesRead(readLength);
                progress.setProgress((readLength * 1.0f / contentLength) * 100);
                emitter.onNext(progress);
            }

            if (fileLength == contentLength) {
                //如果下载完成或者本地已有文件
                progress.setBytesRead(fileLength);
                progress.setProgress((fileLength * 1.0f / contentLength) * 100);
                emitter.onNext(progress);
            }
            emitter.onComplete();
        } catch (IOException e) {
            emitter.onError(e);
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
                if (fos != null)
                    fos.close();
                response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 普通保存文件
     *
     * @param response ResponseBody
     */
    private void normalSaveFile(ResponseBody response, ObservableEmitter<Progress> emitter) {
        long contentLength = response.contentLength();
        InputStream is = null;
        FileOutputStream fos = null;
        try {
            is = response.byteStream();
            File dir = new File(targetDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File file = new File(dir, fileName);
            fos = new FileOutputStream(file);
            Progress progress = new Progress();
            progress.setContentLength(contentLength);
            byte[] buf = new byte[2048];
            int len;
            int readLength = 0;
            while ((len = is.read(buf)) != -1) {
                fos.write(buf, 0, len);
                readLength += len;
                progress.setBytesRead(readLength);
                progress.setProgress((readLength * 1.0f / contentLength) * 100);
                emitter.onNext(progress);
            }
            emitter.onComplete();
        } catch (IOException e) {
            emitter.onError(e);
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
                if (fos != null)
                    fos.close();
                response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 是否支持断点续传
     *
     * @return true 可断点续传
     */
    protected abstract boolean supportBreakPoint();

    /**
     * 订阅开始，开始下载
     */
    protected abstract void start();

    /**
     * 下载进度
     *
     * @param bytesRead     已经下载文件的大小
     * @param contentLength 文件的大小
     * @param progress      当前进度
     */
    protected abstract void progress(long bytesRead, long contentLength, float progress);

    /**
     * 下载完成
     *
     * @param filePath 保存的路径
     */
    protected abstract void complete(String filePath);

    /**
     * 下载失败
     *
     * @param e Throwable
     */
    protected abstract void failed(Throwable e);


    /**
     * 写入进度
     */
    private static final class Progress {
        private long bytesRead;
        private long contentLength;
        private float progress;

        public long getBytesRead() {
            return bytesRead;
        }

        public void setBytesRead(long bytesRead) {
            this.bytesRead = bytesRead;
        }

        public long getContentLength() {
            return contentLength;
        }

        public void setContentLength(long contentLength) {
            this.contentLength = contentLength;
        }

        public float getProgress() {
            return progress;
        }

        public void setProgress(float progress) {
            this.progress = progress;
        }
    }

}
