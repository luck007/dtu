package com.dtu.android.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import javax.net.ssl.SSLHandshakeException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.content.Context;
import android.os.AsyncTask;

public class HttpAsyncTask extends AsyncTask<String, Void, String> {
	private static final String TAG = "HttpAsyncTask";
	
	private static final int MAX_RETRY_TIME = 3;
    private static final int CONN_TIMEOUT = 15000;
    private static final int SO_TIMEOUT = 20000;
    
    private int mRetryCount;
    private HttpTaskListener mListener;
    private ArrayList<NameValuePair> mKeyValueArray;
    private boolean mIsHttpPost;
    private Context mContext;
    
    public HttpAsyncTask(Context context) {
        mContext = context;
    }
    
    public void doPost(HttpTaskListener listener, ArrayList<NameValuePair> keyValueArray, String url) {
        this.mListener = listener;
        this.mIsHttpPost = true;
        this.mKeyValueArray = keyValueArray;
        this.mRetryCount = 0;

        execute(url);
    }
    
    public void doGet(HttpTaskListener listener, String url) {
        this.mListener = listener;
        this.mIsHttpPost = false;
        this.mRetryCount = 0;

        execute(url);
    }
    
	@Override
    protected String doInBackground(String... params) {
		String response = null;
		 while (response == null && mRetryCount < MAX_RETRY_TIME) {
			 if (isCancelled())
				 return null;
			 
			 try {
				 String uri = params[0];
				 
				 HttpResponse httpResp = executeHttp(mContext, uri);
				 if (httpResp != null && !isCancelled()) {
					 int st = httpResp.getStatusLine().getStatusCode();
					 
					 HttpEntity entity = httpResp.getEntity();
					 if (entity != null) {
                        InputStream content = entity.getContent();
                        if (content != null) {
                            response = convertStreamToString(content);
                        }
                    }
				 }
			 
			 } catch (SSLHandshakeException e) {
	             e.printStackTrace();
	         } catch (UnsupportedEncodingException e) {
	             e.printStackTrace();
	         } catch (ClientProtocolException e) {
	             e.printStackTrace();
	         } catch (IOException e) {
	             e.printStackTrace();
	         }
			 
			 mRetryCount++;			 
		 }
		return response;
	}
	
	@Override
    protected void onCancelled() {
        super.onCancelled();

        if (mListener != null) {
            mListener.onCancelled();
            mListener = null;
        }
    }

    @Override
    protected void onPostExecute(String response) {
        super.onPostExecute(response);

        if (mListener != null && !isCancelled()) {
            mListener.onResponse(response);
            mListener = null;
        }
    }
    
    private HttpResponse executeHttp(Context context, String uri) throws SSLHandshakeException,
    ClientProtocolException, IOException {
    	return mIsHttpPost ? post(context, uri, mKeyValueArray) : get(context, uri);
    }
    
    private static String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
    
    private static HttpResponse post(Context context, String url, ArrayList<NameValuePair> params)
            throws SSLHandshakeException, ClientProtocolException, IOException {
        boolean isParam = params != null;
        url = url.replaceAll(" ", "%20");
        HttpClient httpClient = new DefaultHttpClient();
        HttpParams httpParams = httpClient.getParams();
        HttpConnectionParams.setConnectionTimeout(httpParams, CONN_TIMEOUT);
        HttpConnectionParams.setSoTimeout(httpParams, SO_TIMEOUT);
        HttpClientParams.setRedirecting(httpParams, false);

        HttpPost post = new HttpPost(url);
        post.setHeader("Content-Type", "application/x-www-form-urlencoded");
        post.setHeader("Charset", "UTF-8");
        if (isParam) {
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, CharEncoding.UTF_8);
            post.setEntity(entity);
        }
        return httpClient.execute(post);
    }
    
    private static HttpResponse get(Context context, String url)
            throws SSLHandshakeException,
            ClientProtocolException,
            IOException {
        url = url.replaceAll(" ", "%20");
        HttpClient httpClient = new DefaultHttpClient();
        HttpParams httpParams = httpClient.getParams();
        HttpConnectionParams.setConnectionTimeout(httpParams, CONN_TIMEOUT);
        HttpConnectionParams.setSoTimeout(httpParams, SO_TIMEOUT);
        HttpClientParams.setRedirecting(httpParams, false);

        HttpGet get = new HttpGet(url);
        get.setHeader("Content-Type", "application/x-www-form-urlencoded");
        get.setHeader("Charset", CharEncoding.UTF_8);
        return httpClient.execute(get);
    }
    
}
