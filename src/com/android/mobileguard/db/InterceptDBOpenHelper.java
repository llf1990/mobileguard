package com.android.mobileguard.db;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class InterceptDBOpenHelper extends SQLiteOpenHelper {

	public InterceptDBOpenHelper(Context context) {
		super(context, "intercept.db",null, 1);
	}

	public InterceptDBOpenHelper(Context context, String name,
			CursorFactory factory, int version,
			DatabaseErrorHandler errorHandler) {
		super(context, name, factory, version, errorHandler);
	}

	/**
	 * ��洢������ ��id �Զ�������phonenum�绰���룬mode ����ģʽ��1���ŵ绰���� 2�������ء�3�绰���أ�
	 */
	@Override
	
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table blacknumber(_id Integer primary key autoincrement, phonenum varchar(20),mode varchar(2))");
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}

}
