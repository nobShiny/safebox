package com.lsj.safebox.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AppLockOpenHelper extends SQLiteOpenHelper {

	public AppLockOpenHelper(Context context) {
		super(context, "applock.db", null, 1);
	}

	// ���ص���ʱ�����ݿ��Ѿ������ˣ��ʺϴ�����
	@Override
	public void onCreate(SQLiteDatabase db) {
		// _id�����Զ������ģ�packnameӦ�ó���İ���
		db.execSQL("create table applock(_id integer primary key autoincrement,packname varchar(20))");

	}

	// ������ʱ��ص�1-->2
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}
