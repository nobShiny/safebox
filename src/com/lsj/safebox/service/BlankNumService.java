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
		// ע��㲥
		// 1 �����㲥������
		receiver = new SMSReceiver();
		// 2 ��ͼ������
		IntentFilter filter = new IntentFilter();
		filter.setPriority(Integer.MAX_VALUE);
		filter.addAction("android.provider.Telephony.SMS_RECEIVED");
		// 3 ע��㲥������
		registerReceiver(receiver, filter);

		manager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		listener = new MyPhoneStateListener();
		manager.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
		super.onCreate();
	}

	private class MyPhoneStateListener extends PhoneStateListener {
		// incomingNumber �������
		@Override
		public void onCallStateChanged(int state, final String incomingNumber) {
			switch (state) {
			case TelephonyManager.CALL_STATE_RINGING: // �绰������״̬
				// 1 ��ѯ���ݿ�
				int mode = dao.queryBlankNum(incomingNumber);
				if (mode == BlankNum.TEL || mode == BlankNum.ALL) {
					// �Ҷϵ绰
					System.out.println("�Ҷϵ绰");
					// manager.endCall();
					endCall();
					final ContentResolver resolver = getContentResolver();

					final Uri uri = Uri.parse("content://call_log/calls");
					// �绰���� ���������ݿ�����������һ������ ͨ�����ݹ۲��� �۲����ݿ�ı仯
					resolver.registerContentObserver(uri, true,
							new ContentObserver(new Handler()) {
								// ���ݿ����ݷ����仯����

								@Override
								public void onChange(boolean selfChange) {
									super.onChange(selfChange);
									// ͨ�����ݽ����� ɾ��ͨ����¼
									resolver.delete(uri, "number=?",
											new String[] { incomingNumber });
									// ��ע�����ݹ۲���
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
	 * ��������
	 * @author Administrator
	 *
	 */
	
	private class SMSReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			Log.i("TAG","������������");
			//����������Ƿ��Ǻ�������Ա
			Object[] objs = (Object[]) intent.getExtras().get("pdus");
			for (Object obj : objs) {
				// ����һ������
				SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) obj);
				// ��ȡ���ŵ�����
				String body = smsMessage.getMessageBody();
				// ��ȡ���ŵķ�����
				String sender = smsMessage.getOriginatingAddress();

				int mode = dao.queryBlankNum(sender);
				if (mode == BlankNum.ALL || mode == BlankNum.SMS) {
					// �Ѷ��� �浽Ӧ�ó�������ݿ� дһ������
					abortBroadcast();
				}

			}
		}

	}

	@Override
	public void onDestroy() {
		// ��ע��㲥������
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
	 * ���ص绰
	 */
	public void endCall() {
		Class<?> loadClass;
		try {
			loadClass = getClassLoader().loadClass("android.os.ServiceManager");
			Method method = loadClass.getMethod("getService", String.class);
			IBinder invoke = (IBinder) method.invoke(null,Context.TELEPHONY_SERVICE);
			// ������ �����ĵ绰������
			ITelephony asInterface = ITelephony.Stub.asInterface(invoke);
			asInterface.endCall();// �Ҷϵ绰
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
