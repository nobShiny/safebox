package com.lsj.safebox.service;

import com.lsj.safebox.R;
import com.lsj.safebox.db.dao.Address;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.TextView;

/**
 * ��������ķ���
 * 
 * @author Administrator
 *
 */
public class AddressService extends Service {
	private TelephonyManager manager;
	private MyPhoneStateListener listener;
	private Address addressDao;
	private OutGoingCallReceiver receiver;
	private SharedPreferences sp;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	// �����һ�δ�����ʱ����õķ���
	@Override
	public void onCreate() {
		super.onCreate();
		sp = getSharedPreferences("config", MODE_PRIVATE);
		// ע��㲥������
		// 1 ��Ҫ�㲥������
		receiver = new OutGoingCallReceiver();
		// 2 ��ͼ������
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.intent.action.NEW_OUTGOING_CALL");
		// 3 �ڴ�����ע��
		registerReceiver(receiver, filter);

		// �����绰��״̬
		addressDao = new Address(getApplicationContext());
		listener = new MyPhoneStateListener();
		manager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		manager.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
	}

	private class OutGoingCallReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// ����Ⲧ�绰�ĵ绰����
			String num = getResultData();
			String queryAddress = addressDao.queryAddress(num);
			showToast(queryAddress);
		}
	}

	private class MyPhoneStateListener extends PhoneStateListener {
		// incomingNumber �������
		// state �绰״̬
		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			switch (state) {
			case TelephonyManager.CALL_STATE_IDLE: // ����״̬
				hideMyToast();
				break;
			case TelephonyManager.CALL_STATE_RINGING:// ����״̬
				// ��ѯ���������
				String queryAddress = addressDao.queryAddress(incomingNumber);
				if (!TextUtils.isEmpty(queryAddress)) {
					System.out.println(queryAddress);
					// Toast.makeText(getApplicationContext(), queryAddress,
					// 0).show();
					showToast(queryAddress);
				}
				break;
			case TelephonyManager.CALL_STATE_OFFHOOK:// �绰���� �绰ͨ����
				break;

			default:
				break;
			}

			super.onCallStateChanged(state, incomingNumber);
		}

	}

	private WindowManager wm;
	private View view;
	int[] bgcolor = new int[] { R.drawable.call_locate_white,
			R.drawable.call_locate_orange, R.drawable.call_locate_blue,
			R.drawable.call_locate_gray, R.drawable.call_locate_green };
	private WindowManager.LayoutParams params;

	// ��ʾToast
	public void showToast(String str) {
		// ���wm ��Ϊ�� viewҲ��Ϊ�� wm.removeVIew
		if (wm != null && view != null) {
			wm.removeView(view);
		}
		// Toast.makeText(getApplicationContext(), "��˾", 0).show();
		wm = (WindowManager) getSystemService(WINDOW_SERVICE);
		// �����Զ���Toast����
		view = View.inflate(getApplicationContext(), R.layout.my_toast, null);
		view.setBackgroundResource(bgcolor[sp.getInt("which", 0)]);// ���������ļ��д洢�����ݶ�̬�л�toast����
		TextView tv_location = (TextView) view.findViewById(R.id.tv_location);
		tv_location.setText(str);
		// ����Toast��ʾ�Ĳ���
		// ������һ������

		params = new WindowManager.LayoutParams();
		// �޸�Toastλ��
		params.gravity = Gravity.LEFT | Gravity.TOP;// toast ���� ���϶���
		params.x = sp.getInt("lastX", 100); // Gravity.LEFT �����������ľ���
											// Gravity.Right �����������ľ���
		params.y = sp.getInt("lastY", 100);// Gravity.TOP �����������ľ���
											// Gravity.Bottom ��������������

		// ��͸߰�������
		params.height = WindowManager.LayoutParams.WRAP_CONTENT;
		params.width = WindowManager.LayoutParams.WRAP_CONTENT;
		// ���ɻ�ȡ���� ���ɴ��� ��Ļ����
		params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
		// | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
				| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
		params.format = PixelFormat.TRANSLUCENT; // ��˾��͸��
		params.type = WindowManager.LayoutParams.TYPE_PRIORITY_PHONE; // ��������
																		// Toast����
																		// Ĭ�ϾͲ��ܻ�ȡ����

		setTouch();
		// �ڴ������������ ���һ������
		wm.addView(view, params);
	}

	private void setTouch() {
		view.setOnTouchListener(new OnTouchListener() {
			private int startX;
			private int startY;

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN: // �����µ��¼�
					// 1 ��¼��ʼ��λ��
					startX = (int) event.getRawX();
					startY = (int) event.getRawY();
					System.out.println("������");
					break;
				case MotionEvent.ACTION_MOVE: // �ƶ����¼�
					// 2 ��¼�µ�λ��
					int newX = (int) event.getRawX();
					int newY = (int) event.getRawY();
					// 3 �����ƶ�����ı仯
					int dX = newX - startX;
					int dY = newY - startY;
					// 4 �ؼ��ƶ�����ı仯
					// ���·���ؼ���λ�� l left top right bottom
					// int l=view.getLeft()+dX;//view.getLeft() ��ȡ���ؼ����븸�ؼ��ľ���
					// int t=view.getTop()+dY;
					// int r=l+view.getWidth();
					// int b=t+view.getHeight();
					// �������ƶ���Եλ��
					// view.layout(l, t, r, b); // �ڸ������з���ؼ���λ��
					params.x += dX; // �޸��˿ؼ�x ��y����
					params.y += dY;
					// ��������λ��
					wm.updateViewLayout(view, params);

					// 5
					startX = newX;
					startY = newY;

					break;
				case MotionEvent.ACTION_UP:// ��ָ̧����¼�
					// ��¼����
					// int lastX=view.getLeft();
					// int lastY=view.getTop();
					int lastX = params.x;
					int lastY = params.y;
					Editor edit = sp.edit();
					edit.putInt("lastX", lastX);
					edit.putInt("lastY", lastY);
					edit.commit();

					break;

				}
				return true;
			}
		});
	}

	// ����Toast
	public void hideMyToast() {
		if (wm != null && view != null) {
			wm.removeView(view);
			wm = null;
			view = null;
		}
	}

	// �������ٵ�ʱ�����
	public void onDestroy() {
		// ȡ������
		if (listener != null) {
			manager.listen(listener, PhoneStateListener.LISTEN_NONE);// ʲô��������
																		// ȡ������
			listener = null;
		}
		if (receiver != null) {
			unregisterReceiver(receiver);// ��ע��㲥������
			receiver = null;
		}
	}

}
