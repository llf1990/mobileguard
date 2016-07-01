package com.android.mobileguard.activities;

import com.android.mobileguard.R;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class EnterLockedAppActivity extends Activity {
	
	private ImageView iv_lockedapp_icon;
	private TextView tv_lockedapp_name;
	private Button bt_lock_open;
	private EditText et_lock_pwd;
	private String packname;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_enter_lockpwd);
		iv_lockedapp_icon = (ImageView) findViewById(R.id.iv_lockedapp_icon);
		tv_lockedapp_name = (TextView) findViewById(R.id.tv_lockedapp_name);
		et_lock_pwd = (EditText) findViewById(R.id.et_lock_pwd);
		bt_lock_open = (Button) findViewById(R.id.bt_lock_open);
		
		bt_lock_open.setOnClickListener( new OnClickListener() {
			

			@Override
			public void onClick(View v) {
				if("123".equals(et_lock_pwd.getText().toString().trim())){
					//密码正确，发送消息给看门狗，临时停止保护
					Intent intent = new Intent();
					intent.setAction("com.android.moblieguard.stoptemply");
					intent.putExtra("packname", packname);
					sendBroadcast(intent);
					//密码正确关闭界面
					finish();
				}else{
					Toast.makeText(EnterLockedAppActivity.this, "密码错误", 0).show();
				}
				
				
			}
		});
		
		
		Intent intent = getIntent();
		packname = intent.getStringExtra("packname");
		PackageManager pm = getPackageManager();
		try {
			ApplicationInfo appinfo = pm.getApplicationInfo(packname, 0);
			iv_lockedapp_icon.setImageDrawable(appinfo.loadIcon(pm));
			tv_lockedapp_name.setText(appinfo.loadLabel(pm));
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		
	}
}	
