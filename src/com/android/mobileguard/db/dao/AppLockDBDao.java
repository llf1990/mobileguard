package com.android.mobileguard.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.android.mobileguard.db.AppLockDBOpenHelper;


public class AppLockDBDao {
	/**
	 * ���������򽫰���д�����ݿ⣬ȡ�������ͽ�����ȡ�����ݿ�
	 * @param 
	 * @return 
	 */
		private AppLockDBOpenHelper helper;
		public AppLockDBDao(Context context) {
			helper = new AppLockDBOpenHelper(context);
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
		
}