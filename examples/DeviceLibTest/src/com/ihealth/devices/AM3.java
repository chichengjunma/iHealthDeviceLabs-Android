package com.ihealth.devices;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.devicelibtest.R;
import com.ihealth.communication.control.Am3Control;
import com.ihealth.communication.control.AmProfile;
import com.ihealth.communication.manager.iHealthDevicesCallback;
import com.ihealth.communication.manager.iHealthDevicesManager;
import com.ihealth.utils.MyLog;

import android.R.integer;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class AM3 extends Activity implements OnClickListener{

	private static final String TAG = "AM3";
	private MyLog myLog;
	
	private Am3Control am3Control;
	private String mac;
	private int clientId;
	private TextView tv_return;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_am3);
		myLog = new MyLog(TAG);
		
		clientId = iHealthDevicesManager.getInstance().registerClientCallback(iHealthDevicesCallback);
		
		iHealthDevicesManager.getInstance().addCallbackFilterForDeviceType(clientId,
                iHealthDevicesManager.TYPE_AM3);
		
		Intent intent = getIntent();
		this.mac = intent.getStringExtra("mac");
		am3Control = iHealthDevicesManager.getInstance().getAm3Control(this.mac);
		//button
		findViewById(R.id.getbattery).setOnClickListener(this);
		findViewById(R.id.getuserid).setOnClickListener(this);
		findViewById(R.id.getalarmnum).setOnClickListener(this);
		findViewById(R.id.setalarm).setOnClickListener(this);
		findViewById(R.id.syncactivity).setOnClickListener(this);
		findViewById(R.id.syncreal).setOnClickListener(this);
		findViewById(R.id.getuserinfo).setOnClickListener(this);
		findViewById(R.id.setuserinfo).setOnClickListener(this);
		findViewById(R.id.getalarminfo).setOnClickListener(this);
		findViewById(R.id.setuserid).setOnClickListener(this);
		findViewById(R.id.reset).setOnClickListener(this);
		findViewById(R.id.disconnect).setOnClickListener(this);
		tv_return = (TextView)findViewById(R.id.tv_return);
	}
	
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		iHealthDevicesManager.getInstance().unRegisterClientCallback(clientId);
	}


	private iHealthDevicesCallback iHealthDevicesCallback = new iHealthDevicesCallback() {

		@Override
		public void onScanDevice(String mac, String deviceType) {}

		@Override
		public void onDeviceConnectionStateChange(String mac, String deviceType, int status) {}

		@Override
		public void onDeviceNotify(String mac, String deviceType, String action, String message) {
			myLog.i(action);
			if (message != null) {
				myLog.i(message);
			}
			switch (action) {
			case AmProfile.ACTION_QUERY_STATE_AM:
				try {
					JSONObject info = new JSONObject(message);
					String battery =info.getString(AmProfile.QUERY_BATTERY_AM);
					Message msg = new Message();
					msg.what = HANDLER_MESSAGE;
					msg.obj = "battery: " + battery;
					myHandler.sendMessage(msg);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				break;

			case AmProfile.ACTION_USERID_AM:
				try {
					JSONObject info = new JSONObject(message);
					String id =info.getString(AmProfile.USERID_AM);
					Message msg = new Message();
					msg.what = HANDLER_MESSAGE;
					msg.obj = "User ID: " + id;
					myHandler.sendMessage(msg);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				break;
			case AmProfile.ACTION_GET_ALARMNUM_AM:
				try {
					JSONObject info = new JSONObject(message);
					String alarm_num =info.getString(AmProfile.GET_ALARMNUM_AM);
					Message msg = new Message();
					msg.what = HANDLER_MESSAGE;
					msg.obj = "Alarm Num: " + alarm_num;
					myHandler.sendMessage(msg);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				break;
			case AmProfile.ACTION_SYNC_STAGE_DATA_AM:
				try {
					JSONObject info = new JSONObject(message);
					String stage_info =info.getString(AmProfile.SYNC_STAGE_DATA_AM);
					Message msg = new Message();
					msg.what = HANDLER_MESSAGE;
					msg.obj = "Stage Data: " + stage_info;
					myHandler.sendMessage(msg);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				break;
			case AmProfile.ACTION_SYNC_ACTIVITY_DATA_AM:
				try {
					JSONObject info = new JSONObject(message);
					String activity_info =info.getString(AmProfile.SYNC_ACTIVITY_DATA_AM);
					Message msg = new Message();
					msg.what = HANDLER_MESSAGE;
					msg.obj = "Activity Data: " + activity_info;
					myHandler.sendMessage(msg);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				break;
			case AmProfile.ACTION_SYNC_REAL_DATA_AM:
				try {
					JSONObject info = new JSONObject(message);
					String real_info =info.getString(AmProfile.SYNC_REAL_STEP_AM);
					Message msg = new Message();
					msg.what = HANDLER_MESSAGE;
					msg.obj = "Real Step: " + real_info;
					myHandler.sendMessage(msg);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				break;
			case AmProfile.ACTION_GET_USERINFO_AM:
				try {
					JSONObject info = new JSONObject(message);
					String user_info =info.getString(AmProfile.GET_USER_AGE_AM);
					Message msg = new Message();
					msg.what = HANDLER_MESSAGE;
					msg.obj = "User Age: " + user_info;
					myHandler.sendMessage(msg);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				break;
			case AmProfile.ACTION_GET_ALARMINFO_AM:
				try {
					JSONObject info = new JSONObject(message);
					String alarm_id =info.getString(AmProfile.GET_ALARM_ID_AM);
					Message msg = new Message();
					msg.what = HANDLER_MESSAGE;
					msg.obj = "Alarm ID: " + alarm_id;
					myHandler.sendMessage(msg);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				break;
			case AmProfile.ACTION_SET_USERID_AM:
				Message msg = new Message();
				msg.what = HANDLER_MESSAGE;
				msg.obj = "Set ID success";
				myHandler.sendMessage(msg);
				break;
			case AmProfile.ACTION_SET_ALARMINFO_AM:
				Message msg1 = new Message();
				msg1.what = HANDLER_MESSAGE;
				msg1.obj = "Set Alarm success";
				myHandler.sendMessage(msg1);
				break;
			case AmProfile.ACTION_GET_RANDOM_AM:
				try {
					JSONObject info = new JSONObject(message);
					String random =info.getString(AmProfile.GET_RANDOM_AM);
					Message msg2 = new Message();
					msg2.what = HANDLER_MESSAGE;
					msg2.obj = "Random: " + random;
					myHandler.sendMessage(msg2);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				break;
			case AmProfile.ACTION_SET_USERINFO_AM:
				Message msg3 = new Message();
				msg3.what = HANDLER_MESSAGE;
				msg3.obj = "Set User Info success";
				myHandler.sendMessage(msg3);
				break;
			case AmProfile.ACTION_RESET_AM:
				Message msg4 = new Message();
				msg4.what = HANDLER_MESSAGE;
				msg4.obj = "Reset success";
				myHandler.sendMessage(msg4);
				break;
			default:
				break;
			}
		}
	};
	
	@Override
	public void onClick(View arg0) {
		int id = arg0.getId();
		switch (id) {
		case R.id.getbattery:
			if (am3Control != null) {
				am3Control.queryAMState();
			}else
				Toast.makeText(this, "am3Control == null", Toast.LENGTH_LONG).show();
			break;
		case R.id.getuserid:
			if (am3Control != null) {
				am3Control.getUserId();
			}else
				Toast.makeText(this, "am3Control == null", Toast.LENGTH_LONG).show();
			break;
		case R.id.getalarmnum:
			if (am3Control != null) {
				am3Control.getAlarmClockNum();
			}else
				Toast.makeText(this, "am3Control == null", Toast.LENGTH_LONG).show();
			break;
		case R.id.setalarm:
			if (am3Control != null) {
				am3Control.setAlarmClock(1, 10, 20, AmProfile.AM_SWITCH_REPEAT, new int[]{1,1,1,1,1,1,1}, AmProfile.AM_SWITCH_OPEN);
			}else
				Toast.makeText(this, "am3Control == null", Toast.LENGTH_LONG).show();
			break;
		case R.id.syncactivity:
			if (am3Control != null) {
				am3Control.syncActivityData();
			}else
				Toast.makeText(this, "am3Control == null", Toast.LENGTH_LONG).show();
			break;
		case R.id.syncreal:
			if (am3Control != null) {
				am3Control.syncRealData();
			}else
				Toast.makeText(this, "am3Control == null", Toast.LENGTH_LONG).show();
			break;
		case R.id.getuserinfo:
			if (am3Control != null) {
				am3Control.getUserInfo();
			}else
				Toast.makeText(this, "am3Control == null", Toast.LENGTH_LONG).show();
			break;
		case R.id.setuserinfo:
			if (am3Control != null) {
				am3Control.setUserInfo(20, 180, (float) 60.5, AmProfile.AM_SET_MALE, AmProfile.AM_SET_UNIT_METRIC, 200, 0);
			}else
				Toast.makeText(this, "am3Control == null", Toast.LENGTH_LONG).show();
			break;
		case R.id.getalarminfo:
			if (am3Control != null) {
				am3Control.getActivityRemind();
			}else
				Toast.makeText(this, "am3Control == null", Toast.LENGTH_LONG).show();
			break;
		case R.id.setuserid:
			if (am3Control != null) {
				am3Control.setUserId(1);
			}else
				Toast.makeText(this, "am3Control == null", Toast.LENGTH_LONG).show();
			break;
		case R.id.reset:
			if (am3Control != null) {
				am3Control.reset(1);
			}else
				Toast.makeText(this, "am3Control == null", Toast.LENGTH_LONG).show();
			break;
		case R.id.disconnect:
			if (am3Control != null) {
				am3Control.disconnect();
			}else
				Toast.makeText(this, "am3Control == null", Toast.LENGTH_LONG).show();
			break;
		default:
			break;
		}
	}
	
	private static final int HANDLER_MESSAGE = 101;
	Handler myHandler = new Handler() {  
        public void handleMessage(Message msg) {   
             switch (msg.what) {   
                  case HANDLER_MESSAGE:   
                       tv_return.setText((String)msg.obj);  
                       break;   
             }   
             super.handleMessage(msg);   
        }   
   };
}
