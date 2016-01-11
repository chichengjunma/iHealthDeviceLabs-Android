
package com.ihealth.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.example.devicelibtest.R;
import com.ihealth.communication.ins.iHealthDevicesIDPS;
import com.ihealth.communication.manager.iHealthDevicesCallback;
import com.ihealth.communication.manager.iHealthDevicesManager;
import com.ihealth.devices.ABI;
import com.ihealth.devices.AM3;
import com.ihealth.devices.BG1;
import com.ihealth.devices.BG5;
import com.ihealth.devices.BP3L;
import com.ihealth.devices.BP3M;
import com.ihealth.devices.BP5;
import com.ihealth.devices.BP7;
import com.ihealth.devices.HS3;
import com.ihealth.devices.HS4;
import com.ihealth.devices.HS4S;
import com.ihealth.devices.HS5BT;
import com.ihealth.devices.HS5wifi;
import com.ihealth.devices.HS6;
import com.ihealth.devices.Po3;
import com.ihealth.utils.MyLog;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

/**
 * Activity for scan and connect available iHealth devices.
 */
public class MainActivity extends Activity implements OnClickListener {

    private static final String TAG = "MainActivity";
    private MyLog myLog;
    private ListView listview_scan;
    private ListView listview_connected;
    private SimpleAdapter sa_scan;
    private SimpleAdapter sa_connected;
    private TextView tv_discovery;
    private List<HashMap<String, String>> list_ScanDevices = new ArrayList<HashMap<String, String>>();
    private List<HashMap<String, String>> list_ConnectedDevices = new ArrayList<HashMap<String, String>>();
    private int callbackId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myLog = new MyLog("MainActivity");

        /*
         * Initializes the iHealth devices manager. Can discovery available iHealth devices nearby
         * and connect these devices through iHealthDevicesManager.
         */
        iHealthDevicesManager.getInstance().init(this);

        findViewById(R.id.btn_discorvery).setOnClickListener(this);
        findViewById(R.id.btn_stopdiscorvery).setOnClickListener(this);
        findViewById(R.id.btn_Certification).setOnClickListener(this);
        findViewById(R.id.btn_GotoBG1).setOnClickListener(this);
        findViewById(R.id.btn_GotoABI).setOnClickListener(this);
        findViewById(R.id.btn_GotoHS6).setOnClickListener(this);

        tv_discovery = (TextView) findViewById(R.id.tv_discovery);
        listview_scan = (ListView) findViewById(R.id.list_scan);
        listview_connected = (ListView) findViewById(R.id.list_connected);
        if (list_ConnectedDevices != null)
            list_ConnectedDevices.clear();
        if (list_ScanDevices != null)
            list_ScanDevices.clear();

