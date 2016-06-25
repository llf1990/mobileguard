package com.android.mobileguard.activities;

import com.android.mobileguard.R;
import com.android.mobileguard.service.SmsCallSafeService;
import com.android.mobileguard.ui.SwitchImageView;
import com.android.mobileguard.utils.ServiceStatusUtils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

public class SettingActivity extends Activity {
	private static final String TAG = "SettingActivity";
	//自动更新控件
	private SwitchImageView siv_setting_status;
	private RelativeLayout rl_setting_update;
	//骚扰拦截控件
	private SwitchImageView siv_setting_intercept;
	private RelativeLayout rl_setting_intercept;
	
	private SharedPreferences sp;  //记录设置
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.i(TAG,"骚扰拦截服务已开启");
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		//初始化sp
		sp = getSharedPreferences("settingconfig",MODE_PRIVATE);
		
		siv_setting_status = (SwitchImageView) findViewById(R.id.siv_setting_status);
		siv_setting_status.setSwitchStatus(sp.getBoolean("status", true));
		rl_setting_update = (RelativeLayout) findViewById(R.id.rl_setting_update);
		
		siv_setting_intercept = (SwitchImageView) findViewById(R.id.siv_setting_intercept);
		
		rl_setting_intercept = (RelativeLayout) findViewById(R.id.rl_setting_intercept);
		//判断服务是否处于运行状态，回写状态到界面
		boolean status = ServiceStatusUtils.isServiceRunning(this, "com.android.mobileguard.service.SmsCallSafeService");
		siv_setting_intercept.setSwitchStatus(status);
	}
	@Override
	protected void onDestroy() {
		Log.i(TAG,"骚扰拦截服务已关闭");
		super.onDestroy();
	}
	
	@SuppressLint("CommitPrefEdits") 
	public void changeStatus(View view){
		siv_setting_status.changeStatus();
		Editor editor = sp.edit();
		editor.putBoolean("status", siv_setting_status.getSwitchStatus());
		editor.commit();
	}
	
	public void changeInterceptStatus(View view){
		siv_setting_intercept.changeStatus();
		boolean status = siv_setting_intercept.getSwitchStatus();
		Intent service = new Intent(SettingActivity.this,SmsCallSafeService.class);
		if(status){
			startService(service);
		}else{
			stopService(service);
		}
	}
}
