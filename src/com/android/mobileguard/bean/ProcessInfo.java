package com.android.mobileguard.bean;

import android.graphics.drawable.Drawable;

public class ProcessInfo {
	private Drawable appIcon; //应用图标
	private String appName;	//应用名称
	private String packName;	//应用包名
	private long memSize;	//内存占用大小
	private boolean isSys;	//是否系统进行
	private boolean ischecked;//checkbox的备选状态
	public Drawable getAppIcon() {
		return appIcon;
	}
	public void setAppIcon(Drawable appIcon) {
		this.appIcon = appIcon;
	}
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public String getPackName() {
		return packName;
	}
	public void setPackName(String packName) {
		this.packName = packName;
	}
	public long getMemSize() {
		return memSize;
	}
	public void setMemSize(long memSize) {
		this.memSize = memSize;
	}
	public boolean isSys() {
		return isSys;
	}
	public void setSys(boolean isSys) {
		this.isSys = isSys;
	}
	@Override
	public String toString() {
		return "ProcessInfo [appIcon=" + appIcon + ", appName=" + appName
				+ ", packName=" + packName + ", memSize=" + memSize
				+ ", isSys=" + isSys + "]";
	}
	public boolean isIschecked() {
		return ischecked;
	}
	public void setIschecked(boolean ischecked) {
		this.ischecked = ischecked;
	}
	
}
