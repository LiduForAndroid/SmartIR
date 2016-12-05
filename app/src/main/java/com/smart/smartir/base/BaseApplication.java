package com.smart.smartir.base;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

import java.util.HashMap;
import java.util.Map;

public class BaseApplication extends Application {

    private static Context mContext;
    private static Handler mHandler;
    private static long mMainThreadId;

    // 对象
    private Map<String, String> mCacheMap = new HashMap<String, String>();

    public Map<String, String> getCacheMap() {
        return mCacheMap;
    }

    public static Context getContext() {
        return mContext;
    }

    public static Handler getHandler() {
        return mHandler;
    }

    public static long getMainThreadId() {
        return mMainThreadId;
    }

    @Override
    public void onCreate() {

        super.onCreate();

        //上下文
        mContext = getApplicationContext();

        //主线程的Handler
        mHandler = new Handler();

        //得到主线程的Id
        mMainThreadId = android.os.Process.myTid();

        // Tid Thread
        // Pid Process
        // Uid User
    }

}
