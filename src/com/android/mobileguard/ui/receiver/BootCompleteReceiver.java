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
		Log.i(TAG, "�ֻ��������");
		
		String savedSim = sp.getString("bindedSimNum", "");
		TelephonyManager tm = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
		String currentSim = tm.getSimSerialNumber();
		if(currentSim.equals(savedSim)){
			//���SIM������Ϣһ��
			Log.i(TAG,"��󶨿���Ϣһ�£����������ֻ�");
		}else{
			//���SIM����һ�£����ܱ���
			Log.i(TAG,"��󶨿���һ�£����ܱ��������ͱ�������");
			String safenumber = sp.getString("safePhoneNum", "");
			SmsManager smsMan = SmsManager.getDefault();
			smsMan.sendTextMessage(safenumber, null, "SOS", null, null);
		}
		
	}

}
