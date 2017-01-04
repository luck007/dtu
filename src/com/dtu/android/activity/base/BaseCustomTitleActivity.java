package com.dtu.android.activity.base;

import com.dtu.android.R;
import com.dtu.android.utils.AndroidUtils;

import android.app.ActionBar;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.jpush.android.api.JPushInterface;


public class BaseCustomTitleActivity extends BaseActivity
{
	private View headerRoot;
	
	private LinearLayout llLeft;
	private View left;
	private ImageView leftImage;
	private OnClickListener leftClickListener;
	
	private LinearLayout llCenter;
	private TextView tvTitle;
	
	private LinearLayout llRight;
	private View right;
	private TextView rightText;
	private ImageView rightImage;
	private OnClickListener rightClickListener;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        
        setupFakeActionBar(R.layout.actbar_centered_title);
        initView();
	}
	
	public void setupFakeActionBar(int layout) {
        super.setupFakeActionBar(layout);
        
        ActionBar actionbar = getActionBar();
        this.headerRoot = actionbar.getCustomView().findViewById(R.id.title_tag);
    }
	
	private void initView()
	{
		this.llLeft = (LinearLayout) findViewById(R.id.llLeft);
		this.left = findViewById(R.id.left);
		this.left.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
            	if (BaseCustomTitleActivity.this.leftClickListener != null)
            		BaseCustomTitleActivity.this.leftClickListener.onClick(v);
        		else
        			BaseCustomTitleActivity.this.onClickActionBarLeft(v);
            }
        });
		this.leftImage = (ImageView)findViewById(R.id.left_image);
        
        this.llCenter = (LinearLayout) findViewById(R.id.llCenter);
        this.tvTitle = (TextView) findViewById(R.id.tvTitle);
        
        this.llRight = (LinearLayout) findViewById(R.id.llRight);
        this.right = findViewById(R.id.right);
        this.right.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
            	if (BaseCustomTitleActivity.this.rightClickListener != null)
            		BaseCustomTitleActivity.this.rightClickListener.onClick(v);
        		else
        			BaseCustomTitleActivity.this.onClickActionBarRight(v);
            }
        });
        this.rightImage = (ImageView)findViewById(R.id.right_image);
        this.rightText = (TextView) findViewById(R.id.right_text);
	}

	public void onClickActionBarLeft(View v) {
		finish();
	 }

	public void onClickActionBarRight(View v) {	     
	 }
	
	public void setLeftButton(int imageId) {
		if (imageId > 0)
		{
			this.leftImage.setVisibility(View.VISIBLE);
			this.leftImage.setImageResource(imageId);
		}
		else
			this.leftImage.setVisibility(View.GONE);
    }
	
	public void setLeftButtonClickListener(OnClickListener listener)
	{
		this.leftClickListener = listener;
	}
	
	public void setRightText(CharSequence text)
	{
		if (text != null)
		{
			this.rightText.setVisibility(View.VISIBLE);
			this.rightText.setText(text);
		}
		else
			this.rightText.setVisibility(View.GONE);
	}
	
	public void setRightText(int textId)
	{
		this.setRightText(this.getString(textId));
	}
	
	public void setRightImage(int imageId)
	{
		if (imageId > 0)
		{
			this.rightImage.setVisibility(View.VISIBLE);
			this.rightImage.setImageResource(imageId);
		}
		else
			this.rightImage.setVisibility(View.GONE);
	}

	public void setRightButton(CharSequence text, int imageId) {
		this.setRightText(text);
		this.setRightImage(imageId);
    }
	
	public void setRightButtonClickListener(OnClickListener listener)
	{
		this.rightClickListener = listener;
	}

    public void setTitle(CharSequence title) {
    	this.tvTitle.setText(title);
    }

    public void setTitle(int titleId) {
    	this.tvTitle.setText(this.getString(titleId));
    }
    
    public void disableLeft(int viewType)
    {
    	this.left.setVisibility(viewType);
    }
    
    public void disableRight(int viewType)
    {
    	this.right.setVisibility(viewType);    	
    }
    
	@Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(this);
    }

	@Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(this);
    }    

    protected void postSafety(Runnable runnable) {
        AndroidUtils.postSafety(new Handler(), runnable);
    }
    
    protected void toShowToast(int stringResId)
    {
    	this.toShowToast(this.getString(stringResId));
    }
    
    protected void toShowToast(final String paramString)
    {
      postSafety(new Runnable()
      {
    	  public void run()
    	  {
    		  AndroidUtils.showMsg(BaseCustomTitleActivity.this, paramString);
    	  }
      });
    }
}
