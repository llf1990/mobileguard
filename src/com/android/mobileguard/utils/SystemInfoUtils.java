	package com.android.mobileguard.utils;

import java.io.File;

import android.os.Environment;

public class SystemInfoUtils {
	
	/**
	 * ��ȡ�ڲ��洢�ռ��ܴ�С
	 * @return
	 */
	public static long getInternalStorageSize(){
		File file = Environment.getDataDirectory();
		
		return file.getTotalSpace();
		
	}
	
	/**
	 * ��ȡ�ڲ��洢�ռ�ʣ��
	 * @return
	 */
	public static long getInternalStorageAvailableSize(){
		File file = Environment.getDataDirectory();
		
		return file.getFreeSpace();
		
	}
	
	/**
	 * ��ȡ�ⲿ�洢�ռ�ʣ��
	 * @return
	 */
	public static long getExternalStorageAvailableSize(){
		File file = Environment.getExternalStorageDirectory();
		
		return file.getFreeSpace();
		
	}
}
