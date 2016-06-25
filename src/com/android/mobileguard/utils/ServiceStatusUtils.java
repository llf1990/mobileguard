package com.android.mobileguard.utils;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;


/**
 * 检查服务状态的工具类
 * @author feng
 *
 */
public class ServiceStatusUtils {

	public static boolean isServiceRunning(Context context,String serviceFullName){
		//得到系统的进程管理器
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		//得到系统里面正在运行的服务
		List<ActivityManager.RunningServiceInfo> list = am.getRunningServices(200);
		for (RunningServiceInfo info : list) {
			//获得服务的全路径名称并与传入的全路径名称比较
			if(serviceFullName.equals(info.service.getClassName())){
				return true;
			}
		}
		return false;
	}
}
