package com.lsj.safebox.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.lsj.safebox.R;
import com.lsj.safebox.db.dao.AntiVirusQuery;
import com.lsj.safebox.utils.MD5Utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


public class AntiVirusActivity extends Activity {
	protected static final int SCANING = 0;
	protected static final int SCANING_FiNISH = 1;
	private ImageView iv_scanning;
	private TextView tv_status;
	private ProgressBar progressBar1;
	
	private Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SCANING:
				ScanInfo info = (ScanInfo) msg.obj;
				tv_status.setText(info.name);
				TextView tv = new TextView(AntiVirusActivity.this);
				if(info.isAntivirus){
					tv.setTextColor(Color.RED);
					tv.setText("发现病毒:"+info.name);
				}else{
					tv.setTextColor(Color.BLACK);
					tv.setText("扫描安全:"+info.name);
				}
				ll_container.addView(tv, 0);
				break;

			case SCANING_FiNISH:
				iv_scanning.clearAnimation();
				tv_status.setText("扫描完毕");
				if(antiVirusList.size()>0){
					AlertDialog.Builder builder = new Builder(AntiVirusActivity.this);
					builder.setTitle("发现病毒");
					builder.setMessage("您的手机处于十分危险状态，发现了"+antiVirusList.size()+"个病毒");
					builder.setPositiveButton("不怕病毒", new DialogInterface. OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							
						}
					});
					builder.setNegativeButton("立即清除", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							
							//卸载软件
							for(ScanInfo scanInfo :antiVirusList){
								Intent intent = new Intent();
								intent.setAction("android.intent.action.DELETE");
								intent.addCategory("android.intent.category.DEFAULT");
								intent.setData(Uri.parse("package:"+scanInfo.packName));
								startActivity(intent);
							}
							
						}
					});
					
					builder.show();
					
					
					
				}else{
					Toast.makeText(getApplicationContext(), "您的手机十分的安全", 1).show();
				}
				break;
			}
			
		};
	};
	
	private LinearLayout ll_container;
	private List<ScanInfo> antiVirusList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_antivirus);
		iv_scanning = (ImageView) findViewById(R.id.iv_scanning);
		tv_status = (TextView) findViewById(R.id.tv_status);
		progressBar1 = (ProgressBar) findViewById(R.id.progressBar1);
		ll_container = (LinearLayout) findViewById(R.id.ll_container);

		RotateAnimation ra = new RotateAnimation(0, 360,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		ra.setRepeatCount(RotateAnimation.INFINITE);
		ra.setDuration(1000);
		iv_scanning.startAnimation(ra);

		tv_status.setText("杀毒8引擎正在初始化...");
		antiVirusList = new ArrayList<AntiVirusActivity.ScanInfo>();
		new Thread() {
			public void run() {
				PackageManager pm = getPackageManager();
				List<PackageInfo> packInfos = pm
						.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES
								+ PackageManager.GET_SIGNATURES);
				progressBar1.setMax(packInfos.size());
				int progress = 0;
				Random random = new Random();
				for(PackageInfo packInfo : packInfos){
					
					ScanInfo info = new ScanInfo();
					
					System.out.println(packInfo.applicationInfo.loadLabel(pm));
					info.name = packInfo.applicationInfo.loadLabel(pm).toString();
					info.packName = packInfo.packageName;
					//签名信息已经变成md5的
					String signatures =MD5Utils.encode(packInfo.signatures[0].toCharsString());
					String result = AntiVirusQuery.isAntivirus(signatures);
					System.out.println(signatures);
					if(result!=null){
						info.isAntivirus = true;
						antiVirusList.add(info);
						System.out.println("-是病毒----");
					}else{
						System.out.println("-扫描安全----");
						info.isAntivirus = false;
					}
					System.out.println(signatures);
					
					Message msg = Message.obtain();
					msg.obj = info;
					msg.what = SCANING;
					handler.sendMessage(msg);
					progress++;
					progressBar1.setProgress(progress);
					
					try {
						sleep(50+random.nextInt(50));
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
					
					
				}
				
				Message msg = Message.obtain();
				msg.what = SCANING_FiNISH;
				handler.sendMessage(msg);

			};
		}.start();

	}
	
	class ScanInfo{
		String name;
		boolean isAntivirus;
		String packName;
		
	}

}