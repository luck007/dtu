package com.dtu.android.fragments;

import com.dtu.android.R;
import com.dtu.android.utils.MiscUtils;
import com.dtu.android.views.EmptyResults;
import com.dtu.android.views.StickyButton;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListView;

public class BaseListWithStickyFragment extends BaseFragment
{
	//protected LinearListView mListView;
	protected ListView mListView;
	protected StickyButton mStickyButton;
	//protected View mViewStickySpacer;
	protected EmptyResults mEmptyResult;
	
	protected int getLayout()
	{
		return R.layout.fragment_device_list;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		//View view = super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(this.getLayout(), null);
        
        //this.mListView = (LinearListView) view.findViewById(R.id.loader_list);
        this.mListView = (ListView) view.findViewById(R.id.loader_list);
        this.mStickyButton = (StickyButton) view.findViewById(R.id.sticky_button);
        //this.mViewStickySpacer = view.findViewById(R.id.sticky_button_spacer);
        this.mEmptyResult = (EmptyResults) view.findViewById(R.id.empty_results);
        this.showEmptyResults(false);

        if (this.mStickyButton != null)
        {
			this.mStickyButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					BaseListWithStickyFragment.this.onClickStickyButton(view);
				}
			});
        }
		
    	return view;
	}
	
	//protected LinearListView getListView()
	protected ListView getListView()
	{
		return this.mListView;
	}
	
	protected void setStickyButtonTitle(int titleId)
	{
		if (this.mStickyButton != null)
			this.mStickyButton.setTitle(titleId);
	}
	
	protected void onClickStickyButton(View view) {
	}
	
	public void showStickyButton(boolean visible)
	{
//		if (this.mViewStickySpacer != null)
//			this.mViewStickySpacer.setVisibility(MiscUtils.convertBoolToVisible(visible));
		if (this.mStickyButton != null)
			this.mStickyButton.setVisibility(MiscUtils.convertBoolToVisible(visible));
	}
	
	protected void setEmptyResultText(int titleId, int subTitleId)
	{
		if (this.mEmptyResult != null)
		{
			this.mEmptyResult.setTitle(titleId);
			this.mEmptyResult.setSubtitle(subTitleId);
		}
	}
	
	public void showEmptyResults(boolean visible)
	{
		if (this.mEmptyResult != null)
			this.mEmptyResult.setVisibility(MiscUtils.convertBoolToVisible(visible));
	}
	
}
