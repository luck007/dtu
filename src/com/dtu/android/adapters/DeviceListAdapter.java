package com.dtu.android.adapters;

import com.dtu.android.R;
import com.dtu.android.enums.RowType;
import com.dtu.android.events.DtuBus;
import com.dtu.android.events.FinishLoadGroupListEvent;
import com.dtu.android.interfaces.FeedItem;
import com.dtu.android.manager.UserManager;
import com.dtu.android.types.DeviceNameItem;
import com.dtu.android.types.GroupNameItem;
import com.dtu.android.utils.Const;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class DeviceListAdapter extends BaseAutoReqRepeatListAdapter
{	
	private boolean mAddedOtherGroup;	
	
	protected class ViewHolder {
		TextView txt_title;	
		TextView txt_Date;
		TextView txt_Status;
	}	

	public DeviceListAdapter(Context context, boolean allowPagination)
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
		View convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_groupdetail, parent, false);
		ViewHolder holder = new ViewHolder();
		holder.txt_title = (TextView) convertView.findViewById(R.id.txt_device_title);	
		holder.txt_Date = (TextView) convertView.findViewById(R.id.txt_reg_date);	
		holder.txt_Status = (TextView) convertView.findViewById(R.id.txt_device_status);	
		convertView.setTag(holder);
		return convertView;
	}
	
	protected View setupRow(int position, View convertView, ViewGroup parent)
	{
		FeedItem item = (FeedItem)this.getItem(position);
		if ( item != null && item.getType() == RowType.SYSTEM_NOTICE)
		{
			DeviceNameItem devItem = (DeviceNameItem)item;
			final ViewHolder holder = (ViewHolder) convertView.getTag();
			holder.txt_title.setText(devItem.getTitle());
			holder.txt_Date.setText(devItem.getDate());
			holder.txt_Status.setText(devItem.getStatus());
		}
		return convertView;
	}

	@Override
	protected FeedItem newFeedItem() {
		return new DeviceNameItem();
	}

	@Override
	protected String getRequestUrl() {
		return String.format("pid=%s&From=%d&Num=%d&UserId=%d&GroupId=%d&Type=%d"
    			, Const.GET_DeviceDATA
    			, this.getRealCount()
    			, PAGE_LIST_COUNT
    			, UserManager.getInstance().getCurrentUserId()
    			, UserManager.getInstance().getCurrentGroupId()
    			, UserManager.getInstance().getCurrentType());
	}

	@Override
	protected void onFinishLoadingList(boolean success)
	{		
		DtuBus.main.post(new FinishLoadGroupListEvent(success));
	}
	
	public void setType(int value) {		
		refresh();
	}
}
