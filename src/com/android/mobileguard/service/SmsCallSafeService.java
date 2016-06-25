package com.android.mobileguard.service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.android.internal.telephony.ITelephony;
import com.android.mobileguard.db.dao.InterceptDao;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.telephony.PhoneStateListener;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.util.Log;

public class SmsCallSafeService extends Service {
	public static final String TAG = "SmsCallSafeService";
	private TelephonyManager tm;
	private MyPhoneStateListener listener;
	private InterceptDao dao;
	private InnerSmsReceiver receiver;
	public SmsCallSafeService() {
	}

	private class InnerSmsReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			Log.i(TAG, "�����ڲ��㲥�������յ�����");
			Object[] obj =  (Object[]) intent.getExtras().get("pdus");
			for (Object object : obj) {
				SmsMessage sms = SmsMessage.createFromPdu( (byte[]) object);
				String sender = sms.getOriginatingAddress();
				String mode = dao.find(sender);
				if("1".equals(mode) || "2".equals(mode)){
					//�Զ��Ž�������
					Log.i(TAG, "ɧ�Ŷ��ŵ���������");
					abortBroadcast();
				}
			}
		}
		
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public void onCreate() {
		receiver = new InnerSmsReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.provider.Telephony.SMS_RECEIVED");
		filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
		registerReceiver(receiver, filter);
		dao = new InterceptDao(this);
		//ע��ײ�绰���������
		tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
		super.onCreate();
	}
	
	@Override
	public void onDestroy() {
		unregisterReceiver(receiver);
		receiver = null;
		tm.listen(listener, PhoneStateListener.LISTEN_NONE);
		listener = null;
		
	}
	
	private class MyPhoneStateListener extends PhoneStateListener{
		
		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			super.onCallStateChanged(state, incomingNumber);
			switch (state) {
			case TelephonyManager.CALL_STATE_IDLE://����״̬
				
				break;
			case TelephonyManager.CALL_STATE_RINGING://����״̬
				String mode = dao.find(incomingNumber);
				if("1".equals(mode) || "3".equals(mode)){
					Log.i(TAG, "�绰����");
					endCall();
				}
				break;
			case TelephonyManager.CALL_STATE_OFFHOOK://��ͨ״̬
	
				break;

			}
		}
		
	}
	
	public void endCall(){
		try {
			Class clazz = getClassLoader().loadClass("android.os.ServiceManager");
			Method method = clazz.getDeclaredMethod("getService",String.class);
			IBinder ibinder = (IBinder) method.invoke(null, Context.TELEPHONY_SERVICE);
			ITelephony itelephony = ITelephony.Stub.asInterface(ibinder);
			itelephony.endCall();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	public void deleteCallLog(final String inComingNumber){
		final ContentResolver resolver = getContentResolver();
		final Uri uri = Uri.parse("content://call_log/calls");
		resolver.registerContentObserver(uri, true, new ContentObserver( new Handler()) {
			private void onchanged(boolean selfChange) {
				//���ݹ۲��߹۲쵽���ݿ�仯���÷���
				super.onChange(selfChange);
				resolver.delete(uri, "number = ?", new String[]{inComingNumber});
			}
		});
		
	}
}
