package com.dtu.android.types;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

import com.dtu.android.enums.RowType;
import com.dtu.android.interfaces.FeedItem;

@SuppressWarnings("serial")
public class ValueItem implements FeedItem, Serializable
{
	private double mMinValue;
	private double mMaxValue;
	private double mMaxLimit;
	private double mMinLimit;	

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
	
	@Override
	public void parse(JSONObject jObj)
	{
		try
		{
			if (jObj.has("min_value"))
			{
				this.mMinValue = jObj.getDouble("min_value");
			}
			if (jObj.has("max_value"))
			{
				this.mMaxValue = jObj.getDouble("max_value");
			}
			if (jObj.has("min_limit"))
			{
				this.mMinLimit = jObj.getDouble("min_limit");
			}
			if (jObj.has("max_limit"))
			{
				this.mMaxLimit = jObj.getDouble("max_limit");
			}
		}
		catch(JSONException ex) {}
	}

	public double getMinValue() {
		return mMinValue;
	}

	public void setMinValue(float minValue) {
		this.mMinValue = minValue;
	}

	public double getMaxValue() {
		return mMaxValue;
	}

	public void setMaxValue(float maxValue) {
		this.mMaxValue = maxValue;
	}

	public double getMaxLimit() {
		return mMaxLimit;
	}

	public void setMaxLimit(float maxLimit) {
		this.mMaxLimit = maxLimit;
	}

	public double getMinLimit() {
		return mMinLimit;
	}

	public void setMinLimit(float minLimit) {
		this.mMinLimit = minLimit;
	}

}