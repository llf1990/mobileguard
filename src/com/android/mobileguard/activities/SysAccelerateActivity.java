	package com.android.mobileguard.activities;

import com.android.mobileguard.R;
import com.android.mobileguard.fragment.CacheCleanFragment;
import com.android.mobileguard.fragment.SDCleanFragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

public class SysAccelerateActivity extends FragmentActivity implements OnClickListener{
	private LinearLayout ll_sysaccelerate_cache;
	private LinearLayout ll_sysaccelerate_sd;
	private FrameLayout fl_container;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_sys_accelerate);
		
		ll_sysaccelerate_cache = (LinearLayout) findViewById(R.id.ll_sysaccelerate_cache);
		ll_sysaccelerate_sd = (LinearLayout) findViewById(R.id.ll_sysaccelerate_sd);
		fl_container = (FrameLayout) findViewById(R.id.fl_container);
		
		ll_sysaccelerate_cache.setOnClickListener(this);
		ll_sysaccelerate_sd.setOnClickListener(this);
		
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		CacheCleanFragment fcache = new CacheCleanFragment();
		ft.replace( R.id.fl_container, fcache);
		ft.commit();
	}

	@Override
	public void onClick(View v) {
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		switch (v.getId()) {
		case R.id.ll_sysaccelerate_cache:
			CacheCleanFragment fcache = new CacheCleanFragment();
			ft.replace( R.id.fl_container, fcache);
			ll_sysaccelerate_cache.setBackgroundResource(R.drawable.function_greenbutton_normal);
			ll_sysaccelerate_sd.setBackgroundDrawable(null);
			ft.commit();
			break;
		case R.id.ll_sysaccelerate_sd:
			SDCleanFragment fsd = new SDCleanFragment();
			ft.replace(R.id.fl_container, fsd);
			ll_sysaccelerate_sd.setBackgroundResource(R.drawable.function_greenbutton_normal);
			ll_sysaccelerate_cache.setBackgroundDrawable(null);
			ft.commit();
			break;
		}
	}
}
