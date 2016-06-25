package com.android.mobileguard.ui.receiver;

import com.android.mobileguard.R;
import com.android.mobileguard.service.LocationService;

import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.telephony.SmsMessage;
import android.util.Log;

public class SmsReceiver extends BroadcastReceiver {

	private static final String TAG = "SmsReceiver";

	public SmsReceiver() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.i(TAG, "��������");
		Object[] objs = (Object[]) intent.getExtras().get("puds");
		for (Object object : objs) {
			SmsMessage smsMes = SmsMessage.createFromPdu((byte[]) object);
			String body = smsMes.getMessageBody();
			if("#*location*#".equals(body)){
				Log.i(TAG, "�����ֻ�λ��");
				//����һ����̨���񷵻��ֻ���γ��
				Intent inte = new Intent(context,LocationService.class);
				context.startService(inte);
				abortBroadcast();
			}else if( "#*alarm*#".equals(body)){
				Log.i(TAG, "���ű�������");
				MediaPlayer mplayer = MediaPlayer.create(context, R.raw.iswear);
				mplayer.start();
				abortBroadcast();
			}else if( "#*wipedata*#".equals(body)){
				Log.i(TAG, "�����������");
				DevicePolicyManager dpm = (DevicePolicyManager) context.getSystemService(context.DEVICE_POLICY_SERVICE);
				//�����������ݣ�0��ʾ�����ڴ�����
				dpm.wipeData(DevicePolicyManager.WIPE_EXTERNAL_STORAGE);
				
				abortBroadcast();
			}else if( "#*lockscreen*#".equals(body)){
				Log.i(TAG, "��������");
				DevicePolicyManager dpm = (DevicePolicyManager) context.getSystemService(context.DEVICE_POLICY_SERVICE);
				dpm.lockNow();
				
				abortBroadcast();
			}
			
			
		}
	}

}
