package com.ihealth.devices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.example.devicelibtest.R;
import com.ihealth.communication.control.Bp5Control;
import com.ihealth.communication.control.Bp7Control;
import com.ihealth.communication.control.BpProfile;
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

/**
 * Activity for testing Bp7 device. 
 */
public class BP7 extends Activity implements OnClickListener{

	private static final String TAG = "Bp5Demo";
	private Bp7Control bp7Control;
	private String deviceMac;
	private int clientCallbackId;
	private TextView tv_return;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bp7);
		Intent intent = getIntent();
		deviceMac = intent.getStringExtra("mac"); 
		findViewById(R.id.btn_getbattery).setOnClickListener(this);
		findViewById(R.id.btn_isOfflineMeasure).setOnClickListener(this);
		findViewById(R.id.btn_enableOfflineMeasure).setOnClickListener(this);
		findViewById(R.id.btn_disableOfflineMeasure).setOnClickListener(this);
		findViewById(R.id.btn_startMeasure).setOnClickListener(this);
		findViewById(R.id.btn_stopMeasure).setOnClickListener(this);
		findViewById(R.id.btn_getangle).setOnClickListener(this);
		findViewById(R.id.btn_disconnect).setOnClickListener(this);
		tv_return = (TextView)findViewById(R.id.tv_return);
		
		clientCallbackId = iHealthDevicesManager.getInstance().registerClientCallback(iHealthDevicesCallback);
		/* Limited wants to receive notification specified device */
		iHealthDevicesManager.getInstance().addCallbackFilterForDeviceType(clientCallbackId, iHealthDevicesManager.TYPE_BP7);
		/* Get bp7 controller */
		bp7Control = iHealthDevicesManager.getInstance().getBp7Control(deviceMac);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		iHealthDevicesManager.getInstance().unRegisterClientCallback(clientCallbackId);
	}

	private iHealthDevicesCallback iHealthDevicesCallback = new iHealthDevicesCallback() {

		@Override
		public void onScanDevice(String mac, String deviceType) {
			Log.i(TAG, "mac: " + mac);
			Log.i(TAG, "deviceType: " + deviceType);
		}

		@Override
		public void onDeviceConnectionStateChange(String mac,
				String deviceType, int status) {
			Log.i(TAG, "mac: " + mac);
			Log.i(TAG, "deviceType: " + deviceType);
			Log.i(TAG, "status: " + status);
		}

		@Override
		public void onUserStatus(String username, int userStatus) {
			Log.i(TAG, "username: " + username);
			Log.i(TAG, "userState: " + userStatus);
		}

		@Override
		public void onDeviceNotify(String mac, String deviceType,
				String action, String message) {
			Log.i(TAG, "mac: " + mac);
			Log.i(TAG, "deviceType: " + deviceType);
			Log.i(TAG, "action: " + action);
			Log.i(TAG, "message: " + message);
			
			if(BpProfile.ACTION_BATTERY_BP.equals(action)){
				try {
					JSONObject info = new JSONObject(message);
					String battery =info.getString(BpProfile.BATTERY_BP);
					Message msg = new Message();
					msg.what = HANDLER_MESSAGE;
					msg.obj = "battery: " + battery;
					myHandler.sendMessage(msg);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				
				
			}else if(BpProfile.ACTION_DISENABLE_OFFLINE_BP.equals(action)){
				Log.i(TAG, "disable operation is success");
				
			}else if(BpProfile.ACTION_ENABLE_OFFLINE_BP.equals(action)){
				Log.i(TAG, "enable operation is success");
				
			}else if(BpProfile.ACTION_ERROR_BP.equals(action)){
				try {
					JSONObject info = new JSONObject(message);
					String num =info.getString(BpProfile.ERROR_NUM_BP);
					Message msg = new Message();
					msg.what = HANDLER_MESSAGE;
					msg.obj = "error num: " + num;
					myHandler.sendMessage(msg);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				
			}else if(BpProfile.ACTION_HISTORICAL_DATA_BP.equals(action)){
				String str = "";
				try {
					JSONObject info = new JSONObject(message);
					if (info.has(BpProfile.HISTORICAL_DATA_BP)) {
			            JSONArray array = info.getJSONArray(BpProfile.HISTORICAL_DATA_BP);
			            for (int i = 0; i < array.length(); i++) {
			            	JSONObject obj = array.getJSONObject(i);
			            	String date          = obj.getString(BpProfile.MEASUREMENT_DATE_BP);
			            	String hightPressure = obj.getString(BpProfile.HIGH_BLOOD_PRESSURE_BP);
			            	String lowPressure   = obj.getString(BpProfile.LOW_BLOOD_PRESSURE_BP);
			            	String pulseWave     = obj.getString(BpProfile.PULSEWAVE_BP);
			            	String ahr           = obj.getString(BpProfile.MEASUREMENT_AHR_BP);
			            	String hsd           = obj.getString(BpProfile.MEASUREMENT_HSD_BP);
			            	str = "date:" + date
			            			+ "hightPressure:" + hightPressure + "\n"
			            			+ "lowPressure:" + lowPressure + "\n"
			            			+ "pulseWave" + pulseWave + "\n"
			            			+ "ahr:" + ahr + "\n"
			            			+ "hsd:" + hsd + "\n";
			            }
			        }
					Message msg = new Message();
					msg.what = HANDLER_MESSAGE;
					msg.obj =  str;
					myHandler.sendMessage(msg);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				
			}else if(BpProfile.ACTION_HISTORICAL_NUM_BP.equals(action)){
				try {
					JSONObject info = new JSONObject(message);
					String num = info.getString(BpProfile.HISTORICAL_NUM_BP);
					Message msg = new Message();
					msg.what = HANDLER_MESSAGE;
					msg.obj = "num: " + num;
					myHandler.sendMessage(msg);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				
			}else if(BpProfile.ACTION_IS_ENABLE_OFFLINE.equals(action)){
				try {
					JSONObject info = new JSONObject(message);
					String isEnableoffline =info.getString(BpProfile.IS_ENABLE_OFFLINE);
					Message msg = new Message();
					msg.what = HANDLER_MESSAGE;
					msg.obj = "isEnableoffline: " + isEnableoffline;
					myHandler.sendMessage(msg);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				
			}else if(BpProfile.ACTION_ONLINE_PRESSURE_BP.equals(action)){
				try {
					JSONObject info = new JSONObject(message);
					String pressure =info.getString(BpProfile.BLOOD_PRESSURE_BP);
					Message msg = new Message();
					msg.what = HANDLER_MESSAGE;
					msg.obj = "pressure: " + pressure;
					myHandler.sendMessage(msg);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				
			}else if(BpProfile.ACTION_ONLINE_PULSEWAVE_BP.equals(action)){
				try {
					JSONObject info = new JSONObject(message);
					String pressure =info.getString(BpProfile.BLOOD_PRESSURE_BP);
					String wave = info.getString(BpProfile.PULSEWAVE_BP);
					String heartbeat = info.getString(BpProfile.FLAG_HEARTBEAT_BP);
					Message msg = new Message();
					msg.what = HANDLER_MESSAGE;
					msg.obj = "pressure:" + pressure + "\n"
							+ "wave: " + wave + "\n"
							+ " - heartbeat:" + heartbeat;
					myHandler.sendMessage(msg);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				
			}else if(BpProfile.ACTION_ONLINE_RESULT_BP.equals(action)){
				try {
					JSONObject info = new JSONObject(message);
					String highPressure =info.getString(BpProfile.HIGH_BLOOD_PRESSURE_BP);
					String lowPressure =info.getString(BpProfile.LOW_BLOOD_PRESSURE_BP);
					String ahr =info.getString(BpProfile.MEASUREMENT_AHR_BP);
					String pulse =info.getString(BpProfile.PULSE_BP);
					Message msg = new Message();
					msg.what = HANDLER_MESSAGE;
					msg.obj = "highPressure: " + highPressure 
							+ "lowPressure: " + lowPressure
							+ "ahr: " + ahr
							+ "pulse: " + pulse;
					myHandler.sendMessage(msg);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				
			}else if(BpProfile.ACTION_ZOREING_BP.equals(action)){
				Message msg = new Message();
				msg.what = HANDLER_MESSAGE;
				msg.obj = "zoreing";
				myHandler.sendMessage(msg);
				
			}else if(BpProfile.ACTION_ZOREOVER_BP.equals(action)){
				Message msg = new Message();
				msg.what = HANDLER_MESSAGE;
				msg.obj = "zoreover";
				myHandler.sendMessage(msg);

			}
		}
	};
	
	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.btn_getbattery:
			if(bp7Control != null)
				bp7Control.getBattery();
			else
				Toast.makeText(BP7.this, "bp7Control == null", Toast.LENGTH_LONG).show();
			break;
			
		case R.id.btn_isOfflineMeasure:
			if(bp7Control != null)
				bp7Control.isEnableOffline();
			else
				Toast.makeText(BP7.this, "bp7Control == null", Toast.LENGTH_LONG).show();				
			break;
			
		case R.id.btn_enableOfflineMeasure:
			if(bp7Control != null)
				bp7Control.enbleOffline();
			else
				Toast.makeText(BP7.this, "bp7Control == null", Toast.LENGTH_LONG).show();
			break;
			
		case R.id.btn_disableOfflineMeasure:
			if(bp7Control != null)
				bp7Control.disableOffline();
			else
				Toast.makeText(BP7.this, "bp7Control == null", Toast.LENGTH_LONG).show();
			break;
			
		case R.id.btn_startMeasure:
			if(bp7Control != null)
				bp7Control.startMeasure();
			else
				Toast.makeText(BP7.this, "bp7Control == null", Toast.LENGTH_LONG).show();
			break;
			
		case R.id.btn_getangle:
			if(bp7Control != null)
				bp7Control.getAngle();
			else
				Toast.makeText(BP7.this, "bp7Control == null", Toast.LENGTH_LONG).show();
			break;
			
		case R.id.btn_stopMeasure:
			if(bp7Control != null)
				bp7Control.interruptMeasure();
			else
				Toast.makeText(BP7.this, "bp5Control == null", Toast.LENGTH_LONG).show();
			break;
			
		case R.id.btn_disconnect:
			if(bp7Control != null)
				bp7Control.disconnect();
			else
				Toast.makeText(BP7.this, "bp5Control == null", Toast.LENGTH_LONG).show();
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
