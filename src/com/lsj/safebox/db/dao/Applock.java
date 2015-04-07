package com.lsj.safebox.db.dao;

import java.util.ArrayList;
import java.util.List;

import com.lsj.safebox.db.AppLockOpenHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;


public class Applock {

	private AppLockOpenHelper helper;
	private Context context;

	public Applock(Context context) {
		helper = new AppLockOpenHelper(context);
		this.context = context;
	}

	/**
	 * 增加一条要加上的程序信息
	 * 
	 * @param packname
	 *            程序的包名
	 */
	public void add(String packname) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("packname", packname);
		db.insert("applock", null, values);
		db.close();
		// 当数据库变化的时候发这个消息
		Uri uri = Uri.parse("content://com.lsj.safebox.dbchange");
		context.getContentResolver().notifyChange(uri, null);

	}

	/**
	 * 删除一条已加锁数据
	 * 
	 * @param packname
	 *            要删除的程序的包名
	 */
	public void delete(String packname) {
		SQLiteDatabase db = helper.getWritableDatabase();
		db.delete("applock", "packname=?", new String[] { packname });
		db.close();
		// 当数据库变化的时候发这个消息
		Uri uri = Uri.parse("content://com.lsj.safebox.dbchange");
		context.getContentResolver().notifyChange(uri, null);
	}

	/**
	 * 查找一条数据是否存在
	 * 
	 * @param packname
	 *            要查找的已加锁程序
	 * @return true存在，false不存在
	 */
	public boolean find(String packname) {
		boolean result = false;
		SQLiteDatabase db = helper.getWritableDatabase();
		Cursor cursor = db.query("applock", null, "packname=?",
				new String[] { packname }, null, null, null);
		while (cursor.moveToNext()) {
			result = true;
		}
		cursor.close();
		db.close();
		return result;
	}

	/**
	 * 得到所有已加锁的包名
	 * 
	 * @return
	 */
	public List<String> findAll() {
		List<String> result = new ArrayList<String>();
		SQLiteDatabase db = helper.getWritableDatabase();
		Cursor cursor = db.query("applock", new String[] { "packname" }, null,
				null, null, null, null);
		while (cursor.moveToNext()) {
			String packName = cursor.getString(0);
			result.add(packName);
		}
		cursor.close();
		db.close();
		return result;
	}

}
