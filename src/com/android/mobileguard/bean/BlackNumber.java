package com.android.mobileguard.bean;

public class BlackNumber {
	
	private String blackNumber;
	private String mode;//1全拦截，2短信，3电话
	
	public BlackNumber() {
	}

	public String getBlackNumber() {
		return blackNumber;
	}

	public void setBlackNumber(String blackNumber) {
		this.blackNumber = blackNumber;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}
	
}
