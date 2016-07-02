package com.android.mobileguard.service;

import java.util.List;

import com.android.mobileguard.activities.EnterLockedAppActivity;
import com.android.mobileguard.db.dao.AppLockDBDao;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

public class WatchDogService extends Service {
	private static final String TAG = "WatchDogService";
	private ActivityManager am ;
	private boolean flag;
	private AppLockDBDao dao;
	private String tempstoppropackname;//临时停止保护的包名
	private DogBroadcastReceiver receiver;
	private MyObserver observer;
	private List<String> lockedpacklist;//所有被锁定包名
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	@Override
	public void onCreate() {
		super.onCreate();
		dao = new AppLockDBDao(this);
		lockedpacklist = dao.findAll();
		Uri uri = Uri.parse("content://com.android.mobileguard.lockdb");
		observer = new MyObserver(new Handler());
		getContentResolver().registerContentObserver(uri, true, observer);
		receiver = new DogBroadcastReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("com.android.moblieguard.stoptemply");
		filter.addAction(Intent.ACTION_SCREEN_ON);
		filter.addAction(Intent.ACTION_SCREEN_OFF);
		registerReceiver(receiver, filter);
		
		am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		startWatchDog();
	}
	private void startWatchDog() {
		new Thread(){
			public void run() {
				flag = true;
				List<RunningTaskInfo> list;
				String packname;
				Intent intent = new Intent(WatchDogService.this,EnterLockedAppActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				while(flag){
					list = am.getRunningTasks(100);
					packname = list.get(0).topActivity.getPackageName();
					System.out.println(packname+"正在运行。。。。。。。。。");
					//查询数据库改为查询内存
					if(lockedpacklist.contains(packname) &&( ! packname.equals(tempstoppropackname))){
						//弹出输入密码对话框
						intent.putExtra("packname", packname);
						startActivity(intent);
					}
					try {
						Thread.sleep(30);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
				}
			};
		}.start();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		flag = false ;
		getContentResolver().unregisterContentObserver(observer);
		observer = null;
	}
	/**
	 * 内部广播接收者，屏幕关闭停止看门狗服务，屏幕亮起打开看门狗服务，节省电量
	 * @author feng
	 *
	 */
	private class DogBroadcastReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if("com.android.moblieguard.stoptemply".equals(action)){
				tempstoppropackname = intent.getStringExtra("packname");
			}else if(Intent.ACTION_SCREEN_ON.equals(action)){
				Log.i(TAG, "屏幕亮起，开启看门狗服务");
				if(flag = false){
					startWatchDog();
				}
				
			}else if(Intent.ACTION_SCREEN_OFF.equals(action)){
				Log.i(TAG, "屏幕关闭，关闭看门狗服务");
				flag = false;
			}
		}
		
	}
	
	private class MyObserver extends ContentObserver{

		
		public MyObserver(Handler handler) {
			super(handler);
			// TODO Auto-generated constructor stub
		}
		@Override
		public void onChange(boolean selfChange) {
			Log.i(TAG, "看门狗内容观察者发现数据库变化，重新获取数据库");
			lockedpacklist = dao.findAll();
			super.onChange(selfChange);
		}
		
	}

}
