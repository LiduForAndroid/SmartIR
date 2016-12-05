package com.smart.smartir.utils;

import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;

import com.smart.smartir.base.BaseApplication;

public class UIUtils {
    /**
     * 得到上下文
     */
    public static Context getContext() {
        return BaseApplication.getContext();
    }

    /**
     * 得到Resource对象
     */
    public static Resources getResources() {
        return getContext().getResources();
    }

    /**
     * 得到string.xml中的字符
     */
    public static String getString(int resId) {
        return getResources().getString(resId);
    }

    /**
     * 得到string.xml中的字符,带占位符
     */
    public static String getString(int resId, Object... formatArgs) {
        return getResources().getString(resId, formatArgs);
    }

    /**
     * 得到string.xml中的字符数组
     */
    public static String[] getStringArr(int resId) {
        return getResources().getStringArray(resId);
    }

    /**
     * 得到color.xml中的颜色值
     */
    public static int getColor(int resId) {
        return getResources().getColor(resId);
    }

    /**
     * 得到应用程序的包名
     */
    public static String getPackageName() {
        return getContext().getPackageName();
    }

    /**
     * 得到主线程的Id
     */
    public static long getMainThreadId() {
        return BaseApplication.getMainThreadId();
    }

    /**
     * 得到主线程的hanlder
     */
    public static Handler getMainThreadHandler() {
        return BaseApplication.getHandler();
    }

    /**
     * 安全的执行一个task
     *
     * @return
     */
    public static void postTaskSafely(Runnable task) {
        // 当前线程==子线程,通过消息机制,把任务交给主线程的Handler去执行
        // 当前线程==主线程,直接执行任务
        int curThreadId = android.os.Process.myTid();
        long mainThreadId = getMainThreadId();
        if (curThreadId == mainThreadId) {
            task.run();
        } else {
            getMainThreadHandler().post(task);
        }
    }

    /**
     * dip-->px
     *
     * @param dip
     * @return
     */
    public static int dip2Px(int dip) {
        // 得到px和dip的比例关系
        float density = getResources().getDisplayMetrics().density;
        // 得到的是ppi(屏幕尺寸,屏幕的分辨率)
        float densityDpi = getResources().getDisplayMetrics().densityDpi;
        // 1. px/dip = density;
        // 2. px/(densityDpi/160) = dp;

        /**
         320x480  ppi=160    1px = 1dp;
         480x800  ppi = 240  1.5px = 1dp
         1280x720 ppi = 320  2px = 1dp;
         */
        // float转int的时候需要加上.5f
        int px = (int) (dip * density + .5f);
        return px;
    }

    /**
     * px-->dp
     *
     * @param px
     * @return
     */
    public static int px2Dip(int px) {
        // 得到px和dip的比例关系
        float density = getResources().getDisplayMetrics().density;
        // 1. px/dip = density;
        int dip = (int) (px / density + .5f);
        return dip;
    }
}
