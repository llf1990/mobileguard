package com.android.mobileguard.activities;

import com.android.mobileguard.R;
import com.android.mobileguard.service.RocketService;
import com.android.mobileguard.utils.SmsTools;
import com.android.mobileguard.utils.SmsTools.BackUpInterface;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

public class CommonToolsActivity extends Activity {

	protected static final String TAG = "CommonToolsActivity";
	private RelativeLayout rl_setting_rocket;
	private ProgressDialog pd ;
	private ProgressBar pb;
	private boolean rocketswitch;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_commontools);
		rl_setting_rocket = (RelativeLayout) findViewById(R.id.rl_setting_rocket);
		pb = (ProgressBar) findViewById(R.id.progressBar1);
	}
	public void queryCallerLoc(View view){
		Intent intent = new Intent(CommonToolsActivity.this,CallerLocQueryActivity.class);
		startActivity(intent);
	}
	
	@SuppressWarnings("unused")
	public void rocketswitch (View view){
		Intent intent  = new Intent(CommonToolsActivity.this,RocketService.class );
		if(!rocketswitch){
				Log.i(TAG, "服务开启");
				startService(intent);
				rocketswitch = true;
			}else{
				stopService(intent);
				rocketswitch = false;
			}
		
	}
	
	public void smsBackup(View view){
		Log.i(TAG,"开始短信备份");
		pd = new ProgressDialog(CommonToolsActivity.this);
		pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		pd.show();
		new Thread(){
			public void run(){
				SmsTools.bakupSms(CommonToolsActivity.this,new BackUpInterface() {
					
					@Override
					public void setMaxBefore(int max) {
						pd.setMax(max);
						pb.setMax(max);
					}
					
					@Override
					public void setInProgress(int progress) {
						pd.setProgress(progress);
						pb.setProgress(progress);
					}
				});
				pd.dismiss();
			}
		}.start();
	}
	public void enterAppLock(View view){
		Intent intent = new Intent(this,AppLockActivity.class);
		startActivity(intent);
	}
	
}
