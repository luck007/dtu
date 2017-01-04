package com.dtu.android.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dtu.android.R;
import com.dtu.android.activity.MainActivity;
import com.dtu.android.adapters.DeviceValueListAdapter;
import com.dtu.android.events.DtuBus;
import com.dtu.android.events.FinishLoadDeviceValueEvent;
import com.dtu.android.manager.UserManager;
import com.squareup.otto.Subscribe;

public class DeviceValueHistoFragment extends BaseListWithStickyFragment{
    protected static final String ARG_SORT_TYPE = "sort_type";
    
    protected int mSortType;
	
	protected DeviceValueListAdapter mListingAdapter;
	
	private MainActivity parentActivity;
	private Handler mHandler = new Handler();
	
	public static DeviceValueHistoFragment newInstance(int sortType)
	{
		DeviceValueHistoFragment f = new DeviceValueHistoFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SORT_TYPE, sortType);
        f.setArguments(args);
        return f;
	}

	@Override
	protected int getLayout()
	{
		return R.layout.fragment_list_with_sticky_header;
	}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
    	View view = super.onCreateView(inflater, container, savedInstanceState);
        DtuBus.main.register(this);
        
        Bundle bundle = this.getArguments();
        if (bundle != null)
        {
            this.mSortType = bundle.getInt(ARG_SORT_TYPE);
        }
                
        parentActivity = (MainActivity)getActivity();
        parentActivity.setTitle("设备状态");
        
        // title
        if (UserManager.getInstance().getCurrentType() == 1) {
        	TextView tv = (TextView) view.findViewById(R.id.title_histo_potential);
        	tv.setText(parentActivity.getString(R.string.histo_potential1));
        	
        	tv = (TextView) view.findViewById(R.id.title_histo_current);
        	tv.setText(parentActivity.getString(R.string.histo_current1));
        	
        	tv = (TextView) view.findViewById(R.id.title_histo_voltage);
        	tv.setText(parentActivity.getString(R.string.histo_voltage1));        	
        }
        
        showStickyButton(false);
        // init list		
  		mHandler.postDelayed(new Runnable()
	      {
	          @Override
	          public void run()
	          {
	        	  DeviceValueHistoFragment.this.mListingAdapter 
	        	  		= new DeviceValueListAdapter(DeviceValueHistoFragment.this.getActivity(), true);
	        	  DeviceValueHistoFragment.this.getListView().setAdapter(DeviceValueHistoFragment.this.mListingAdapter);
	          }
	      }, parentActivity.getResources().getInteger(R.integer.fragment_transition_duration_long_ms));
        return view;
    }

    //정보가 달라진 경우 갱신
    public void refreshList()
    {
    	this.mListingAdapter.refresh();
    }

    @Override
    public void onDestroyView() {
    	DtuBus.main.unregister(this);
        super.onDestroyView();
    }

    @Subscribe
    public void onFinishLoadingList(FinishLoadDeviceValueEvent flmae)
    {   	
	   	this.showEmptyResults((this.mListingAdapter.getRealCount() == 0));    	
    }	
}
