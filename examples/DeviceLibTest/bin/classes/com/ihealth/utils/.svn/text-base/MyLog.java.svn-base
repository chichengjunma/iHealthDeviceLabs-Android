package com.ihealth.utils;

import android.util.Log;

public class MyLog {
	
	private String tagString;
	private boolean flag = true;
	
	public MyLog(String tag){
		this.tagString = tag;
	}
	
	public void i(String info) {
		if (flag) {
			Log.i(tagString, info);
		}
	}
	
	public void e(String info) {
		if (flag) {
			Log.e(tagString, info);
		}
	}
}
