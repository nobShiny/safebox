package com.lsj.safebox.utils;

import android.os.Handler;

/**
 * 模板设计模式
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

	// 在子线程之前执行的代码
	public abstract void preTask();

	// 在子线程之后执行的代码
	public abstract void postTask();

	// 在子线程中执行的代码
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
