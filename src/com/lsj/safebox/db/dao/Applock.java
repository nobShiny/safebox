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
	 * ����һ��Ҫ���ϵĳ�����Ϣ
	 * 
	 * @param packname
	 *            ����İ���
	 */
	public void add(String packname) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("packname", packname);
		db.insert("applock", null, values);
		db.close();
		// �����ݿ�仯��ʱ�������Ϣ
		Uri uri = Uri.parse("content://com.lsj.safebox.dbchange");
		context.getContentResolver().notifyChange(uri, null);

	}

	/**
	 * ɾ��һ���Ѽ�������
	 * 
	 * @param packname
	 *            Ҫɾ���ĳ���İ���
	 */
	public void delete(String packname) {
		SQLiteDatabase db = helper.getWritableDatabase();
		db.delete("applock", "packname=?", new String[] { packname });
		db.close();
		// �����ݿ�仯��ʱ�������Ϣ
		Uri uri = Uri.parse("content://com.lsj.safebox.dbchange");
		context.getContentResolver().notifyChange(uri, null);
	}

	/**
	 * ����һ�������Ƿ����
	 * 
	 * @param packname
	 *            Ҫ���ҵ��Ѽ�������
	 * @return true���ڣ�false������
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
	 * �õ������Ѽ����İ���
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
