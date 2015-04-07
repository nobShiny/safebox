package com.lsj.safebox.service;

import java.util.Timer;
import java.util.TimerTask;

import com.lsj.safebox.R;
import com.lsj.safebox.receiver.MyAppWidget;
import com.lsj.safebox.utils.SystemInfoUtils;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.text.format.Formatter;
import android.widget.RemoteViews;

/**
 * widget服务
 * @author Administrator
 *
 */
public class UpdateAppWidgetService extends Service {

	private Timer timer;
	private TimerTask task;
	private AppWidgetManager awm;
	private InnerScreenReceiver receiver;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();

		if (timer == null && task == null) {
			startUpdateWidget();
		}
		// 监听锁屏
		receiver = new InnerScreenReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_SCREEN_OFF);// 屏幕关闭-锁屏
		filter.addAction(Intent.ACTION_SCREEN_ON);// 屏幕打开
		registerReceiver(receiver, filter);
	}

	private void startUpdateWidget() {
		awm = AppWidgetManager.getInstance(this);

		timer = new Timer();
		task = new TimerTask() {

			@Override
			public void run() {
				System.out.println("开始刷新widget");
				ComponentName provider = new ComponentName(
						UpdateAppWidgetService.this, MyAppWidget.class);
				// 远程View可以序列化
				RemoteViews views = new RemoteViews(getPackageName(),
						R.layout.mywidget);
				views.setTextViewText(
						R.id.process_count,
						"正在运行的软件:"
								+ SystemInfoUtils
										.getRunngProcessCount(UpdateAppWidgetService.this));
				views.setTextViewText(
						R.id.process_memory,
						"可用内存:"
								+ Formatter
										.formatFileSize(
												UpdateAppWidgetService.this,
												SystemInfoUtils
														.getAvailRam(UpdateAppWidgetService.this)));
				// 动作
				Intent intent = new Intent();
				intent.setAction("com.lsj.safebox.killprocess");
				// 延期意图；
				PendingIntent pendingIntent = PendingIntent.getBroadcast(
						UpdateAppWidgetService.this, 0, intent,
						PendingIntent.FLAG_UPDATE_CURRENT);
				views.setOnClickPendingIntent(R.id.btn_clear, pendingIntent);
				awm.updateAppWidget(provider, views);

			}
		};
		timer.schedule(task, 0, 4000);
	}

	private class InnerScreenReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction() == Intent.ACTION_SCREEN_OFF) {
				// 锁屏
				if (timer != null && task != null) {
					timer.cancel();
					timer = null;
					task.cancel();
					task = null;
				}

			} else if (intent.getAction() == Intent.ACTION_SCREEN_ON) {
				// 屏幕打开
				if (timer == null && task == null) {
					startUpdateWidget();
				}

			}

		}

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		// 取消屏幕监听
		unregisterReceiver(receiver);
		receiver = null;
		if (timer != null && task != null) {
			timer.cancel();
			timer = null;
			task.cancel();
			task = null;
		}
	}

}
