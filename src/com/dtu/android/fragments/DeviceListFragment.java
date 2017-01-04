package com.dtu.android.fragments;

import com.dtu.android.R;
import com.dtu.android.activity.MainActivity;
import com.dtu.android.activity.ManagerActivity;
import com.dtu.android.adapters.DeviceListAdapter;
import com.dtu.android.adapters.DeviceValueListAdapter;
import com.dtu.android.events.DtuBus;
import com.dtu.android.events.FinishLoadGroupListEvent;
import com.dtu.android.manager.UserManager;
import com.dtu.android.types.DeviceNameItem;
import com.squareup.otto.Subscribe;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.RadioButton;
import android.widget.AdapterView.OnItemClickListener;

public class DeviceListFragment extends BaseListWithStickyFragment{
    protected static final String ARG_SORT_TYPE = "sort_type";
    
    protected int mSortType;    
    protected boolean mCreated = false;
	protected DeviceListAdapter mListingAdapter;
	
	private MainActivity parentActivity;
	
	public static DeviceListFragment newInstance(int sortType)
	{
		DeviceListFragment f = new DeviceListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SORT_TYPE, sortType);
        f.setArguments(args);
        return f;
	}

	@Override
	protected int getLayout()
	{
		return R.layout.fragment_device_list;
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
        
        this.getListView().setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {	
				//클릭된 해당 이벤트의 상세정보창으로 이동
				DeviceNameItem selectedItem = (DeviceNameItem)mListingAdapter.getItem(position);
				if (selectedItem != null)
				{					
					UserManager.getInstance().setCurrentDeviceId(selectedItem.getId());
					DeviceListFragment.this.parentActivity.selectFragment(MainActivity.eListStatus.HISTORY);
				}			
			}
		});
        
        RadioButton rb =(RadioButton) view.findViewById(R.id.rb_group0);
        //rb.setChecked(true);
        rb.setOnClickListener( new OnClickListener() {			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				DeviceListFragment.this.refreshList(0);
			}
		});
        
        rb =(RadioButton) view.findViewById(R.id.rb_group1);
        //rb.setChecked(false);
        rb.setOnClickListener( new OnClickListener() {			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				DeviceListFragment.this.refreshList(1);
			}
		});        
        
        mCreated = true;
        
        parentActivity.setTitle("设备");
        parentActivity.disableLeft(View.VISIBLE);
        showStickyButton(false);
        
        // init list
        Handler mHandler = new Handler();
  		mHandler.postDelayed(new Runnable()
	      {
	          @Override
	          public void run()
	          {
	        	  DeviceListFragment.this.mListingAdapter = new DeviceListAdapter(DeviceListFragment.this.getActivity(), true);
	        	  DeviceListFragment.this.getListView().setAdapter(DeviceListFragment.this.mListingAdapter);
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
    public void onFinishLoadingList(FinishLoadGroupListEvent flmae)
    {   	
	   	this.showEmptyResults((this.mListingAdapter.getRealCount() == 0));    	
    }
    
    public void refreshList(int type) {
    	UserManager.getInstance().setCurrentType(type);
    	this.mListingAdapter.setType(type);
    }   
    
}
