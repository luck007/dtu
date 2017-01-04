package com.dtu.android;

import com.dtu.android.manager.PushNoticesManager;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import android.app.Application;
import android.util.Log;
import cn.jpush.android.api.JPushInterface;

public class MyApplication extends Application
{
	private static final String TAG;
	static MyApplication instance;
	
	public static ImageLoader imageLoader = null;
	public static DisplayImageOptions generalImgOptions = null;	
	
	
	static {
        TAG = MyApplication.class.getSimpleName();
    }

    public static MyApplication getInstance() {
        if (instance == null) {
            Log.e(TAG, "oldInstance is null");
        }
        return instance;
    }
	
	//----------------------------------------------
	@Override
	public void onCreate()
	{
		super.onCreate();
		instance = this;
		
		 
	    
		initApp();
	}
	
	//-----------------------------------------------
	private void initApp()
	{
		//universal 鞚措歆� 搿滊崝 齑堦赴頇�
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
			.threadPriority(Thread.NORM_PRIORITY - 2)
			.denyCacheImageMultipleSizesInMemory()
			.discCacheFileNameGenerator(new Md5FileNameGenerator())
			.tasksProcessingOrder(QueueProcessingType.LIFO)
			.writeDebugLogs() // Remove for release app
			.build();
        imageLoader =  ImageLoader.getInstance();
        imageLoader.init(config);
        
        // j-push
     	JPushInterface.setDebugMode(false);
     	JPushInterface.init(this); 
     	PushNoticesManager.getInstance().setContext(this);
     	
        //鞚茧皹鞝侅澑 鞚措歆�搿滊敥鞓奠厴鞝曥潣
        generalImgOptions = new DisplayImageOptions.Builder()
//	  		.showImageOnLoading(R.drawable.loading_img)
//	  		.showImageForEmptyUri(R.drawable.ic_empty)
//	  		.showImageOnFail(R.drawable.ic_error)
	  		.cacheInMemory(true)
	  		.cacheOnDisc(true)
	  		.considerExifParams(true)
	  		//.displayer(new RoundedBitmapDisplayer(20))
	  		.build();
	} 
}
