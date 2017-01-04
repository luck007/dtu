package com.dtu.android.types;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.dtu.android.enums.RowType;
import com.dtu.android.interfaces.FeedItem;

@SuppressWarnings("serial")
public class UserBase implements FeedItem, Serializable, Cloneable
{
	public static int MIN_EMPLOYER_ID = 10000000;
	
	protected long mId;	
	protected String mAccount;	
	protected String mPassword;	
	
	protected List<ValueItem>			mValueItems;
	
	public static UserBase createUser(long userId)
	{
		UserBase user = null;		
		user = new UserBase();
		user.setId(userId);
		
		return user;
	}
	
	@Override  
    public Object clone()
	{  
		UserBase sc = null;  
        try  
        {  
            sc = (UserBase) super.clone();  
        }
        catch (CloneNotSupportedException ex)
        {  
            ex.printStackTrace();  
        }  
        return sc;  
    }
	
	public UserBase()
	{
		mId = 0;	
		mAccount = "";	
		mPassword = "";
		
		mValueItems = new ArrayList<ValueItem>();
	}
	
	public void clear()
	{
		mId = 0;	
		mAccount = "";	
		mPassword = "";
		
		mValueItems.clear();
	}
	
	@Override
	public long getId()
	{
		return this.mId;
	}
	
	public void setId(long id)
	{
		this.mId = id;
	}
	
	public String getAccount()
	{
		return this.mAccount;
	}

	public void setAccount(String value)
	{
		this.mAccount = value;
	}
	
	public String getPassword()
	{
		return this.mPassword;
	}

	public void setPassword(String value)
	{
		this.mPassword = value;
	}
	
	public List<ValueItem> getValueList()
	{
		return mValueItems;
	}
	
	@Override
	public void parse(JSONObject jObj)
	{
		try
		{
			if (jObj != null)
			{
				if (jObj.has("userid"))
				{
					long id = jObj.getLong("userid");
					this.setId(id);
				}	
				
				JSONArray jDatas = jObj.getJSONArray("data");
				mValueItems.clear();
				
				if (jDatas != null)
				{
					int count = jDatas.length();
        			for (int i = 0; i < count; i++)
        			{
        				JSONObject jObjEvent = jDatas.getJSONObject(i);
        				ValueItem newItem = new ValueItem();
        				newItem.parse(jObjEvent);
        				mValueItems.add(newItem);
        			}
				}
			}
		}
		catch(JSONException ex) {}
	}

	@Override
	public RowType getType() {
		return null;
	}

}
