package com.dtu.android.types;

import java.io.Serializable;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import com.dtu.android.enums.RowType;
import com.dtu.android.interfaces.FeedItem;
import com.dtu.android.manager.UserManager;

@SuppressWarnings("serial")
public class DeviceNameItem implements FeedItem, Serializable
{
	private String mTitle;
	private String mDate;
	private String mStatus;
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
			if (jObj.has("reg_date"))
			{
				this.mDate = jObj.getString("reg_date");
			}
			if (jObj.has("status"))
			{				
				int nStatus = jObj.getInt("status");
				if (UserManager.getInstance().getCurrentType() == 1) {
					if (nStatus == 0)
						this.mStatus = "离线";
					else if (nStatus == 1)
						this.mStatus = "在线";
				}
				else {
					if (nStatus == 0)
						this.mStatus = "485";
					else if (nStatus == 1)
						this.mStatus = "dtu";
				}
			}
		}
		catch(JSONException ex) {}
	}

	public String getDate() {
		return mDate;
	}

	public void setDate(String mDate) {
		this.mDate = mDate;
	}

	public String getStatus() {
		return mStatus;
	}

	public void setStatus(String mStatus) {
		this.mStatus = mStatus;
	}

}
