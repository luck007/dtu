package com.dtu.android.events;

public class FinishLoadDeviceValueEvent
{
	private boolean mSuccessLoad;
	
	public FinishLoadDeviceValueEvent(boolean succesLoad)
	{
		this.mSuccessLoad = succesLoad;
	}
	
	public boolean getSuccessLoad()
	{
		return this.mSuccessLoad;
	}
}
