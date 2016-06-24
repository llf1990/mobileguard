package com.android.mobileguard.activities;

import com.android.mobileguard.R;

import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SetFindStepThird extends SetBaseActivity {
	private Button bt_set_stepthird_next;
	private Button bt_set_stepthird_pre;
	private Button bt_set_stepthird_select;
	private EditText et_set_stepthird_pnum;
	private Editor editor;
	private String phoneNum;

	public SetFindStepThird() {
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set_stepthird);
		editor = getSp().edit();
		bt_set_stepthird_next = (Button) findViewById(R.id.bt_set_stepthird_next);
		bt_set_stepthird_pre = (Button) findViewById(R.id.bt_set_stepthird_pre);
		bt_set_stepthird_select = (Button) findViewById(R.id.bt_set_stepthird_select);
		et_set_stepthird_pnum = (EditText) findViewById(R.id.et_set_stepthird_pnum);
		
		phoneNum = getSp().getString("safePhoneNum", null);
		
		bt_set_stepthird_pre.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				pre();
			}
		});
		bt_set_stepthird_next.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				next();
			}
		});
		
		bt_set_stepthird_select.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(SetFindStepThird.this,ContactSelectActivity.class);
				startActivityForResult(intent, 0);
				
			}
		});
		
		
	}

	@Override
	void next() {
		
		phoneNum= et_set_stepthird_pnum.getText().toString().trim();
		if(TextUtils.isEmpty(phoneNum)){
			Toast.makeText(SetFindStepThird.this, "请先设置安全号码", 0).show();
			return;
		}else{
			Intent intent = new Intent(SetFindStepThird.this,SetFindStepFouth.class);
			startActivity(intent);
			finish();
			
			editor.putString("safePhoneNum", phoneNum);
			editor.commit();
		}
		
	}

	@Override
	void pre() {
		Intent intent = new Intent(SetFindStepThird.this,SetFindStepSec.class);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.anim_in, R.anim.anim_out);
		
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(data != null){
			String phone = data.getStringExtra("phoneNum");
			et_set_stepthird_pnum.setText(phone);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

}
