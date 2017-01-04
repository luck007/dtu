package com.dtu.android.manager;

import com.dtu.android.types.UserBase;

public class UserManager {
static UserManager m_Instance = null;
	
	private UserBase mCurrentUser;						//현재 로그인된 유저의 정보
	private long mCurrentGroupId;
	private long mCurrentDeviceId;
	private int mCurrentType;
	private boolean mLogined;
	////
	public static UserManager getInstance()
	{
		if ( m_Instance == null )
			m_Instance = new UserManager();
		return m_Instance;
	}
	
	////////
	public UserManager()
	{
		mCurrentUser = UserBase.createUser(0);
		mCurrentGroupId = -1;
		mCurrentDeviceId = -1;
		mLogined = false;
	}

	public long getCurrentUserId()
	{		
		return mCurrentUser.getId();
	}
		
	public String getCurrentUserAccountId() {
		return mCurrentUser.getAccount();
	}

	public void setCurrentUserAccountId(String currentUserAccountId) {
		this.mCurrentUser.setAccount(currentUserAccountId);
	}

	public UserBase getCurrentUser() {
		return mCurrentUser;
	}

	public void setCurrentUser(UserBase mCurrentUser) {
		this.mCurrentUser = mCurrentUser;
	}

	public String getCurrentUserPassword() {
		return this.mCurrentUser.getAccount();
	}

	public void setCurrentUserPassword(String currentUserPassword) {
		this.mCurrentUser.setPassword(currentUserPassword);
	}

	public long getCurrentGroupId() {
		return mCurrentGroupId;
	}

	public void setCurrentGroupId(long CurrentGroupId) {
		this.mCurrentGroupId = CurrentGroupId;
	}

	public long getCurrentDeviceId() {
		return mCurrentDeviceId;
	}

	public void setCurrentDeviceId(long CurrentDeviceId) {
		this.mCurrentDeviceId = CurrentDeviceId;
	}

	public void setLogined(boolean login){
		mLogined = login;
	}
	
	public boolean isLogined() {
		// TODO Auto-generated method stub
		return mLogined;
	}
	
	public String getUserPushAlias()
	{
		String strPushAlias = null;
		if (UserManager.getInstance().isLogined())
		{			
			UserBase user = UserManager.getInstance().getCurrentUser();
			strPushAlias = String.valueOf(user.getId());
			
			strPushAlias = "a_" + strPushAlias;
		}
		return strPushAlias;
	}

	public int getCurrentType() {
		return mCurrentType;
	}

	public void setCurrentType(int mCurrentType) {
		this.mCurrentType = mCurrentType;
	}
}
