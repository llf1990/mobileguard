package com.android.mobileguard.db;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class AppLockDBOpenHelper extends SQLiteOpenHelper {

	public AppLockDBOpenHelper(Context context) {
		super(context, "applock.db",null, 1);
	}

	public AppLockDBOpenHelper(Context context, String name,
			CursorFactory factory, int version,
			DatabaseErrorHandler errorHandler) {
		super(context, name, factory, version, errorHandler);
	}

	/**
	 * 表存储添加锁定应用程序包名
	 */
	@Override
	
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table locked(_id Integer primary key autoincrement,packname varchar(20))");
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}

}
