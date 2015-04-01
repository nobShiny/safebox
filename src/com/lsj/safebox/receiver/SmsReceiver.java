package com.lsj.safebox.receiver;

import com.lsj.safebox.service.LocationService;

import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.provider.MediaStore.Audio;
import android.sax.StartElementListener;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.text.TextUtils;

public class SmsReceiver extends BroadcastReceiver {
	private SharedPreferences sp;
	private DevicePolicyManager manager;

	@Override
	public void onReceive(Context context, Intent intent) {
		sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		boolean isProtected = sp.getBoolean("protected", false);
		if (isProtected) {
			manager = (DevicePolicyManager) context
					.getSystemService(Context.DEVICE_POLICY_SERVICE);
			// ��ȡ���ŵ�����
			Object[] objs = (Object[]) intent.getExtras().get("pdus");
			for (Object obj : objs) {
				// ����һ������
				SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) obj);
				// ��ȡ���ŵ�����
				String body = smsMessage.getMessageBody();
				// ��ȡ���ŵķ�����
				String sender = smsMessage.getOriginatingAddress();

				if ("#*location*#".equals(body)) {
					// ��ȡ�ֻ�λ��
					System.out.println("location");
					abortBroadcast();// ���ε�������
					// ������λ�ķ���
					Intent location = new Intent(context, LocationService.class);
					context.startService(location);

					String latitude = sp.getString("latitude", "");
					String longitude = sp.getString("longitude", "");
					if (TextUtils.isEmpty(latitude)
							|| TextUtils.isEmpty(longitude)) {

					} else {
						SmsManager manager = SmsManager.getDefault();
						manager.sendTextMessage(sp.getString("safenum", ""),
								null, latitude + "\n" + longitude, null, null);
					}

				} else if ("#*alarm*#".equals(body)) {
					// ���ű�������
					System.out.println("alarm");
					abortBroadcast();
					// �����Ĺ�����
					AudioManager audioManager = (AudioManager) context
							.getSystemService(Context.AUDIO_SERVICE);
					// ��ȡ�������������ֵ
					int streamMaxVolume = audioManager
							.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
					// ���������Ĵ�С ����1 ��һ������ϵͳ ����2 ���õ����ֵĴ�С
					audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
							streamMaxVolume, 0);

//					MediaPlayer mediaPlayer = MediaPlayer.create(context,
//							R.raw.ylzs);
//					mediaPlayer.setVolume(1.0f, 1.0f);
//					mediaPlayer.start();// ���ű�������

				} else if ("#*wipe*#".equals(body)) {
					// Զ�̲�������
					System.out.println("wipe");
					abortBroadcast();
					ComponentName who = new ComponentName(context, Admin.class);
					if (manager.isAdminActive(who)) {
						manager.wipeData(0);// ��������
					}
				} else if ("#*lockScreen*#".equals(body)) {
					// Զ������
					System.out.println("lockScreen");
					abortBroadcast();
					ComponentName who = new ComponentName(context, Admin.class);
					if (manager.isAdminActive(who)) {
						manager.lockNow();
						manager.resetPassword("123", 0);// ��������
						// manager.removeActiveAdmin(who);// �����������Ա
					}
				}

			}
		}

	}

}
