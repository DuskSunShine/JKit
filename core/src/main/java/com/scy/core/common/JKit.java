package com.scy.core.common;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.jeremyliao.liveeventbus.LiveEventBus;

/**
 * @author: SCY
 * @date: 2020/11/19   10:33
 * @version: 1.0
 * @desc: 无侵入获取context
 */
public final class JKit extends ContentProvider {

    @SuppressLint("StaticFieldLeak")
    private static Context mContext;
    private static Application mApplication;
    private static boolean debug=false;
    private static final String JK_TAG="Jkit";
    private static String targetDir;
    private static String cacheDir;


    public static String getTargetDir() {
        return targetDir;
    }

    public static void setTargetDir(String targetDir) {
        JKit.targetDir = targetDir;
    }

    public static String getCacheDir() {
        return cacheDir;
    }

    public static void setCacheDir(String cacheDir) {
        JKit.cacheDir = cacheDir;
    }

    public static boolean isDebug() {
        return debug;
    }

    public static void setDebug(boolean debug) {
        JKit.debug = debug;
    }

    public static Context getmContext() {
        return mContext;
    }


    public static Application getmApplication() {
        return mApplication;
    }


    public static void log(String msg){
        if (debug && !TextUtils.isEmpty(msg)){
            Log.i(JK_TAG,msg);
        }
    }
    @Override
    public boolean onCreate() {
        mContext=getContext();
        mApplication= (Application) getContext().getApplicationContext();
        targetDir= mApplication.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
        cacheDir= mApplication.getExternalCacheDir().getAbsolutePath();
        LiveEventBus.config().autoClear(true).enableLogger(debug)
                .lifecycleObserverAlwaysActive(false)
                .setContext(mContext);
        return true;
    }


    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }


}
