package com.dtu.android;

import com.dtu.android.utils.Const;

import android.content.Context;
import android.content.SharedPreferences;

public class DTUApi {
	static DTUApi instance;
	Context context;
	private SharedPreferences mPrefs;
	
    public static DTUApi getInstance()
    {
        return instance;
    }

    public static DTUApi getInstance(Context context) {
        if (instance == null) {
            instance = new DTUApi(context.getApplicationContext());
        }
        return instance;
    }

    protected DTUApi(Context context)
    {
    	this.context = context;
    	this.mPrefs = this.context.getSharedPreferences(Const.DTU_GLOBAL_PREFS, 0);
    }

    public SharedPreferences getSharedPreferences() {
        return this.mPrefs;
    }

    public SharedPreferences getGlobalSharedPreferences() {
        return this.context.getSharedPreferences(Const.DTU_GLOBAL_PREFS, 0);
    }
}
