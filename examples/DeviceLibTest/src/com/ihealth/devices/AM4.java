package com.ihealth.devices;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.devicelibtest.R;
import com.ihealth.communication.control.Am3sControl;
import com.ihealth.communication.control.Am4Control;
import com.ihealth.communication.control.AmProfile;
import com.ihealth.communication.manager.iHealthDevicesCallback;
import com.ihealth.communication.manager.iHealthDevicesManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class AM4 extends Activity implements OnClickListener{

	private static final String TAG = "AM4Activity";
	
	private Am4Control am4Control;
	private String deviceMac;
	private int clientId;
	private TextView tv_return;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_am4);
		Intent intent = getIntent();
		this.deviceMac = intent.getStringExtra("mac");
		
		clientId = iHealthDevicesManager.getInstance().registerClientCallback(iHealthDevicesCallback);
		
		iHealthDevicesManager.getInstance().addCallbackFilterForDeviceType(clientId,
                iHealthDevicesManager.TYPE_AM4);
		
		am4Control = iHealthDevicesManager.getInstance().getAm4Control(deviceMac);

		findViewById(R.id.btn_SetUserMessage).setOnClickListener(this);
		findViewById(R.id.btn_SetUserID).setOnClickListener(this);
		findViewById(R.id.btn_checkSwimPara).setOnClickListener(this);
		findViewById(R.id.btn_SetSwimPara).setOnClickListener(this);
		findViewById(R.id.btn_GetBattery).setOnClickListener(this);
		findViewById(R.id.btn_SyncStage).setOnClickListener(this);
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
			Log.i(TAG, "Action-----> "+action);
			if (message != null) {
				Log.i(TAG, "message-----> "+message);
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
			case AmProfile.ACTION_SET_SWIMINFO_AM:
				Message msg5 = new Message();
				msg5.what = HANDLER_MESSAGE;
				msg5.obj = "Set Swim Para success";
				myHandler.sendMessage(msg5);
				break;
			case AmProfile.ACTION_GET_SWIMINFO_AM:
				try {
					JSONObject info = new JSONObject(message);
					String length =info.getString(AmProfile.GET_SWIMLANE_LENGTH_AM);
					Message msg6 = new Message();
					msg6.what = HANDLER_MESSAGE;
					msg6.obj = "泳池长度: " + length;
					myHandler.sendMessage(msg6);
				} catch (JSONException e) {
					e.printStackTrace();
				}
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
		case R.id.btn_SetUserMessage:
			if (am4Control != null) 
				am4Control.setUserInfo(29, 177, 65, 1, 1, 10000, 1);
			else
				Toast.makeText(AM4.this, "am4Control == null", Toast.LENGTH_LONG).show();
			break;
			
		case R.id.btn_SetUserID:
			if (am4Control != null) 
				am4Control.setUserId(1);
			else
				Toast.makeText(AM4.this, "am4Control == null", Toast.LENGTH_LONG).show();
			break;
			
		case R.id.btn_checkSwimPara:
			if (am4Control != null) 
				am4Control.checkSwimPara();
			else
				Toast.makeText(AM4.this, "am4Control == null", Toast.LENGTH_LONG).show();
			break;
			
		case R.id.btn_SetSwimPara:
			if (am4Control != null) 
				am4Control.setSwimPara(AmProfile.AM_SWITCH_OPEN, (byte) 10, (byte) 10, (byte) 10, (byte) AmProfile.AM_SET_UNIT_METRIC);
			else
				Toast.makeText(AM4.this, "am4Control == null", Toast.LENGTH_LONG).show();
			break;
			
		case R.id.btn_GetBattery:
			if (am4Control != null) 
				am4Control.queryAMState();
			else
				Toast.makeText(AM4.this, "am4Control == null", Toast.LENGTH_LONG).show();
			break;
			
		case R.id.btn_SyncStage:
			if (am4Control != null) 
				am4Control.syncStageReprotData();
			else
				Toast.makeText(AM4.this, "am4Control == null", Toast.LENGTH_LONG).show();
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
