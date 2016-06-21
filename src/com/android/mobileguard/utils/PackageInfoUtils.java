package com.android.mobileguard.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
/**
 * 获取包信息的工具类
 * @author feng
 *
 */
public class PackageInfoUtils {
	
	
	public static String getPackageVersion(Context context){
		PackageInfo packageinfo;
		try {
			packageinfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			String version = packageinfo.versionName;
			return version;
			
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return "解析版本号失败";
		}
	}
	 

}
