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
		 * ���û���ָ����Ļ������ʱ����õķ���
		 * e1 ��ָ��һ�δ�������Ļ���¼�
		 * e2��ָ�뿪��Ļ��һ˲���Ӧ���¼�
		 * velocityX ˮƽ������ٶ�
		 * velocityY ��ֱ������ٶ�
		 */
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2,
				float velocityX, float velocityY) {
			
			if(Math.abs(velocityX)<100){
				Log.i(TAG,"�ƶ��ٶ�̫��,��Ч����");
				return true;
			}
			if(Math.abs(e2.getRawY()-e1.getRawY()) > 500){
				Log.i(TAG, "��ֱ�ƶ�������Ч����");
				return true;
				
			}
			if((e1.getRawX() - e2.getRawX()) > 100 ){
				Log.i(TAG, "������ʾ��һ������");
				next();
				
			}
			if((e2.getRawX() - e1.getRawX()) > 100 ){
				Log.i(TAG, "���֣���ʾ��һ������");
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
