package com.android.mobileguard.activities;

import com.android.mobileguard.R;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class LostFindActivity extends Activity {
	private ImageView iv_lostfind_islock;
	private TextView tv_lostfind_safephone;
	private SharedPreferences sp;
	
	
	public LostFindActivity() {
		
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lostfind);
		sp = getSharedPreferences("account", MODE_PRIVATE);
		iv_lostfind_islock = (ImageView) findViewById(R.id.iv_lostfind_islock);
		tv_lostfind_safephone = (TextView) findViewById(R.id.tv_lostfind_safephone);
		boolean protecting = sp.getBoolean("protecting",false);
		String safePhoneNum = sp.getString("safePhoneNum", "");
		
		tv_lostfind_safephone.setText(safePhoneNum);
		if(!protecting){
			iv_lostfind_islock.setImageResource(R.drawable.unlock);
		}else{
			iv_lostfind_islock.setImageResource(R.drawable.lock);
		}
		
		
	}
}
