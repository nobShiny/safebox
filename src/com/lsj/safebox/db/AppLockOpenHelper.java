package com.lsj.safebox.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AppLockOpenHelper extends SQLiteOpenHelper {

	public AppLockOpenHelper(Context context) {
		super(context, "applock.db", null, 1);
	}

	// 当回调的时候数据库已经创建了，适合创建表
	@Override
	public void onCreate(SQLiteDatabase db) {
		// _id主键自动增长的，packname应用程序的包名
		db.execSQL("create table applock(_id integer primary key autoincrement,packname varchar(20))");

	}

	// 升级的时候回调1-->2
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}
