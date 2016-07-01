package com.android.mobileguard.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.android.mobileguard.db.AppLockDBOpenHelper;


public class AppLockDBDao {
	/**
	 * 锁定程序则将包名写入数据库，取消锁定就讲程序取出数据库
	 * @param 
	 * @return 
	 */
		private AppLockDBOpenHelper helper;
		public AppLockDBDao(Context context) {
			helper = new AppLockDBOpenHelper(context);
		}
		
		/**
		 * 添加要锁定应用包名到数据库
		 * @param packname
		 * @return 插入的行数，-1表示失败
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
		 * 解锁一个应用时从数据库删除
		 * @param packname
		 * @return 删除的数量，0表示失败
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
		 * 查询一个报名是否已锁定
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