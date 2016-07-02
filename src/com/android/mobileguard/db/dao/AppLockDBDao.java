package com.android.mobileguard.db.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.android.mobileguard.db.AppLockDBOpenHelper;


public class AppLockDBDao {
	/**
	 * ���������򽫰���д�����ݿ⣬ȡ�������ͽ�����ȡ�����ݿ�
	 * @param 
	 * @return 
	 */
		private AppLockDBOpenHelper helper;
		private Context context;
		
		public AppLockDBDao(Context context) {
			helper = new AppLockDBOpenHelper(context);
			this.context = context;
		}
		
		/**
		 * ���Ҫ����Ӧ�ð��������ݿ�
		 * @param packname
		 * @return �����������-1��ʾʧ��
		 */
		public long add(String packname){
			SQLiteDatabase db = helper.getWritableDatabase();
			ContentValues values = new ContentValues();
			values.put("packname", packname);
			long row = db.insert("locked", null, values);
			db.close();
			//֪ͨ���ݹ۲������ݿ�仯
			Uri uri = Uri.parse("content://com.android.mobileguard.lockdb");
			context.getContentResolver().notifyChange(uri, null);
			return row;
		}
		/**
		 * ����һ��Ӧ��ʱ�����ݿ�ɾ��
		 * @param packname
		 * @return ɾ����������0��ʾʧ��
		 */
		public int delete(String packname){
			SQLiteDatabase db = helper.getWritableDatabase();
			ContentValues values = new ContentValues();
			values.put("packname", packname);
			
			 int number = db.delete("locked", "packname = ?",new String[]{packname} );
			 db.close();
			//֪ͨ���ݹ۲������ݿ�仯
				Uri uri = Uri.parse("content://com.android.mobileguard.lockdb");
				context.getContentResolver().notifyChange(uri, null);
			 return number;
		}
		/**
		 * ��ѯһ�������Ƿ�������
		 * @param packname
		 * @return
		 */
		public boolean find(String packname){
			boolean result = false;
			SQLiteDatabase db = helper.getReadableDatabase();
			Cursor cursor = db.rawQuery("select * from locked where packname = ?", new String[]{packname});
			if(cursor.moveToNext()){
				result = true;
			}
			cursor.close();
			db.close();
			return result;
		}
		
		/**
		 * ��ȡ��������������
		 * @param packname
		 * @return
		 */
		public List<String> findAll(){
			List<String> packlist = new ArrayList<String>();
			SQLiteDatabase db = helper.getReadableDatabase();
			Cursor cursor = db.rawQuery("select * from locked",null);
			if(cursor.moveToNext()){
				packlist.add(cursor.getString(0));
			}
			cursor.close();
			db.close();
			return packlist;
		}
		
}