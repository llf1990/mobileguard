package com.android.mobileguard.db.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class AddressDBDao {
	/**
	 * 查询手机号码归属地
	 * @param mobilenumber
	 * @return 使用嵌套查询表2 outkey对应归属地信息
	 */
	public static String findLocation(String mobilenumber){
		String path = "/data/data/com.android.mobileguard/files/address.db";
		SQLiteDatabase db = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
		Cursor cursor = db.rawQuery("select location from data2 where id = " +
				"(select outkey from data1 where id = ?)", new String[]{mobilenumber.substring(0, 7)});
		String location = "";
		if(cursor.moveToNext()){
			location = cursor.getString(0);
		}
		cursor.close();
		db.close();
		return location;
	}
}