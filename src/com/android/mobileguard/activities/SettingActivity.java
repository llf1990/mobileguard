package com.android.mobileguard.activities;

import com.android.mobileguard.R;
import com.android.mobileguard.ui.SwitchImageView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

public class SettingActivity extends Activity {
	
	private SwitchImageView siv_setting_status;
	private RelativeLayout rl_setting_update;
	private SharedPreferences sp;  //记录设置
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		//初始化sp
		sp = getSharedPreferences("settingconfig",MODE_PRIVATE);
		
		siv_setting_status = (SwitchImageView) findViewById(R.id.siv_setting_status);
		siv_setting_status.setSwitchStatus(sp.getBoolean("status", true));
		rl_setting_update = (RelativeLayout) findViewById(R.id.rl_setting_update);
	}
	
	
	@SuppressLint("CommitPrefEdits") public void changeStatus(View view){
		siv_setting_status.changeStatus();
		Editor editor = sp.edit();
		editor.putBoolean("status", siv_setting_status.getSwitchStatus());
		editor.commit();
	}
}
