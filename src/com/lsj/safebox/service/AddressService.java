package com.lsj.safebox.service;

import com.lsj.safebox.R;
import com.lsj.safebox.db.dao.Address;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

/**
 *监听来电的服务
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

	// 服务第一次创建的时候调用的方法
	@Override
	public void onCreate() {
		super.onCreate();
		sp = getSharedPreferences("config", MODE_PRIVATE);
		// 注册广播接受者
		// 1 需要广播接受者
		receiver = new OutGoingCallReceiver();
		// 2 意图过滤器
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.intent.action.NEW_OUTGOING_CALL");
		// 3 在代码中注册
		registerReceiver(receiver, filter);

		// 监听电话的状态
		addressDao = new Address(getApplicationContext());
		listener = new MyPhoneStateListener();
		manager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		manager.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
	}

	private class OutGoingCallReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// 获得外拨电话的电话号码
			String num = getResultData();
			String queryAddress = addressDao.queryAddress(num);
			showToast(queryAddress);
		}
	}

	private class MyPhoneStateListener extends PhoneStateListener {
		// incomingNumber 来电号码
		// state 电话状态
		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			switch (state) {
			case TelephonyManager.CALL_STATE_IDLE: // 空闲状态
				hideMyToast();
				break;
			case TelephonyManager.CALL_STATE_RINGING:// 响铃状态
				// 查询号码归属地
				String queryAddress = addressDao.queryAddress(incomingNumber);
				if (!TextUtils.isEmpty(queryAddress)) {
					System.out.println(queryAddress);
					// Toast.makeText(getApplicationContext(), queryAddress,
					// 0).show();
					showToast(queryAddress);
				}
				break;
			case TelephonyManager.CALL_STATE_OFFHOOK:// 电话挂起 电话通话中
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

	// 显示Toast
	public void showToast(String str) {
		// Toast.makeText(getApplicationContext(), "土司", 0).show();
		wm = (WindowManager) getSystemService(WINDOW_SERVICE);
		// 创建自定义Toast布局
		view = View.inflate(getApplicationContext(), R.layout.my_toast, null);
		view.setBackgroundResource(bgcolor[sp.getInt("which", 0)]);// 根据配置文件中存储的内容动态切换toast背景
		TextView tv_location = (TextView) view.findViewById(R.id.tv_location);
		tv_location.setText(str);
		// 定义Toast显示的参数
		// 创建了一个参数
		WindowManager.LayoutParams params = new WindowManager.LayoutParams();
		// 修改Toast位置
		params.gravity = Gravity.LEFT | Gravity.TOP;// toast 靠左 靠上对齐
		params.x = 200; // Gravity.LEFT 代表距离左面的距离 Gravity.Right 代表距离右面的距离
		params.y = 200;// Gravity.TOP 代表距离上面的距离 Gravity.Bottom 代表距离下面距离

		// 宽和高包裹内容
		params.height = WindowManager.LayoutParams.WRAP_CONTENT;
		params.width = WindowManager.LayoutParams.WRAP_CONTENT;
		// 不可获取焦点 不可触摸 屏幕常亮
		params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
				| WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
				| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
		params.format = PixelFormat.TRANSLUCENT; // 土司半透明
		params.type = WindowManager.LayoutParams.TYPE_TOAST; // 参数类型 Toast类型
		// 在窗体管理者上面 添加一个布局
		wm.addView(view, params);
	}

	// 隐藏Toast
	public void hideMyToast() {
		if (wm != null && view != null) {
			wm.removeView(view);
			wm = null;
			view = null;
		}
	}

	// 服务销毁的时候调用
	public void onDestroy() {
		// 取消监听
		if (listener != null) {
			manager.listen(listener, PhoneStateListener.LISTEN_NONE);// 什么都不监听
																		// 取消监听
			listener = null;
		}
		if (receiver != null) {
			unregisterReceiver(receiver);// 反注册广播接受者
			receiver = null;
		}
	}

}
