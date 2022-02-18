package com.example.spokenwapp.utilities;

import android.util.Log;

public class SpokenLogHelper {
    public static void v(String tag, Object... messages) {
        log(tag, Log.VERBOSE, null, messages);
    }
    public static void d(String tag, Object... messages) {
        log(tag, Log.DEBUG, null, messages);
    }
    public static void i(String tag, Object... messages) {
        log(tag, Log.INFO, null, messages);
    }
    public static void w(String tag, Object... messages) {
        log(tag, Log.WARN, null, messages);
    }
    public static void w(String tag, Throwable t, Object... messages) {
        log(tag, Log.WARN, t, messages);
    }
    public static void e(String tag, Object... messages) {
        log(tag, Log.ERROR, null, messages);
    }
    public static void e(String tag, Throwable t, Object... messages) {
        log(tag, Log.ERROR, t, messages);
    }
    public static void log(String tag, int level, Throwable t, Object... messages) {
        if (messages != null && Log.isLoggable(tag, level)) {
            String message = null;
            if (messages.length == 1) {
                message = messages[0] == null ? null : messages[0].toString();
            } else {
                StringBuilder sb = new StringBuilder();
                for (Object m: messages) {
                    sb.append(m);
                }
                message = sb.toString();
            }
            Log.d(tag, message, t);
        }
    }
}
