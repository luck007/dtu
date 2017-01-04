package com.dtu.android.views;

import com.dtu.android.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

public class StickyButton extends FrameLayout {
    protected LinearLayout mRootView;
    private TextView mSubtitle;
    private TextView mTitle;

    public StickyButton(Context context) {
        super(context);
        setupViews(null);
    }

    public StickyButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupViews(attrs);
    }

    public StickyButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setupViews(attrs);
    }

    private void setupViews(AttributeSet attrs) {
        LayoutInflater.from(getContext()).inflate(R.layout.sticky_button, this);
        this.mRootView = (LinearLayout) findViewById(R.id.sticky_btn_root);
        this.mTitle = (TextView) this.mRootView.findViewById(R.id.title);
        this.mSubtitle = (TextView) this.mRootView.findViewById(R.id.subtitle);
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.StickyButton, 0, 0);
            String titleText = a.getString(R.styleable.StickyButton_stickyButtonTitle);
            int backgroundDrawbleId = a.getResourceId(R.styleable.StickyButton_stickyBackgroundDrawble, 0);
            a.recycle();
            if (!TextUtils.isEmpty(titleText))
            	this.mTitle.setText(titleText);
            if (backgroundDrawbleId > 0)
            	this.mRootView.setBackgroundResource(backgroundDrawbleId);
        }
    }

    public void setTitle(String title) {
        this.mTitle.setText(title);
    }

    public TextView getTitle() {
        return this.mTitle;
    }

    public void setTitle(int titleRes) {
        this.mTitle.setText(titleRes);
    }

    public void setSubtitle(String title) {
        this.mSubtitle.setText(title);
        enableSubtitle();
    }

    public void setSubtitle(int titleRes) {
        this.mSubtitle.setText(titleRes);
        enableSubtitle();
    }

    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        this.mRootView.setEnabled(enabled);
        this.mTitle.setEnabled(enabled);
        this.mSubtitle.setEnabled(enabled);
    }

    public void setRoundedCorners(boolean roundCorners) {
        this.mRootView.setBackgroundResource(roundCorners ? R.drawable.canonical_blue_button_selector : R.drawable.canonical_rausch_button_selector_no_corners);
    }

    public void setCustomBackgroundColorResource(int colorResId) {
        addView(LayoutInflater.from(getContext()).inflate(R.layout.sticky_press_overlay, this, false));
        this.mRootView.setBackgroundColor(getResources().getColor(colorResId));
    }

    private void enableSubtitle() {
        LayoutParams params = (LayoutParams) this.mTitle.getLayoutParams();
        params.gravity |= 80;
        this.mSubtitle.setVisibility(View.VISIBLE);
        int heightWithSubtitle = (int) getResources().getDimension(R.dimen.ro_bottom_btn_height);
        this.mRootView.getLayoutParams().height = heightWithSubtitle;
        getLayoutParams().height = heightWithSubtitle;
    }

    public int getRootViewId() {
        return this.mRootView.getId();
    }

    public int getButtonHeight() {
        return this.mRootView.getLayoutParams().height;
    }
}
