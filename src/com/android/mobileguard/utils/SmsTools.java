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
	 * �����һ����ڣ��ṩ���ý��������ֵ�͹����б仯ֵ���������۽���UI�仯������Ҫ����
	 * @author feng
	 *
	 */
	public interface BackUpInterface{
		/**
		 * ���ȿ�ʼǰ���ý������ֵ
		 */
		public void setMaxBefore(int max);
		/**
		 * ���������ý���ֵ
		 */
		public void setInProgress(int progress);
	}
	/**
	 * 
	 * @param context ������
	 * @param callback �ص������޸����ֵ�ͽ���
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
			//���ý����������ֵ
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
