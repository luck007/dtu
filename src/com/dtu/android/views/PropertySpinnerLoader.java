package com.dtu.android.views;

import com.dtu.android.R;
import com.dtu.android.utils.BuildHelper;
import com.dtu.android.utils.MemoryUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class PropertySpinnerLoader extends FrameLayout {
    public static final int FRAMERATE = 36;
    private int index;
    private ImageView ivLoader;
    private Options mOptions;
    private Bitmap mSpinnerImageFrame;
    private int[] resources;
    Runnable runnable;

    public PropertySpinnerLoader(Context context) {
        super(context);
        this.resources = new int[]{R.drawable.loading_00, R.drawable.loading_01, R.drawable.loading_02, R.drawable.loading_03, R.drawable.loading_04, R.drawable.loading_05, R.drawable.loading_06, R.drawable.loading_07, R.drawable.loading_08, R.drawable.loading_09, R.drawable.loading_10, R.drawable.loading_11};
        this.runnable = new Runnable() {
            private boolean inBitmapFailReported;

            {
                this.inBitmapFailReported = false;
            }

            public void run() {
                PropertySpinnerLoader.this.removeCallbacks(this);
                PropertySpinnerLoader.this.postDelayed(this, 27);
                PropertySpinnerLoader.this.index = (PropertySpinnerLoader.this.index + 1) % PropertySpinnerLoader.this.resources.length;
                if (PropertySpinnerLoader.this.mSpinnerImageFrame != null) {
                    PropertySpinnerLoader.this.mOptions.inBitmap = PropertySpinnerLoader.this.mSpinnerImageFrame;
                }
                try {
                    PropertySpinnerLoader.this.mSpinnerImageFrame = BitmapFactory.decodeResource(PropertySpinnerLoader.this.getResources(), PropertySpinnerLoader.this.resources[PropertySpinnerLoader.this.index], PropertySpinnerLoader.this.mOptions);
                } catch (IllegalArgumentException e) {
                    try {
                        if (!this.inBitmapFailReported) {
//                            AirEventLogger.track(Trebuchet.KEY_ANDROID_ENG, Strap.make().kv(SalehouseConstants.IN_BITMAP_FAILED, ServerProtocol.DIALOG_RETURN_SCOPES_TRUE).kv(ReviewsAnalytics.FIELD_ACTION, "property_spinner_loader"));
                            this.inBitmapFailReported = true;
                        }
                        PropertySpinnerLoader.this.mOptions.inBitmap = null;
                        PropertySpinnerLoader.this.mSpinnerImageFrame = BitmapFactory.decodeResource(PropertySpinnerLoader.this.getResources(), PropertySpinnerLoader.this.resources[PropertySpinnerLoader.this.index], PropertySpinnerLoader.this.mOptions);
                    } catch (OutOfMemoryError e2) {
                        MemoryUtils.handleCaughtOOM("loader_frame");
                    }
                }
                PropertySpinnerLoader.this.ivLoader.setImageBitmap(PropertySpinnerLoader.this.mSpinnerImageFrame);
            }
        };
        init(context);
    }

    public PropertySpinnerLoader(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.resources = new int[]{R.drawable.loading_00, R.drawable.loading_01, R.drawable.loading_02, R.drawable.loading_03, R.drawable.loading_04, R.drawable.loading_05, R.drawable.loading_06, R.drawable.loading_07, R.drawable.loading_08, R.drawable.loading_09, R.drawable.loading_10, R.drawable.loading_11};
        this.runnable = new Runnable() {
            private boolean inBitmapFailReported;

            {
                this.inBitmapFailReported = false;
            }

            public void run() {
                PropertySpinnerLoader.this.removeCallbacks(this);
                PropertySpinnerLoader.this.postDelayed(this, 27);
                PropertySpinnerLoader.this.index = (PropertySpinnerLoader.this.index + 1) % PropertySpinnerLoader.this.resources.length;
                if (PropertySpinnerLoader.this.mSpinnerImageFrame != null) {
                    PropertySpinnerLoader.this.mOptions.inBitmap = PropertySpinnerLoader.this.mSpinnerImageFrame;
                }
                try {
                    PropertySpinnerLoader.this.mSpinnerImageFrame = BitmapFactory.decodeResource(PropertySpinnerLoader.this.getResources(), PropertySpinnerLoader.this.resources[PropertySpinnerLoader.this.index], PropertySpinnerLoader.this.mOptions);
                } catch (IllegalArgumentException e) {
                    try {
                        if (!this.inBitmapFailReported) {
//                            AirEventLogger.track(Trebuchet.KEY_ANDROID_ENG, Strap.make().kv(SalehouseConstants.IN_BITMAP_FAILED, ServerProtocol.DIALOG_RETURN_SCOPES_TRUE).kv(ReviewsAnalytics.FIELD_ACTION, "property_spinner_loader"));
                            this.inBitmapFailReported = true;
                        }
                        PropertySpinnerLoader.this.mOptions.inBitmap = null;
                        PropertySpinnerLoader.this.mSpinnerImageFrame = BitmapFactory.decodeResource(PropertySpinnerLoader.this.getResources(), PropertySpinnerLoader.this.resources[PropertySpinnerLoader.this.index], PropertySpinnerLoader.this.mOptions);
                    } catch (OutOfMemoryError e2) {
                        MemoryUtils.handleCaughtOOM("loader_frame");
                    }
                }
                PropertySpinnerLoader.this.ivLoader.setImageBitmap(PropertySpinnerLoader.this.mSpinnerImageFrame);
            }
        };
        init(context);
    }

    public PropertySpinnerLoader(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.resources = new int[]{R.drawable.loading_00, R.drawable.loading_01, R.drawable.loading_02, R.drawable.loading_03, R.drawable.loading_04, R.drawable.loading_05, R.drawable.loading_06, R.drawable.loading_07, R.drawable.loading_08, R.drawable.loading_09, R.drawable.loading_10, R.drawable.loading_11};
        this.runnable = new Runnable() {
            private boolean inBitmapFailReported;

            {
                this.inBitmapFailReported = false;
            }

            public void run() {
                PropertySpinnerLoader.this.removeCallbacks(this);
                PropertySpinnerLoader.this.postDelayed(this, 27);
                PropertySpinnerLoader.this.index = (PropertySpinnerLoader.this.index + 1) % PropertySpinnerLoader.this.resources.length;
                if (PropertySpinnerLoader.this.mSpinnerImageFrame != null) {
                    PropertySpinnerLoader.this.mOptions.inBitmap = PropertySpinnerLoader.this.mSpinnerImageFrame;
                }
                try {
                    PropertySpinnerLoader.this.mSpinnerImageFrame = BitmapFactory.decodeResource(PropertySpinnerLoader.this.getResources(), PropertySpinnerLoader.this.resources[PropertySpinnerLoader.this.index], PropertySpinnerLoader.this.mOptions);
                } catch (IllegalArgumentException e) {
                    try {
                        if (!this.inBitmapFailReported) {
//                            AirEventLogger.track(Trebuchet.KEY_ANDROID_ENG, Strap.make().kv(SalehouseConstants.IN_BITMAP_FAILED, ServerProtocol.DIALOG_RETURN_SCOPES_TRUE).kv(ReviewsAnalytics.FIELD_ACTION, "property_spinner_loader"));
                            this.inBitmapFailReported = true;
                        }
                        PropertySpinnerLoader.this.mOptions.inBitmap = null;
                        PropertySpinnerLoader.this.mSpinnerImageFrame = BitmapFactory.decodeResource(PropertySpinnerLoader.this.getResources(), PropertySpinnerLoader.this.resources[PropertySpinnerLoader.this.index], PropertySpinnerLoader.this.mOptions);
                    } catch (OutOfMemoryError e2) {
                        MemoryUtils.handleCaughtOOM("loader_frame");
                    }
                }
                PropertySpinnerLoader.this.ivLoader.setImageBitmap(PropertySpinnerLoader.this.mSpinnerImageFrame);
            }
        };
        init(context);
    }

    private void init(Context ctx) {
        this.ivLoader = (ImageView) ((LayoutInflater) ctx.getSystemService("layout_inflater")).inflate(R.layout.spinner, this, true).findViewById(R.id.anim_image);
        int padding = (int) TypedValue.applyDimension(1, 15.0f, getResources().getDisplayMetrics());
        setPadding(padding, padding, padding, padding);
        setBackgroundResource(R.drawable.loader_background);
        this.mOptions = new Options();
        this.mOptions.inMutable = true;
        if (!BuildHelper.isAtLeastKitKat()) {
            this.mOptions.inSampleSize = 1;
        }
        this.mOptions.inPreferredConfig = Config.ARGB_8888;
    }

    public void startAnimation() {
        setVisibility(View.VISIBLE);
        removeCallbacks(this.runnable);
        post(this.runnable);
    }

    public void stopAnimation() {
        removeCallbacks(this.runnable);
        setVisibility(View.GONE);
    }

    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopAnimation();
    }
}
