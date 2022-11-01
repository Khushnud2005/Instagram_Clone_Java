package uz.example.instajclon.utils;

import android.util.Log;

public class Logger {
    static boolean IS_TESTER = true;
    public static void d(String tag,String msg) {
        if (IS_TESTER) Log.d(tag, msg);
    }

    public static void i(String tag,String msg) {
        if (IS_TESTER) Log.i(tag, msg);
    }

    public static void v(String tag,String msg) {
        if (IS_TESTER) Log.v(tag, msg);
    }

    public static void e(String tag,String msg) {
        if (IS_TESTER) Log.e(tag, msg);
    }
}
