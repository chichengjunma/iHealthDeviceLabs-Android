package com.ihealth.devices;

import com.example.devicelibtest.R;
import com.ihealth.communication.control.Am3sControl;
import com.ihealth.communication.control.Am4Control;
import com.ihealth.communication.control.AmProfile;
import com.ihealth.communication.manager.iHealthDevicesCallback;
import com.ihealth.communication.manager.iHealthDevicesManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class AM4 extends Activity implements OnClickListener{

	private static final String TAG = "AM4Activity";
	
	private Am4Control am4Control;
	private String deviceMac;
	private int clientId;
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
}
