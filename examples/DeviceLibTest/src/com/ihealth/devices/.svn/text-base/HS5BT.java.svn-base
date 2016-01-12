/**
 * @title
 * @Description
 * @author
 * @date 2015年11月17日 下午6:18:41 
 * @version V1.0  
 */

package com.ihealth.devices;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.devicelibtest.R;
import com.ihealth.communication.control.Hs5ControlForBt;
import com.ihealth.communication.control.HsProfile;
import com.ihealth.communication.manager.iHealthDevicesCallback;
import com.ihealth.communication.manager.iHealthDevicesManager;

@SuppressLint("ShowToast")
public class HS5BT extends Activity implements OnClickListener {
    private Button btn_setWifi;
    private Button btn_disconnect;
    private TextView tv_return;
    private Hs5ControlForBt mHs5ControlForBt;
    private String deviceMac;
    private static String TAG = "HS5BT";
    private int clientId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hs5_bt);
        initView();
        Intent intent = getIntent();
        deviceMac = intent.getStringExtra("mac");
        clientId = iHealthDevicesManager.getInstance().registerClientCallback(mIHealthDevicesCallback);
        /* Limited wants to receive notification specified device */
        iHealthDevicesManager.getInstance().addCallbackFilterForDeviceType(clientId,
                iHealthDevicesManager.TYPE_HS5_BT);
        /* Get hs5bt controller */
        mHs5ControlForBt = iHealthDevicesManager.getInstance().getHs5ControlForBt(deviceMac);
    }

    private void initView() {
        btn_setWifi = (Button) findViewById(R.id.btn_setWifi);
        btn_setWifi.setOnClickListener(this);
        btn_disconnect = (Button) findViewById(R.id.btn_disconnect);
        btn_disconnect.setOnClickListener(this);
        tv_return = (TextView) findViewById(R.id.tv_return);
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        iHealthDevicesManager.getInstance().unRegisterClientCallback(clientId);

    }

    iHealthDevicesCallback mIHealthDevicesCallback = new iHealthDevicesCallback() {
        @SuppressLint("ShowToast")
        public void onDeviceConnectionStateChange(String mac, String deviceType, int status) {
            Log.i(TAG, "mac:" + mac + "-deviceType:" + deviceType + "-status:" + status);
            switch (status) {
                case iHealthDevicesManager.DEVICE_STATE_DISCONNECTED:
                    mHs5ControlForBt = null;
                    // mHandler.sendEmptyMessage(4);
                    Toast.makeText(HS5BT.this, "This device disconnect", Toast.LENGTH_LONG).show();
                    break;

                default:
                    break;
            }
        };

        public void onDeviceNotify(String mac, String deviceType, String action, String message) {
            Log.d(TAG, "mac:" + mac + "-action:" + action + "-message:" + message);
            switch (action) {
                case HsProfile.ACTION_SETTINGWIFI:
                    mHandler.sendEmptyMessage(1);
                    break;
                case HsProfile.ACTION_SETWIFI_FAIL:
                    mHandler.sendEmptyMessage(2);
                    break;
                case HsProfile.ACTION_SETWIFI_SUCCESS:
                    mHandler.sendEmptyMessage(3);
                    break;
                default:
                    break;
            }
        };
    };

    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    tv_return.setText("setting wifi");
                    break;
                case 2:
                    dismissDialog();
                    tv_return.setText("setting wifi fail");
                    break;
                case 3:
                    dismissDialog();
                    tv_return.setText("setting wifi success");
                    break;
                case 4:
                    dismissDialog();
                    tv_return.setText("device disconnect");
                    break;
                case 0:
                    showDialog();
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.btn_setWifi:
                if (mHs5ControlForBt == null) {
                    Toast.makeText(HS5BT.this, "mHs5ControlForBt == null", Toast.LENGTH_LONG);

                } else {
                    getWiFiInfo(this);
                    showSetWifiDialog();
                }
                break;
            case R.id.btn_disconnect:
                if (mHs5ControlForBt == null) {
                    Toast.makeText(HS5BT.this, "mHs5ControlForBt == null", Toast.LENGTH_LONG);

                } else {
                    mHs5ControlForBt.disconnect();

                }

                break;
            default:
                break;
        }

    }

    private AlertDialog dialog_wifi = null;// wifi-wifi转框

    private void showDialog() {
        AlertDialog.Builder builder = new Builder(this);
        builder.setView(new ProgressBar(this));
        dialog_wifi = builder.create();
        dialog_wifi.setCancelable(false);
        dialog_wifi.setCanceledOnTouchOutside(false);
        dialog_wifi.show();
        // setting alertdialog size
        WindowManager.LayoutParams params = dialog_wifi.getWindow().getAttributes();
        params.width = 300;
        dialog_wifi.getWindow().setAttributes(params);
    }

    private void dismissDialog() {
        if (dialog_wifi != null) {
            dialog_wifi.dismiss();
            dialog_wifi = null;
        }
    }

    private WifiManager mWifiMange;
    private LayoutInflater wifiPwdInput;
    private String ssid;
    private int type = 4;

    public void getWiFiInfo(Context context) {
        mWifiMange = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        ssid = mWifiMange.getConnectionInfo().getSSID();
        // scan wifi
        mWifiMange.startScan();
        // get scan result
        List<ScanResult> mWifiList;
        mWifiList = mWifiMange.getScanResults();
        for (int i = 0; i < mWifiList.size(); i++) {
            Log.i("wifi", mWifiList.get(i).SSID + "||" + mWifiList.get(i).capabilities);
            Log.e("wifi", "mWifiManager.getConnectionInfo().getSSID()=" + mWifiMange.getConnectionInfo().getSSID());
            if (mWifiMange.getConnectionInfo().getSSID().contains(mWifiList.get(i).SSID)) {
                if (mWifiList.get(i).capabilities.contains("WPA2")
                        && mWifiList.get(i).capabilities.contains("WPA")
                        && mWifiList.get(i).capabilities.contains("Mixed")) {
                    type = 4;
                } else if (mWifiList.get(i).capabilities.contains("WPA2")) {
                    type = 3;
                } else if (mWifiList.get(i).capabilities.contains("WPA")) {
                    type = 2;
                } else if (mWifiList.get(i).capabilities.contains("WEP")) {
                    type = 1;
                } else {
                    type = 0;
                }
                break;
            }
        }
    }

    private void showSetWifiDialog() {
        wifiPwdInput = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewPwd = wifiPwdInput.inflate(R.layout.bluetoothsetwifidialogeitem, null);
        final EditText etPwd;
        final TextView tvSSID;
        tvSSID = (TextView) viewPwd.findViewById(R.id.btSetWiFi_tv_SSID);
        tvSSID.setText(ssid);
        etPwd = (EditText) viewPwd.findViewById(R.id.btSetWiFi_et_Pwd);
        new AlertDialog.Builder(this).setTitle(this.getResources().getString(R.string.bluetooth_setwifi_title))
                .setView(viewPwd)
                .setCancelable(false)
                .setPositiveButton(R.string.ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // wifiPwd = etPwd.getText().toString();
                                try {
                                    new Thread() {
                                        @Override
                                        public void run() {
                                            mHandler.sendEmptyMessage(0);
                                            mHs5ControlForBt.setWifi(ssid, type, etPwd.getText().toString());
                                        }
                                    }.start();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                        })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create().show();

    }

}
