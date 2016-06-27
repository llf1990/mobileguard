package com.android.mobileguard.bean;

import android.graphics.drawable.Drawable;


public class AppInfo {
	private Drawable appIcon;//ͼ��
	private String appName;//Ӧ������
	private long appSize;//Ӧ�ó����С
	private String packageName;//����
	private boolean inRom; //�Ƿ�װ���ڴ�
	private boolean isSystem;//�Ƿ�ϵͳӦ��
	
	public boolean isInRom() {
		return inRom;
	}
	public void setInRom(boolean inRom) {
		this.inRom = inRom;
	}
	public boolean isSystem() {
		return isSystem;
	}
	public void setSystem(boolean isSystem) {
		this.isSystem = isSystem;
	}
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public long getAppSize() {
		return appSize;
	}
	public void setAppSize(long appSize) {
		this.appSize = appSize;
	}
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	public Drawable getAppIcon() {
		return appIcon;
	}
	public void setAppIcon(Drawable appIcon) {
		this.appIcon = appIcon;
	}
	
}
