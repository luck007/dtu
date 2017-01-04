package com.dtu.android.interfaces;

import org.json.JSONObject;

import com.dtu.android.enums.RowType;

public interface FeedItem
{
	RowType getType();
	long getId();
	
	public void parse(JSONObject jObj);
}
