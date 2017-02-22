package cn.youmi.framework.debug;


import android.util.Log;

import cn.youmi.framework.BuildConfig;

/**
 * Wrapper API for sending log output.
 */
public class LogUtils {
    protected static final String TAG = "youmilog";

    public static boolean isDebug() {
		return BuildConfig.DEBUG;
    }
    
    private LogUtils() {
    }

    /**
     * Send a VERBOSE log message.
     *
     * @param msg The message you would like logged.
     */
    public static void v(String msg) {
        if (BuildConfig.DEBUG)
            Log.v(TAG, buildMessage(msg));
    }
    public static void v(String tag, String msg) {
        if (BuildConfig.DEBUG)
             Log.v(tag, buildMessage(msg));
    }

    /**
     * Send a VERBOSE log message and log the exception.
     *
     * @param msg The message you would like logged.
     * @param thr An exception to log
     */
    public static void v(String msg, Throwable thr) {
        if (BuildConfig.DEBUG)
             Log.v(TAG, buildMessage(msg), thr);
    }

    /**
     * Send a DEBUG log message.
     *
     * @param msg
     */
    public static void d(String msg) {
        if (BuildConfig.DEBUG)
             Log.d(TAG, buildMessage(msg));
    }
    public static void d(String tag, String msg) {
        if (BuildConfig.DEBUG)
             Log.d(tag, buildMessage(msg));
    }

    /**
     * Send a DEBUG log message and log the exception.
     *
     * @param msg The message you would like logged.
     * @param thr An exception to log
     */
    public static void d(String msg, Throwable thr) {
        if (BuildConfig.DEBUG)
             Log.d(TAG, buildMessage(msg), thr);
    }

    /**
     * Send an INFO log message.
     *
     * @param msg The message you would like logged.
     */
    public static void i(String msg) {
        if (BuildConfig.DEBUG)
             Log.i(TAG, buildMessage(msg));
    }
    public static void i(String tag, String msg) {
        if (BuildConfig.DEBUG)
             Log.i(tag, buildMessage(msg));
    }

    /**
     * Send a INFO log message and log the exception.
     *
     * @param msg The message you would like logged.
     * @param thr An exception to log
     */
    public static void i(String msg, Throwable thr) {
        if (BuildConfig.DEBUG)
             Log.i(TAG, buildMessage(msg), thr);
    }

    /**
     * Send an ERROR log message.
     *
     * @param msg The message you would like logged.
     */
    public static void e(String msg) {
        if (BuildConfig.DEBUG)
             Log.e(TAG, buildMessage(msg));
    }
    public static void e(String tag, String msg) {
        if (BuildConfig.DEBUG)
             Log.e(tag, buildMessage(msg));
    }

    /**
     * Send a WARN log message
     *
     * @param msg The message you would like logged.
     */
    public static void w(String msg) {
        if (BuildConfig.DEBUG)
             Log.w(TAG, buildMessage(msg));
    }
    public static void w(String tag, String msg) {
        if (BuildConfig.DEBUG)
             Log.w(tag, buildMessage(msg));
    }

    /**
     * Send a WARN log message and log the exception.
     *
     * @param msg The message you would like logged.
     * @param thr An exception to log
     */
    public static void w(String msg, Throwable thr) {
        if (BuildConfig.DEBUG)
             Log.w(TAG, buildMessage(msg), thr);
    }

    /**
     * Send an empty WARN log message and log the exception.
     *
     * @param thr An exception to log
     */
    public static void w(Throwable thr) {
        if (BuildConfig.DEBUG)
             Log.w(TAG, buildMessage(""), thr);
    }

    /**
     * Send an ERROR log message and log the exception.
     *
     * @param msg The message you would like logged.
     * @param thr An exception to log
     */
    public static void e(String msg, Throwable thr) {
        if (BuildConfig.DEBUG)
             Log.e(TAG, buildMessage(msg), thr);
    }

    /**
     * Building Message
     *
     * @param msg The message you would like logged.
     * @return Message String
     */
    protected static String buildMessage(String msg) {
        StackTraceElement caller = new Throwable().fillInStackTrace().getStackTrace()[2];

        return new StringBuilder()
                .append(caller.getClassName())
                .append(".")
                .append(caller.getMethodName())
                .append("(): ")
                .append(msg).toString();
    }
}
