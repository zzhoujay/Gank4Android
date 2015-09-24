package zhou.gank.io.util;

import android.util.Log;

/**
 * Created by zzhoujay on 2015/8/22 0022.
 * LogKit 可以设置日志级别
 */
public class LogKit {

    /**
     * Priority constant for the println method; use Log.v.
     */
    public static final int VERBOSE = 2;

    /**
     * Priority constant for the println method; use Log.d.
     */
    public static final int DEBUG = 3;

    /**
     * Priority constant for the println method; use Log.i.
     */
    public static final int INFO = 4;

    /**
     * Priority constant for the println method; use Log.w.
     */
    public static final int WARN = 5;

    /**
     * Priority constant for the println method; use Log.e.
     */
    public static final int ERROR = 6;

    /**
     * Priority constant for the println method.
     */
    public static final int ASSERT = 7;

    public static int level = VERBOSE;

    public static void v(String tag, String msg) {
        if (level <= VERBOSE) {
            Log.v(tag, msg);
        }
    }

    public static void v(String tag, String msg, Throwable e) {
        if (level <= VERBOSE) {
            Log.v(tag, msg, e);
        }
    }

    public static void v(String tag, Object msg) {
        v(tag, msg.toString());
    }

    public static void d(String tag, String msg) {
        if (level <= DEBUG) {
            Log.d(tag, msg);
        }
    }

    public static void d(String tag, String msg, Throwable e) {
        if (level <= DEBUG) {
            Log.d(tag, msg, e);
        }
    }

    public static void d(String tag, Object msg) {
        d(tag, msg.toString());
    }

    public static void i(String tag, String msg) {
        if (level <= INFO) {
            Log.i(tag, msg);
        }
    }

    public static void i(String tag, String msg, Throwable e) {
        if (level <= INFO) {
            Log.i(tag, msg, e);
        }
    }

    public static void i(String tag, Object msg) {
        i(tag, msg.toString());
    }

    public static void w(String tag, String msg) {
        if (level <= WARN) {
            Log.w(tag, msg);
        }
    }

    public static void w(String tag, String msg, Throwable e) {
        if (level <= WARN) {
            Log.w(tag, msg, e);
        }
    }

    public static void w(String tag, Throwable e) {
        if (level <= WARN) {
            Log.w(tag, e);
        }
    }

    public static void w(String tag, Object msg) {
        w(tag, msg.toString());
    }

    public static void e(String tag, String msg) {
        if (level <= ERROR) {
            Log.e(tag, msg);
        }
    }

    public static void e(String tag, String msg, Throwable e) {
        if (level <= ERROR) {
            Log.e(tag, msg, e);
        }
    }

    public static void e(String tag, Object msg) {
        e(tag, msg.toString());
    }
}
