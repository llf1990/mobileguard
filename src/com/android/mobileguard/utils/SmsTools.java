package com.android.mobileguard.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.xmlpull.v1.XmlSerializer;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.SystemClock;
import android.util.Xml;
import android.widget.ProgressBar;

public class SmsTools {
	/**
	 * 抽象出一个借口，提供设置进度条最大值和过程中变化值方法，无论界面UI变化都不需要调整
	 * @author feng
	 *
	 */
	public interface BackUpInterface{
		/**
		 * 进度开始前设置进度最大值
		 */
		public void setMaxBefore(int max);
		/**
		 * 过程中设置进度值
		 */
		public void setInProgress(int progress);
	}
	/**
	 * 
	 * @param context 上下文
	 * @param callback 回调函数修改最大值和进度
	 */
	public  static void bakupSms(Context context,BackUpInterface callback){
		ContentResolver  resolver = context.getContentResolver();
		Uri uri = Uri.parse("content://sms");
		XmlSerializer serializer = Xml.newSerializer();
		File file = new File(Environment.getExternalStorageDirectory(),"smsbackup.xml");
		try {
			FileOutputStream fos = new FileOutputStream(file);
			serializer.setOutput(fos, "utf-8");
			serializer.startDocument("utf-8", true);
			serializer.startTag(null, "infos");
			Cursor cursor = resolver.query(uri, new String[]{"address","body","type","date"}, null, null, null);
			//设置进度条的最大值
			int progress = 0;
			/*pd.setMax(cursor.getCount());
			pb.setMax(cursor.getCount());*/
			callback.setMaxBefore(cursor.getCount());
			while(cursor.moveToNext()){
				serializer.startTag(null, "info");
				
				String address = cursor.getString(0);
				serializer.startTag(null, "address");
				serializer.text(address);
				serializer.endTag(null, "address");
				
				String body = cursor.getString(1);
				serializer.startTag(null, "body");
				serializer.text(body);
				serializer.endTag(null, "body");
				
				String type = cursor.getString(2);
				serializer.startTag(null, "type");
				serializer.text(type);
				serializer.endTag(null, "type");
				
				String date = cursor.getString(3);
				serializer.startTag(null, "date");
				serializer.text(date);
				serializer.endTag(null, "date");
				
				serializer.endTag(null, "info");
				progress++;
				SystemClock.sleep(1000);
				callback.setInProgress(progress);
				/*pd.setProgress(progress);
				pb.setProgress(progress);*/
			}
			
			cursor.close();
			serializer.endTag(null, "infos");
			serializer.endDocument();
			fos.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}
	
}
