package com.dtu.android.utils;

import org.json.JSONException;
import org.json.JSONObject;

import com.dtu.android.MyApplication;
import com.dtu.android.R;
import com.dtu.android.activity.ManagerActivity;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

public class MiscUtils
{
    public static Point getScreenSize() {
        DisplayMetrics metrics = MyApplication.getInstance().getResources().getDisplayMetrics();
        return new Point(metrics.widthPixels, metrics.heightPixels);
    }

    public static Rect getScreenLocation(View view) {
        Rect loc = new Rect();
        int[] coords = new int[2];
        view.getLocationOnScreen(coords);
        loc.set(coords[0], coords[1], coords[0] + view.getWidth(), coords[1] + view.getHeight());
        return loc;
    }

    public static int getDimenSizeFromAttribute(Activity activity, int attribute) {
        TypedValue typedValue = new TypedValue();
        activity.getTheme().resolveAttribute(attribute, typedValue, true);
        return TypedValue.complexToDimensionPixelSize(typedValue.data, activity.getResources().getDisplayMetrics());
    }

    public static boolean getBooleanFromAttribute(Activity activity, int attribute) {
        TypedValue typedValue = new TypedValue();
        activity.getTheme().resolveAttribute(attribute, typedValue, true);
        TypedArray a = activity.obtainStyledAttributes(typedValue.data, new int[]{attribute});
        boolean value = a.getBoolean(0, false);
        a.recycle();
        return value;
    }
    
    //JsonObject로 전환
    public static JSONObject convertObjectToJson(Object obj)
    {
    	JSONObject jsonObj = null;
    	try
		{
			if ( obj instanceof String )
			{
				String strToParse = (String)obj;
				if (!TextUtils.isEmpty(strToParse)) {
					jsonObj = new JSONObject(strToParse);
				}
			}
			else if ( obj instanceof JSONObject )
			{
				jsonObj = (JSONObject)obj;
			}
		} catch (JSONException e) {
            e.printStackTrace();
        }
    	return jsonObj;
    }
	
    //-----키보드 hide
	public static void hideKeyboard(Activity activity) {
		if (activity != null)
		{
		    // Check if no view has focus:
		    View view = activity.getCurrentFocus();
		    if (view != null) {
		        InputMethodManager inputManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
		        inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		    }
		}
	}
	
    //-----키보드 hide
	public static void hideKeyboard(Activity activity, Dialog dialog) {
		if (activity != null && dialog != null)
		{
		    // Check if no view has focus:
		    View view = dialog.getCurrentFocus();
		    if (view != null) {
		        InputMethodManager inputManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
		        inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		    }
		}
	}
	
	//home activity로 이동
	public static void moveToHomePage(Context context)
	{
    	Intent intent = ManagerActivity.intentSingleTop(context);
    	context.startActivity(intent);
	}
    
    //전화걸기진행
    public static void goToContactHost(Context context, String phoneNum)
    {
    	String strRealTel = "tel:" + phoneNum;
    	//ListingDetailsFragment.this.startActivity(new Intent("android.intent.action.CALL", Uri.parse(strRealTel)));
    	context.startActivity(new Intent("android.intent.action.DIAL", Uri.parse(strRealTel)));
    }
	
	public static boolean checkTextViewNotEmpty(TextView field) {
        return !TextUtils.isEmpty(field.getText().toString().trim());
    }
	
	public static boolean convertIntToBool(int value)
	{
		if (value > 0)
			return true;
		else
			return false;
	}
	
	public static int convertBoolToVisible(boolean value)
	{
		return value? View.VISIBLE : View.GONE;
	}
	
	//서버에서 온 응답의 정당성검사
    public static boolean isValidResponse(final Context context, String receivedInfo)
    {
    	//빈문자렬이면 서버에서 응답을 받지 못한 경우도 포함되므로 망검사
    	if (TextUtils.isEmpty(receivedInfo))
    	{
    		AndroidUtils.checkNetworkAndMessage(context);
    		return false;
    	}
		//
    	return true;
    }
}
