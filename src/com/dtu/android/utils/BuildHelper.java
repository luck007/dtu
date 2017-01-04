package com.dtu.android.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Build.VERSION;
import android.util.Log;

public class BuildHelper {
    public static final String KEY_NEW_NAV = "new_nav";
    private static final String TAG = "BuildHelper";

    public static boolean isAtLeastJellyBeanMR1() {
        return VERSION.SDK_INT >= 17;
    }

    public static boolean isAtLeastJellyBeanMR2() {
        return VERSION.SDK_INT >= 18;
    }

    public static boolean isAtLeastKitKat() {
        return VERSION.SDK_INT >= 19;
    }

    public static boolean isLPreview() {
        return VERSION.SDK_INT == 20;
    }

    public static boolean isAtLeastLollipop() {
        return VERSION.SDK_INT >= 21;
    }

    public static boolean isBrokenFontSpansLG412() {
        return VERSION.SDK_INT == 16 && "LGE".equalsIgnoreCase(Build.MANUFACTURER);
    }

    public static boolean hasTelephony(Context context) {
        return context.getPackageManager().hasSystemFeature("android.hardware.telephony");
    }

    public static boolean hasCamera(Context context) {
        return context.getPackageManager().hasSystemFeature("android.hardware.camera");
    }

    public static boolean supportsSendTo(Context context) {
        return !context.getPackageManager().queryIntentActivities(new Intent("android.intent.action.SENDTO"), 0).isEmpty();
    }

    public static boolean hasVoiceRecognition(Context context) {
        return context.getPackageManager().queryIntentActivities(new Intent("android.speech.action.RECOGNIZE_SPEECH"), 0).size() != 0;
    }

    public static void debugLog(String tag, String message) {
        if (!isProduction()) {
            Log.d(tag, message);
        }
    }

    public static void debugErrorLog(String tag, String message, Exception exception) {
        if (!isProduction()) {
            Log.e(tag, message, exception);
        }
    }

    public static boolean canDeviceRunRenderscript() {
        return isAtLeastJellyBeanMR2();
    }

    public static boolean isProduction() {
        return false;
    }

    public static boolean optEnabled(String opt) {
    	return isDebugEnabled();
    }

    public static boolean isFuture() {
        return optEnabled("future");
    }

    public static boolean isAmazonDevice() {
        return "amazon".equalsIgnoreCase(Build.MANUFACTURER);
    }

    public static boolean isDebugEnabled() {
        return !isProduction() || isQa();
    }

    public static boolean isQa() {
        return false;
    }
}
