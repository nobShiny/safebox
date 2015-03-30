package com.lsj.safebox.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

// 创建数据库 
public class BlankNumOpenHelper extends SQLiteOpenHelper {

	public BlankNumOpenHelper(Context context) {
		super(context, "blanknum.db", null, 1);
	}
	// 数据库第一次创建的时候调用
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table info (_id integer primary key autoincrement,blanknum varchar(20),mode varchar(2));");

	}
	// 数据库版本更新的时候调用
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

}

