package com.lsj.safebox.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

public class BootCompleteReceiver extends BroadcastReceiver {
	private TelephonyManager manager;
	private SharedPreferences sp;
	private SmsManager smsManager;

	@Override
	public void onReceive(Context context, Intent intent) {
		manager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		if (sp.getBoolean("protected", false)) {
			// 1 获取配置文件中 sim卡串号
			String sim_sp = sp.getString("sim", "");
			if (TextUtils.isEmpty(sim_sp)) {
				return;
			}
			// 2 再获取下当前手机sim卡串号
			String simSerialNumber = manager.getSimSerialNumber();
			// 3 如果发现两个串号 不一致的情况
			if (sim_sp.equals(simSerialNumber)) {
				System.out.println("sim卡没有发生变化");
			} else {
				// sim卡串号发生变化
				System.out.println("sim卡发生变化");
				smsManager = SmsManager.getDefault();// 初始化短信的管理者
				// 1 目标号码 //3 短信的内容
				String safenum = sp.getString("safenum", "");
				if (!TextUtils.isEmpty(safenum)) {
					smsManager.sendTextMessage(safenum, null, "sim changed",
							null, null);
				}
			}
		}
	}

}
