package com.dtu.android.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.dtu.android.R;
import com.dtu.android.activity.MainActivity;
import com.dtu.android.adapters.DeviceListAdapter;
import com.dtu.android.adapters.GroupListAdapter;
import com.dtu.android.manager.UserManager;
import com.dtu.android.types.GroupNameItem;

public class GroupListFragment extends BaseListWithStickyFragment{
    protected static final String ARG_SORT_TYPE = "sort_type";
    
    protected int mSortType;
    
	private MainActivity parentActivity;
	
	protected GroupListAdapter mListingAdapter;
	
	public static GroupListFragment newInstance(int sortType)
	{
		GroupListFragment f = new GroupListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SORT_TYPE, sortType);
        f.setArguments(args);
        return f;
	}

	@Override
	protected int getLayout()
	{
		return R.layout.fragment_list_with_sticky;
	}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
    	View view = super.onCreateView(inflater, container, savedInstanceState);
        //DtuBus.main.register(this);
        
        Bundle bundle = this.getArguments();
        if (bundle != null)
        {
            this.mSortType = bundle.getInt(ARG_SORT_TYPE);
        }        
        
        this.getListView().setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {				
				GroupNameItem selectedItem = (GroupNameItem)mListingAdapter.getItem(position);
				if (selectedItem != null)
				{
					UserManager.getInstance().setCurrentType(0);
					UserManager.getInstance().setCurrentGroupId(selectedItem.getId());
					parentActivity.selectFragment(MainActivity.eListStatus.DETAIL);
				}				
			}
		});  
        
        parentActivity = (MainActivity)getActivity();
        showStickyButton(false);
        
        // initialize list
        Handler mHandler = new Handler();
  		mHandler.postDelayed(new Runnable()
	      {
	          @Override
	          public void run()
	          {
	        	  GroupListFragment.this.mListingAdapter = new GroupListAdapter(GroupListFragment.this.getActivity(), true);
	        	  GroupListFragment.this.getListView().setAdapter(GroupListFragment.this.mListingAdapter);
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
    	//DtuBus.main.unregister(this);
        super.onDestroyView();
    }	
}
