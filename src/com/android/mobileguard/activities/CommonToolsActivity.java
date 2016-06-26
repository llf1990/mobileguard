package com.android.mobileguard.activities;

import com.android.mobileguard.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class CommonToolsActivity extends Activity {

	public CommonToolsActivity() {
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_commontoos);
	}
	public void queryCallerLoc(View view){
		Intent intent = new Intent(CommonToolsActivity.this,CallerLocQueryActivity.class);
		startActivity(intent);
	}
}
