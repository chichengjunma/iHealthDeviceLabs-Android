package com.ihealth.devices;

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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class AM3 extends Activity implements OnClickListener{

	private static final String TAG = "AM3";
	private MyLog myLog;
	
	private Am3Control am3Control;
	private String mac;
	private int clientId;
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
		default:
			break;
		}
	}
}
