package com.android.mobileguard.service;

import com.android.mobileguard.R;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.drawable.AnimationDrawable;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.Toast;

public class RocketService extends Service {
	
	private static final String TAG = "RocketService";
	private WindowManager wm;
	private ImageView iv;
	
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
	
	@Override
	public void onCreate() {
		Log.i(TAG, "oncreate method start!");
		wm = (WindowManager) getSystemService(WINDOW_SERVICE);
		iv = new ImageView(this);
		iv.setBackgroundResource(R.drawable.rocket);
		AnimationDrawable rocketani = (AnimationDrawable) iv.getBackground();
		rocketani.start();
		Log.i(TAG,"rocketani start finish!");
		final WindowManager.LayoutParams params = new LayoutParams();
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
        params.format = PixelFormat.TRANSLUCENT;
        params.gravity = Gravity.TOP+Gravity.LEFT;
        params.type = WindowManager.LayoutParams.TYPE_PRIORITY_PHONE;
		wm.addView(iv, params);
		Log.i(TAG, "add view finish");
		super.onCreate();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		wm.removeView(iv);
		iv = null;
	}

}
