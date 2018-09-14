package fantansee.cn.ling.yu.sdk.utils;

import android.util.Log;

/**
 * 日志
 *
 * @author Yu L.
 */
public class CardLogUtils {

    private static boolean debug = false;

    public static void printELog(String tag, String msg) {
        if (debug) {
            Log.e(tag, msg);
        }
    }

    public static void printDLog(String tag, String msg) {
        if (debug) {
            Log.d(tag, msg);
        }
    }

    public static void printWLog(String tag, String msg) {
        if (debug) {
            Log.w(tag, msg);
        }
    }

    public static void printILog(String tag, String msg) {
        if (debug) {
            Log.i(tag, msg);
        }
    }

    public static void printWtfLog(String tag, String msg) {
        if (debug) {
            Log.wtf(tag, msg);
        }
    }

    public static void setDebug(boolean debug) {
        CardLogUtils.debug = debug;
    }

}