        /*
         * Register callback to the manager. This method will return a callback Id.
         */
        callbackId = iHealthDevicesManager.getInstance().registerClientCallback(iHealthDevicesCallback);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        /*
         * When the Activity is destroyed , need to call unRegisterClientCallback method to
         * unregister callback
         */
        iHealthDevicesManager.getInstance().unRegisterClientCallback(callbackId);
        /*
         * When the Activity is destroyed , need to call destroy method of iHealthDeivcesManager to
         * release resources
         */
        iHealthDevicesManager.getInstance().destroy();
    }

    private iHealthDevicesCallback iHealthDevicesCallback = new iHealthDevicesCallback() {

        @Override
        public void onScanDevice(String mac, String deviceType) {
            Log.i(TAG, "onScanDevice - mac:" + mac + " - deviceType:" + deviceType);
            Bundle bundle = new Bundle();
            bundle.putString("mac", mac);
            bundle.putString("type", deviceType);
            Message msg = new Message();
            msg.what = HANDLER_SCAN;
            msg.setData(bundle);
            myHandler.sendMessage(msg);
        }

        @Override
        public void onDeviceConnectionStateChange(String mac, String deviceType, int status) {
            Bundle bundle = new Bundle();
            bundle.putString("mac", mac);
            bundle.putString("type", deviceType);
            Message msg = new Message();
            if (status == iHealthDevicesManager.DEVICE_STATE_CONNECTED) {
                msg.what = HANDLER_CONNECTED;
            } else if (status == iHealthDevicesManager.DEVICE_STATE_DISCONNECTED) {
                msg.what = HANDLER_DISCONNECT;
            }
            msg.setData(bundle);
            myHandler.sendMessage(msg);
        }

        @Override
        public void onUserStatus(String username, int userStatus) {
            Bundle bundle = new Bundle();
            bundle.putString("username", username);
            bundle.putString("userstatus", userStatus + "");
            Message msg = new Message();
            msg.what = HANDLER_USER_STATUE;
            msg.setData(bundle);
            myHandler.sendMessage(msg);
        }

        @Override
        public void onDeviceNotify(String mac, String deviceType, String action, String message) {
        }

		@Override
		public void onScanFinish() {
			tv_discovery.setText("discover finish");
		}
		
    };

    /*
     * userId the identification of the user, could be the form of email address or mobile phone
     * number (mobile phone number is not supported temporarily). clientID and clientSecret, as the
     * identification of the SDK, will be issued after the iHealth SDK registration. please contact
     * lvjincan@ihealthlabs.com.cn.com for registration.
     */
    String userName = "";
    String clientId = "";
    String clientSecret = "";

    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()) {
            case R.id.btn_discorvery:
                int type = iHealthDevicesManager.DISCOVERY_BP3L;
                /*
                 * discovery iHealth devices, This method can specify only to search for the devices
                 * that you want to connect
                 */
                list_ScanDevices.clear();
                updateViewForScan();
                iHealthDevicesManager.getInstance().startDiscovery(type);
                tv_discovery.setText("discovering...");
                break;

            case R.id.btn_stopdiscorvery:
                /* stop discovery iHealth devices */
                iHealthDevicesManager.getInstance().stopDiscovery();
                break;

            case R.id.btn_Certification:
                iHealthDevicesManager.getInstance().sdkUserInAuthor(MainActivity.this, userName, clientId,
                        clientSecret, callbackId);
                break;

            case R.id.btn_GotoBG1:
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, BG1.class);
                intent.putExtra("userName", userName);
                startActivity(intent);
                break;
            case R.id.btn_GotoABI:
                Intent intentAbi = new Intent();
                intentAbi.setClass(MainActivity.this, ABI.class);
                startActivity(intentAbi);
                break;

            case R.id.btn_GotoHS6:
                Intent intentHS6 = new Intent();
                intentHS6.setClass(MainActivity.this, HS6.class);
                startActivity(intentHS6);
                break;
            default:
                break;
        }
    }

    private static final int HANDLER_SCAN = 101;
    private static final int HANDLER_CONNECTED = 102;
    private static final int HANDLER_DISCONNECT = 103;
    private static final int HANDLER_USER_STATUE = 104;
    private Handler myHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case HANDLER_SCAN:
                    Bundle bundle_scan = msg.getData();
                    String mac_scan = bundle_scan.getString("mac");
                    String type_scan = bundle_scan.getString("type");
                    HashMap<String, String> hm_scan = new HashMap<String, String>();
                    hm_scan.put("mac", mac_scan);
                    hm_scan.put("type", type_scan);
                    list_ScanDevices.add(hm_scan);
                    updateViewForScan();
                    break;

                case HANDLER_CONNECTED:
                    Bundle bundle_connect = msg.getData();
                    String mac_connect = bundle_connect.getString("mac");
                    String type_connect = bundle_connect.getString("type");
                    HashMap<String, String> hm_connect = new HashMap<String, String>();
                    hm_connect.put("mac", mac_connect);
                    hm_connect.put("type", type_connect);
                    list_ConnectedDevices.add(hm_connect);
                    updateViewForConnected();

                    Iterator<HashMap<String, String>> i_scan = list_ScanDevices.listIterator();
                    int index_connected = 0;
                    while (i_scan.hasNext()) {
                        HashMap<String, String> hm_disconnect = i_scan.next();
                        String mac = hm_disconnect.get("mac");
                        if (mac.equals(mac_connect)) {
                            break;
                        } else {
                            index_connected += 1;
                        }
                    }
                    list_ScanDevices.remove(index_connected);
                    updateViewForScan();
                    break;

                case HANDLER_DISCONNECT:
                    Bundle bundle_disconnect = msg.getData();
                    String mac_disconnect = bundle_disconnect.getString("mac");
                    String type_disconnect = bundle_disconnect.getString("type");
                    Iterator<HashMap<String, String>> i = list_ConnectedDevices.listIterator();
                    int index = 0;
                    while (i.hasNext()) {
                        HashMap<String, String> hm_disconnect = i.next();
                        String mac = hm_disconnect.get("mac");
                        if (mac.equals(mac_disconnect)) {
                            break;
                        } else {
                            index += 1;
                        }
                    }
                    if (list_ConnectedDevices.size() > 0) {
                        list_ConnectedDevices.remove(index);
                        updateViewForConnected();
                    }

                    break;
                case HANDLER_USER_STATUE:
                    Bundle bundle_status = msg.getData();
                    String username = bundle_status.getString("username");
                    String userstatus = bundle_status.getString("userstatus");
                    String str = "username:" + username + " - userstatus:" + userstatus;
                    Toast.makeText(MainActivity.this, str, Toast.LENGTH_LONG).show();
                    break;

                default:
                    break;
            }
        }
    };

    private void updateViewForScan() {
        sa_scan = new SimpleAdapter(this, this.list_ScanDevices, R.layout.bp_listview_baseview,
                new String[] {
                        "type", "mac"
                },
                new int[] {
                        R.id.tv_type, R.id.tv_mac
                });

        listview_scan.setAdapter(sa_scan);
        listview_scan.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
                HashMap<String, String> hm = list_ScanDevices.get(position);
                String type = hm.get("type");
                String mac = hm.get("mac");
                boolean req = iHealthDevicesManager.getInstance().connectDevice(userName, mac);
                if (!req) {
                    Log.i(TAG, "Haven’t permission to connect this device");
                }
            }
        });
        sa_scan.notifyDataSetChanged();
    }

    private void updateViewForConnected() {
        sa_connected = new SimpleAdapter(this, this.list_ConnectedDevices, R.layout.bp_listview_baseview,
                new String[] {
                        "type", "mac"
                },
                new int[] {
                        R.id.tv_type, R.id.tv_mac
                });

        listview_connected.setAdapter(sa_connected);
        listview_connected.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
                HashMap<String, String> hm = list_ConnectedDevices.get(position);
                String type = hm.get("type");
                String mac = hm.get("mac");
                Intent intent = new Intent();
                intent.putExtra("mac", mac);
                if (iHealthDevicesManager.TYPE_AM3.equals(type)) {
                    intent.setClass(MainActivity.this, AM3.class);
                    startActivity(intent);

                } else if (iHealthDevicesManager.TYPE_AM3S.equals(type)) {
                    intent.setClass(MainActivity.this, BP5.class);
                    startActivity(intent);

                } else if (iHealthDevicesManager.TYPE_AM4.equals(type)) {
                    intent.setClass(MainActivity.this, BP5.class);
                    startActivity(intent);

                } else if (iHealthDevicesManager.TYPE_BG5.equals(type)) {
                    intent.setClass(MainActivity.this, BG5.class);
                    startActivity(intent);

                } else if (iHealthDevicesManager.TYPE_BP3L.equals(type)) {
                    intent.setClass(MainActivity.this, BP3L.class);
                    startActivity(intent);

                } else if (iHealthDevicesManager.TYPE_BP3M.equals(type)) {
                    intent.setClass(MainActivity.this, BP3M.class);
                    startActivity(intent);

                } else if (iHealthDevicesManager.TYPE_BP5.equals(type)) {
                    intent.setClass(MainActivity.this, BP5.class);
                    startActivity(intent);

                } else if (iHealthDevicesManager.TYPE_BP7.equals(type)) {
                    intent.setClass(MainActivity.this, BP7.class);
                    startActivity(intent);

                } else if (iHealthDevicesManager.TYPE_BP7S.equals(type)) {
                    intent.setClass(MainActivity.this, BP5.class);
                    startActivity(intent);

                } else if (iHealthDevicesManager.TYPE_HS3.equals(type)) {
                    intent.setClass(MainActivity.this, HS3.class);
                    startActivity(intent);

                } else if (iHealthDevicesManager.TYPE_HS4.equals(type)) {
                    intent.setClass(MainActivity.this, HS4.class);
                    startActivity(intent);

                } else if (iHealthDevicesManager.TYPE_HS4S.equals(type)) {
                    intent.setClass(MainActivity.this, HS4S.class);
                    startActivity(intent);

                } else if (iHealthDevicesManager.TYPE_HS5.equals(type)) {
                    intent.setClass(MainActivity.this, HS5wifi.class);
                    startActivity(intent);

                } else if (iHealthDevicesManager.TYPE_HS5_BT.equals(type)) {
                    intent.setClass(MainActivity.this, HS5BT.class);
                    startActivity(intent);

                } else if (iHealthDevicesManager.TYPE_HS6.equals(type)) {
                    intent.setClass(MainActivity.this, BP5.class);
                    startActivity(intent);

                } else if (iHealthDevicesManager.TYPE_PO3.equals(type)) {
                    intent.setClass(MainActivity.this, Po3.class);
                    startActivity(intent);

                }
            }
        });
        sa_connected.notifyDataSetChanged();
    }
}
