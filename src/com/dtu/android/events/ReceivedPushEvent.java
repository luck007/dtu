package com.dtu.android.events;

public class ReceivedPushEvent
{
	private boolean mSuccessLoad;
	
	public ReceivedPushEvent(boolean succesLoad)
	{
		this.mSuccessLoad = succesLoad;
	}
	
	public boolean getSuccessLoad()
	{
		return this.mSuccessLoad;
	}
}
