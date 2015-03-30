package com.lsj.safebox.utils;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;

public class ServiceUtils {
	/**
	 * �жϷ����Ƿ���������
	 * 
	 * @param context
	 * @param serviceName
	 *            �����ȫ����
	 * @return
	 */
	public static boolean isRunning(Context context, String serviceName) {
		// ActivityManager ���̵Ĺ�����
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningServiceInfo> runningServices = am.getRunningServices(1000); // ����
																				// ��ȡ�����������
																				// RAM
																				// 3G
																				// 64
																				// 128
																				// Rom
		for (RunningServiceInfo info : runningServices) {
			String className = info.service.getClassName();
			if (serviceName.equals(className)) {
				return true;
			}
		}

		return false;
	}
}
