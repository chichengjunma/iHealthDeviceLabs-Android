/**
 * @title
 * @Description
 * @author
 * @date 2015年11月18日 下午11:02:19 
 * @version V1.0  
 */

package com.ihealth.devices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.example.devicelibtest.R;
import com.ihealth.communication.control.Hs3Control;
import com.ihealth.communication.control.HsProfile;
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

public class HS3 extends Activity implements OnClickListener {
    private TextView tv_return;
    private String deviceMac;
    private static String TAG = "HS3";
    private int clientId;
    private Hs3Control mHs3control;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hs3);
        initView();
        Intent intent = getIntent();
        deviceMac = intent.getStringExtra("mac");
        clientId = iHealthDevicesManager.getInstance().registerClientCallback(mIHealthDevicesCallback);
        /* Limited wants to receive notification specified device */
        iHealthDevicesManager.getInstance().addCallbackFilterForDeviceType(clientId,
                iHealthDevicesManager.TYPE_HS3);
        /* Get hs3 controller */
        mHs3control = iHealthDevicesManager.getInstance().getHs3Control(deviceMac);
    }

    iHealthDevicesCallback mIHealthDevicesCallback = new iHealthDevicesCallback() {
        public void onScanDevice(String mac, String deviceType) {
        };

        public void onDeviceConnectionStateChange(String mac, String deviceType, int status) {
            Log.e(TAG, "mac:" + mac + "-deviceType:" + deviceType + "-status:" + status);

            switch (status) {
                case iHealthDevicesManager.DEVICE_STATE_DISCONNECTED:
                    mHs3control = null;
                    Log.d(TAG, "The device disconnect");
                    Message message2 = new Message();
                    message2.what = 1;
                    message2.obj = "The device disconnect";
                    mHandler.sendMessage(message2);
                    Toast.makeText(HS3.this, "The device disconnect", Toast.LENGTH_LONG).show();
                    break;

                default:
                    break;
            }
        };

        public void onDeviceNotify(String mac, String deviceType, String action, String message) {
            Log.d(TAG, "mac:" + mac + "--type:" + deviceType + "--action:" + action + "--message:" + message);
            JSONTokener jsonTokener = new JSONTokener(message);
            switch (action) {
                case HsProfile.ACTION_HISTORICAL_DATA_HS:
                    try {
                        JSONObject object = (JSONObject) jsonTokener.nextValue();
                        JSONArray jsonArray = object.getJSONArray(HsProfile.HISTORDATA__HS);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                            String dateString = jsonObject.getString(HsProfile.MEASUREMENT_DATE_HS);
                            float weight =  (float) jsonObject.getDouble(HsProfile.WEIGHT_HS);
                            Log.d(TAG, "date:" + dateString + "-weight:" + weight);
                        }
                        Message message2 = new Message();
                        message2.what = 1;
                        message2.obj = message;
                        mHandler.sendMessage(message2);
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    break;
                case HsProfile.ACTION_ONLINE_RESULT_HS:
                    try {
                        JSONObject jsonObject = (JSONObject) jsonTokener.nextValue();
                        float weight = (float) jsonObject.getDouble(HsProfile.WEIGHT_HS);
                        Log.d(TAG, "weight:" + weight);
                        Message message3 = new Message();
                        message3.what = 1;
                        message3.obj = message;
                        mHandler.sendMessage(message3);
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    break;
                case HsProfile.ACTION_NO_HISTORICALDATA:
                    noticeString = "no history data";
                    Message message2 = new Message();
                    message2.what = 1;
                    message2.obj = noticeString;
                    mHandler.sendMessage(message2);
                    break;
                case HsProfile.ACTION_ERROR_HS:
                    try {
                        JSONObject jsonObject = (JSONObject) jsonTokener.nextValue();
                        int error = jsonObject.getInt(HsProfile.ERROR_NUM_HS);
                        Log.d(TAG, "error:" + error);
                        Message message3 = new Message();
                        message3.what = 1;
                        message3.obj = message;
                        mHandler.sendMessage(message3);
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    break;
                default:
                    break;
            }
        };
    };
    String noticeString = "";
    Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 1:
                    tv_return.setText((String) msg.obj);
                    break;

                default:
                    break;
            }
        };
    };

    private void initView() {
        tv_return = (TextView) findViewById(R.id.tv_return);
        findViewById(R.id.btn_getOfflineData).setOnClickListener(this);
        findViewById(R.id.btn_disconnect).setOnClickListener(this);

    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        iHealthDevicesManager.getInstance().unRegisterClientCallback(clientId);

    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.btn_getOfflineData:
                if (mHs3control == null) {
                    Toast.makeText(HS3.this, "mHs3control == null", Toast.LENGTH_LONG).show();

                } else {
                    mHs3control.getOfflineData();
                }
                break;
            case R.id.btn_disconnect:
                if (mHs3control == null) {
                    Toast.makeText(HS3.this, "mHs3control == null", Toast.LENGTH_LONG).show();

                } else {
                    mHs3control.disconnect();
                }
                break;
            default:
                break;

        }
    }
}
