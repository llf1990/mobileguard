package com.android.mobileguard.activities;

import com.android.mobileguard.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class InterceptActivity extends Activity {

	public InterceptActivity() {
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_intercept);
		
	}
	
	public void addBlackNumber(View view){
		Intent intent = new Intent(InterceptActivity.this,addBlackNumberActivity.class);
		startActivityForResult(intent, 0);
	}
}
