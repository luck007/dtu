package com.dtu.android.adapters;

import com.dtu.android.R;
import com.dtu.android.enums.RowType;
import com.dtu.android.events.DtuBus;
import com.dtu.android.events.FinishLoadDeviceValueEvent;
import com.dtu.android.interfaces.FeedItem;
import com.dtu.android.manager.UserManager;
import com.dtu.android.types.DeviceValueItem;
import com.dtu.android.utils.Const;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class DeviceValueListAdapter extends BaseAutoReqRepeatListAdapter
{	
	private boolean mAddedOtherGroup;
	
	protected class ViewHolder {
		View v_bg;
		TextView txt_Date;
		TextView txt_Potential;
		TextView txt_Current;
		TextView txt_Voltage;
	}	

	public DeviceValueListAdapter(Context context, boolean allowPagination)
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
		View convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_device_value, parent, false);
		ViewHolder holder = new ViewHolder();			
		holder.txt_Date = (TextView) convertView.findViewById(R.id.txt_histo_regdate);	
		holder.txt_Potential = (TextView) convertView.findViewById(R.id.txt_histo_potential);
		holder.txt_Current = (TextView) convertView.findViewById(R.id.txt_histo_current);
		holder.txt_Voltage = (TextView) convertView.findViewById(R.id.txt_histo_voltage);
		holder.v_bg = convertView.findViewById(R.id.histo_group);
		convertView.setTag(holder);
		return convertView;
	}
	
	protected View setupRow(int position, View convertView, ViewGroup parent)
	{
		FeedItem item = (FeedItem)this.getItem(position);
		if ( item != null && item.getType() == RowType.SYSTEM_NOTICE)
		{
			DeviceValueItem devItem = (DeviceValueItem)item;
			final ViewHolder holder = (ViewHolder) convertView.getTag();			
			holder.txt_Date.setText(devItem.getDate());
			holder.txt_Potential.setText(devItem.getPotential());
			holder.txt_Current.setText(devItem.getCurrent());
			holder.txt_Voltage.setText(devItem.getVoltage());
			
			// change the text color	
			if (devItem.NoExpected(0)){
				holder.txt_Potential.setTextColor(Color.RED);
			}
			else
				holder.txt_Potential.setTextColor(Color.BLACK);
			
			if (devItem.NoExpected(1)){
				holder.txt_Current.setTextColor(Color.RED);
			}
			else
				holder.txt_Current.setTextColor(Color.BLACK);
			
			if (devItem.NoExpected(2)){
				holder.txt_Voltage.setTextColor(Color.RED);
			}
			else
				holder.txt_Voltage.setTextColor(Color.BLACK);
			
			// change background color
			if (devItem.isStopFlag() == 1)
				holder.v_bg.setBackgroundColor(Color.YELLOW);
			else
				holder.v_bg.setBackgroundColor(Color.WHITE);
		}
		return convertView;
	}

	@Override
	protected FeedItem newFeedItem() {
		return new DeviceValueItem();
	}

	@Override
	protected String getRequestUrl() {
		return String.format("pid=%s&From=%d&Num=%d&UserId=%d&EquipId=%d"
    			, Const.GET_DeviceValue
    			, this.getRealCount()
    			, PAGE_LIST_COUNT
    			, UserManager.getInstance().getCurrentUserId()
    			, UserManager.getInstance().getCurrentDeviceId());
	}

	@Override
	protected void onFinishLoadingList(boolean success)
	{		
		DtuBus.main.post(new FinishLoadDeviceValueEvent(success));
	}
}
