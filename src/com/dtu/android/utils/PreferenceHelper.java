package com.dtu.android.utils;

import com.dtu.android.DTUApi;

import android.content.Context;
import android.content.SharedPreferences.Editor;

public class PreferenceHelper {	
	public static boolean getValue(String key) {
		return DTUApi.getInstance().getGlobalSharedPreferences().getBoolean(key, false);		
	}
	
	public static void setValue(String key, boolean value) {
		Editor editor = DTUApi.getInstance().getGlobalSharedPreferences().edit();
        editor.putBoolean(key, value);
        editor.apply();
	}
	
	
	public static int getIntValue(String key , int defaultValue) {
		return DTUApi.getInstance().getGlobalSharedPreferences().getInt(key, defaultValue);		
	}
	
	public static void setIntValue(String key, int value) {
		Editor editor = DTUApi.getInstance().getGlobalSharedPreferences().edit();
        editor.putInt(key, value);
        editor.apply();
	}
	
	public static String getStringValue(String key, String defaultValue) {		
		return DTUApi.getInstance().getGlobalSharedPreferences().getString(key, defaultValue);
	}
	
	public static void setStringValue(String key, String value) {
		Editor editor = DTUApi.getInstance().getGlobalSharedPreferences().edit();
        editor.putString(key, value);
        editor.apply();
	}
	public static long getLongValue(String key , long defaultValue) {
		return DTUApi.getInstance().getGlobalSharedPreferences().getLong(key, defaultValue);		
	}
	
	public static void setLongValue(String key, long value) {
		Editor editor = DTUApi.getInstance().getGlobalSharedPreferences().edit();
        editor.putLong(key, value);
        editor.apply();
	}
}
