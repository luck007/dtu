package com.dtu.android.activity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.dtu.android.R;
import com.dtu.android.activity.base.BaseCustomLoaderActivity;
import com.dtu.android.events.DtuBus;
import com.dtu.android.events.ReceivedPushEvent;
import com.dtu.android.manager.UserManager;
import com.dtu.android.types.DeviceValueItem;
import com.dtu.android.types.ValueItem;
import com.dtu.android.utils.Const;
import com.dtu.android.utils.HttpCallListener;
import com.dtu.android.utils.HttpGetTask;
import com.dtu.android.utils.MiscUtils;
import com.dtu.android.utils.ToastFactory;
import com.squareup.otto.Subscribe;

public class PushActivity extends BaseCustomLoaderActivity{
	private static final String TAG = "PushActivity";
	
	private static final String PARAM_PUSH = "param_push";
	private static final String PARAM_EXTRAS = "param_extras";
	
	private HttpGetTask mTaskPush;
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push);
        
        ManagerActivity.instance().CurrentActivity = Const.VIEW_PUSH;
    	this.setTitle(this.getString(R.string.push_title));
        this.disableRight(View.INVISIBLE);    
        
        this.setLeftButtonClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intentApp = MainActivity.intentSingleTop(PushActivity.this);
				PushActivity.this.startActivity(intentApp);
				ManagerActivity.instance().CurrentActivity = Const.VIEW_MAIN;
				PushActivity.this.finish();
			}
		}); 
        
        String msgString = getIntent().getStringExtra(PARAM_EXTRAS);
        // request push content to server
        requestServer(msgString); 
        
        DtuBus.main.register(this);
	}
	
	@Override
	protected void onDestroy() {
		DtuBus.main.unregister(this);
		
		super.onDestroy();
	};
	
	@Override
	public void onBackPressed() {
		Intent intentApp = MainActivity.intentSingleTop(PushActivity.this);
		PushActivity.this.startActivity(intentApp);
		ManagerActivity.instance().CurrentActivity = Const.VIEW_MAIN;
		PushActivity.this.finish();
	}
	
	private void requestServer(String msgString){
		showLoader(true, false);
		// get push message id
        int pushId = -1;
        
        try {
			JSONObject jMsg =  new JSONObject(msgString);
			//this.setUI(jMsg);
			pushId = jMsg.getInt("msg_id");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}        
        
		this.mTaskPush = HttpGetTask.newInstance();
    	String accessToken = String.format("pid=%s&UserId=%d&MsgId=%d"
    			, Const.GET_PUSHDATA
    			, UserManager.getInstance().getCurrentUser().getId()
    			, pushId); 
    	
    	this.mTaskPush.doRequest(this, accessToken, new HttpCallListener() {
            @Override
            public void onReceived(String receivedInfo)
            {            	            	
            	if (MiscUtils.isValidResponse(PushActivity.this, receivedInfo))
            	{            		
            		try
            		{
            			JSONObject jReceived =  new JSONObject(receivedInfo);
            			if (jReceived != null){
            				String status = jReceived.getString(Const.KEY_RESULT);
            				if (!TextUtils.isEmpty(status) && status.equals(Const.RESULT_STATUS_SUCCESS))
            				{
            					JSONArray jDatas = jReceived.getJSONArray(Const.KEY_DATA);
            					if (jDatas != null && jDatas.length() > 0)
            					{            			
            						JSONObject jObjEvent = jDatas.getJSONObject(0);
            						PushActivity.this.setUI(jObjEvent);
            					}            				
            				}
            				else
            				{
            					status = jReceived.getString(Const.KEY_RESULT_MSG);
            					ToastFactory.getInstance().showToast(PushActivity.this, status);
            				}
            			}
            		}
            		catch(JSONException ex)
                	{
                       	Log.e("dtu", "->" + ex.toString());
                    }
                    catch(Exception ex)
                    {
                      	Log.e("dtu", "->" + ex.toString());
                    }
                }
            	PushActivity.this.showLoader(false);
        	}            
    	});
	}
	
	private String getFloatValue(int arg0, int valueIndex){
		String returnValue = "";
		
		ValueItem vItem = UserManager.getInstance().getCurrentUser().getValueList().get(valueIndex);
		double fValue = vItem.getMinValue() + (arg0 / DeviceValueItem.MAX_COFF) * (vItem.getMaxValue() - vItem.getMinValue());
		
		returnValue = String.format("%.2f", fValue); 
		
		return returnValue;
	}
	
	private void setUI(JSONObject jObjEvent){
		String valueString = "";
		TextView outUI = null;
		try{
			if (jObjEvent.has("for_test")) {
				int nType = jObjEvent.getInt("for_test");
				if (nType == 0) {
					outUI = (TextView)findViewById(R.id.tv_potential); 
					outUI.setText(getString(R.string.histo_potential));
					
					outUI = (TextView)findViewById(R.id.tv_current); 
					outUI.setText(getString(R.string.histo_current));
					
					outUI = (TextView)findViewById(R.id.tv_voltage); 
					outUI.setText(getString(R.string.histo_voltage));
					
				}
				else if (nType == 1) {
					outUI = (TextView)findViewById(R.id.tv_potential); 
					outUI.setText(getString(R.string.histo_potential1));
					
					outUI = (TextView)findViewById(R.id.tv_current); 
					outUI.setText(getString(R.string.histo_current1));
					
					outUI = (TextView)findViewById(R.id.tv_voltage); 
					outUI.setText(getString(R.string.histo_voltage1));
				}
			}
			
			// get group name
			if (jObjEvent.has("groupname"))
			{
				valueString = jObjEvent.getString("groupname"); 
				outUI = (TextView)findViewById(R.id.txtGroupName); 
				outUI.setText(valueString);
			}
			if (jObjEvent.has("equipname"))
			{
				valueString = jObjEvent.getString("equipname");
				outUI = (TextView)findViewById(R.id.txtDeviceName); 
				outUI.setText(valueString);
			}
			if (jObjEvent.has("potential"))
			{
				valueString = jObjEvent.getString("potential");
				outUI = (TextView)findViewById(R.id.txtPotential);
				outUI.setText(valueString);
//				int nPotential = jObjEvent.getInt("potential");
//				valueString = getFloatValue(nPotential, 0);
//				outUI = (TextView)findViewById(R.id.txtPotential); 
//				outUI.setText(valueString);
			}
			if (jObjEvent.has("current"))
			{
				valueString = jObjEvent.getString("current");
				outUI = (TextView)findViewById(R.id.txtCurrent);
				outUI.setText(valueString);
//				int nCurrent = jObjEvent.getInt("current");
//				valueString = getFloatValue(nCurrent, 1);
//				outUI = (TextView)findViewById(R.id.txtCurrent); 
//				outUI.setText(valueString);
			}
			if (jObjEvent.has("voltage"))
			{
				valueString = jObjEvent.getString("voltage");
				outUI = (TextView)findViewById(R.id.txtVoltage);
				outUI.setText(valueString);
//				int nVoltage = jObjEvent.getInt("voltage");
//				valueString = getFloatValue(nVoltage, 2);
//				outUI = (TextView)findViewById(R.id.txtVoltage); 
//				outUI.setText(valueString);
			}  	
			if (jObjEvent.has("regdate"))
			{
				valueString = jObjEvent.getString("regdate");
				outUI = (TextView)findViewById(R.id.txtRegDate); 
				outUI.setText(valueString);
			}
		}
		catch(JSONException ex) {}		
	}
	
	@Subscribe
    public void onFinishLoadingList(ReceivedPushEvent flmae)
    {   	
	   	this.requestServer(ManagerActivity.instance().getPushExtraString());	  
	   	ManagerActivity.instance().resetPushExtraString();
    }
}
