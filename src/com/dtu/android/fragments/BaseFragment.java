package com.dtu.android.fragments;

import java.util.List;

import com.dtu.android.R;
import com.dtu.android.activity.base.BaseCustomTitleActivity;
import com.dtu.android.interfaces.LoaderFrameInterface;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

public class BaseFragment extends Fragment
{
    private boolean mWideMode;

    public Object getRequestMarker() {
        return Integer.valueOf(System.identityHashCode(this));
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mWideMode = getResources().getBoolean(R.bool.is_wide_mode);
    }

    public boolean isWideMode() {
        return this.mWideMode;
    }
    
    public BaseCustomTitleActivity getTitleActivity()
    {
    	if (getActivity() instanceof BaseCustomTitleActivity) {
            return (BaseCustomTitleActivity) getActivity();
        }
    	return null;
    }

    public void showLoader(boolean show) {
        if (getActivity() instanceof LoaderFrameInterface) {
            ((LoaderFrameInterface) getActivity()).showLoader(show);
        }
    }

    public void showLoader(boolean show, boolean shielding) {
        if (getActivity() instanceof LoaderFrameInterface) {
            ((LoaderFrameInterface) getActivity()).showLoader(show, shielding);
        }
    }

    public void setLoaderFrameBackground(int color) {
        if (getActivity() instanceof LoaderFrameInterface) {
            ((LoaderFrameInterface) getActivity()).setLoaderFrameBackground(color);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        List<Fragment> fragments = getChildFragmentManager().getFragments();
        if (fragments != null) {
            for (Fragment fragment : fragments) {
                if (fragment != null) {
                    fragment.onActivityResult(requestCode, resultCode, data);
                }
            }
        }
    }
}
