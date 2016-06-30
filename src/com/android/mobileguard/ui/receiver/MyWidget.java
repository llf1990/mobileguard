package com.android.mobileguard.ui.receiver;

import com.android.mobileguard.service.UpdatewidgetService;

import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
/**
 * ����ռ�ʵ��������㲥������
 * @author feng
 *
 */
public class MyWidget extends AppWidgetProvider {
	@Override
	public void onEnabled(Context context) {
		System.out.println("my widget ��������");
		Intent intent = new Intent(context,UpdatewidgetService.class);
		context.startService(intent);
		super.onEnabled(context);
	}
	
	@Override
	public void onDisabled(Context context) {
		
		super.onDisabled(context);
		System.out.println("my widget ��������");
		Intent intent = new Intent(context,UpdatewidgetService.class);
		context.stopService(intent);
	}
}
