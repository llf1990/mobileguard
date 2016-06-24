package com.android.mobileguard.activities;

import com.android.mobileguard.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class SetFindStepOne extends SetBaseActivity {
	protected static final String TAG = "SetFindStepOne";
	
	private Button bt_set_stepone_next;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set_stepone);
		
		
		bt_set_stepone_next = (Button) findViewById(R.id.bt_set_stepone_next);
		bt_set_stepone_next.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				next();
			}
		});
		
	}

	@Override
	void next() {
		Intent intent = new Intent(SetFindStepOne.this,SetFindStepSec.class);
		startActivity(intent);
		finish();
	}

	@Override
	void pre() {
		
	}
	
	
	
}
