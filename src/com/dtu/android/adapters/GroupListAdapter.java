package com.dtu.android.adapters;

import com.dtu.android.R;
import com.dtu.android.enums.RowType;
import com.dtu.android.interfaces.FeedItem;
import com.dtu.android.manager.UserManager;
import com.dtu.android.types.GroupNameItem;
import com.dtu.android.utils.Const;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class GroupListAdapter extends BaseAutoReqRepeatListAdapter
{	
	private boolean mAddedOtherGroup;
	
	protected class ViewHolder {
		TextView txt_title;		
	}	

	public GroupListAdapter(Context context, boolean allowPagination)
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
		View convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_group, parent, false);
		ViewHolder holder = new ViewHolder();
		holder.txt_title = (TextView) convertView.findViewById(R.id.txt_notice_title);		
		convertView.setTag(holder);
		return convertView;
	}
	
	protected View setupRow(int position, View convertView, ViewGroup parent)
	{
		FeedItem item = (FeedItem)this.getItem(position);
		if ( item != null && item.getType() == RowType.SYSTEM_NOTICE)
		{
			GroupNameItem noticeItem = (GroupNameItem)item;
			final ViewHolder holder = (ViewHolder) convertView.getTag();
			holder.txt_title.setText(noticeItem.getTitle());			
		}
		return convertView;
	}

	@Override
	protected FeedItem newFeedItem() {
		return new GroupNameItem();
	}

	@Override
	protected String getRequestUrl() {
		return String.format("pid=%s&From=%d&Num=%d&UserId=%d"
    			, Const.GET_GROUPDATA
    			, this.getRealCount()
    			, PAGE_LIST_COUNT
    			, UserManager.getInstance().getCurrentUserId()
    			);
	}

	@Override
	protected void onFinishLoadingList(boolean success)
	{	
		if (!mAddedOtherGroup)
		{
			mAddedOtherGroup = true;
			// add other group
			GroupNameItem newItem = new GroupNameItem();
			newItem.setId(0);
			newItem.setTitle(mContext.getString(R.string.group_default_name));
			addFeedItem(0, newItem);
		}						
			
		//DtuBus.main.post(new FinishLoadGroupListEvent(success));
	}	
}
