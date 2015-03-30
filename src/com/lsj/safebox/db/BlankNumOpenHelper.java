package com.lsj.safebox.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

// �������ݿ� 
public class BlankNumOpenHelper extends SQLiteOpenHelper {

	public BlankNumOpenHelper(Context context) {
		super(context, "blanknum.db", null, 1);
	}
	// ���ݿ��һ�δ�����ʱ�����
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table info (_id integer primary key autoincrement,blanknum varchar(20),mode varchar(2));");

	}
	// ���ݿ�汾���µ�ʱ�����
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

}

