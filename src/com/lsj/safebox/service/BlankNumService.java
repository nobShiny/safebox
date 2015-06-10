package com.lsj.safebox.service;

import java.lang.reflect.Method;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.android.internal.telephony.ITelephony;
import com.lsj.safebox.db.dao.BlankNum;

public class BlankNumService extends Service {
	private SMSReceiver receiver;
	private BlankNum dao;
	private TelephonyManager manager;
	private MyPhoneStateListener listener;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		dao = new BlankNum(getApplicationContext());
		// 注册广播
		// 1 创建广播接受者
		receiver = new SMSReceiver();
		// 2 意图过滤器
		IntentFilter filter = new IntentFilter();
		filter.setPriority(Integer.MAX_VALUE);
		filter.addAction("android.provider.Telephony.SMS_RECEIVED");
		// 3 注册广播接受者
		registerReceiver(receiver, filter);

		manager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		listener = new MyPhoneStateListener();
		manager.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
		super.onCreate();
	}

	private class MyPhoneStateListener extends PhoneStateListener {
		// incomingNumber 来电号码
		@Override
		public void onCallStateChanged(int state, final String incomingNumber) {
			switch (state) {
			case TelephonyManager.CALL_STATE_RINGING: // 电话到来的状态
				// 1 查询数据库
				int mode = dao.queryBlankNum(incomingNumber);
				if (mode == BlankNum.TEL || mode == BlankNum.ALL) {
					// 挂断电话
					System.out.println("挂断电话");
					// manager.endCall();
					endCall();
					final ContentResolver resolver = getContentResolver();

					final Uri uri = Uri.parse("content://call_log/calls");
					// 电话到来 不会再数据库中立刻生成一条数据 通过内容观察者 观察数据库的变化
					resolver.registerContentObserver(uri, true,
							new ContentObserver(new Handler()) {
								// 数据库数据发生变化调用

								@Override
								public void onChange(boolean selfChange) {
									super.onChange(selfChange);
									// 通过内容解析者 删除通话记录
									resolver.delete(uri, "number=?",
											new String[] { incomingNumber });
									// 反注册内容观察者
									resolver.unregisterContentObserver(this);
									// uri
								}

							});

				}

				break;
			}

			super.onCallStateChanged(state, incomingNumber);
		}

	}

	/**
	 * 短信拦截
	 * @author Administrator
	 *
	 */
	
	private class SMSReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			Log.i("TAG","短信拦截拦截");
			//检查来信人是否是黑名单人员
			Object[] objs = (Object[]) intent.getExtras().get("pdus");
			for (Object obj : objs) {
				// 创建一条短信
				SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) obj);
				// 获取短信的内容
				String body = smsMessage.getMessageBody();
				// 获取短信的发送者
				String sender = smsMessage.getOriginatingAddress();

				int mode = dao.queryBlankNum(sender);
				if (mode == BlankNum.ALL || mode == BlankNum.SMS) {
					// 把短信 存到应用程序的数据库 写一个界面
					abortBroadcast();
				}

			}
		}

	}

	@Override
	public void onDestroy() {
		// 反注册广播接受者
		if (receiver != null) {
			unregisterReceiver(receiver);
			receiver = null;

		}
		if (listener != null && manager != null) {
			manager.listen(listener, PhoneStateListener.LISTEN_NONE);
			listener = null;
			manager = null;
		}
		super.onDestroy();
	}

	/**
	 * 拦截电话
	 */
	public void endCall() {
		Class<?> loadClass;
		try {
			loadClass = getClassLoader().loadClass("android.os.ServiceManager");
			Method method = loadClass.getMethod("getService", String.class);
			IBinder invoke = (IBinder) method.invoke(null,Context.TELEPHONY_SERVICE);
			// 返回了 真正的电话管理者
			ITelephony asInterface = ITelephony.Stub.asInterface(invoke);
			asInterface.endCall();// 挂断电话
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
