package com.dtu.android.events;

public class FinishLoadGroupListEvent
{
	private boolean mSuccessLoad;
	
	public FinishLoadGroupListEvent(boolean succesLoad)
	{
		this.mSuccessLoad = succesLoad;
	}
	
	public boolean getSuccessLoad()
	{
		return this.mSuccessLoad;
	}
}
