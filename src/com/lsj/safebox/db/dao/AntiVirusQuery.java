package com.lsj.safebox.db.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class AntiVirusQuery {
	private static String path = "/data/data/com.lsj.safebox/files/antivirus.db";

	/**
	 * 查询数据库是否是病毒
	 * @param signatures 签名信息
	 * @return 返回null就是正常软件，返回有值就是病毒
	 */
	public static String isAntivirus(String signatures) {
		String result = null;
		SQLiteDatabase db = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
		Cursor cursor = db.rawQuery("select desc from datable where md5 = ?", new String[]{signatures});
		
		if(cursor.moveToNext()){
			result = cursor.getString(0);
		}
		cursor.close();
		db.close();
		return result;
		
	}

}
