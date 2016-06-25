package com.android.mobileguard.utils;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;


/**
 * ������״̬�Ĺ�����
 * @author feng
 *
 */
public class ServiceStatusUtils {

	public static boolean isServiceRunning(Context context,String serviceFullName){
		//�õ�ϵͳ�Ľ��̹�����
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		//�õ�ϵͳ�����������еķ���
		List<ActivityManager.RunningServiceInfo> list = am.getRunningServices(200);
		for (RunningServiceInfo info : list) {
			//��÷����ȫ·�����Ʋ��봫���ȫ·�����ƱȽ�
			if(serviceFullName.equals(info.service.getClassName())){
				return true;
			}
		}
		return false;
	}
}
