package com.dtu.android.activity;

import com.dtu.android.R;
import com.dtu.android.activity.base.BaseCustomTitleActivity;
import com.dtu.android.fragments.DeviceListFragment;
import com.dtu.android.fragments.DeviceValueHistoFragment;
import com.dtu.android.fragments.GroupListFragment;
import com.dtu.android.manager.UserManager;
import com.dtu.android.utils.AndroidUtils;
import com.dtu.android.utils.Const;

import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

public class MainActivity extends BaseCustomTitleActivity{
    private static final String TAG;	
	private static final String PARAM_PUSH = "param_push";
	private static final String PARAM_EXTRAS = "param_extras";
	private static final String PARAM_IS_LOGOUT = "param_is_logout";
	
	
	public enum eListStatus{LIST, DETAIL, HISTORY};
	
	private eListStatus mFragmentStatus;
	
    static {
        TAG = MainActivity.class.getSimpleName();
    }

    public static Intent intentNewTask(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
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
    	Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        ManagerActivity.instance().CurrentActivity = Const.VIEW_MAIN;
    	this.setTitle(this.getString(R.string.main_title));
    	this.setRightButton("", R.drawable.push_history);
        this.disableLeft(View.INVISIBLE);                
        
        this.setLeftButtonClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MainActivity.this.onBackPressed();
			}
		});        
        
        this.setRightButtonClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
		        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
		        MainActivity.this.startActivity(intent);
			}
		});
        // load list fragment
        mFragmentStatus = eListStatus.LIST;
        selectFragment();
    } 
    
    private void selectFragment(){
    	FragmentTransaction transaction = this.getSupportFragmentManager().beginTransaction();    	
    	String fragmentTag = "f";
    	Fragment fragment = null;
    	switch (mFragmentStatus){
    	case LIST:    	    				
    		fragmentTag = fragmentTag + String.valueOf(mFragmentStatus); 
    		fragment = this.getSupportFragmentManager().findFragmentByTag(fragmentTag);
    		if (fragment == null)
    			fragment = GroupListFragment.newInstance(0);
    		break;    	   		
    	case DETAIL:
    		fragmentTag = fragmentTag + String.valueOf(mFragmentStatus); 
    		fragment = this.getSupportFragmentManager().findFragmentByTag(fragmentTag);
    		if (fragment == null)
    			fragment = DeviceListFragment.newInstance(0);
    		break;
    	case HISTORY:    		
    		fragmentTag = fragmentTag + String.valueOf(mFragmentStatus); 
    		fragment = this.getSupportFragmentManager().findFragmentByTag(fragmentTag);
    		if (fragment == null)
    			fragment = DeviceValueHistoFragment.newInstance(0);
    		break;
    	}
    	
    	transaction.setCustomAnimations(R.anim.fragment_enter, R.anim.fragment_exit, R.anim.fragment_enter_pop, R.anim.fragment_exit_pop);
    	transaction.replace(R.id.content_frame, fragment, fragmentTag);
		transaction.addToBackStack(null);
    	transaction.commitAllowingStateLoss(); 
    } 
    
    public void selectFragment(eListStatus status){
    	mFragmentStatus = status;
    	selectFragment();
    }
    
    @Override
	public void onBackPressed() { 
    	if (mFragmentStatus == eListStatus.LIST)
    		this.showExitAppDialog();
    	else
    		super.onBackPressed();
    	
    	switch (mFragmentStatus){    	    	   		
    	case DETAIL:
    		mFragmentStatus = eListStatus.LIST;
    		this.setTitle("分组");
    		this.disableLeft(View.INVISIBLE);
    		break;
    	case HISTORY:    		
    		mFragmentStatus = eListStatus.DETAIL;
    		setTitle("设备");
    		break;
    	}
	}
    
  //app닫기확인 dialog
  	private void showExitAppDialog()
  	{  		
  		Builder builder = new Builder(this);
  		String strMsg = this.getString(R.string.msg_confirm_exit_app);
  		builder.setMessage(strMsg).setTitle(R.string.msg_title_exit_app)
  			.setCancelable(true)
  			.setNegativeButton(R.string.cancel, null)
  			.setPositiveButton(R.string.exit, new android.content.DialogInterface.OnClickListener() {
  				@Override
  				public void onClick(DialogInterface dialog, int index) {
  					MainActivity.this.finish();
  					ManagerActivity.instance().releaseApp();
  				}
  			});
  		builder.create().show();
  	}
}
