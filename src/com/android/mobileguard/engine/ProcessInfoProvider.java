package com.android.mobileguard.engine;

import java.util.ArrayList;
import java.util.List;

import com.android.mobileguard.R;
import com.android.mobileguard.bean.ProcessInfo;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;

public class ProcessInfoProvider {
	
	public static List<ProcessInfo> getRunningProcessInfo(Context context){
		List<ProcessInfo> plist = new ArrayList<ProcessInfo>();
		ActivityManager am = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
		PackageManager pm = context.getPackageManager();
		List<RunningAppProcessInfo> templist = am.getRunningAppProcesses();
		for (RunningAppProcessInfo runningProInfo : templist) {
			ProcessInfo pinfo = new ProcessInfo();
			String packName = runningProInfo.processName;
			pinfo.setPackName(packName);
			long memSize = am.getProcessMemoryInfo(new int[]{runningProInfo.pid})[0].getTotalPrivateDirty()/2014;
			pinfo.setMemSize(memSize);
			try {
				PackageInfo packinfo = pm.getPackageInfo(packName, 0);
				Drawable icon = packinfo.applicationInfo.loadIcon(pm);
				pinfo.setAppIcon(icon);
				String appName = packinfo.applicationInfo.loadLabel(pm).toString();
				pinfo.setAppName(appName);
				
				if((packinfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM)!= 0){
					//系统进程
					pinfo.setSys(true);
				}else{
					//用户进程
					pinfo.setSys(false);
				}
				
			} catch (NameNotFoundException e) {
				pinfo.setAppName(packName);
				pinfo.setAppIcon(context.getResources().getDrawable(R.drawable.ic_launcher));
				e.printStackTrace();
			}
			plist.add(pinfo);
		}
		
		return plist;
	}
	
}
