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
			// 1 ��ȡ�����ļ��� sim������
			String sim_sp = sp.getString("sim", "");
			if (TextUtils.isEmpty(sim_sp)) {
				return;
			}
			// 2 �ٻ�ȡ�µ�ǰ�ֻ�sim������
			String simSerialNumber = manager.getSimSerialNumber();
			// 3 ��������������� ��һ�µ����
			if (sim_sp.equals(simSerialNumber)) {
				System.out.println("sim��û�з����仯");
			} else {
				// sim�����ŷ����仯
				System.out.println("sim�������仯");
				smsManager = SmsManager.getDefault();// ��ʼ�����ŵĹ�����
				// 1 Ŀ����� //3 ���ŵ�����
				String safenum = sp.getString("safenum", "");
				if (!TextUtils.isEmpty(safenum)) {
					smsManager.sendTextMessage(safenum, null, "sim changed",
							null, null);
				}
			}
		}
	}

}
