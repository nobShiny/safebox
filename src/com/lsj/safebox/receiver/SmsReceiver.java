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
			// 获取短信的数组
			Object[] objs = (Object[]) intent.getExtras().get("pdus");
			for (Object obj : objs) {
				// 创建一条短信
				SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) obj);
				// 获取短信的内容
				String body = smsMessage.getMessageBody();
				// 获取短信的发送者
				String sender = smsMessage.getOriginatingAddress();

				if ("#*location*#".equals(body)) {
					// 获取手机位置
					System.out.println("location");
					abortBroadcast();// 屏蔽掉短信了
					// 开启定位的服务
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
					// 播放报警音乐
					System.out.println("alarm");
					abortBroadcast();
					// 声音的管理者
					AudioManager audioManager = (AudioManager) context
							.getSystemService(Context.AUDIO_SERVICE);
					// 获取音乐声音的最大值
					int streamMaxVolume = audioManager
							.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
					// 设置音量的大小 参数1 那一套音乐系统 参数2 设置的音乐的大小
					audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
							streamMaxVolume, 0);

//					MediaPlayer mediaPlayer = MediaPlayer.create(context,
//							R.raw.ylzs);
//					mediaPlayer.setVolume(1.0f, 1.0f);
//					mediaPlayer.start();// 播放报警音乐

				} else if ("#*wipe*#".equals(body)) {
					// 远程擦除数据
					System.out.println("wipe");
					abortBroadcast();
					ComponentName who = new ComponentName(context, Admin.class);
					if (manager.isAdminActive(who)) {
						manager.wipeData(0);// 擦除数据
					}
				} else if ("#*lockScreen*#".equals(body)) {
					// 远程锁屏
					System.out.println("lockScreen");
					abortBroadcast();
					ComponentName who = new ComponentName(context, Admin.class);
					if (manager.isAdminActive(who)) {
						manager.lockNow();
						manager.resetPassword("123", 0);// 重置密码
						// manager.removeActiveAdmin(who);// 反激活超级管理员
					}
				}

			}
		}

	}

}
