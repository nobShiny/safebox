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
	 * 得到当前系统运行的进程总数
	 * 
	 * @param context
	 *            上下文
	 * @return 进程总数
	 */
	public static int getRunngProcessCount(Context context) {
		// ActivityManager
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		return am.getRunningAppProcesses().size();
	}

	/**
	 * 得到可用内存/剩余内存
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
	 * 得到总的内存
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
			// 字符串-字符
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
