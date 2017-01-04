package com.dtu.android.utils;

import com.dtu.android.MyApplication;
import com.dtu.android.DTUApi;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class MemoryUtils {
    private static final String OOM_OCCURRED = "memory_utils_oom_occurred";
    private static final String OOM_TIME = "memory_utils_oom_time";

    @TargetApi(19)
    public static boolean isLowMemoryDevice() {
        boolean isLowMemory = false;
        ActivityManager activityManager = (ActivityManager) MyApplication.getInstance().getSystemService("activity");
//        if (BuildHelper.isAtLeastKitKat()) {
//            isLowMemory = activityManager.isLowRamDevice();
//        }
        return activityManager.getMemoryClass() <= 48 || isLowMemory;
    }

    public static void handleCaughtOOM(String action) {
        markOOMOccurred(true);
    }

    public static void markOOMOccurred(boolean occurred) {
        Editor editor = DTUApi.getInstance().getGlobalSharedPreferences().edit();
        editor.putBoolean(OOM_OCCURRED, occurred);
        editor.putLong(OOM_TIME, System.currentTimeMillis());
        editor.apply();
    }

    public static boolean isLowMemoryDeviceOrOomOccurredInLastWeek() {
        return isLowMemoryDevice() || hasOomOccurredInLastWeek();
    }

    public static boolean hasOomOccurredInLastWeek() {
        SharedPreferences prefs = DTUApi.getInstance().getGlobalSharedPreferences();
        try {
            return prefs.getBoolean(OOM_OCCURRED, false) && System.currentTimeMillis() - prefs.getLong(OOM_TIME, 0) < 604800000;
        } catch (ClassCastException e) {
            prefs.edit().remove(OOM_OCCURRED).remove(OOM_TIME).commit();
            return false;
        }
    }
}
