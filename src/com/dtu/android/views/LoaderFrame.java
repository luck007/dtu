package com.dtu.android.views;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

public class LoaderFrame extends FrameLayout {
    public static final long FADE_TIME = 300;
    private boolean mAnimating;
    private PropertySpinnerLoader mLoader;

    public static interface LoaderFrameDisplay {
        void displayLoaderFrame(boolean z);
    }

    public LoaderFrame(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!this.isInEditMode())
        {
	        this.mLoader = new PropertySpinnerLoader(context);
	        LayoutParams params = new LayoutParams(-2, -2);
	        params.gravity = 17;
	        setClickable(true);
	        addView(this.mLoader, params);
        }
    }

    public void finish() {
        this.mAnimating = false;
        if (getVisibility() == 0) {
            animate().alpha(0.0f).setDuration(FADE_TIME).setListener(new AnimatorListener() {
                public void onAnimationStart(Animator animation) {
                }

                public void onAnimationEnd(Animator animation) {
                    if (!LoaderFrame.this.mAnimating) {
                        LoaderFrame.this.setVisibility(View.GONE);
                    }
                }

                public void onAnimationCancel(Animator animation) {
                }

                public void onAnimationRepeat(Animator animation) {
                }
            });
        }
        this.mLoader.stopAnimation();
    }

    public void startAnimation() {
        setVisibility(View.VISIBLE);
        setAlpha(1.0f);
        this.mLoader.startAnimation();
        this.mAnimating = true;
    }

    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        setClickable(visibility == 0);
    }
}
