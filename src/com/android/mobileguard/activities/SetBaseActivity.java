package com.android.mobileguard.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

public abstract class SetBaseActivity extends Activity {

	protected static final String TAG = null;
	private GestureDetector mGestureDetector;
	private SharedPreferences sp ;

	public SetBaseActivity() {
	}

	protected void onCreate(android.os.Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setSp(getSharedPreferences("account", MODE_PRIVATE));
		
	mGestureDetector = new GestureDetector(this,new GestureDetector.SimpleOnGestureListener(){
		/**
		 * 当用户手指在屏幕滑动的时候调用的方法
		 * e1 手指第一次触摸到屏幕的事件
		 * e2手指离开屏幕的一瞬间对应的事件
		 * velocityX 水平方向的速度
		 * velocityY 垂直方向的速度
		 */
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2,
				float velocityX, float velocityY) {
			
			if(Math.abs(velocityX)<100){
				Log.i(TAG,"移动速度太慢,无效动作");
				return true;
			}
			if(Math.abs(e2.getRawY()-e1.getRawY()) > 500){
				Log.i(TAG, "垂直移动过大，无效动作");
				return true;
				
			}
			if((e1.getRawX() - e2.getRawX()) > 100 ){
				Log.i(TAG, "向左，显示下一个界面");
				next();
				
			}
			if((e2.getRawX() - e1.getRawX()) > 100 ){
				Log.i(TAG, "向又，显示上一个界面");
				pre();
				
			}
				
			
			return super.onFling(e1, e2, velocityX, velocityY);
		}	
	});	
	

	
	}

	public boolean onTouchEvent(MotionEvent event) {

		mGestureDetector.onTouchEvent(event);

		return super.onTouchEvent(event);

	}
	abstract void next();
	abstract void pre();

	public SharedPreferences getSp() {
		return sp;
	}

	public void setSp(SharedPreferences sp) {
		this.sp = sp;
	}
}
