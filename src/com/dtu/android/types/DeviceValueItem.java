package com.dtu.android.types;

import java.io.Serializable;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import com.dtu.android.enums.RowType;
import com.dtu.android.interfaces.FeedItem;
import com.dtu.android.manager.UserManager;

@SuppressWarnings("serial")
public class DeviceValueItem implements FeedItem, Serializable
{	
	public static final double MAX_COFF = (float)0x0ffff;
	private String mDate;
	private String mPotential;
	private String mCurrent;
	private String mVoltage;
	private int mStopFlag = 0;
	private boolean mNoExpected[] = new boolean[3];
	
	@Override
	public RowType getType()
	{
		return RowType.SYSTEM_NOTICE;
	}

	@Override
	public long getId()
	{
		return -1;
	}
	
	private String getFloatValue(int arg0, int valueIndex){
		String returnValue = "";
		
		ValueItem vItem = UserManager.getInstance().getCurrentUser().getValueList().get(valueIndex);
		double fValue = vItem.getMinValue() + (arg0 / MAX_COFF) * (vItem.getMaxValue() - vItem.getMinValue());
		
		returnValue = String.format("%.2f", fValue); 
		
		if (fValue < vItem.getMinLimit() | fValue > vItem.getMaxLimit() )
			mNoExpected[valueIndex%3] = true;
		else
			mNoExpected[valueIndex%3] = false;
			
		return returnValue;
	}
	@Override
	public void parse(JSONObject jObj)
	{
		try
		{			
			if (jObj.has("reg_date"))
			{
				this.mDate = jObj.getString("reg_date");
			}
			if (jObj.has("potential"))
			{
				int nPotential = jObj.getInt("potential");
				
				if (UserManager.getInstance().getCurrentType() == 0) 
					this.mPotential = getFloatValue(nPotential, 0);
				else if (UserManager.getInstance().getCurrentType() == 1)
					this.mPotential = getFloatValue(nPotential, 3);
			}
			if (jObj.has("current"))
			{
				int nCurrent = jObj.getInt("current");				
				
				if (UserManager.getInstance().getCurrentType() == 0) 
					this.mCurrent = getFloatValue(nCurrent, 1);
				else if (UserManager.getInstance().getCurrentType() == 1)
					this.mCurrent = getFloatValue(nCurrent, 4);
			}
			if (jObj.has("voltage"))
			{
				int nVoltage = jObj.getInt("voltage");				
				
				if (UserManager.getInstance().getCurrentType() == 0) 
					this.mVoltage = getFloatValue(nVoltage, 2);
				else if (UserManager.getInstance().getCurrentType() == 1)
					this.mVoltage = getFloatValue(nVoltage, 5);
			}
			if (jObj.has("stopmode_flag"))
			{
				this.mStopFlag = jObj.getInt("stopmode_flag");
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

	public String getPotential() {
		return mPotential;
	}

	public void setPotential(String potential) {
		this.mPotential = potential;
	}

	public String getCurrent() {
		return mCurrent;
	}

	public void setCurrent(String current) {
		this.mCurrent = current;
	}

	public String getVoltage() {
		return mVoltage;
	}

	public void setVoltage(String voltage) {
		this.mVoltage = voltage;
	}

	public boolean NoExpected(int index) {
		return mNoExpected[index];
	}

	public int isStopFlag() {
		return mStopFlag;
	}

	public void setStopFlag(int flag) {
		this.mStopFlag = flag;
	}	
}
