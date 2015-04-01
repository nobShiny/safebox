package com.lsj.safebox.utils;

import android.os.Handler;

/**
 * ģ�����ģʽ
 * 
 * @author yu
 *
 */
public abstract class MyAsynTask {
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			postTask();
		};
	};

	// �����߳�֮ǰִ�еĴ���
	public abstract void preTask();

	// �����߳�֮��ִ�еĴ���
	public abstract void postTask();

	// �����߳���ִ�еĴ���
	public abstract void doInBack();

	public void excute() {
		preTask();
		new Thread() {
			public void run() {
				doInBack();
				handler.sendEmptyMessage(0);
			};
		}.start();
	}
}
