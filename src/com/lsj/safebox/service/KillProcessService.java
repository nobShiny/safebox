package com.lsj.safebox.service;

import java.util.Timer;
import java.util.TimerTask;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

public class KillProcessService extends Service {
	private InnerScreenReceiver receiver;
	
	//定时器
	private Timer timer;
	private TimerTask task;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	@Override
	public void onCreate() {
		super.onCreate();
		
		timer = new Timer();
		task = new TimerTask() {
			
			@Override
			public void run() {
				System.out.println("定时器5秒执行一下");
				
			}
		};
		timer.schedule(task, 1000, 5000);
		//监听锁屏
		receiver = new InnerScreenReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_SCREEN_OFF);//屏幕关闭-锁屏
		registerReceiver(receiver, filter);
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
		unregisterReceiver(receiver);
		receiver = null;
		
		if(timer!= null){
			timer.cancel();
			timer = null;
		}
		
		if(task != null){
			task.cancel();
			task = null;
		}
	}
	
	private class InnerScreenReceiver  extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
			for(RunningAppProcessInfo processInfo :am.getRunningAppProcesses()){
				am.killBackgroundProcesses(processInfo.processName);
			}
			
			
		}
		
	}

}
