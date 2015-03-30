package com.lsj.safebox.utils;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;

public class ServiceUtils {
	/**
	 * 判断服务是否正在运行
	 * 
	 * @param context
	 * @param serviceName
	 *            服务的全类名
	 * @return
	 */
	public static boolean isRunning(Context context, String serviceName) {
		// ActivityManager 进程的管理者
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningServiceInfo> runningServices = am.getRunningServices(1000); // 参数
																				// 获取集合最大数量
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
