package com.dtu.android.manager;

import java.util.LinkedHashSet;
import java.util.Set;

import com.dtu.android.R;
import com.dtu.android.utils.AndroidUtils;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

public class PushNoticesManager
{	
	private static final String TAG = "DTUPush";
	private static final int MSG_SET_ALIAS = 1001;
	private static final int MSG_SET_TAGS = 1002;
	
	private static PushNoticesManager mInstance = null;
	private Context mContext;
	
	private final TagAliasCallback mAliasCallback = new TagAliasCallback() {

        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs ;
            switch (code) {
            case 0:
                logs = "Set tag and alias success";
                Log.i(TAG, logs);
                break;
                
            case 6002:
                logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
                Log.i(TAG, logs);
                if (AndroidUtils.isNetworkValid(mContext)) {
                	mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_ALIAS, alias), 1000 * 60);
                } else {
                	Log.i(TAG, "No network");
                }
                break;
            
            default:
                logs = "Failed with errorCode = " + code;
                Log.e(TAG, logs);
            }
            
            //AndroidUtils.showMsg(mContext, logs);
        }
	    
	};
	
	private final TagAliasCallback mTagsCallback = new TagAliasCallback() {

        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs ;
            switch (code) {
            case 0:
                logs = "Set tag and alias success";
                Log.i(TAG, logs);
                break;
                
            case 6002:
                logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
                Log.i(TAG, logs);
                if (AndroidUtils.isNetworkValid(mContext))  {
                	mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_TAGS, tags), 1000 * 60);
                } else {
                	Log.i(TAG, "No network");
                }
                break;
            
            default:
                logs = "Failed with errorCode = " + code;
                Log.e(TAG, logs);
            }
            
            AndroidUtils.showMsg(mContext, logs);
        }
        
    };
    
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
            case MSG_SET_ALIAS:
                Log.d(TAG, "Set alias in handler.");
                JPushInterface.setAliasAndTags(mContext, (String) msg.obj, null, mAliasCallback);
                break;
                
            case MSG_SET_TAGS:
                Log.d(TAG, "Set tags in handler.");
                JPushInterface.setAliasAndTags(mContext, null, (Set<String>) msg.obj, mTagsCallback);
                break;
                
            default:
                Log.i(TAG, "Unhandled msg - " + msg.what);
            }
        }
    };
    
	//---------
	public static PushNoticesManager getInstance()
	{
		if ( mInstance == null )
			mInstance = new PushNoticesManager();
		return mInstance;
	}
	
	//---------
	private PushNoticesManager()
	{	
	}
	
	public void setContext(Context context){
		mContext = context;
	}	
	
	public void setTag(String tag){
		// ","隔开的多个 转换成 Set
		String[] sArray = tag.split(",");
		Set<String> tagSet = new LinkedHashSet<String>();
		for (String sTagItme : sArray) {
			if (!AndroidUtils.isValidTagAndAlias(sTagItme)) {
				AndroidUtils.showMsg(mContext, mContext.getString(R.string.error_tag_gs_empty));				
				return;
			}
			tagSet.add(sTagItme);
		}
		
		//调用JPush API设置Tag
		mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_TAGS, tagSet));
	}
	
	public void setAlias(String alias){
		if (!AndroidUtils.isValidTagAndAlias(alias)) {
			AndroidUtils.showMsg(mContext,mContext.getString(R.string.error_tag_gs_empty));
			return;
		}
		
		mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS, alias));
	}
}
