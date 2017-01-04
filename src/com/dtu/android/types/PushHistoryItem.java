package com.dtu.android.types;

import java.io.Serializable;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

import com.dtu.android.enums.RowType;
import com.dtu.android.interfaces.FeedItem;

@SuppressWarnings("serial")
public class PushHistoryItem implements FeedItem, Serializable
{
	private String mTitle;
	private String mRegDate;
	private long mId;
	
	@Override
	public RowType getType()
	{
		return RowType.SYSTEM_NOTICE;
	}

	@Override
	public long getId()
	{
		return mId;
	}
	
	public String getTitle()
	{
		return this.mTitle;
	}
	
	public void setId(long id){
		mId = id;
	}
	
	public void setTitle(String title){
		this.mTitle = title;
	}
	
	public String getRegDate() {
		return mRegDate;
	}

	public void setRegDate(String regDate) {
		this.mRegDate = regDate;
	}

	@Override
	public void parse(JSONObject jObj)
	{
		try
		{
			if (jObj.has("groupname"))
			{
				this.mTitle = jObj.getString("groupname");
			}
			if (jObj.has("equipname"))
			{
				if (TextUtils.isEmpty(this.mTitle))
					this.mTitle = "未分组";
				this.mTitle = this.mTitle + "/";
				this.mTitle = this.mTitle + jObj.getString("equipname");
			}	
			if (jObj.has("regdate"))
			{
				this.mRegDate = jObj.getString("regdate");
			}
			
			if (jObj.has("msgid"))
			{
				this.mId = jObj.getInt("msgid");
			}			
		}
		catch(JSONException ex) {}
	}

}
