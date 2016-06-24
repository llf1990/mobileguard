package com.android.mobileguard.activities;

import com.android.mobileguard.R;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

public class SetFindStepFouth extends SetBaseActivity {
	private Button bt_set_stepfouth_next;
	private Button bt_set_stepfouth_pre;
	private CheckBox cb_set_stepfouth_protect;
	private TextView tv_stepfouth_status;
	
	public SetFindStepFouth() {
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set_stepfouth);
		
		bt_set_stepfouth_pre = (Button) findViewById(R.id.bt_set_stepfouth_pre);
		bt_set_stepfouth_next = (Button) findViewById(R.id.bt_set_stepfouth_next);
		cb_set_stepfouth_protect = (CheckBox) findViewById(R.id.cb_set_stepfouth_protect);
		tv_stepfouth_status = (TextView) findViewById(R.id.tv_stepfouth_status);
		//读取保护状态
		boolean isProtect = getSp().getBoolean("protecting", false);
		cb_set_stepfouth_protect.setChecked(isProtect);
		if(isProtect){
			tv_stepfouth_status.setVisibility(View.VISIBLE);
		}else{
			tv_stepfouth_status.setVisibility(View.INVISIBLE);
		}
		
		cb_set_stepfouth_protect.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				//记录保护状态的开启状态
				Editor editor = getSp().edit();
				editor.putBoolean("protecting", isChecked);
				editor.commit();
				if(isChecked){
					tv_stepfouth_status.setVisibility(View.VISIBLE);
				}else{
					tv_stepfouth_status.setVisibility(View.INVISIBLE);
				}
			}
		});
		
		bt_set_stepfouth_pre.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				pre();
			}
		});
		/**
		 * 设置完成修改数据库是否设置标记为true
		 */
		bt_set_stepfouth_next.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				next();
			}
			
		});
		
	}

	@Override
	void next() {
		Intent intent = new Intent(SetFindStepFouth.this,LostFindActivity.class);
		startActivity(intent);
		SharedPreferences sp = getSharedPreferences("account", MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.putBoolean("configed",true);
		editor.commit();
		finish();		
	}

	@Override
	void pre() {
		Intent intent = new Intent(SetFindStepFouth.this,SetFindStepThird.class);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.anim_in, R.anim.anim_out);		
	}

}
