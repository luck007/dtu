package com.dtu.android.views;

import com.dtu.android.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

public class EmptyResults extends LinearLayout {
    private TextView mSubTitle;
    private TextView mTitle;

    public EmptyResults(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.EmptyResults, 0, 0);
        String titleText = a.getString(0);
        String subTitleText = a.getString(1);
        a.recycle();
        setOrientation(0);
        setGravity(16);
        ((LayoutInflater) context.getSystemService("layout_inflater")).inflate(R.layout.empty_results, this, true);
        this.mTitle = (TextView) findViewById(R.id.no_results_title);
        this.mSubTitle = (TextView) findViewById(R.id.no_results_subtitle);
        if (!TextUtils.isEmpty(titleText) || !TextUtils.isEmpty(subTitleText))
        {
        	this.mTitle.setText(titleText);
        	this.mSubTitle.setText(subTitleText);
        }
    }

    public EmptyResults(Context context) {
        this(context, null);
    }

    public void setTitle(String title) {
        this.mTitle.setText(title);
    }

    public void setTitle(int titleRes) {
        this.mTitle.setText(titleRes);
    }

    public void setSubTitle(String subTitle) {
        this.mSubTitle.setText(subTitle);
    }

    public void setSubtitle(int subtitleRes) {
        this.mSubTitle.setText(subtitleRes);
    }
}
