package com.android.mobileguard.activities;

import com.android.mobileguard.R;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class SetFindStepSec extends SetBaseActivity {

	private Button bt_setsec_next;
	private Button bt_setsec_pre;
	private Editor editor;
	private Button bt_setsec_bind;
	private TelephonyManager tm;
	private ImageView iv_setsec_bind;
	
	public SetFindStepSec() {
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set_stepsec);
		
		bt_setsec_next = (Button) findViewById(R.id.bt_setsec_next);
		bt_setsec_pre = (Button) findViewById(R.id.bt_setsec_pre);
		bt_setsec_bind = (Button) findViewById(R.id.bt_setsec_binding);
		iv_setsec_bind = (ImageView) findViewById(R.id.iv_setsec_bind);
		//获取editor对象
		editor = getSp().edit(); 
		//获取电话管理的服务
		tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		
		String bind = getSp().getString("bindedSimNum", null);
		if(TextUtils.isEmpty(bind)){
			iv_setsec_bind.setImageResource(R.drawable.unlock);
		}else{
			iv_setsec_bind.setImageResource(R.drawable.lock);

		}
		
		
		bt_setsec_bind.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String bindedNum  = getSp().getString("bindedSimNum",null);
				if(!TextUtils.isEmpty(bindedNum)){
					//如果绑定，点击解绑，并将存储的sim号清空
					editor.putString("bindedSimNum",null);
					editor.commit();
					iv_setsec_bind.setImageResource(R.drawable.unlock);
				}else{
					//未绑定，将sim信息进行存储
					String simNum = tm.getSimSerialNumber();
					editor.putString("bindedSimNum", simNum);
					editor.commit();
					iv_setsec_bind.setImageResource(R.drawable.lock);
				}
				
			}
		});
		
		bt_setsec_next.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				next();
			}
		});
		
		bt_setsec_pre.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				pre();
			}
		});
	}

	@Override
	void next() {
		String bind = getSp().getString("bindedSimNum", null);
		if(!TextUtils.isEmpty(bind)){
			Intent intent = new Intent(SetFindStepSec.this,SetFindStepThird.class);
			startActivity(intent);
			finish();
		}else{
			Toast.makeText(this, "请先绑定SIM卡", 0).show();
			return;
		}
		
	}

	@Override
	void pre() {
		Intent intent = new Intent(SetFindStepSec.this,SetFindStepOne.class);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.anim_in, R.anim.anim_out);
	}

}
