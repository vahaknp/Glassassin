package com.gdkdemo.livecard.common;

import android.os.SystemClock;


final class Log 
{
    private static final String TAG = "LiveCardDemo.common";

    static final boolean V = Config.LOGLEVEL <= android.util.Log.VERBOSE;
    static final boolean D = Config.LOGLEVEL <= android.util.Log.DEBUG;
    static final boolean I = Config.LOGLEVEL <= android.util.Log.INFO;
    static final boolean W = Config.LOGLEVEL <= android.util.Log.WARN;
    static final boolean E = Config.LOGLEVEL <= android.util.Log.ERROR;

    static void v(String logMe)
    {
        android.util.Log.v(TAG, SystemClock.uptimeMillis() + " " + logMe);
    }
    static void v(String logMe, Throwable ex)
    {
        android.util.Log.v(TAG, SystemClock.uptimeMillis() + " " + logMe, ex);
    }

    static void d(String logMe) 
    {
        android.util.Log.d(TAG, SystemClock.uptimeMillis() + " " + logMe);
    }
    static void d(String logMe, Throwable ex)
    {
        android.util.Log.d(TAG, SystemClock.uptimeMillis() + " " + logMe, ex);
    }

    static void i(String logMe)
    {
        android.util.Log.i(TAG, logMe);
    }
    static void i(String logMe, Throwable ex)
    {
        android.util.Log.i(TAG, logMe, ex);
    }

    static void w(String logMe)
    {
        android.util.Log.w(TAG, logMe);
    }
    static void w(String logMe, Throwable ex)
    {
        android.util.Log.w(TAG, logMe, ex);
    }

    static void e(String logMe)
    {
        android.util.Log.e(TAG, logMe);
    }
    static void e(String logMe, Exception ex)
    {
        android.util.Log.e(TAG, logMe, ex);
    }
}
