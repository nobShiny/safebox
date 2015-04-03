package com.lsj.safebox.utils;

import java.io.File;
import java.io.FileOutputStream;

import org.xmlpull.v1.XmlSerializer;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.SystemClock;
import android.util.Xml;

/**
 * ���ű��ݹ���
 * @author Administrator
 *
 */
public class SmsBackupUtils {

	public interface SmsBackupCallback {
		/**
		 * ���ű���ǰ����
		 * @param total
		 *            ���ŵ�������
		 */
		public void beforeSmsbackup(int total);

		/**
		 * ���ű����е���
		 * @param progress
		 *            ���ű��ݵĽ���
		 */
		public void progressSmsbackup(int progress);
	}

	/**
	 * ���ű��ݵķ���
	 * 
	 * @param context
	 *            ������
	 * @param path
	 *            ���ݵ��Ǹ�·��
	 * @throws Exception
	 */
	/**
	 * �����û��Ķ���
	 * 
	 * @param context
	 *            ������
	 * @param BackUpCallBack
	 *            ���ݶ��ŵĽӿ�
	 */
	public static void backupSms(Context context, SmsBackupCallback callBack)
			throws Exception {
		ContentResolver resolver = context.getContentResolver();
		File file = new File(Environment.getExternalStorageDirectory(),"backup.xml");
		FileOutputStream fos = new FileOutputStream(file);
		// ���û��Ķ���һ��һ��������������һ���ĸ�ʽд���ļ���
		XmlSerializer serializer = Xml.newSerializer();// ��ȡxml�ļ��������������л�����
		// ��ʼ��������
		serializer.setOutput(fos, "utf-8");
		serializer.startDocument("utf-8", true);
		serializer.startTag(null, "smss");
		Uri uri = Uri.parse("content://sms/");
		Cursor cursor = resolver.query(uri, new String[] { "body", "address",
				"type", "date" }, null, null, null);
		// ��ʼ���ݵ�ʱ�����ý����������ֵ
		int max = cursor.getCount();
		callBack.beforeSmsbackup(max);
		serializer.attribute(null, "max", max + "");
		int process = 0;
		while (cursor.moveToNext()) {
			Thread.sleep(500);
			String body = cursor.getString(0);
			String address = cursor.getString(1);
			String type = cursor.getString(2);
			String date = cursor.getString(3);
			serializer.startTag(null, "sms");

			serializer.startTag(null, "body");
			serializer.text(body);
			serializer.endTag(null, "body");

			serializer.startTag(null, "address");
			serializer.text(address);
			serializer.endTag(null, "address");

			serializer.startTag(null, "type");
			serializer.text(type);
			serializer.endTag(null, "type");

			serializer.startTag(null, "date");
			serializer.text(date);
			serializer.endTag(null, "date");

			serializer.endTag(null, "sms");
			// ���ݹ����У����ӽ���
			process++;
			callBack.beforeSmsbackup(process);
		}
		cursor.close();
		serializer.endTag(null, "smss");
		serializer.endDocument();
		fos.close();
	}
	
	/**
	 * ��ԭ����
	 * 
	 * @param context
	 * @param flag
	 *            �Ƿ�����ԭ���Ķ���
	 */
	public static void restoreSms(Context context, boolean flag) {
		Uri uri = Uri.parse("content://sms/");
		if (flag) {
			context.getContentResolver().delete(uri, null, null);
		}
		// 1.��ȡsd���ϵ�xml�ļ�
		// Xml.newPullParser();

		// 2.��ȡmax

		// 3.��ȡÿһ��������Ϣ��body date type address

		// 4.�Ѷ��Ų��뵽ϵͳ��ϢӦ�á�

		ContentValues values = new ContentValues();
		values.put("body", "woshi duanxin de neirong");
		values.put("date", "1395045035573");
		values.put("type", "1");
		values.put("address", "5558");
		context.getContentResolver().insert(uri, values);
	}

}
