package com.lsj.safebox.db.dao;

import java.io.File;

import com.lsj.safebox.db.BlankNumOpenHelper;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class Address {
		private BlankNumOpenHelper helper;
		private Context context;

		public Address(Context context) {
			helper = new BlankNumOpenHelper(context);
			this.context = context;
		}

		/**
		 * 8 void ��ѯ���������
		 * 
		 * @param num
		 * @return
		 */
		public String queryAddress(String num) {
			String location = null;
			File file = new File(context.getFilesDir(), "address.db");
			if (file.exists()) {
				// ������ʽ ^1[3458]\\d{9}$
				SQLiteDatabase sql = SQLiteDatabase.openDatabase(
						file.getAbsolutePath(), null, SQLiteDatabase.OPEN_READONLY);
				if (num.matches("^1[3458]\\d{9}$")) {
					Cursor cursor = sql
							.rawQuery(
									"select location from data2 where id=(select outkey from data1 where id=?);",
									new String[] { num.substring(0, 7) });
					if (cursor.moveToNext()) {
						location = cursor.getString(0);
					}
					cursor.close();

				} else {

					switch (num.length()) {
					case 3: // 110 911 120 999
						location = "�������";
						break;
					case 6:
					case 4: // 5556 5554
						location = "����绰";
						break;
					case 5: // 10086 95588
						location = "����/�ƶ���Ӫ�̵ȿͷ��绰";
						break;
					case 7:
					case 8:
						location = "���ص绰";
						break;
					default: // ��;�绰
						if (num.length() >= 10) {
							Cursor cursor = sql.rawQuery(
									"select location from data2 where area= ?; ",
									new String[] { num.substring(1, 3) });
							if (cursor.moveToNext()) {
								String name = cursor.getString(0);
								location = name.substring(0,
										name.length()-2);

							} else {
								cursor = sql
										.rawQuery(
												"select location from data2 where area= ?; ",
												new String[] { num.substring(1, 4) });
								if (cursor.moveToNext()) {
									String name = cursor.getString(0);
									location = name.substring(0,
											name.length() - 2);
								}
							}
							cursor.close();
						}

						break;
					}
				}
				sql.close();
			}

			return location;
		}

}
