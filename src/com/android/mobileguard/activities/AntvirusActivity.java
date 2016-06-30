package com.android.mobileguard.activities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import com.android.mobileguard.R;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class AntvirusActivity extends Activity {
	
	protected static final int SAFE = 0;
	protected static final int FOUND_VIRUS = 1;
	protected static final int SCAN_FINISH = 3;
	private ImageView iv_scanning;
	private LinearLayout ll_antvirus_list;
	private ProgressBar pb_antivirus_process;
	private TextView tv_scan_status;
	
	
	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			PackageInfo pinfo;
			switch (msg.what) {
			case SAFE:
				TextView tv_safe = new TextView(getApplicationContext());
				pinfo = (PackageInfo) msg.obj;
				tv_safe.setText("扫描安全："+pinfo.applicationInfo.loadLabel(getPackageManager()));
				ll_antvirus_list.addView(tv_safe,0);
				break;

			case FOUND_VIRUS:
				TextView tv_findvirus = new TextView(getApplicationContext());
				pinfo = (PackageInfo) msg.obj;
				tv_findvirus.setText("发现病毒："+pinfo.applicationInfo.loadLabel(getPackageManager()));
				tv_findvirus.setTextColor(Color.RED);
				ll_antvirus_list.addView(tv_findvirus,0);
				break;
			case SCAN_FINISH:
				tv_scan_status.setText("扫描完毕");
				iv_scanning.clearAnimation();
				iv_scanning.setVisibility(View.INVISIBLE);
				
				break;
			}
		}
		
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_antvirus);
		
		iv_scanning = (ImageView) findViewById(R.id.iv_scanning);
		ll_antvirus_list = (LinearLayout) findViewById(R.id.ll_antvirus_list);
		pb_antivirus_process = (ProgressBar) findViewById(R.id.pb_antivirus_process);
		tv_scan_status = (TextView) findViewById(R.id.tv_scan_status);
		
		RotateAnimation ra = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		ra.setDuration(3000);
		ra.setRepeatMode(Animation.RESTART);
		ra.setRepeatCount(Animation.INFINITE);
		iv_scanning.setAnimation(ra);
		/**
		 * 查杀病毒
		 */
		scanVirus();
	}
	/**
	 * 查杀病毒是遍历文件系统的操作，比较耗时，因此开启子线程进行该操作。
	 */
	private void scanVirus(){
		new Thread(){
			public void run() {
				//遍历系统所有apk文件，获取他们的MD5特征码
				PackageManager pm = getPackageManager();
				List<PackageInfo> list = pm.getInstalledPackages(0);
				pb_antivirus_process.setMax(list.size());
				int progress = 0;
				for (PackageInfo pinfo : list) {
					String path = pinfo.applicationInfo.sourceDir;
					String md5 = getFileMD5(path);
					SQLiteDatabase db = SQLiteDatabase.openDatabase(getFilesDir().getAbsolutePath()+"/antivirus.db", null, SQLiteDatabase.OPEN_READONLY);
					Cursor cursor = db.rawQuery("select desc from datable where md5 = ?",new String[]{md5});
					if(cursor.moveToNext()){
						String desc = cursor.getString(0);
						System.out.println("发现病毒:"+desc+"   "+pinfo.applicationInfo.loadLabel(pm));
						Message msg = Message.obtain();
						msg.what = FOUND_VIRUS;
						msg.obj = pinfo;
						handler.sendMessage(msg);
					}else{
						System.out.println("扫描安全:"+pinfo.applicationInfo.loadLabel(pm));
						Message msg = Message.obtain();
						msg.what = SAFE;
						msg.obj = pinfo;
						handler.sendMessage(msg);
					}
					progress ++;
					pb_antivirus_process.setProgress(progress);
				}
				Message msg = Message.obtain();
				msg.what = SCAN_FINISH;
				handler.sendMessage(msg);
			};
		}.start();
	}
	/**
	 * 根据文件路径得到文件生成文件对应MD5特征码
	 */
	@SuppressWarnings({ "finally", "resource" })
	private String getFileMD5(String path){
		File file = new File(path);
		StringBuilder sb = new StringBuilder();
		try {
			MessageDigest digest = MessageDigest.getInstance("MD5");
			FileInputStream fis = new FileInputStream(file);
			byte[] buffer = new byte[1024];
			int len ;
			while((len = fis.read(buffer)) != -1){
				digest.update(buffer,0,len);
			}
			byte[] result = digest.digest();
			
			for (byte b : result) {
				int number = b&0xff;
				String str = Integer.toHexString(number);
				if(str.length() == 1){
					sb.append("0");
				}
				sb.append(str);
			}
			
			
			
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
				return sb.toString();
		}
		
	}
	
}
