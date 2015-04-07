package com.lsj.safebox.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.content.Context;

public class SystemInfoUtils {

	/**
	 * �õ���ǰϵͳ���еĽ�������
	 * 
	 * @param context
	 *            ������
	 * @return ��������
	 */
	public static int getRunngProcessCount(Context context) {
		// ActivityManager
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		return am.getRunningAppProcesses().size();
	}

	/**
	 * �õ������ڴ�/ʣ���ڴ�
	 * 
	 * @param context
	 * @return
	 */
	public static long getAvailRam(Context context) {
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);

		MemoryInfo outInfo = new MemoryInfo();
		am.getMemoryInfo(outInfo);
		return outInfo.availMem;

	}

	/**
	 * �õ��ܵ��ڴ�
	 * 
	 * @param context
	 * @return
	 */
	public static long getTotalRam(Context context) {

		StringBuffer buffer = null;
		try {
			File file = new File("/proc/meminfo");
			// MemTotal: 516452 kB
			FileInputStream fis = new FileInputStream(file);
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					fis));
			String result = reader.readLine();
			buffer = new StringBuffer();
			// �ַ���-�ַ�
			for (char c : result.toCharArray()) {
				if (c >= '0' && c <= '9') {
					buffer.append(c);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}

		return Integer.valueOf(buffer.toString()) * 1024;

	}

}
