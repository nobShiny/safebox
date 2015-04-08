package com.lsj.safebox.ui;

import java.lang.reflect.Method;
import java.util.List;

import com.lsj.safebox.R;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.IPackageDataObserver;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.PackageStats;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.os.SystemClock;
import android.text.format.Formatter;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


public class CleanCacheActivity extends Activity {
	protected static final int SCANING = 0;
	protected static final int SCANING_FINISH = 1;
	public static final int SHOW_CACHE_INFO = 2;
	private Animation animation;
	private ImageView smallman,garbage1,garbage2;
	private TextView tv_status;
	private ProgressBar progressBar1;
	private LinearLayout container;
	private LinearLayout clear_main_tv,clear_main_tv2;
	private PackageManager pm;
	private Button main_back;
	private RelativeLayout bigclear;
	
	private Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SCANING:
				String name = (String) msg.obj;
				tv_status.setText(name);
				
				break;

			case SCANING_FINISH:
				tv_status.setText("扫描完毕");
				break;
			case SHOW_CACHE_INFO:
				

				final CacheInfo cacheInfo = (CacheInfo) msg.obj;
				View view = View.inflate(CleanCacheActivity.this, R.layout.list_cache_item, null);
				ImageView iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
				TextView tv_name = (TextView) view.findViewById(R.id.tv_name);
				TextView tv_cache_info = (TextView) view.findViewById(R.id.tv_cache_info);
				ImageView iv_delete = (ImageView) view.findViewById(R.id.iv_delete);
				
				
				
				iv_icon.setImageDrawable(cacheInfo.icon);
				tv_name.setText(cacheInfo.name);
				tv_cache_info.setText(Formatter.formatFileSize(CleanCacheActivity.this, cacheInfo.cacheSize));
				iv_delete.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						
						Method methods [] = PackageManager.class.getMethods();
						for(Method method: methods){
							if("deleteApplicationCacheFiles".equals(method.getName())){
								try {
									method.invoke(pm, cacheInfo.packName,new IPackageDataObserver.Stub() {
										
										public void onRemoveCompleted(String packageName, boolean succeeded)
												throws RemoteException {
											
										}
									});
								} catch (Exception e) {

									Intent intent = new Intent();
									intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
									intent.setData(Uri.parse("package:"+cacheInfo.packName));
									startActivity(intent);
									e.printStackTrace();
								}
							}
						}
						
					}
				});
				
				
				container.addView(view, 0);
				
				break;
			}
		};
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_clean_cache);
		tv_status = (TextView) findViewById(R.id.tv_status);
		progressBar1 = (ProgressBar) findViewById(R.id.progressBar1);
		container = (LinearLayout) findViewById(R.id.container);
		bigclear = (RelativeLayout) findViewById(R.id.clear_main_bigclear);
		main_back = (Button) findViewById(R.id.main_back);
		
		//清理动画
		clear_main_tv=(LinearLayout) findViewById(R.id.clear_main_tv);
		clear_main_tv2=(LinearLayout) findViewById(R.id.clear_main_tv2);
		smallman=(ImageView) findViewById(R.id.clear_main_clearman);
		garbage1=(ImageView) findViewById(R.id.clear_main_garbage1);
		garbage2=(ImageView) findViewById(R.id.clear_main_garbage2);
		
		main_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.main_back://返回
					finish();
					break;
				default:
					break;
				}
			}
		});
		
		new Thread(){
			public void run() {
				
				
				pm = getPackageManager();
				List<PackageInfo> packInfos = pm.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);
				progressBar1.setMax(packInfos.size());
				int progress = 0;
				for(PackageInfo packInfo:packInfos){
					SystemClock.sleep(50);
					String name = packInfo.applicationInfo.loadLabel(pm).toString();
					Message msg = Message.obtain();
					msg.obj = name;
					msg.what = SCANING;
					handler.sendMessage(msg);
					progress ++;
					progressBar1.setProgress(progress);
					
					
					try {
						Method method = PackageManager.class.getMethod("getPackageSizeInfo", String.class,IPackageStatsObserver.class);
						
						method.invoke(pm, packInfo.packageName,new MyIPackageStatsObserver());
					} catch (Exception e) {
						e.printStackTrace();
					}
					
					
					
				}
				
				Message msg = Message.obtain();
				msg.what = SCANING_FINISH;
				handler.sendMessage(msg);
			};
		}.start();
	}
	
	
	class MyIPackageStatsObserver extends IPackageStatsObserver.Stub{

		@Override
		public void onGetStatsCompleted(PackageStats pStats, boolean succeeded)
				throws RemoteException {
			
			long cacheSize = pStats.cacheSize;
			if(cacheSize > 0){
				
				try {
					CacheInfo cacheInfo = new CacheInfo();
					cacheInfo.cacheSize = cacheSize;
					cacheInfo.icon = pm.getApplicationInfo(pStats.packageName, 0).loadIcon(pm);
					cacheInfo.name = pm.getApplicationInfo(pStats.packageName, 0).loadLabel(pm).toString();
					cacheInfo.packName = pStats.packageName;
					Message msg = Message.obtain();
					msg.obj = cacheInfo;
					msg.what = SHOW_CACHE_INFO;
					handler.sendMessage(msg);
				} catch (NameNotFoundException e) {
					e.printStackTrace();
				}
				
			}
			
			
		}
		
	}
	
	class CacheInfo{
		long cacheSize;
		String name;
		Drawable icon;
		String packName;
	}
	
	//清除全部缓存
	public void cleanCache(View view){
		try {
			Method methods []= PackageManager.class.getMethods();
			for(Method method:methods){
				if("freeStorageAndNotify".equals(method.getName())){
					method.invoke(pm, Integer.MAX_VALUE,new IPackageDataObserver.Stub() {
						
						@Override
						public void onRemoveCompleted(String packageName, boolean succeeded)
								throws RemoteException {
							System.out.println(succeeded);
							
						}
					});
					bigclear.setVisibility(View.GONE);
					
				}
				
			}
		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), "清理失败, 请重试", 0).show();
 			e.printStackTrace();
		}
		
		clear_main_tv.setVisibility(View.GONE);	
		showAnim();
		garbageShowAnim();
		
	}
	
	/**
	 * 清理动画
	 */
	private void showAnim(){
		animation=AnimationUtils.loadAnimation(this, R.anim.clear);
		smallman.startAnimation(animation);
		animation.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				smallman.setImageResource(R.drawable.float_window_anzai_clean_finish);
				clear_main_tv2.setVisibility(View.VISIBLE);
				container.setVisibility(View.GONE);
				garbage1.setVisibility(View.GONE);	
				garbage2.setVisibility(View.GONE);
				
			}
		});
		
		
	}
	private void garbageShowAnim(){
		animation=AnimationUtils.loadAnimation(this, R.anim.garbage_anim);
		garbage2.startAnimation(animation);
		garbage1.startAnimation(animation);
	}
	

}
