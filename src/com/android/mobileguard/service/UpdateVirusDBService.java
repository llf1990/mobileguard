package com.android.mobileguard.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.util.Log;

public class UpdateVirusDBService extends Service {


	protected static final String TAG = "UpdateVirusDBService";

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		final Timer timer;
		final TimerTask task;
		
		timer = new Timer();
		task = new TimerTask(){
			@Override
			public void run() {
				try {
					URL url = new URL("http://192.168.1.108:8080/update.info");
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					InputStream is = conn.getInputStream();
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					byte[] buffer = new byte[2048];
					int len ;
					while((len = is.read(buffer)) != -1){
						baos.write(buffer,0,len);
					}
					is.close();
					String json = baos.toString();
					JSONObject obj = new JSONObject(json);
					//获取服务数据库版本号
					int serverVersion = obj.getInt("version");
					
					//查询本地数据库版本号
					SQLiteDatabase db = SQLiteDatabase.openDatabase(getFilesDir().getAbsolutePath()+"/antivirus.db", null, SQLiteDatabase.OPEN_READWRITE);
					Cursor cursor = db.rawQuery("select subcnt from version", null);
					cursor.moveToNext();
					int localVersion = cursor.getInt(0);
					if(localVersion >= serverVersion){
						Log.i(TAG, "数据库已是最新版本，无需更新");
					}else{//开启事务，确保更新同步
						db.beginTransaction();
							try {
								String name = obj.getString("name");
								String desc = obj.getString("desc");
								String type = obj.getString("type");
								String md5 = obj.getString("md5");
								ContentValues values = new ContentValues();
								values.put("name", name);
								values.put("desc",desc);
								values.put("type", type);
								values.put("md5", md5);
								db.insert("datable", null, values);
								//更新本地数据库版本号
								ContentValues dbversion = new ContentValues();
								dbversion.put("subcnt",serverVersion);
								db.update("version", dbversion, null, null);
							    db.setTransactionSuccessful();
							   } finally {
							     db.endTransaction();
							   }
					}
					
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		};
		timer.schedule(task, 0, 30*1000);
		
		
	}
	
}
