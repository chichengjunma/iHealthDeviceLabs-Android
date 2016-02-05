package com.ihealth.devices;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.widget.TextView;

import com.example.devicelibtest.R;
import com.ihealth.communication.control.Bg1Control;
import com.ihealth.communication.control.Bg1Profile;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class BG1 extends Activity {

    private static final String TAG = "BG1";
    private TextView mTextView;
    public Bg1Control mBg1Control;

    AudioManager myAudio;
    private static int volumIndex = 0;
    public static boolean bg1Plugged = false;
    Timer BG1Timer = null;
    TimerTask BG1TimerTask = null;
    Thread mConnect1305Thread = null;
    Thread mConnect1304Thread = null;
    private boolean isGetStripInBg1 = false;
    private boolean isGetResultBg1 = false;
    private boolean isGetBloodBg1 = false;

    public String QRCode = "02323C641E3114322D0800A064646464646464646464FA012261000E1CCC";

    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss--");

    Runnable connect1305_Runnable = new Runnable() {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            mBg1Control.connect1305Device();
        }
    };

    Runnable connect1304_Runnable = new Runnable() {
        @Override
        public void run() {
            mBg1Control.connect1304Device();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bg1);
        mTextView = (TextView) findViewById(R.id.startBG1);

        registerBroadcast();
        Intent intent = getIntent();
        String userName = intent.getExtras().getString("userName");

        myAudio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        volumIndex = myAudio.getStreamVolume(AudioManager.STREAM_MUSIC);

        mBg1Control = Bg1Control.getInstance();
        mBg1Control.init(BG1.this, userName);
    }

    private void registerBroadcast(){
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_HEADSET_PLUG);
        filter.addAction(Bg1Profile.ACTION_BG1_IDENTIFY_RESULT);
        filter.addAction(Bg1Profile.ACTION_BG1_DEVICE_ID);
        filter.addAction(Bg1Profile.ACTION_BG1_CONNECT_RESULT);
        filter.addAction(Bg1Profile.ACTION_BG1_DEVICE_READY_NEW);
        filter.addAction(Bg1Profile.ACTION_BG1_IDPS_NEW);
        filter.addAction(Bg1Profile.ACTION_BG1_CONNECT_RESULT_NEW);
        filter.addAction(Bg1Profile.ACTION_BG1_MEASURE_ERROR);
        filter.addAction(Bg1Profile.ACTION_BG1_MEASURE_STRIP_IN);
        filter.addAction(Bg1Profile.ACTION_BG1_MEASURE_STRIP_OUT);
        filter.addAction(Bg1Profile.ACTION_BG1_MEASURE_GET_BLOOD);
        filter.addAction(Bg1Profile.ACTION_BG1_MEASURE_RESULT);
        filter.addAction(Bg1Profile.ACTION_BG1_MEASURE_STANDBY);
        this.registerReceiver(mBroadcastReceiver, filter);
    }

    BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_HEADSET_PLUG)) {

                if (intent.hasExtra("state")) {
                    if (intent.getIntExtra("state", 0) == 0) {
                        Log.e(TAG, "headset out");

                        if (bg1Plugged) {
                            myAudio.setStreamVolume(AudioManager.STREAM_MUSIC, volumIndex, AudioManager.FLAG_SHOW_UI);// 还原音量
                            mTextView.setText(SDF.format(new Date()) + "headset out\n");
                        }
                        bg1Plugged = false;

                        if(mConnect1305Thread != null) {
                            mConnect1305Thread.interrupt();
                            mConnect1305Thread = null;
                        }
                        if(mConnect1304Thread != null) {
                            mConnect1304Thread.interrupt();
                            mConnect1304Thread = null;
                        }
                        if (null != BG1Timer) {
                            BG1Timer.cancel();
                            BG1Timer = null;
                        }
                        mBg1Control.stop();
                    }
                    if (intent.getIntExtra("state", 0) == 1) {
                        Log.e(TAG, "headset in\n");
                        bg1Plugged = true;

                        mTextView.setText(SDF.format(new Date()) + "headset in\n");
                        int maxVolum = myAudio.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
                        myAudio.setStreamVolume(AudioManager.STREAM_MUSIC, maxVolum, 1);// set the volum a max value to support communication

                        String QRInfo = mBg1Control.getBottleInfoFromQR(QRCode);
                        Log.e(TAG, "QRInfo =" + QRInfo);

                        mBg1Control.start();
                        BG1Timer = new Timer();
                        BG1TimerTask = new TimerTask() {
                            @Override
                            public void run() {
                                if(mConnect1304Thread == null) {
                                    mConnect1304Thread = new Thread(connect1304_Runnable);
                                    mConnect1304Thread.start();
                                }
                            }
                        };
                        BG1Timer.schedule(BG1TimerTask, 3500);
                    }
                }
            }
            //1304
            else if (action.equals(Bg1Profile.ACTION_BG1_DEVICE_ID)){
                String deviceId = intent.getStringExtra(Bg1Profile.BG1_DEVICE_ID);
                mTextView.append(SDF.format(new Date()) + "deviceId = " + deviceId + "\n");
            } else if (action.equals(Bg1Profile.ACTION_BG1_IDENTIFY_RESULT)) {
                int iden1304 = intent.getIntExtra(Bg1Profile.BG1_IDENTIFY_RESULT, 0);

                mTextView.append(SDF.format(new Date()) + "identifyFlag：" + iden1304 + "\n");
                if(iden1304 == 1) {
                    new Thread() {
                        public void run() {
                            mBg1Control.sendCode(QRCode, true);
                        }
                    }.start();
                } else if(iden1304 == 2) {
                    new Thread() {
                        public void run() {
                            mBg1Control.sendCode(QRCode, false);
                        }
                    }.start();
                }

            } else if (action.equals(Bg1Profile.ACTION_BG1_CONNECT_RESULT)) {
                int flag1304 = intent.getIntExtra(Bg1Profile.BG1_CONNECT_RESULT, -1);
                mTextView.append(SDF.format(new Date()) + "conect flag1304 = " + flag1304 + "\n");
                if(flag1304 == 0) {
                    mTextView.append(SDF.format(new Date()) + "connected,ready to measure\n");
                } else {
                    mTextView.append(SDF.format(new Date()) + "connect failed\n");
                    mBg1Control.stop();
                }
            }
            //1305
            else if (action.equals(Bg1Profile.ACTION_BG1_DEVICE_READY_NEW)) {
                if (null != BG1Timer) {
                    BG1Timer.cancel();
                    BG1Timer = null;
                }
                mTextView.append(SDF.format(new Date()) + "device handshake\n");
                if(mConnect1305Thread == null) {
                    mConnect1305Thread = new Thread(connect1305_Runnable);
                    mConnect1305Thread.start();
                }
            } else if (action.equals(Bg1Profile.ACTION_BG1_IDPS_NEW)) {
                String idps = intent.getStringExtra(Bg1Profile.BG1_IDPS_NEW);
                mTextView.append(SDF.format(new Date()) + "deviceInfo = " + idps + "\n");
                if(!idps.equals("")) {
                    new Thread() {
                        public void run() {
                            mBg1Control.sendCodeNew(QRCode);
                        }
                    }.start();

                }

            } else if (action.equals(Bg1Profile.ACTION_BG1_CONNECT_RESULT_NEW)) {
                int flag1305 =  intent.getIntExtra(Bg1Profile.BG1_CONNECT_RESULT_NEW, -1);
                mTextView.append(SDF.format(new Date()) + "connect flag1305 = " + flag1305 + "\n");
                if(flag1305 == 0) {
                    mTextView.append(SDF.format(new Date()) + "connected,ready to measure\n");
                } else {
                    mTextView.append(SDF.format(new Date()) + "connect failed\n");
                    mBg1Control.stop();

                }

            } else if (action.equals(Bg1Profile.ACTION_BG1_MEASURE_ERROR)) {
                int errorNum = intent.getIntExtra(Bg1Profile.BG1_MEASURE_ERROR, -1);
                Log.e(TAG, "---------------------msgError = " + errorNum + "------------------------------");

                mTextView.append(SDF.format(new Date()) + "msgError = " + errorNum + "\n");

            } else if (action.equals(Bg1Profile.ACTION_BG1_MEASURE_STRIP_IN)) {
                if (!isGetStripInBg1) {
                    isGetStripInBg1 = true;
                    Log.e(TAG, "---------------------msgStripIn------------------------------");

                    mTextView.append(SDF.format(new Date()) + "Strip In\n");
                }
                new Thread() {
                    @Override
                    public void run() {
                        SystemClock.sleep(3000);
                        isGetStripInBg1 = false;
                    }
                }.start();
            } else if (action.equals(Bg1Profile.ACTION_BG1_MEASURE_GET_BLOOD)) {
                if (!isGetBloodBg1) {
                    isGetBloodBg1 = true;
                    Log.e(TAG, "---------------------msgGetBlood------------------------------");

                    mTextView.append(SDF.format(new Date()) + "Get Blood\n");
                }
                new Thread() {
                    @Override
                    public void run() {
                        SystemClock.sleep(3000);
                        isGetBloodBg1 = false;
                    }
                }.start();
            } else if (action.equals(Bg1Profile.ACTION_BG1_MEASURE_RESULT)) {
                if (!isGetResultBg1) {
                    isGetResultBg1 = true;
                    int measureResult = intent.getIntExtra(Bg1Profile.BG1_MEASURE_RESULT, -1);
                    Log.e(TAG, "---------------------msgResult ＝ " + measureResult + "------------------------------");

                    mTextView.append(SDF.format(new Date()) + "msgResult ＝ " + measureResult + "\n");
                }
                new Thread() {
                    @Override
                    public void run() {
                        SystemClock.sleep(3000);
                        isGetResultBg1 = false;
                    }
                }.start();

            } else if (action.equals(Bg1Profile.ACTION_BG1_MEASURE_STRIP_OUT)) {
                Log.e(TAG, "---------------------msgStripOut------------------------------");
                mTextView.append(SDF.format(new Date()) + "Strip Out\n");
            } else if (action.equals(Bg1Profile.ACTION_BG1_MEASURE_STANDBY)) {

                if (!isGetResultBg1) {
                    isGetResultBg1 = true;
                    mTextView.append(SDF.format(new Date()) + "Stand By\n");
                }
                new Thread() {
                    @Override
                    public void run() {
                        SystemClock.sleep(3000);
                        isGetResultBg1 = false;
                    }
                }.start();
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        if (null != BG1Timer) {
            BG1Timer.cancel();
            BG1Timer = null;
        }
        mBg1Control.stop();

        unregisterReceiver(mBroadcastReceiver);
        super.onDestroy();
    }

}
