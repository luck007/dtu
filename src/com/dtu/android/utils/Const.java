package com.dtu.android.utils;

public class Const {
	//public static final String SERVER_IP = "192.168.1.141";
	public static final String SERVER_IP = "121.43.112.116";
	public static final String BASE_URL = "http://" + SERVER_IP + "/Web/";
	public static final String BASE_API_URL = "http://" + SERVER_IP + "/chkDev/api/";
	public static final String BASE_GET_URL = BASE_API_URL + "query.php?";
	public static final String BASE_POST_URL = BASE_API_URL + "post.php";
	
	public static final String GET_LOGIN = "Login";
	public static final String GET_GROUPDATA = "GroupData";
	public static final String GET_DeviceDATA = "EquipData";
	public static final String GET_DeviceValue = "GetData";
	public static final String GET_PUSHDATA = "GetPushData";
	public static final String GET_PUSHLIST = "GetPushList";
	
	public static final String DTU_GLOBAL_PREFS = "dtu_global_prefs";
	public static final String PREFS_ACCOUNT = "prefs_account";
	
	
	public static final String KEY_RESULT = "errCode";
	public static final String KEY_RESULT_MSG = "errMsg";
	public static final String KEY_DATA = "datalist";
	public static final String RESULT_STATUS_SUCCESS = "0";
	
	public static final String VIEW_LOGIN = "login";
	public static final String VIEW_MAIN = "main";
	public static final String VIEW_PUSH = "push";	
	public static final String VIEW_HISTORY = "histo";	
}
