package com.dtu.android.activity.base;

import com.dtu.android.R;
import com.dtu.android.utils.MiscUtils;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.text.TextUtils;
import android.view.MenuItem;


public class BaseActivity extends FragmentActivity
{
    private boolean mWideMode;
    private boolean mFloatingActivity;

    @Override
	protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.mWideMode = getResources().getBoolean(R.bool.is_wide_mode);
        this.mFloatingActivity = MiscUtils.getBooleanFromAttribute(this, 16842839);
        if (!isFloatingActivity()) {
        	overridePendingTransition(R.anim.activity_transition_slide_in_new, R.anim.activity_transition_fade_out_prev);
        }
    }

    protected void onHomeActionPressed() {
        navigateUp();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() != android.R.id.home) {
            return super.onOptionsItemSelected(item);
        }
        onHomeActionPressed();
        return true;
    }

    protected void navigateUp() {
        if (TextUtils.isEmpty(NavUtils.getParentActivityName(this))) {
            finish();
            return;
        }
        Intent upIntent = NavUtils.getParentActivityIntent(this);
        if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
            TaskStackBuilder.create(this).addNextIntentWithParentStack(upIntent).startActivities();
            overridePendingTransition(R.anim.activity_transition_back_to_prev, R.anim.activity_transition_slide_out_new);
            return;
        }
        finish();
    }

    public void finish() {
        super.finish();
        if (!this.mFloatingActivity) {
        	overridePendingTransition(R.anim.activity_transition_back_to_prev, R.anim.activity_transition_slide_out_new);
        }
    }

//    @TargetApi(16)
//    public boolean navigateUpTo(Intent upIntent) {
//        boolean navigateUpTo = super.navigateUpTo(upIntent);
//        if (!this.mFloatingActivity) {
//            overridePendingTransition(R.anim.activity_transition_back_to_prev, R.anim.activity_transition_slide_out_new);
//        }
//        return navigateUpTo;
//    }

    public ActionBar setupActionBar(String title, String subtitle) {
        ActionBar actionBar = enableActionBarHomeAsUpAndShowTitle();
        actionBar.setTitle(title);
        actionBar.setSubtitle(subtitle);
        return actionBar;
    }

    protected void setupTransparentActionBar() {
        setupActionBar(0, new Object[0]);
        //getActionBar().setIcon(ColorizedDrawable.forIdWithColor(R.drawable.ic_app, R.color.white));
    }

    public ActionBar setupActionBar(int title, Object... args) {
        ActionBar actionBar = enableActionBarHomeAsUpAndShowTitle();
        if (title > 0) {
            if (actionBar != null) {
                if (args == null || args.length <= 0) {
                    //actionBar.setTitle(FontManager.wrapActionbarSpan(getString(title)));
                	actionBar.setTitle(getString(title));
                } else {
                    //actionBar.setTitle(FontManager.wrapActionbarSpan(getString(title, args)));
                	actionBar.setTitle(getString(title, args));
                }
            } else if (args == null || args.length <= 0) {
                //setTitle(FontManager.wrapActionbarSpan(getString(title)));
            	setTitle(getString(title));
            } else {
                //setTitle(FontManager.wrapActionbarSpan(getString(title, args)));
            	setTitle(getString(title, args));
            }
        }
        return actionBar;
    }

    private ActionBar enableActionBarHomeAsUpAndShowTitle() {
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(true);
        } else {
//            try {
//                Resources resources = getResources();
//                getWindow().getDecorView().findViewById(resources.getIdentifier("titleDivider", "id", ErfExperimentsRequest.ERF_CLIENT_ANDROID)).setBackgroundColor(resources.getColor(R.color.c_rausch));
//            } catch (Exception e) {
//            }
        }
        return actionBar;
    }

    public void setupFakeActionBar(int layout) {
        ActionBar actionbar = getActionBar();
        actionbar.setCustomView(layout);
        actionbar.setDisplayHomeAsUpEnabled(false);
        actionbar.setDisplayShowCustomEnabled(true);
        actionbar.setDisplayShowTitleEnabled(false);
        actionbar.setDisplayShowHomeEnabled(false);
    }

    public void dismantleFakeActionBar() {
        ActionBar actionbar = getActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setDisplayShowCustomEnabled(false);
        actionbar.setDisplayShowTitleEnabled(true);
        actionbar.setDisplayShowHomeEnabled(true);
    }

    protected final boolean isWideMode() {
        return this.mWideMode;
    }

    public void dispatchActivityResult(int requestCode, int resultCode, Intent data) {
        onActivityResult(requestCode, resultCode, data);
    }

    public boolean hasActionBar() {
        return true;
    }

    public void setSupportProgressBarIndeterminateVisibility(boolean visible) {
    }

    public boolean isFloatingActivity() {
        return this.mFloatingActivity;
    }
}
