package com.dtu.android.types;

import java.io.Serializable;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import com.dtu.android.enums.RowType;
import com.dtu.android.interfaces.FeedItem;

@SuppressWarnings("serial")
public class GroupNameItem implements FeedItem, Serializable
{
	private String mTitle;	
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
	
	@Override
	public void parse(JSONObject jObj)
	{
		try
		{
			if (jObj.has("name"))
			{
				this.mTitle = jObj.getString("name");
			}
			if (jObj.has("id"))
			{
				this.mId = jObj.getInt("id");
			}			
		}
		catch(JSONException ex) {}
	}

}
