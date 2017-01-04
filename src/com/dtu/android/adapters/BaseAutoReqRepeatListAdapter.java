package com.dtu.android.adapters;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.dtu.android.interfaces.FeedItem;
import com.dtu.android.utils.AndroidUtils;
import com.dtu.android.utils.Const;
import com.dtu.android.utils.HttpCallListener;
import com.dtu.android.utils.HttpGetTask;
import com.dtu.android.utils.MiscUtils;

public abstract class BaseAutoReqRepeatListAdapter extends BaseRepeatListAdapter
{
	private static final String TAG;
	public static final int PAGE_LIST_COUNT = 10;			//한번에 받아올 이벤트개수
	
	protected HttpGetTask				mReqTask;
	
	protected abstract FeedItem newFeedItem();
	protected abstract String getRequestUrl();
	
	static {
        TAG = BaseAutoReqRepeatListAdapter.class.getSimpleName();
    }
	
	protected abstract void onFinishLoadingList(boolean success);
	
	public BaseAutoReqRepeatListAdapter(Context context, boolean allowPagination)
	{
		super(context, allowPagination);
		this.mReqTask = null;
	}
	
	//서버에 event정보요청
	@Override
	protected void loadMoreListings()
    {
		boolean bLoad = ( this.mIsListBlank || ( this.mContinueLoading && this.mLoadMoreEnabled ));
		if (bLoad)
		{
			this.mIsListBlank = false;
			
			if (this.mReqTask != null)
				this.mReqTask.doCancel();
			
			this.mReqTask = HttpGetTask.newInstance();
	    	String accessToken = this.getRequestUrl();
	    	if (!TextUtils.isEmpty(accessToken))
	    	{
		    	this.mReqTask.doRequest(this.mContext, accessToken, new HttpCallListener() {
		            @Override
		            public void onReceived(String receivedInfo)
		            {
		            	boolean bLoadingSuccess = false;
		            	BaseAutoReqRepeatListAdapter.this.enableLoadMore();
		            	if (MiscUtils.isValidResponse(BaseAutoReqRepeatListAdapter.this.mContext, receivedInfo))
		            	{
		            		try
		            		{
		            			JSONObject jReceived =  new JSONObject(receivedInfo);
		            			if (jReceived != null)
		            			{
		            				String status = jReceived.getString(Const.KEY_RESULT);
		            				if (!TextUtils.isEmpty(status) && status.equals(Const.RESULT_STATUS_SUCCESS))
		            				{
		            					JSONArray jDatas = jReceived.getJSONArray(Const.KEY_DATA);
		            					if (jDatas != null)
		            					{
		            						bLoadingSuccess = true;
		            						List<FeedItem> items = new ArrayList<FeedItem>();
		            						int count = jDatas.length();
		        	            			for (int i = 0; i < count; i++)
		        	            			{
		        	            				JSONObject jObjEvent = jDatas.getJSONObject(i);
		        	            				FeedItem newItem = BaseAutoReqRepeatListAdapter.this.newFeedItem();
		        	            				newItem.parse(jObjEvent);
		        	            				items.add(newItem);
		        	            			}
		        	            			BaseAutoReqRepeatListAdapter.this.addFeedItems(items, false);
		        	            			//요구한 개수보다 작게 온 경우 더 요구하지 않는다.
		        	            			if (!BaseAutoReqRepeatListAdapter.this.mContinueLoading 
		        	            					|| count < PAGE_LIST_COUNT)
		        	            				BaseAutoReqRepeatListAdapter.this.setRemainingData(false);
		            					}
//		            					//서버시간
//		            					if (jReceived.has(Const.KEY_CURRENT_TIME))
//		            					{
//		            						String strServerTime = jReceived.getString(Const.KEY_CURRENT_TIME);
//		            						DateTimeUtils.changeDiffCSTime(strServerTime);
//		            					}
		            				}
		            				//else
		            				//	AndroidUtils.showMsg(BaseAutoReqRepeatListAdapter.this.mContext, jReceived.getString(Const.KEY_RESULT_MSG));
		            			}
		            			BaseAutoReqRepeatListAdapter.this.additionalParsing(jReceived);
		            		}
		            		catch(JSONException ex)
		            		{
		                    	Log.e("zbex", "BaseAutoReqRepeatListAdapter:loadMoreListings() ->" + ex.toString());
		                    }
		                    catch(Exception ex)
		                    {
		                    	Log.e("zbex", "BaseAutoReqRepeatListAdapter:loadMoreListings() ->" + ex.toString());
		                    }
		            	}
		            	//리스트받기실패인 경우 더 로딩하지 않는다.
		            	if (!bLoadingSuccess)
		            		BaseAutoReqRepeatListAdapter.this.setRemainingData(false);
		            	BaseAutoReqRepeatListAdapter.this.onFinishLoadingList(bLoadingSuccess);
		            }
		    	});
	    	}
		}
    }
	
	protected void additionalParsing(JSONObject jObj)
	{
	}
	
	@Override
	public void refresh()
	{
		if (this.mReqTask != null && this.mReqTask.isRunning())
			this.mReqTask.doCancel();
		super.refresh();
	}
}
