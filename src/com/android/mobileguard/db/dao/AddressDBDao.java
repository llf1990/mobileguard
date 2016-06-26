package com.android.mobileguard.db.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class AddressDBDao {
	/**
	 * 查询号码归属地
	 * @param mobilenumber
	 * @return 使用嵌套查询表2 outkey对应归属地信息
	 */
	public static String findLocation(String number){
		String location = "查无此号";
		Cursor cursor = null;
		String path = "/data/data/com.android.mobileguard/files/address.db";
		SQLiteDatabase db = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
		if(number.matches("^1[34578]\\d{9}$")){//手机号码
			cursor = db.rawQuery("select location from data2 where id = " +
					"(select outkey from data1 where id = ?)", new String[]{number.substring(0, 7)});
			
			if(cursor.moveToNext()){
				location = cursor.getString(0);
			}
			cursor.close();
		}else{//非手机号码
			switch (number.length()) {
			case 3:
				location = "紧急救援";
				break;
			case 4:
				location = "模拟器";
				break;
			case 5:
				location = "商业客服";
				break;
			case 7:
				location = "本地电话";
				break;
			case 8:
				location = "本地电话";
				break;
			default:
				if(number.length() >= 10 && number.startsWith("0")){
					cursor = db.rawQuery("select location from data2 where area = ?", new String[]{number.substring(1, 3)});
					if(cursor.moveToNext()){
						String temp = cursor.getString(0);
						location = temp.substring(0,temp.length()-2);
					}
					cursor = db.rawQuery("select location from data2 where area = ?", new String[]{number.substring(1, 4)});
					if(cursor.moveToNext()){
						String temp = cursor.getString(0);
						location = temp.substring(0,temp.length()-2);
					}
					cursor.close();
				}
				break;
			}
		}
		
		db.close();
		return location;
	}
}