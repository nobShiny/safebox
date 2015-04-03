package com.lsj.safebox.engine;

import java.util.ArrayList;
import java.util.List;

import com.lsj.safebox.domin.AppInfo;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;


public class AppInfoProvider {
	/**
	 * 获取手机所有应用程序信息
	 * 
	 * @param context
	 *            上下文
	 * @return List集合
	 */
	public static List<AppInfo> getAppInfos(Context context) {

		List<AppInfo> appInfos = new ArrayList<AppInfo>();
		// 创建包管理器 // 可以获取清单文件中的所有信息
		PackageManager manager = context.getPackageManager();
		List<PackageInfo> installedPackages = manager.getInstalledPackages(0);
		for (PackageInfo info : installedPackages) {
			AppInfo appInfo = new AppInfo();
			String packageName = info.packageName;
			appInfo.setPackageName(packageName);

			String versionName = info.versionName;
			appInfo.setVersionName(versionName);

			String loadLabel = info.applicationInfo.loadLabel(manager)
					.toString();// 加载应用程序 名字
			appInfo.setName(loadLabel);

			Drawable loadIcon = info.applicationInfo.loadIcon(manager);// 加载应用程序的图标
			appInfo.setIcon(loadIcon);

			int flags = info.applicationInfo.flags; // 装备
			if ((flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
				// 系统应用
				appInfo.setUser(false);
			} else {
				// 用户应用
				appInfo.setUser(true);
			}
			if ((flags & ApplicationInfo.FLAG_EXTERNAL_STORAGE) != 0) {
				// sd
				appInfo.setRom(false);
			} else {
				// rom
				appInfo.setRom(true);
			}
			appInfos.add(appInfo);

		}

		return appInfos;
	}
}
