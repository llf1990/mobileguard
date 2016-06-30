package com.android.mobileguard.service;

import java.util.Timer;
import java.util.TimerTask;

import com.android.mobileguard.R;
import com.android.mobileguard.ui.receiver.MyWidget;

import android.app.ActivityManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.ActivityManager.MemoryInfo;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.IBinder;
import android.text.format.Formatter;
import android.widget.RemoteViews;

public class UpdatewidgetService extends Service {

	private Timer timer;
	private TimerTask task;
	private AppWidgetManager awm;
	@Override
	public IBinder onBind(Intent intent) {
		
		return null;
	}

	@Override
	public void onCreate() {
		
		timer = new Timer();
		task = new TimerTask(){

			@Override
			public void run() {
				/**
				 * 由于更新桌面进程的view对象，因此需要将应用程序里的view对象传递给桌面
				 * 要求view是特殊类型
				 */
				
				RemoteViews views = new RemoteViews(getPackageName(), R.layout.process_widget);
				views.setTextViewText(R.id.process_count, "正在运行软件"+getRunningProcess());
				views.setTextViewText(R.id.process_memory, "内存剩余空间"+Formatter.formatFileSize(UpdatewidgetService.this, getMemoryAvaliable()));
				/**
				 * 延期意图是让另一个应用执行该意图
				 * 自定义广播消息
				 */
				
				Intent intent = new Intent();
				intent.setAction("com.android.mobileguard.killall");
				PendingIntent pi =PendingIntent.getBroadcast(UpdatewidgetService.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
				/**
				 *给意见清理按钮注册一个点击事件，事件是一个延期的意图 
				 */
				views.setOnClickPendingIntent(R.id.btn_clear, pi);
				ComponentName cpn = new ComponentName(getApplicationContext(),MyWidget.class);
				awm.updateAppWidget(cpn, views);
				
			}
			
		};
		
		timer.schedule(task, 0,3000);
		
		super.onCreate();
	}
	
	@Override
	public void onDestroy() {
		
		super.onDestroy();
	}
	/**
	 * 获取系统当前运行进程数量
	 * @return
	 */
	public int getRunningProcess() {
		ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		return am.getRunningAppProcesses().size();
	}

	public long getMemoryAvaliable() {
		ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		// 获取当前系统内存信息，将信息存放在outinfo里面
		MemoryInfo outinfo = new MemoryInfo();
		am.getMemoryInfo(outinfo);
		return outinfo.availMem;
	}
}
