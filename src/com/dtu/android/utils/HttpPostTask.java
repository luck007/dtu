package com.dtu.android.utils;

import java.util.ArrayList;

import org.apache.http.NameValuePair;

import android.content.Context;

public class HttpPostTask {
	private static final String TAG = "HttpPostTask";
	private HttpAsyncTask httpAsyncTask;
	private String mServerUrl;
	
    public static HttpPostTask newInstance(){
        return new HttpPostTask();
     }
	
	public HttpPostTask()
	{
		StringBuilder bufString = new StringBuilder();
		bufString.append(Const.BASE_POST_URL);
		mServerUrl = bufString.toString();
	}
	
	public void doRequest(Context context, ArrayList<NameValuePair> keyValueArray, final HttpCallListener listener) {
		// 如果存在，取消上一次请求
		if (httpAsyncTask != null) {
			httpAsyncTask.cancel(true);			
		}
		
		httpAsyncTask = new HttpAsyncTask(context);
		httpAsyncTask.doPost(new HttpTaskListener() {
			
	        @Override
	        public void onResponse(String response) {
//	               Log.d(TAG, "onResponse=" + response);            	
	        	listener.onReceived(response);           	
				httpAsyncTask = null;
	        }

	        @Override
	        public void onCancelled() {
	            listener.onReceived(null);
	            httpAsyncTask = null;
	        }
	    }, keyValueArray, mServerUrl);
	}
	
	public boolean doCancel() {
		return (httpAsyncTask != null) ? httpAsyncTask.cancel(true) : false;
	}
    
    public boolean isRunning()
    {
    	return (httpAsyncTask != null);
    }
}