package com.android.mobileguard.db.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class AddressDBDao {
	/**
	 * ��ѯ���������
	 * @param mobilenumber
	 * @return ʹ��Ƕ�ײ�ѯ��2 outkey��Ӧ��������Ϣ
	 */
	public static String findLocation(String number){
		String location = "���޴˺�";
		Cursor cursor = null;
		String path = "/data/data/com.android.mobileguard/files/address.db";
		SQLiteDatabase db = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
		if(number.matches("^1[34578]\\d{9}$")){//�ֻ�����
			cursor = db.rawQuery("select location from data2 where id = " +
					"(select outkey from data1 where id = ?)", new String[]{number.substring(0, 7)});
			
			if(cursor.moveToNext()){
				location = cursor.getString(0);
			}
			cursor.close();
		}else{//���ֻ�����
			switch (number.length()) {
			case 3:
				location = "������Ԯ";
				break;
			case 4:
				location = "ģ����";
				break;
			case 5:
				location = "��ҵ�ͷ�";
				break;
			case 7:
				location = "���ص绰";
				break;
			case 8:
				location = "���ص绰";
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