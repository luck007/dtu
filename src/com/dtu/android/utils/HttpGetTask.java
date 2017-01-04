package com.dtu.android.utils;

import android.content.Context;

public class HttpGetTask {

    private static final String TAG = "HttpGetTask";

    private HttpAsyncTask httpAsyncTask;

    public static HttpGetTask newInstance(){
        return new HttpGetTask();
     }

    /**
     * @param context
     * @param accessToken "id=xxx&sign=xxxxxxx" or "http://192.168.0.xxx/yyy?id=xxx&sign=xxxxxxx"
     * @param listener
     */
    public void doRequest(Context context, String accessToken, final HttpCallListener listener) {

    	String url = null;
    	if (accessToken.contains("http://"))
    		url = accessToken;
    	else
    	{
    		StringBuilder bufString = new StringBuilder();
    		bufString.append(Const.BASE_GET_URL);    		
    		bufString.append(accessToken);
    		
    		url = bufString.toString();    		
    	}    		

        if (httpAsyncTask != null) {
        	httpAsyncTask.cancel(true);
        }

        httpAsyncTask = new HttpAsyncTask(context);
        httpAsyncTask.doGet(new HttpTaskListener() {

            @Override
            public void onResponse(String response) {      	
            	listener.onReceived(response);           	
				httpAsyncTask = null;
            }

            @Override
            public void onCancelled() {
                listener.onReceived(null);
                httpAsyncTask = null;
            }

        }, url);
    }

    public boolean doCancel() {
        return (httpAsyncTask != null) ? httpAsyncTask.cancel(true) : false;
    }
    
    public boolean isRunning()
    {
    	return (httpAsyncTask != null);
    }
    
}
