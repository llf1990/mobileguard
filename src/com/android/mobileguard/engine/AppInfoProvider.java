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
 * ��ȡ����APP��Ϣ
 * @author feng
 *
 */
public class AppInfoProvider {
	/**
	 * ��ȡ����APP��Ϣ
	 * @return
	 */
	public static List<AppInfo> getAllAppInfo(Context context){
		//PackageManager��������
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
			//ϵͳӦ��
				ainfo.setSystem(true);
			}else{
				//�û�Ӧ��
				ainfo.setSystem(false);
			}
			if((flag &ApplicationInfo.FLAG_EXTERNAL_STORAGE) != 0){
				//��װ��sd��
				ainfo.setInRom(false);
			}else{
				//��װ���ڴ�
				ainfo.setInRom(true);
			}
			applist.add(ainfo);
		}
		return applist;
	}
}
