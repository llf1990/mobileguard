package com.android.mobileguard.ui.receiver;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class KillAllProcessReceiver extends BroadcastReceiver {

	private static final String TAG = "KillAllProcessReceiver";

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.i(TAG, "�Զ���㲥����������");
		ActivityManager am = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
		List <RunningAppProcessInfo> rlist = am.getRunningAppProcesses();
		for (RunningAppProcessInfo rInfo : rlist) {
			am.killBackgroundProcesses(rInfo.processName);
		}
		Toast.makeText(context, "�������", 1).show();
	}

}
