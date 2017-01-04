package com.dtu.android.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.dtu.android.activity.base.BaseCustomTitleActivity;
import com.dtu.android.events.DtuBus;
import com.dtu.android.events.ReceivedPushEvent;
import com.dtu.android.manager.UserManager;
import com.dtu.android.utils.AndroidUtils;
import com.dtu.android.utils.Const;

public class ManagerActivity extends BaseCustomTitleActivity {
    private static final String TAG;	
	private static final String PARAM_PUSH = "param_push";
	private static final String PARAM_EXTRAS = "param_extras";
		
	private static ManagerActivity instance;	
	public static final boolean isInstanced(){
		return instance != null;
	}
	
	public static final ManagerActivity instance() {
		return instance;
	}
	
	private MessageReceiver mMessageReceiver;
	public static final String MESSAGE_RECEIVED_ACTION = "com.dtu.android.MESSAGE_RECEIVED_ACTION";

	public class MessageReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
				AndroidUtils.showMsg(ManagerActivity.this, "Received Push");
			}
		}
	}
	
	public enum eListStatus{LIST, DETAIL, HISTORY};
	
	private eListStatus mFragmentStatus;
	private String mPushExtra = "";
	private static final int LOGIN_SUCCESS = 0;
	private static final int RECEIVED_SUCCESS = 1;

	private Handler mGoToActivityHandler;
	public String CurrentActivity;
	
    static {
        TAG = ManagerActivity.class.getSimpleName();
    }

    public static Intent intentNewTask(Context context) {
        Intent intent = new Intent(context, ManagerActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return intent;
    }

    public static Intent intentForPushParam(Context context, String param) {
    	Intent intent = intentNewTask(context);
    	intent.putExtra(PARAM_PUSH, PARAM_PUSH);
    	intent.putExtra(PARAM_EXTRAS, param);
    	return intent;
    }
    
    public static Intent intentSingleTop(Context context)
    {
    	Intent intent = new Intent(context, ManagerActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); 
        registerMessageReceiver();  // used for receive msg
        
        instance = this;
        
        checkAnalysePushMsg(getIntent());
                
        Intent intent = new Intent(this, LogInActivity.class);        
        this.startActivity(intent);
        CurrentActivity = Const.VIEW_LOGIN;
    }
    
//    @Override
//	public void onNewIntent(Intent intent)
//	{
//		super.onNewIntent(intent);
//		
//		//push처리
//        if (this.checkAnalysePushMsg(intent)){
//        	receivedPushMessage();        	
//        }        	
//	}
    
    @Override
	protected void onDestroy() {
		unregisterReceiver(mMessageReceiver);
		super.onDestroy();
	}
    
    //--------push 메시지로 들어온 경우인가 분석
  	public boolean checkAnalysePushMsg(Intent intent){
  		boolean bFromPush = false;
  		String paramPush = intent.getStringExtra(PARAM_PUSH);
  		//push로 들어온 경우
    	if (!TextUtils.isEmpty(paramPush) && paramPush.equals(PARAM_PUSH) )
    	{
    		bFromPush = true;
    		mPushExtra = intent.getStringExtra(PARAM_EXTRAS);    		
    	}
    	
  		return bFromPush;  		
  	}
  	
  	public void registerMessageReceiver() {
  		mGoToActivityHandler = new Handler() {
  			public void handleMessage(Message msg) {
  				super.handleMessage(msg);
  				
  				switch (msg.what) {
  				case LOGIN_SUCCESS:					
  					ManagerActivity.this.gotoActivity();
  					break;
  				case RECEIVED_SUCCESS:
  					ManagerActivity.this.gotoActivity2();
  					break;
  				default:                    	
  	                break;
  				}
  			}
  		};
  		
		mMessageReceiver = new MessageReceiver();
		IntentFilter filter = new IntentFilter();
		filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
		filter.addAction(MESSAGE_RECEIVED_ACTION);
		registerReceiver(mMessageReceiver, filter);
	}
  	
  	public void releaseApp(){
  		UserManager.getInstance().getCurrentUser().clear();
		
		this.finish();			
		System.exit(0);
  	}
  	
  	private void receivedPushMessage(){
  		Message msg = this.mGoToActivityHandler.obtainMessage();
        msg.what = RECEIVED_SUCCESS;
        
        this.mGoToActivityHandler.sendMessage(msg);
  	}
  	
  	private void gotoActivity(){
  	// if push message had received, go to push activity.
  		if (!TextUtils.isEmpty(mPushExtra)){
  			Intent intentApp = new Intent(this, PushActivity.class);
    		intentApp.putExtra(PARAM_EXTRAS, mPushExtra);
    		intentApp.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
  			this.startActivity(intentApp);
  			
  			mPushExtra = "";
  		}
  		// go to main activity
  		else{
  			Intent intent = new Intent(this, MainActivity.class);        
  			this.startActivity(intent);  			
  		}
  	}
  	
  	private void gotoActivity2(){
  	// if user have logged off, goto login window
    	if (!UserManager.getInstance().isLogined()){
    		Intent newIntent = new Intent(this, LogInActivity.class);
    		newIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP );
            this.startActivity(newIntent);
    	}
    	// if user is logged in, goto push window
    	else{
    		Intent intentApp = new Intent(this, PushActivity.class);
    		intentApp.putExtra(PARAM_EXTRAS, mPushExtra);
    		intentApp.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
  			this.startActivity(intentApp);
  			
  			mPushExtra = "";
    	}
  	}
  	
  	public void loginSuccess(){
  		Message msg = this.mGoToActivityHandler.obtainMessage();
        msg.what = LOGIN_SUCCESS;
        
        this.mGoToActivityHandler.sendMessage(msg);                
  	}
  	
  	public void onReceivedPushMessage(String extra){
  		mPushExtra = extra;
  		
  		if (CurrentActivity.equals(Const.VIEW_PUSH)){
  			DtuBus.main.post(new ReceivedPushEvent(true));
  			return;
  		}
  			
  		
  		if (CurrentActivity.equals(Const.VIEW_LOGIN))
  			return;
  		
  		Message msg = this.mGoToActivityHandler.obtainMessage();
        msg.what = RECEIVED_SUCCESS;
        
        this.mGoToActivityHandler.sendMessage(msg);
  	}
  	
  	public void resetPushExtraString(){
  		mPushExtra = "";
  	}
  	
  	public String getPushExtraString(){
  		return mPushExtra;
  	}
}
