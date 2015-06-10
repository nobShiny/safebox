package com.lsj.safebox.db.dao;

import java.util.ArrayList;
import java.util.List;

import com.lsj.safebox.db.BlankNumOpenHelper;
import com.lsj.safebox.domin.BlankNumInfo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


/**
 * 黑名单数据库的增删改查
 * 
 * @author yu
 * 
 */
public class BlankNum {
	public static final int SMS = 0;
	public static final int TEL = 1;
	public static final int ALL = 2;
	private BlankNumOpenHelper helper;
	private Context context;

	public BlankNum(Context context) {
		super();
		this.context = context;
		helper = new BlankNumOpenHelper(context);
	}

	/**
	 * 
	 * @param blanknum
	 *            黑名单号码
	 * @param mode
	 *            拦截模式 0 短信拦截 1电话拦截 2 全部拦截
	 */
	public void addBlankNum(String blanknum, int mode) {
		// 可读数据库 没有加锁的数据库 效率比较高,安全性很低, 可写数据库枷锁的数据库 效率慢,安全性比较高
		SQLiteDatabase sql = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("blanknum", blanknum);
		values.put("mode", mode);
		sql.insert("info", null, values);
		sql.close();
	}

	/**
	 * 修改黑名单拦截模式
	 * 
	 * @param newMode
	 *            新的拦截模式
	 */
	public void updateBlankNumMode(int newMode, String blanknum) {
		SQLiteDatabase sql = helper.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put("mode", newMode);
		sql.update("info", values, "blanknum=?", new String[] { blanknum });
		sql.close();
	}

	/**
	 * 查询黑名单的拦截模式
	 * 
	 * @param blanknum
	 * @return -1 代表没有黑名单
	 */
	public int queryBlankNum(String blanknum) {
		int mode = -1;
		SQLiteDatabase sql = helper.getReadableDatabase();
		// 参数1 要查询的表名 参数2 要查询的哪一个列 参数3 查询条件
		Cursor cursor = sql.query("info", new String[] { "mode" },
				"blanknum=?", new String[] { blanknum }, null, null, null);
		if (cursor.moveToNext()) {
			mode = Integer.parseInt(cursor.getString(0));
		}
		cursor.close();
		sql.close();
		return mode;

	}

	/**
	 * 删除黑名单
	 * 
	 * @param blanknum
	 */
	public void deleteBlankNum(String blanknum) {
		SQLiteDatabase sql = helper.getWritableDatabase();
		sql.delete("info", "blanknum=?", new String[] { blanknum });
		sql.close();
	}

	/**
	 * 查询全部黑名单
	 * 
	 * @return
	 */
	public List<BlankNumInfo> queryAll() {
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		List<BlankNumInfo> nums = new ArrayList<BlankNumInfo>();

		SQLiteDatabase sql = helper.getReadableDatabase();
		Cursor cursor = sql.query("info", new String[] { "blanknum", "mode" },
				null, null, null, null, "_id desc");
		while (cursor.moveToNext()) {
			String blankNum = cursor.getString(0);
			int mode = cursor.getInt(1);
			BlankNumInfo info = new BlankNumInfo(blankNum, mode);
			nums.add(info);
		}
		cursor.close();
		sql.close();

		return nums;
	}

	/**
	 * 查询部分黑名单
	 * 
	 * @return
	 */
	public List<BlankNumInfo> queryPart(int maxNum, int startIndex) {
		try {
			Thread.sleep(400);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		List<BlankNumInfo> nums = new ArrayList<BlankNumInfo>();

		SQLiteDatabase sql = helper.getReadableDatabase();
		Cursor cursor = sql
				.rawQuery(
						"select blanknum,mode from info order by _id desc  limit ? offset ? ; ",
						new String[] { maxNum + "", startIndex + "" });
		while (cursor.moveToNext()) {
			String blankNum = cursor.getString(0);
			int mode = cursor.getInt(1);
			BlankNumInfo info = new BlankNumInfo(blankNum, mode);
			nums.add(info);
		}
		cursor.close();
		sql.close();

		return nums;
	}
}
