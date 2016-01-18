/**
 * @title
 * @Description
 * @author
 * @date 2015年12月17日 下午2:00:36 
 * @version V1.0  
 */

package com.ihealth.devices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.example.devicelibtest.R;
import com.ihealth.communication.control.HS6Control;
import com.ihealth.communication.manager.iHealthDeviceHs6Callback;
import com.ihealth.communication.manager.iHealthDevicesManager;

import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

public class HS6 extends Activity implements OnClickListener {

    private TextView tv_notice;
    private Button btn_setWifi, btn_bind, btn_unbind;
    private HS6Control mHS6control;
    private final String TAG = "HS6";
    private String userName = "liu01234345555@jiuan.com";
    private String code = "MODEL:HS6 MAC:ACCF2337A94E LOGICVER:1.0.1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hs6);
        initView();
        mHS6control = new HS6Control(userName, this, iHealthDevicesManager.TYPE_HS6, mIHealthDeviceHs6Callback);
    }

    iHealthDeviceHs6Callback mIHealthDeviceHs6Callback = new iHealthDeviceHs6Callback() {
        public void setWifiNotify(String Type, String action, String message) {
            Log.e(TAG, "type:" + Type + "--action:" + action + "--mesage:" + message);
            if (action.equals(HS6Control.ACTION_HS6_SETWIFI)) {
                mHandler.sendEmptyMessage(2);
                JSONTokener jsonTokener = new JSONTokener(message);
                JSONObject jsonObject;
                try {
                    jsonObject = (JSONObject) jsonTokener.nextValue();
                    boolean result = jsonObject.getBoolean(HS6Control.SETWIFI_RESULT);
                    Log.d(TAG, "result:" + result);
                    noticeString = message;
                    mHandler.sendEmptyMessage(1);
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        };

        public void onNotify(String mac, String Type, String action, String message) {
            Log.e(TAG, "mac:" + mac + "--type:" + Type + "--action:" + action + "--mesage:" + message);
            JSONTokener jsonTokener = new JSONTokener(message);
            switch (action) {
                case HS6Control.ACTION_HS6_BIND:
                    try {
                        JSONObject jsonObject = (JSONObject) jsonTokener.nextValue();
                        JSONArray jsonArray = jsonObject.getJSONArray(HS6Control.HS6_BIND_EXTRA);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject2 = (JSONObject) jsonArray.get(i);
                            int result = jsonObject2.getInt(HS6Control.BIND_HS6_RESULT);
                            String model = jsonObject2.getString(HS6Control.HS6_MODEL);
                            String firmwareVersion = jsonObject2.getString(HS6Control.HS6_FIRMWARE_VERSION);
                            int position = jsonObject2.getInt(HS6Control.HS6_POSITION);
                            int settedWifi = jsonObject2.getInt(HS6Control.HS6_SETTED_WIFI);
                            if (result == 1) {
                                noticeString = "bind success";
                            } else if (result == 2) {
                                noticeString = "the scale has no empty position";
                            } else {
                                noticeString = "bind fail";
                            }
                            noticeString =noticeString+"\n "+"result:" + result + "--model:" + model + "--firmwareVersion"
                                    + firmwareVersion + "--position:" + position + "--setted:" + settedWifi;
                            Log.d(TAG, noticeString);
                            mHandler.sendEmptyMessage(1);

                        }

                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    break;
                case HS6Control.ACTION_HS6_ERROR:
                    try {
                        JSONObject jsonObject = (JSONObject) jsonTokener.nextValue();
                        int error = jsonObject.getInt(HS6Control.HS6_ERROR);
                        Log.d(TAG, error + "");
                        noticeString = message;
                        mHandler.sendEmptyMessage(1);
                    } catch (Exception e) {
                        // TODO: handle exception
                    }
                    break;
                case HS6Control.ACTION_HS6_UNBIND:
                    try {
                        JSONObject jsonObject = (JSONObject) jsonTokener.nextValue();
                        boolean result = jsonObject.getBoolean(HS6Control.HS6_UNBIND_RESULT);
                        Log.d(TAG, "result:" + result);
                        noticeString = message;
                        mHandler.sendEmptyMessage(1);

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

    private void initView() {
        tv_notice = (TextView) findViewById(R.id.tv_notice);
        btn_setWifi = (Button) findViewById(R.id.btn_setWifi);
        btn_setWifi.setOnClickListener(this);
        btn_bind = (Button) findViewById(R.id.btn_bind);
        btn_bind.setOnClickListener(this);
        btn_unbind = (Button) findViewById(R.id.btn_unbind);
        btn_unbind.setOnClickListener(this);

    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }

    String noticeString = "";
    Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 1:
                    tv_notice.setText(noticeString);
                    break;
                case 2:
                    dismissProgress();
                    break;
                case 3:
                    showProgress();
                    break;
                default:
                    break;
            }
        };
    };

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.btn_setWifi:
                showSetWifiDialog();
                break;
            case R.id.btn_bind:
                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        boolean permission = mHS6control.bindDeviceHS6("1999-11-12 11:29:10", 160, 2, 1, code);
                        if (!permission) {
                            noticeString = "You haven't gotten the permission, please go to certificate firstly";
                            mHandler.sendEmptyMessage(1);
                        }
                    }
                }).start();

                break;
            case R.id.btn_unbind:
                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        boolean permission = mHS6control.unBindDeviceHS6(code);
                        if (!permission) {
                            noticeString = "You haven't gotten the permission, please go to certificate firstly";
                            mHandler.sendEmptyMessage(1);
                        }
                    }
                }).start();
                break;
            default:
                break;
        }

    }

    private String ssid;

    private void showSetWifiDialog() {
        LayoutInflater wifiPwdInput = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewPwd = wifiPwdInput.inflate(R.layout.bluetoothsetwifidialogeitem, null);
        final EditText etPwd;
        final TextView tvSSID;
        tvSSID = (TextView) viewPwd.findViewById(R.id.btSetWiFi_tv_SSID);
        ssid = getSSid();
        tvSSID.setText(ssid);
        etPwd = (EditText) viewPwd.findViewById(R.id.btSetWiFi_et_Pwd);
        new AlertDialog.Builder(this).setTitle(this.getResources().getString(R.string.bluetooth_setwifi_title))
                .setView(viewPwd)
                .setCancelable(false)
                .setPositiveButton(R.string.ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mHandler.sendEmptyMessage(3);
                                try {
                                    new Thread() {
                                        @Override
                                        public void run() {
                                            boolean permission = mHS6control.setWifi(ssid, etPwd.getText().toString());
                                            if (!permission) {
                                                noticeString = "You haven't gotten the permission, please go to certificate firstly";
                                                mHandler.sendEmptyMessage(1);
                                            }
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

    private String getSSid() {
        WifiManager wm = (WifiManager) this.getSystemService(this.WIFI_SERVICE);
        if (wm != null) {
            WifiInfo wi = wm.getConnectionInfo();
            if (wi != null) {
                String s = wi.getSSID();
                if (s.length() > 2 && s.charAt(0) == '"' && s.charAt(s.length() - 1) == '"') {
                    return s.substring(1, s.length() - 1);
                } else {
                    return s;
                }
            }
        }
        return "";
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    private AlertDialog dialog_wifi;

    private void showProgress() {
        if (dialog_wifi == null) {
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
    }

    private void dismissProgress() {
        if (dialog_wifi != null) {
            dialog_wifi.cancel();
        }
    }

}
