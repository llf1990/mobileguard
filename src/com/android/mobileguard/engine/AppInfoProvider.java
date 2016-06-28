package com.android.mobileguard.engine;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.format.Formatter;

import com.android.mobileguard.bean.AppInfo;


/**
 * 获取所有APP信息
 * @author feng
 *
 */
public class AppInfoProvider {
	/**
	 * 获取所有APP信息
	 * @return
	 */
	public static List<AppInfo> getAllAppInfo(Context context){
		//PackageManager包管理器
		PackageManager pm = context.getPackageManager();
		List<PackageInfo> list = pm.getInstalledPackages(0);
		List<AppInfo> applist = new ArrayList<AppInfo>();
		for (PackageInfo appInfo : list) {
			AppInfo ainfo = new AppInfo();
			ainfo.setAppName(appInfo.packageName);
			ainfo.setAppIcon(appInfo.applicationInfo.loadIcon(pm));
			ainfo.setPackageName(appInfo.packageName);
			String apkpath = appInfo.applicationInfo.sourceDir;
			File file = new File(apkpath);
			ainfo.setAppSize(file.length());
			int flag  = appInfo.applicationInfo.flags;
			if((flag & ApplicationInfo.FLAG_SYSTEM) != 0){
			//系统应用
				ainfo.setSystem(true);
			}else{
				//用户应用
				ainfo.setSystem(false);
			}
			if((flag &ApplicationInfo.FLAG_EXTERNAL_STORAGE) != 0){
				//安装在sd卡
				ainfo.setInRom(false);
			}else{
				//安装在内存
				ainfo.setInRom(true);
			}
			applist.add(ainfo);
		}
		return applist;
	}
}
