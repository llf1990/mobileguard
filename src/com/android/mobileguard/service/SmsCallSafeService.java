package com.android.mobileguard.service;

import com.android.mobileguard.db.dao.InterceptDao;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

public class SmsCallSafeService extends Service {
	public static final String TAG = "SmsCallSafeService";
	private TelephonyManager tm;
	private MyPhoneStateListener listener;
	private InterceptDao dao;
	public SmsCallSafeService() {
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public void onCreate() {
		dao = new InterceptDao(this);
		//×¢²áµ×²ãµç»°·þÎñ¼àÌýÆ÷
		tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
		super.onCreate();
	}
	
	@Override
	public void onDestroy() {
		tm.listen(listener, PhoneStateListener.LISTEN_NONE);
		listener = null;
		
	}
	
	private class MyPhoneStateListener extends PhoneStateListener{
		
		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			super.onCallStateChanged(state, incomingNumber);
			switch (state) {
			case TelephonyManager.CALL_STATE_IDLE://¿ÕÏÐ×´Ì¬
				
				break;
			case TelephonyManager.CALL_STATE_RINGING://ÏìÁå×´Ì¬
				String mode = dao.find(incomingNumber);
				if("1".equals(mode) || "3".equals(mode)){
					Log.i(TAG, "µç»°À¹½Ø");
				}
				break;
			case TelephonyManager.CALL_STATE_OFFHOOK://½ÓÍ¨×´Ì¬
	
				break;

			}
		}
		
	}

}
