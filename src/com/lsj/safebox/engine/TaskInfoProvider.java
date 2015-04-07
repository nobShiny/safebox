package com.lsj.safebox.engine;

import java.util.ArrayList;
import java.util.List;

import com.lsj.safebox.R;
import com.lsj.safebox.domin.TaskInfo;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.os.Debug.MemoryInfo;


public class TaskInfoProvider {

	/**
	 * �õ���ǰ�ֻ��������еĽ�����Ϣ
	 * 
	 * @param context
	 *            ������
	 * @return
	 */

	public static List<TaskInfo> getTaskInfos(Context context) {
		List<TaskInfo> taskInfos = new ArrayList<TaskInfo>();
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> appProcessInfos = am
				.getRunningAppProcesses();
		PackageManager pm = context.getPackageManager();
		for (RunningAppProcessInfo processInfo : appProcessInfos) {
			TaskInfo info = new TaskInfo();

			String packNmae = processInfo.processName;
			info.setPackName(packNmae);
			int[] pids = { processInfo.pid };
			MemoryInfo memoryInfo = am.getProcessMemoryInfo(pids)[0];
			// �ڴ��С
			long memsize = memoryInfo.getTotalPrivateDirty() * 1024;
			info.setMemsize(memsize);

			try {
				PackageInfo packInfo = pm.getPackageInfo(packNmae, 0);
				Drawable icon = packInfo.applicationInfo.loadIcon(pm);
				info.setIcon(icon);
				String name = packInfo.applicationInfo.loadLabel(pm).toString();
				info.setName(name);
				if ((packInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
					// �û�����
					info.setUser(true);
				} else {
					// ϵͳ����
					info.setUser(false);
				}
			} catch (NameNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				info.setName(packNmae);
				info.setIcon(context.getResources().getDrawable(
						R.drawable.default_icon));

			}

			taskInfos.add(info);

		}
		return taskInfos;
	}

}
