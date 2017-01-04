package com.dtu.android.utils;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.os.Build.VERSION;
import android.os.Handler;
import android.util.DisplayMetrics;

public class AndroidUtils {
	private static final int DP_RATIO = 160;
	private static final String LOG_TAG = AndroidUtils.class.getSimpleName();
	
	public static int getPixelByDp(Activity activity, int dp) {
        if (activity != null) {
            DisplayMetrics metric = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(metric);
            return (int) ((((double) metric.densityDpi) / DP_RATIO) * ((double) dp));
        }
        throw new RuntimeException("Activity must not be null.");
    }
    
    public static String getPlatformNum() {
        return "Android " + getVersionRelease();
    }
    
    public static String getVersionRelease() {
        return VERSION.RELEASE;
    }

    public static File getSdCardPathFile() {
        if (isExternalStorageWritable()) {
            return Environment.getExternalStorageDirectory();
        }
        throw new RuntimeException("SD卡不可用");
    }

    public static boolean isExternalStorageWritable() {
        return "mounted".equals(Environment.getExternalStorageState());
    }
    
    public static void postSafety(Handler paramHandler, final Runnable paramRunnable)
    {
    	paramHandler.post(new Runnable()
		{
    		public void run()
		    {
		      try
		      {
		    	  paramRunnable.run();
		    	  return;
		      }
		      catch (Exception localException)
		      {
		    	  localException.printStackTrace();
		      }
		    }
		 });
    }

    public static boolean isNetworkValid(Context context) {
    	try {
            ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity == null) {
                return false;
            }
            NetworkInfo[] networkInfos = connectivity.getAllNetworkInfo();
            if (networkInfos == null) {
                return false;
            }
            for (NetworkInfo networkInfo : networkInfos) {
                if (networkInfo.isConnectedOrConnecting()) {
                    return true;
                }
            }
            return false;
        } catch (Throwable e) {
            return false;
        }
    }  
    
    public static void showMsg(Context paramContext, String paramString)
    {
    	if ((paramContext != null) && (paramString != null)) {
    		ToastFactory.getInstance().showToast(paramContext, paramString);
		}
    }

    public static void hiddenMsg() {
        ToastFactory.getInstance().cancelToast();
    }
    
    public static void closeDialog(Dialog dialog) {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }
    
    //망상태를 검사하고 메시지 현시
    public static void checkNetworkAndMessage(Context context)
    {
    	if (!isNetworkValid(context))
    	{
    		showMsg(context, "当前处于离线状态。");
    	}
    }

    // 校验Tag Alias 只能是数字,英文字母和中文
    public static boolean isValidTagAndAlias(String s) {
        Pattern p = Pattern.compile("^[\u4E00-\u9FA50-9a-zA-Z_-]{0,}$");
        Matcher m = p.matcher(s);
        return m.matches();
    }
}
