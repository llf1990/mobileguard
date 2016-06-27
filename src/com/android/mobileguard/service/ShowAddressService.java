package com.android.mobileguard.service;

import com.android.mobileguard.R;
import com.android.mobileguard.db.dao.AddressDBDao;

import android.R.color;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.TextView;

public class ShowAddressService extends Service {
	private TelephonyManager tm ;
	private MyPhoneListener listener;
	private outCallReceiver outcallreceiver; 
	private View view;
	private WindowManager wm;
	private int[] styleID = {R.drawable.call_locate_white,R.drawable.call_locate_orange,R.drawable.call_locate_blue,R.drawable.call_locate_gray,R.drawable.call_locate_green};
	private WindowManager.LayoutParams params;
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public void onCreate() {
		//注册播出电话广播接收者
		wm = (WindowManager) getSystemService(WINDOW_SERVICE);
		IntentFilter filter = new IntentFilter();
		outcallreceiver = new outCallReceiver();
		filter.addAction(Intent.ACTION_NEW_OUTGOING_CALL);
		registerReceiver(outcallreceiver, filter);
		//注册一个电话监听服务
		listener = new MyPhoneListener();
		tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
		super.onCreate();
	}
	
	@Override
	public void onDestroy() {
		//取消拨出电话广播接收者
		unregisterReceiver(outcallreceiver);
		outcallreceiver = null;
		//关闭监听服务
		tm.listen(listener, PhoneStateListener.LISTEN_NONE);
		super.onDestroy();
	}
	
	private class MyPhoneListener extends PhoneStateListener{
		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			switch (state) {
			case TelephonyManager.CALL_STATE_RINGING://来电时调用查询归属地，并通过自定义toast显示
				String address = AddressDBDao.findLocation(incomingNumber);
				myToast(address);
				break;

			case TelephonyManager.CALL_STATE_IDLE://空闲时将view从windowmanager移除，并将view置为空
				if(view != null){
					wm.removeView(view);
					view = null;
				}
				break;
			}
			super.onCallStateChanged(state, incomingNumber);
		}
	}
	private class outCallReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			String number = getResultData();
			String location = AddressDBDao.findLocation(number);
			//Toast.makeText(ShowAddressService.this, location, 1).show();//当广播接收到电话时查询归属地，通过自定义toast显示
			myToast(location);
		}
		
	}
	//自定义toast控制显示时长
	private void  myToast(String text){
	view = View.inflate(this, R.layout.item_callerlocview, null);
	view.setOnTouchListener(new OnTouchListener() {
		int startX,startY;
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				startX = (int) event.getRawX();
				startY = (int) event.getRawY();
				break;

			case MotionEvent.ACTION_MOVE:
				int newX = (int) event.getRawX();
				int newY = (int) event.getRawY();
				int dx = newX - startX;
				int dy = newY - startY;
				params.x += dx;
				params.y += dy;
				startX = (int) event.getRawX();
				startY = (int) event.getRawY();
				wm.updateViewLayout(view, params);
				break;
			}
			return true;
		}
	});
	SharedPreferences sp = getSharedPreferences("settingconfig", MODE_PRIVATE);
	int which= sp.getInt("stylewhich", 0);
	view.setBackgroundResource(styleID[which]);
	TextView tv_toast_location = (TextView) view.findViewById(R.id.tv_toast_location);
	tv_toast_location.setText(text);
	tv_toast_location.setTextColor(Color.BLUE);
	tv_toast_location.setTextSize(20);
	params = new LayoutParams();
	params.gravity = Gravity.LEFT+Gravity.TOP;
	params.x = 200;
	params.y = 200;
	params.width = WindowManager.LayoutParams.WRAP_CONTENT;
	params.height = WindowManager.LayoutParams.WRAP_CONTENT;
	params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE 
			|WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
			|WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
	params.format = PixelFormat.TRANSLUCENT;
	params.type = WindowManager.LayoutParams.TYPE_PRIORITY_PHONE;
	wm.addView(view, params);
	}

}
