package com.dtu.android.activity.base;

import com.dtu.android.R;
import com.dtu.android.interfaces.LoaderFrameInterface;
import com.dtu.android.views.LoaderFrame;

public class BaseLoaderActivity extends BaseActivity implements LoaderFrameInterface
{
	private LoaderFrame mLoaderFrame;
	

    protected LoaderFrame getLoaderFrame() {
    	if  (this.mLoaderFrame == null)
    		this.mLoaderFrame = (LoaderFrame) this.findViewById(R.id.loader_frame);
        return this.mLoaderFrame;
    }

	@Override
	public void setLoaderFrameBackground(int color) {
		this.getLoaderFrame().setBackgroundColor(getResources().getColor(color));
	}

	@Override
	public void showLoader(boolean show) {

        LoaderFrame loaderFrame = getLoaderFrame();
        if (show) {
            loaderFrame.startAnimation();
        } else {
            loaderFrame.finish();
        }
	}
	
	@Override
	public void showLoader(boolean show, boolean shielding) {
		if (show)
		{
			if (shielding)
		    	this.setLoaderFrameBackground(R.color.white);
			else
		    	this.setLoaderFrameBackground(R.color.loader_overlay_background);
		}
		this.showLoader(show);
	}
}
