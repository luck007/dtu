package com.dtu.android.fragments;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dtu.android.R;
import com.dtu.android.activity.HistoryActivity;
import com.dtu.android.manager.UserManager;
import com.dtu.android.types.DeviceValueItem;
import com.dtu.android.types.ValueItem;
import com.dtu.android.utils.Const;
import com.dtu.android.utils.HttpCallListener;
import com.dtu.android.utils.HttpGetTask;
import com.dtu.android.utils.MiscUtils;
import com.dtu.android.utils.ToastFactory;

public class ErrorDetailFragment extends BaseListWithStickyFragment{
    protected static final String ARG_SORT_TYPE = "sort_type";
    
    protected int mSortType;
	private HistoryActivity parentActivity;	
	private HttpGetTask mTaskPush;
	private View mSelf;
	public static ErrorDetailFragment newInstance(int sortType)
	{
		ErrorDetailFragment f = new ErrorDetailFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SORT_TYPE, sortType);
        f.setArguments(args);
        return f;
	}

	@Override
	protected int getLayout()
	{
		return R.layout.activity_push;
	}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
    	mSelf = super.onCreateView(inflater, container, savedInstanceState);
        //DtuBus.main.register(this);
        
        Bundle bundle = this.getArguments();
        if (bundle != null)
        {
            this.mSortType = bundle.getInt(ARG_SORT_TYPE);
        }
        
        parentActivity = (HistoryActivity)getActivity();
        parentActivity.showLoader(true, false); 
     // request push content to server
        this.mTaskPush = HttpGetTask.newInstance();
    	String accessToken = String.format("pid=%s&UserId=%d&MsgId=%d"
    			, Const.GET_PUSHDATA
    			, UserManager.getInstance().getCurrentUser().getId()
    			, UserManager.getInstance().getCurrentDeviceId()
    			); 
    	
    	this.mTaskPush.doRequest(parentActivity, accessToken, new HttpCallListener() {
            @Override
            public void onReceived(String receivedInfo)
            {            	            	
            	if (MiscUtils.isValidResponse(parentActivity, receivedInfo))
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
            						ErrorDetailFragment.this.setUI(jObjEvent);
            					}            				
            				}
            				else
            				{
            					status = jReceived.getString(Const.KEY_RESULT_MSG);
            					ToastFactory.getInstance().showToast(parentActivity, status);
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
            	parentActivity.showLoader(false);
        	}            
    	});
    	
        showStickyButton(false);
        return mSelf;
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
					outUI = (TextView)mSelf.findViewById(R.id.tv_potential); 
					outUI.setText(getString(R.string.histo_potential));
					
					outUI = (TextView)mSelf.findViewById(R.id.tv_current); 
					outUI.setText(getString(R.string.histo_current));
					
					outUI = (TextView)mSelf.findViewById(R.id.tv_voltage); 
					outUI.setText(getString(R.string.histo_voltage));
					
				}
				else if (nType == 1) {
					outUI = (TextView)mSelf.findViewById(R.id.tv_potential); 
					outUI.setText(getString(R.string.histo_potential1));
					
					outUI = (TextView)mSelf.findViewById(R.id.tv_current); 
					outUI.setText(getString(R.string.histo_current1));
					
					outUI = (TextView)mSelf.findViewById(R.id.tv_voltage); 
					outUI.setText(getString(R.string.histo_voltage1));
				}
			}
			
			// get group name
			if (jObjEvent.has("groupname"))
			{
				valueString = jObjEvent.getString("groupname"); 
				outUI = (TextView)mSelf.findViewById(R.id.txtGroupName); 
				outUI.setText(valueString);
			}
			if (jObjEvent.has("equipname"))
			{
				valueString = jObjEvent.getString("equipname");
				outUI = (TextView)mSelf.findViewById(R.id.txtDeviceName); 
				outUI.setText(valueString);
			}
			if (jObjEvent.has("potential"))
			{
				valueString = jObjEvent.getString("potential");
				outUI = (TextView)mSelf.findViewById(R.id.txtPotential);
				outUI.setText(valueString);
//				int nPotential = jObjEvent.getInt("potential");
//				valueString = getFloatValue(nPotential, 0);
//				outUI = (TextView)mSelf.findViewById(R.id.txtPotential); 
//				outUI.setText(valueString);
			}
			if (jObjEvent.has("current"))
			{
				valueString = jObjEvent.getString("current");
				outUI = (TextView)mSelf.findViewById(R.id.txtCurrent);
				outUI.setText(valueString);
//				int nCurrent = jObjEvent.getInt("current");
//				valueString = getFloatValue(nCurrent, 1);
//				outUI = (TextView)mSelf.findViewById(R.id.txtCurrent); 
//				outUI.setText(valueString);
			}
			if (jObjEvent.has("voltage"))
			{
				valueString = jObjEvent.getString("voltage");
				outUI = (TextView)mSelf.findViewById(R.id.txtVoltage);
				outUI.setText(valueString);
//				int nVoltage = jObjEvent.getInt("voltage");
//				valueString = getFloatValue(nVoltage, 2);
//				outUI = (TextView)mSelf.findViewById(R.id.txtVoltage); 
//				outUI.setText(valueString);
			}  	
			if (jObjEvent.has("regdate"))
			{
				valueString = jObjEvent.getString("regdate");
				outUI = (TextView)mSelf.findViewById(R.id.txtRegDate); 
				outUI.setText(valueString);
			}
		}
		catch(JSONException ex) {}		
	}
}
