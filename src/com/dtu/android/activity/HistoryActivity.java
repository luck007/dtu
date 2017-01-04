package com.dtu.android.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;

import com.dtu.android.R;
import com.dtu.android.activity.base.BaseCustomLoaderActivity;
import com.dtu.android.fragments.ErrorDetailFragment;
import com.dtu.android.fragments.ErrorListFragment;
import com.dtu.android.utils.Const;

public class HistoryActivity extends BaseCustomLoaderActivity{
    private static final String TAG;	
	private static final String PARAM_PUSH = "param_push";
	private static final String PARAM_EXTRAS = "param_extras";
	private static final String PARAM_IS_LOGOUT = "param_is_logout";
	
	
	public enum eListStatus{PUSHLIST, PUSHDETAIL};
	
	private eListStatus mFragmentStatus;
	
    static {
        TAG = HistoryActivity.class.getSimpleName();
    }

    public static Intent intentNewTask(Context context) {
        Intent intent = new Intent(context, HistoryActivity.class);
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
    	Intent intent = new Intent(context, HistoryActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
    	this.setTitle(this.getString(R.string.push_history_title));
        this.disableLeft(View.VISIBLE);
        this.disableRight(View.INVISIBLE);        
        
        this.setLeftButtonClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				HistoryActivity.this.onBackPressed();
			}
		});        
        
        ManagerActivity.instance().CurrentActivity = Const.VIEW_HISTORY;
        
        // load list fragment
        mFragmentStatus = eListStatus.PUSHLIST;
        selectFragment();
    } 
    
    private void selectFragment(){
    	FragmentTransaction transaction = this.getSupportFragmentManager().beginTransaction();    	
    	String fragmentTag = "f";
    	Fragment fragment = null;
    	switch (mFragmentStatus){
    	case PUSHLIST:    	    				
    		fragmentTag = fragmentTag + String.valueOf(mFragmentStatus); 
    		fragment = this.getSupportFragmentManager().findFragmentByTag(fragmentTag);
    		if (fragment == null)
    			fragment = ErrorListFragment.newInstance(0);
    		break;    	   		
    	case PUSHDETAIL:
    		fragmentTag = fragmentTag + String.valueOf(mFragmentStatus); 
    		fragment = this.getSupportFragmentManager().findFragmentByTag(fragmentTag);
    		if (fragment == null)
    			fragment = ErrorDetailFragment.newInstance(0);
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
    	if (mFragmentStatus == eListStatus.PUSHLIST){
    		ManagerActivity.instance().CurrentActivity = Const.VIEW_MAIN;
    		this.finish();
    	}
    	else
    		super.onBackPressed();
    	
    	switch (mFragmentStatus){    	    	   		
    	case PUSHDETAIL:
    		mFragmentStatus = eListStatus.PUSHLIST;
    		break;
    	}
	}
}
