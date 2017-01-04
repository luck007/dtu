package com.dtu.android.activity;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dtu.android.DTUApi;
import com.dtu.android.R;
import com.dtu.android.activity.base.BaseCustomLoaderActivity;
import com.dtu.android.manager.PushNoticesManager;
import com.dtu.android.manager.UserManager;
import com.dtu.android.utils.Const;
import com.dtu.android.utils.HttpCallListener;
import com.dtu.android.utils.HttpGetTask;
import com.dtu.android.utils.MiscUtils;
import com.dtu.android.utils.PreferenceHelper;
import com.dtu.android.utils.ToastFactory;
import com.dtu.android.utils.UpdateConfig;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;
import com.umeng.update.UpdateStatus;

public class LogInActivity extends BaseCustomLoaderActivity{
	private static final String TAG;
	
	// layout variables
	private EditText etPassword;	
    private EditText etUsername;    
    private Button tvRegister;
    
    // logic variables
    private String mUserName;
    private String mPassword;
    private HttpGetTask mTaskLogin;
    
    private TextWatcher mTextWatcher = new TextWatcher() {
    	@Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }
    	@Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }
    	@Override
        public void afterTextChanged(Editable s) {
    		LogInActivity.this.enableSignInIfNecessary();
        }
    };
    
	static {
        TAG = ManagerActivity.class.getSimpleName();
    }
	
	private void onInit(){
		DTUApi dtuApi = DTUApi.getInstance(this);
	}
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ManagerActivity.instance().CurrentActivity = Const.VIEW_LOGIN;
    	this.setTitle(this.getString(R.string.login_title));
        this.disableLeft(View.INVISIBLE);
        this.disableRight(View.INVISIBLE);
        
        // initialize layout variables
        etUsername = (EditText)findViewById(R.id.etUsername);
        etPassword = (EditText)findViewById(R.id.etPassword);
       
        etUsername.addTextChangedListener(this.mTextWatcher);
        etPassword.addTextChangedListener(this.mTextWatcher);
        
        tvRegister = (Button)findViewById(R.id.register);
        tvRegister.setOnClickListener(new OnClickListener() {
	        public void onClick(View v) {
	        	LogInActivity.this.btnLogin(v);
	        }
	    });
	    
        // initialize global variables
        onInit();	
        
        etUsername.setText(PreferenceHelper.getStringValue(Const.PREFS_ACCOUNT, ""));
	    tvRegister.setEnabled(false);
	    
	    checkUpdate();
    }
	
	@Override
	public void onBackPressed() {
		this.finish();
		
		ManagerActivity.instance().releaseApp();
	};
	
	private void btnLogin(View v)
	{
		hideSoftInput();
		
		mUserName = etUsername.getText().toString().trim();
        mPassword = etPassword.getText().toString().trim();
        
		showLoader(true, false);
		this.mTaskLogin = HttpGetTask.newInstance();
    	String accessToken = String.format("pid=%s&Account=%s&Password=%s"
    			, Const.GET_LOGIN
    			, mUserName
    			, mPassword);
    			
    	this.mTaskLogin.doRequest(this, accessToken, new HttpCallListener() {
            @Override
            public void onReceived(String receivedInfo)
            {            	            	
            	if (MiscUtils.isValidResponse(LogInActivity.this, receivedInfo))
            	{            		
            		try
            		{
            			JSONObject jReceived =  new JSONObject(receivedInfo);
            			if (jReceived != null)
            			{
            				String status = jReceived.getString(Const.KEY_RESULT);
            				if (!TextUtils.isEmpty(status) && status.equals(Const.RESULT_STATUS_SUCCESS))
            				{
            					JSONObject jDatas = jReceived.getJSONObject(Const.KEY_DATA);
            					if (jDatas != null)
            					{            						
            				    	LogInActivity.this.onLoginSuccess(jDatas);
            					}            				
            				}
            				else
            				{
            					status = jReceived.getString(Const.KEY_RESULT_MSG);
            					ToastFactory.getInstance().showToast(LogInActivity.this, status);
            				}
            			}
            		}
            		catch(JSONException ex)
            		{
                    	Log.e("dtu", "->" + ex.toString());
                    }
                    catch(Exception ex)
                    {
                    	Log.e("dtu", "->" + ex.toString());
                    }
            	}
            	
            	LogInActivity.this.showLoader(false);
            }
    	});
	}
	
	private void checkUpdate()
    {        
		UmengUpdateAgent.setDefault();
        UmengUpdateAgent.update(this);
     
//		UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {
//			@Override
//			public void onUpdateReturned(int updateStatus,
//					UpdateResponse updateInfo) {
//				UmengUpdateAgent.setDefault();
//				if (updateStatus == UpdateStatus.Yes) {
//					UpdateConfig.mResponse = updateInfo;
//				}
//				if (UpdateConfig.mResponse != null) {
//					File file = UmengUpdateAgent.downloadedFile(
//							LogInActivity.this,
//							UpdateConfig.mResponse);
//					String md5 = new String(
//							UpdateConfig.mResponse.new_md5);
//					UpdateConfig.mResponse.new_md5 = "";
//					UmengUpdateAgent.ignoreUpdate(LogInActivity.this,
//							UpdateConfig.mResponse);
//					UpdateConfig.mResponse.new_md5 = md5;
//					if (file != null) {
//						if (file.delete()) {
//							Toast.makeText(LogInActivity.this, "删除完成",
//									Toast.LENGTH_SHORT).show();
//						} else {
//							Toast.makeText(LogInActivity.this, "删除失败",
//									Toast.LENGTH_SHORT).show();
//						}
//					} else {
//						Toast.makeText(LogInActivity.this, "删除完成",
//								Toast.LENGTH_SHORT).show();
//					}
//				}
//			}
//		});
    }
	
	private void onLoginSuccess(JSONObject jDatas){
		UserManager.getInstance().getCurrentUser().parse(jDatas);
		UserManager.getInstance().setCurrentUserAccountId(mUserName);
		UserManager.getInstance().setCurrentUserPassword(mPassword);
		UserManager.getInstance().setLogined(true);
		PreferenceHelper.setStringValue(Const.PREFS_ACCOUNT, mUserName);		
		
		// JPUSH set alias
		PushNoticesManager.getInstance().setAlias(mUserName);
		
		ToastFactory.getInstance().showToast(this, "登录成功");
		
		ManagerActivity.instance().loginSuccess();		
		this.finish();
	}
	
	private void hideSoftInput() {
        new Timer().schedule(new TimerTask() {
            public void run() {
            	InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            	imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);            
            }
        }, 10);
    }	
	
	private void enableSignInIfNecessary() {
        boolean enableSignInButton = fieldNotEmpty(etUsername) && fieldNotEmpty(etPassword);
        tvRegister.setEnabled(enableSignInButton);
    }
    
    private boolean fieldNotEmpty(TextView field) {
        return !TextUtils.isEmpty(field.getText().toString().trim());
    }    
}
