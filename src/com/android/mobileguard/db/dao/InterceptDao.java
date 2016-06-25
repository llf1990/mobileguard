package com.android.mobileguard.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.android.mobileguard.db.InterceptDBOpenHelper;

public class InterceptDao {
	
	private InterceptDBOpenHelper helper;
	
	/**
	 * �����ʼhelper
	 * @param context
	 */
	public InterceptDao(Context context) {
		helper = new InterceptDBOpenHelper(context);
	}
	
	/**
	 * 
	 * @param number
	 * @param mode
	 * @return ����-1���������󣬷��ز�������¼�¼�к�
	 */
	public long add(String number , String mode){
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("phonenum", number);
		values.put("mode", mode);
		long flag = db.insert("blacknumber", null, values);
		db.close();
		return flag;
	}
	
	/**
	 * 
	 * @param number
	 * @return ����ɾ����������0��ʾɾ��ʧ��
	 */
	public int delete(String number){
		SQLiteDatabase db = helper.getWritableDatabase();
		String[] whereArgs = new String[]{number};
		int count = db.delete("blacknumber", "phonenum = ?", whereArgs);
		db.close();
		return count;
	}
	
	/**
	 * 
	 * @param number
	 * @param mode
	 * @return ���ظ��µ�����
	 */
	public int update(String number,String mode){
		SQLiteDatabase db = helper.getWritableDatabase();
		String[] whereArgs = new String[]{number};
		ContentValues values = new ContentValues();
		values.put("mode", mode);
		int count = db.update("blacknumber", values, "phonenum = ?", whereArgs);
		db.close();
		return count;
	}
	/**
	 * 
	 * @param phonenum
	 * @return ��������ģʽ���������ģʽΪ�գ���ʾ�������غ��� 	
	 */
	public String find(String phonenum){
		String mode = null;
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.query("blacknumber", new String[]{"mode"}, "phonenum = ?", new String[]{phonenum}, null, null, null);
		while(cursor.moveToNext()){
			mode = cursor.getString(0);
		}
		db.close();
		cursor.close();
		return mode;
	}

}
