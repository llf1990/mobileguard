	package com.android.mobileguard.utils;

import java.io.File;

import android.os.Environment;

public class SystemInfoUtils {
	
	/**
	 * 获取内部存储空间总大小
	 * @return
	 */
	public static long getInternalStorageSize(){
		File file = Environment.getDataDirectory();
		
		return file.getTotalSpace();
		
	}
	
	/**
	 * 获取内部存储空间剩余
	 * @return
	 */
	public static long getInternalStorageAvailableSize(){
		File file = Environment.getDataDirectory();
		
		return file.getFreeSpace();
		
	}
	
	/**
	 * 获取外部存储空间剩余
	 * @return
	 */
	public static long getExternalStorageAvailableSize(){
		File file = Environment.getExternalStorageDirectory();
		
		return file.getFreeSpace();
		
	}
}
