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
import android.os.IBinder;

public class WatchDogService extends Service {
	private ActivityManager am ;
	private boolean flag;
	private AppLockDBDao dao;
	private String tempstoppropackname;//临时停止保护的包名
	private DogBroadcastReceiver receiver;
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	@Override
	public void onCreate() {
		super.onCreate();
		dao = new AppLockDBDao(this);
		receiver = new DogBroadcastReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("com.android.moblieguard.stoptemply");
		registerReceiver(receiver, filter);
		
		am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		new Thread(){
			public void run() {
				flag = true;
				while(flag){
					List<RunningTaskInfo> list = am.getRunningTasks(100);
					String packname = list.get(0).topActivity.getPackageName();
					System.out.println(packname+"正在运行。。。。。。。。。");
					if(dao.find(packname) &&( ! packname.equals(tempstoppropackname))){
						//弹出输入密码对话框
						Intent intent = new Intent(WatchDogService.this,EnterLockedAppActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						intent.putExtra("packname", packname);
						startActivity(intent);
					}
					try {
						Thread.sleep(300);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
				}
			};
		}.start();
	}
	
	private void onstop() {
		flag = false ;

	}
	private class DogBroadcastReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			tempstoppropackname = intent.getStringExtra("packname");
		}
		
	}

}
