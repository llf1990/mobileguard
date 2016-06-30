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
				 * ���ڸ���������̵�view���������Ҫ��Ӧ�ó������view���󴫵ݸ�����
				 * Ҫ��view����������
				 */
				
				RemoteViews views = new RemoteViews(getPackageName(), R.layout.process_widget);
				views.setTextViewText(R.id.process_count, "�����������"+getRunningProcess());
				views.setTextViewText(R.id.process_memory, "�ڴ�ʣ��ռ�"+Formatter.formatFileSize(UpdatewidgetService.this, getMemoryAvaliable()));
				/**
				 * ������ͼ������һ��Ӧ��ִ�и���ͼ
				 * �Զ���㲥��Ϣ
				 */
				
				Intent intent = new Intent();
				intent.setAction("com.android.mobileguard.killall");
				PendingIntent pi =PendingIntent.getBroadcast(UpdatewidgetService.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
				/**
				 *���������ťע��һ������¼����¼���һ�����ڵ���ͼ 
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
	 * ��ȡϵͳ��ǰ���н�������
	 * @return
	 */
	public int getRunningProcess() {
		ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		return am.getRunningAppProcesses().size();
	}

	public long getMemoryAvaliable() {
		ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		// ��ȡ��ǰϵͳ�ڴ���Ϣ������Ϣ�����outinfo����
		MemoryInfo outinfo = new MemoryInfo();
		am.getMemoryInfo(outinfo);
		return outinfo.availMem;
	}
}
