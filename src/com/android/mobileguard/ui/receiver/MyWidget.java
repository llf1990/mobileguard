package com.android.mobileguard.ui.receiver;

import com.android.mobileguard.service.UpdatewidgetService;

import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
/**
 * 桌面空间实际是特殊广播接收者
 * @author feng
 *
 */
public class MyWidget extends AppWidgetProvider {
	@Override
	public void onEnabled(Context context) {
		System.out.println("my widget 被创建了");
		Intent intent = new Intent(context,UpdatewidgetService.class);
		context.startService(intent);
		super.onEnabled(context);
	}
	
	@Override
	public void onDisabled(Context context) {
		
		super.onDisabled(context);
		System.out.println("my widget 被销毁了");
		Intent intent = new Intent(context,UpdatewidgetService.class);
		context.stopService(intent);
	}
}
