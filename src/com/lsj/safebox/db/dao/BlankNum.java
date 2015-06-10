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
 * ���������ݿ����ɾ�Ĳ�
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
	 *            ����������
	 * @param mode
	 *            ����ģʽ 0 �������� 1�绰���� 2 ȫ������
	 */
	public void addBlankNum(String blanknum, int mode) {
		// �ɶ����ݿ� û�м��������ݿ� Ч�ʱȽϸ�,��ȫ�Ժܵ�, ��д���ݿ���������ݿ� Ч����,��ȫ�ԱȽϸ�
		SQLiteDatabase sql = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("blanknum", blanknum);
		values.put("mode", mode);
		sql.insert("info", null, values);
		sql.close();
	}

	/**
	 * �޸ĺ���������ģʽ
	 * 
	 * @param newMode
	 *            �µ�����ģʽ
	 */
	public void updateBlankNumMode(int newMode, String blanknum) {
		SQLiteDatabase sql = helper.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put("mode", newMode);
		sql.update("info", values, "blanknum=?", new String[] { blanknum });
		sql.close();
	}

	/**
	 * ��ѯ������������ģʽ
	 * 
	 * @param blanknum
	 * @return -1 ����û�к�����
	 */
	public int queryBlankNum(String blanknum) {
		int mode = -1;
		SQLiteDatabase sql = helper.getReadableDatabase();
		// ����1 Ҫ��ѯ�ı��� ����2 Ҫ��ѯ����һ���� ����3 ��ѯ����
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
	 * ɾ��������
	 * 
	 * @param blanknum
	 */
	public void deleteBlankNum(String blanknum) {
		SQLiteDatabase sql = helper.getWritableDatabase();
		sql.delete("info", "blanknum=?", new String[] { blanknum });
		sql.close();
	}

	/**
	 * ��ѯȫ��������
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
	 * ��ѯ���ֺ�����
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
