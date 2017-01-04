package com.dtu.android.adapters;

import java.util.ArrayList;
import java.util.List;

import com.dtu.android.R;
import com.dtu.android.enums.RowType;
import com.dtu.android.interfaces.FeedItem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public abstract class BaseRepeatListAdapter extends BaseAdapter
{
	protected Context					mContext;
	
	protected List<FeedItem>			mListItems;
	protected boolean					mRemainingData;
	protected boolean					mIsListBlank;
	protected boolean					mLoadMoreEnabled;
	protected boolean					mContinueLoading;
	
	protected boolean					mHasSticky;
	protected boolean					mHasFootDivider;
	
	
	public BaseRepeatListAdapter(Context context, boolean allowPagination)
	{
		this.mContext = context;
		this.mRemainingData = allowPagination;
		this.mListItems = new ArrayList<FeedItem>();
		this.mIsListBlank = true;
		this.mLoadMoreEnabled = false;
		this.mContinueLoading = true;
		
		this.mHasSticky = false;
		this.mHasFootDivider = true;
	}

    public int getItemViewType(int position)
    {
//        if (hasRemainingData() && position == getCount() - 1)
//        	return RowType.LOADING.ordinal();
//        else
//        	return ((FeedItem) getItem(position)).getType().ordinal();
    	
    	int lastItemPos = getCount() - 1;
    	//bottom가름선
    	if (hasFootDivider() && !hasSticky())
    	{
    		if (position == lastItemPos)
    			return RowType.DIVIDER.ordinal();
    		lastItemPos--;
    	}
    	
    	if (hasRemainingData() && hasSticky())
    	{
    		if (position == lastItemPos)
    			return RowType.STICKY_SPACE.ordinal();
    		else if (position == lastItemPos - 1)
    			return RowType.LOADING.ordinal();
    	}
    	else if (hasSticky())
    	{
    		if (position == lastItemPos)
    			return RowType.STICKY_SPACE.ordinal();
    	}
    	else if (hasRemainingData())
    	{
    		if (position == lastItemPos)
    			return RowType.LOADING.ordinal();
    	}
    	return ((FeedItem) getItem(position)).getType().ordinal();
    }

    protected boolean hasRemainingData()
    {
        return this.mRemainingData;
    }

    public void setRemainingData(boolean remainingData)
    {
        this.mRemainingData = remainingData;
        notifyDataSetChanged();
    }
    
    public void setContinueLoading(boolean continueLoading)
    {
    	this.mContinueLoading = continueLoading;
    }
    
    public boolean hasSticky()
    {
    	return this.mHasSticky;
    }
    
    public void setHasSticky(boolean hasSticky)
    {
    	this.mHasSticky = hasSticky;
    }
    
    public boolean hasFootDivider()
    {
    	return this.mHasFootDivider;
    }
    
    public void setHasFootDivider(boolean hasFootDivider)
    {
    	this.mHasFootDivider = hasFootDivider;
    }

	@Override
	public int getCount()
	{
		//return (hasRemainingData() ? 1 : 0) + this.mListItems.size();
		return this.mListItems.size() + (hasRemainingData() ? 1 : 0) + (hasSticky() ? 1 : 0) + ((hasFootDivider() && !hasSticky()) ? 1 : 0);
	}
	
	public int getRealCount()
	{
		return this.mListItems.size();
	}

    public int getViewTypeCount()
    {
        return RowType.values().length;
    }

	@Override
	public Object getItem(int position)
	{
		if (position < this.mListItems.size())
			return this.mListItems.get(position);
		return null;
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

    public boolean isEnabled(int position)
    {
        //return getItemViewType(position) != RowType.LOADING.ordinal();
    	return getItemViewType(position) != RowType.LOADING.ordinal() 
    			&& getItemViewType(position) != RowType.STICKY_SPACE.ordinal()
    			&& getItemViewType(position) != RowType.DIVIDER.ordinal();
    }

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		Context c = parent.getContext();
		if (getItemViewType(position) == RowType.LOADING.ordinal())
		{
			loadMoreListings();
			convertView = LayoutInflater.from(c).inflate(R.layout.list_item_loading, parent, false);
		}
		else if (getItemViewType(position) == RowType.STICKY_SPACE.ordinal())
		{
			convertView = LayoutInflater.from(c).inflate(R.layout.list_item_bottom_sticky, parent, false);
		}
		else if (getItemViewType(position) == RowType.DIVIDER.ordinal())
		{
			convertView = LayoutInflater.from(c).inflate(R.layout.list_item_divider, parent, false);
		}
		return convertView;
	}

    protected List<FeedItem> getItems()
    {
        return this.mListItems;
    }

    public void enableLoadMore()
    {
        this.mLoadMoreEnabled = true;
    }
    
    protected void loadMoreListings()
    {
    }
    
    public void addFeedItem(int position, FeedItem item)
    {
        this.mListItems.remove(item);
        if (position > this.mListItems.size())
        	position = this.mListItems.size();
        this.mListItems.add(position, item);        
        notifyDataSetChanged();
    }

    public void addFeedItems(List<FeedItem> discoverData, boolean clear) {
    	if (clear)
    	{
        	//this.mListItems.removeAll(discoverData);
        	this.mListItems.clear();
    	}
        this.mListItems.addAll(discoverData);
        notifyDataSetChanged();
    }
    
    public void refresh()
    {
    	this.mListItems.clear();
        this.mLoadMoreEnabled = false;
        this.mIsListBlank = true;
        this.setRemainingData(true);
    }
    
    public void updateFeedItem(FeedItem item)
    {
    	if (item.getId() >= 0)
    	{
    		for (int i = 0; i < this.mListItems.size(); i++)
    		{
    			if (this.mListItems.get(i).getId() == item.getId())
    			{
    	    		this.mListItems.set(i, item);
    	            this.notifyDataSetChanged();
    	            break;
    			}
    		}
    	}
    }
    
    public void updateFeedItem(int position, FeedItem item)
    {
    	if (position < this.mListItems.size())
    	{
    		this.mListItems.set(position, item);
            notifyDataSetChanged();
    	}
    }
    
    public void removeFeedItem(FeedItem item)
    {
    	this.removeFeedItem(item.getId());
    }
    
    public void removeFeedItem(long id)
    {
    	if (id >= 0)
    	{
    		for (int i = 0; i < this.mListItems.size(); i++)
    		{
    			if (this.mListItems.get(i).getId() == id)
    			{
    	    		this.mListItems.remove(i);
    	            this.notifyDataSetChanged();
    	            break;
    			}
    		}
    	}
    }
    
}
