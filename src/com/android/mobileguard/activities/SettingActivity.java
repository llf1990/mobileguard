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
	//�Զ����¿ؼ�
	private SwitchImageView siv_setting_status;
	private RelativeLayout rl_setting_update;
	//ɧ�����ؿؼ�
	private SwitchImageView siv_setting_intercept;
	private RelativeLayout rl_setting_intercept;
	
	private SharedPreferences sp;  //��¼����
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.i(TAG,"ɧ�����ط����ѿ���");
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		//��ʼ��sp
		sp = getSharedPreferences("settingconfig",MODE_PRIVATE);
		
		siv_setting_status = (SwitchImageView) findViewById(R.id.siv_setting_status);
		siv_setting_status.setSwitchStatus(sp.getBoolean("status", true));
		rl_setting_update = (RelativeLayout) findViewById(R.id.rl_setting_update);
		
		siv_setting_intercept = (SwitchImageView) findViewById(R.id.siv_setting_intercept);
		
		rl_setting_intercept = (RelativeLayout) findViewById(R.id.rl_setting_intercept);
		//�жϷ����Ƿ�������״̬����д״̬������
		boolean status = ServiceStatusUtils.isServiceRunning(this, "com.android.mobileguard.service.SmsCallSafeService");
		siv_setting_intercept.setSwitchStatus(status);
	}
	@Override
	protected void onDestroy() {
		Log.i(TAG,"ɧ�����ط����ѹر�");
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
