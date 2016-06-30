package com.android.mobileguard.activities;

import com.android.mobileguard.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class AntvirusActivity extends Activity {
	
	private ImageView iv_scanning;
	private LinearLayout ll_antvirus_list;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_antvirus);
		
		iv_scanning = (ImageView) findViewById(R.id.iv_scanning);
		ll_antvirus_list = (LinearLayout) findViewById(R.id.ll_antvirus_list);
		
		RotateAnimation ra = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		ra.setDuration(3000);
		ra.setRepeatMode(Animation.RESTART);
		ra.setRepeatCount(Animation.INFINITE);
		iv_scanning.setAnimation(ra);
		
	}
}
