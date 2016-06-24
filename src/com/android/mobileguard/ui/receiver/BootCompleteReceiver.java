package com.android.mobileguard.ui.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;

public class BootCompleteReceiver extends BroadcastReceiver {

	private static final String TAG = "BootCompleteReceiver";

	public BootCompleteReceiver() {
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		SharedPreferences sp = context.getSharedPreferences("account", context.MODE_PRIVATE);
		Log.i(TAG, "手机启动完毕");
		
		String savedSim = sp.getString("bindedSimNum", "");
		TelephonyManager tm = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
		String currentSim = tm.getSimSerialNumber();
		if(currentSim.equals(savedSim)){
			//与绑定SIM卡绑定信息一致
			Log.i(TAG,"与绑定卡信息一致，还是您的手机");
		}else{
			//与绑定SIM卡不一致，可能被盗
			Log.i(TAG,"与绑定卡不一致，可能被盗，发送报警短信");
			String safenumber = sp.getString("safePhoneNum", "");
			SmsManager smsMan = SmsManager.getDefault();
			smsMan.sendTextMessage(safenumber, null, "SOS", null, null);
		}
		
	}

}
