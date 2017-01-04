package com.dtu.android.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dtu.android.R;
import com.dtu.android.enums.RowType;
import com.dtu.android.events.DtuBus;
import com.dtu.android.events.FinishLoadGroupListEvent;
import com.dtu.android.interfaces.FeedItem;
import com.dtu.android.manager.UserManager;
import com.dtu.android.types.DeviceNameItem;
import com.dtu.android.types.PushHistoryItem;
import com.dtu.android.utils.Const;

public class ErrorListAdapter extends BaseAutoReqRepeatListAdapter
{	
	private boolean mAddedOtherGroup;
	
	protected class ViewHolder {
		TextView txt_title;	
		TextView txt_Date;
		
	}	

	public ErrorListAdapter(Context context, boolean allowPagination)
	{
		super(context, allowPagination);
		
		mAddedOtherGroup = false;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		if ( this.getItemViewType(position) == RowType.SYSTEM_NOTICE.ordinal() )
		{
			//loading행이면 ViewHolder가 null
			boolean bNewCreate = (convertView == null || ((ViewHolder)convertView.getTag() == null));
			if (bNewCreate)
				convertView = createRow(position, parent);
			convertView = setupRow(position, convertView, parent);
		}
		else
			convertView = super.getView(position, convertView, parent);
		return convertView;
	}
	
	protected View createRow(int position, ViewGroup parent)
	{
		View convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pushlist, parent, false);
		ViewHolder holder = new ViewHolder();
		holder.txt_title = (TextView) convertView.findViewById(R.id.txt_device_title);	
		holder.txt_Date = (TextView) convertView.findViewById(R.id.txt_reg_date);	
		
		convertView.setTag(holder);
		return convertView;
	}
	
	protected View setupRow(int position, View convertView, ViewGroup parent)
	{
		FeedItem item = (FeedItem)this.getItem(position);
		if ( item != null && item.getType() == RowType.SYSTEM_NOTICE)
		{
			PushHistoryItem devItem = (PushHistoryItem)item;
			final ViewHolder holder = (ViewHolder) convertView.getTag();
			holder.txt_title.setText(devItem.getTitle());
			holder.txt_Date.setText(devItem.getRegDate());			
		}
		return convertView;
	}

	@Override
	protected FeedItem newFeedItem() {
		return new PushHistoryItem();
	}

	@Override
	protected String getRequestUrl() {
		return String.format("pid=%s&From=%d&Num=%d&UserId=%d"
    			, Const.GET_PUSHLIST
    			, this.getRealCount()
    			, PAGE_LIST_COUNT
    			, UserManager.getInstance().getCurrentUserId());
	}

	@Override
	protected void onFinishLoadingList(boolean success)
	{		
		DtuBus.main.post(new FinishLoadGroupListEvent(success));
	}
}
