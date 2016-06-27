package com.android.mobileguard.activities;

import com.android.mobileguard.R;
import com.android.mobileguard.service.ShowAddressService;
import com.android.mobileguard.service.SmsCallSafeService;
import com.android.mobileguard.ui.SwitchImageView;
import com.android.mobileguard.utils.ServiceStatusUtils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
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
	
	//��ʾ���繦�ܿ���
	private SwitchImageView siv_setting_showlocation;
	private RelativeLayout rl_setting_showlocation; 
	
	
	
	//��������ʾ���
	private String[] styleName = {"��͸��","������","��ʿ��","������","ƻ����"};
	private int[] styleID = {R.drawable.call_locate_white,R.drawable.call_locate_orange,R.drawable.call_locate_blue,R.drawable.call_locate_gray,R.drawable.call_locate_green};
	
	
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
		
		siv_setting_showlocation = (SwitchImageView) findViewById(R.id.siv_setting_showlocation);
		rl_setting_showlocation = (RelativeLayout) findViewById(R.id.rl_setting_showlocation);
		
		
		
		
		
		//�жϷ����Ƿ�������״̬����д״̬������
		boolean status = ServiceStatusUtils.isServiceRunning(this, "com.android.mobileguard.service.SmsCallSafeService");
		siv_setting_intercept.setSwitchStatus(status);
		//�ж���ʾ�����ط����Ƿ�������д״̬������
		boolean isOpen = ServiceStatusUtils.isServiceRunning(this, "com.android.mobileguard.service.ShowAddressService");
		siv_setting_showlocation.setSwitchStatus(isOpen);
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
	
	public void showlocationswitch(View view){
		siv_setting_showlocation.changeStatus();
		boolean status = siv_setting_showlocation.getSwitchStatus();
		Intent service = new Intent(SettingActivity.this,ShowAddressService.class);
		if(status){
			startService(service);
		}else{
			stopService(service);
		}
	}
	
	public void setCallerlocStyle(View view){
		AlertDialog.Builder builder = new Builder(SettingActivity.this);
		builder.setIcon(R.drawable.dialog_title_default_icon);
		builder.setTitle("�����ط������");
		builder.setSingleChoiceItems(styleName, sp.getInt("stylewhich", 0),new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				 Editor editor =  sp.edit();
				 editor.putInt("stylewhich", which);
				 editor.commit();
				 dialog.dismiss();
			}
		});
		builder.show();
	}
}
