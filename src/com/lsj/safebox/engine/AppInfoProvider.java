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
	 * ��ȡ�ֻ�����Ӧ�ó�����Ϣ
	 * 
	 * @param context
	 *            ������
	 * @return List����
	 */
	public static List<AppInfo> getAppInfos(Context context) {

		List<AppInfo> appInfos = new ArrayList<AppInfo>();
		// ������������ // ���Ի�ȡ�嵥�ļ��е�������Ϣ
		PackageManager manager = context.getPackageManager();
		List<PackageInfo> installedPackages = manager.getInstalledPackages(0);
		for (PackageInfo info : installedPackages) {
			AppInfo appInfo = new AppInfo();
			String packageName = info.packageName;
			appInfo.setPackageName(packageName);

			String versionName = info.versionName;
			appInfo.setVersionName(versionName);

			String loadLabel = info.applicationInfo.loadLabel(manager)
					.toString();// ����Ӧ�ó��� ����
			appInfo.setName(loadLabel);

			Drawable loadIcon = info.applicationInfo.loadIcon(manager);// ����Ӧ�ó����ͼ��
			appInfo.setIcon(loadIcon);

			int flags = info.applicationInfo.flags; // װ��
			if ((flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
				// ϵͳӦ��
				appInfo.setUser(false);
			} else {
				// �û�Ӧ��
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
