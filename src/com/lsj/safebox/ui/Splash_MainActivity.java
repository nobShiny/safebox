package com.lsj.safebox.ui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import com.lsj.safebox.R;
import com.lsj.safebox.service.AddressService;
import com.lsj.safebox.service.UpdateAppWidgetService;
import com.lsj.safebox.utils.StreamTools;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;
import android.widget.Toast;

public class Splash_MainActivity extends Activity {

	protected static final String TAG = "Splash_MainActivity";
	protected static final int ENTER_HOME = 0;
	protected static final int SHOW_UPDATE_DIALOG = 1;
	protected static final int URL_ERROR = 2;
	protected static final int NETWORK_ERROR = 3;
	protected static final int JSON_ERROR = 4;
	private TextView versionNum;
	private String description;
	private String apkurl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		versionNum = (TextView) findViewById(R.id.version);
		versionNum.setText("版本: v_" + getVersionName());

		// 检查升级
		checkUpdate();
		//加入进入延迟动画效果
		AlphaAnimation aa = new AlphaAnimation(0.2f, 1.0f);
		//设置时间
		aa.setDuration(1500);
		findViewById(R.id.container).startAnimation(aa);
		
//		//启动widget服务
//		Intent intent = new Intent(this, UpdateAppWidgetService.class);
//		startService(intent);
		
		//加载相关数据库文件
		copyDB("address.db");
		copyDB("antivirus.db");
		
	}
	
	/**
	 * 复制相关数据库
	 */
	private void copyDB(String dbname) {
		InputStream is = null;
		FileOutputStream fos = null;
		File file = new File(getFilesDir(), dbname);
		if(file.exists()){
			System.out.println("数据库已经存在不需要拷贝");
		}else{
			try {
				is = getAssets().open(dbname);
			
				fos = new FileOutputStream(file);
				int len = 0;
				byte[] buffer = new byte[1024];
				while ((len = is.read(buffer)) != -1) {
					fos.write(buffer, 0, len);
				}

			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					is.close();
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}

			}

		}
		
	}
	
	

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			/**
			 * 错误信息处理提示
			 */
			switch (msg.what) {
			case ENTER_HOME:// 进入主页面
				enterHome();
				break;
			case SHOW_UPDATE_DIALOG:// 显示升级对话框
				Log.i(TAG, "显示升级对话框");
				shouUpdateDialog();
				break;
			case URL_ERROR:// 地址错误
				Toast.makeText(getApplicationContext(), "下载地址错误,请稍后再试", 0).show();
				enterHome();
				break;
			case NETWORK_ERROR:// 网络连接错误
//				Toast.makeText(getApplicationContext(), "网络连接错误,请稍后再试", 0).show();
				enterHome();
				break;
			case JSON_ERROR:// json解析出错
				Toast.makeText(Splash_MainActivity.this, "json解析出错,请稍后再试", 0).show();
				enterHome();
				break;
			default:
				break;
			}
		}

	};

	/**
	 * 检查升级功能
	 */
	private void checkUpdate() {
		new Thread() {
			public void run() {
				Message mes = Message.obtain();// 得到一个已经得到的信息
				Long startTime = System.currentTimeMillis();
				
				try {

					URL url = new URL(getString(R.string.serverurl));
					// 联网
					HttpURLConnection hc;
					hc = (HttpURLConnection) url.openConnection();
					hc.setRequestMethod("GET");
					hc.setConnectTimeout(3000);
					int code = hc.getResponseCode();
					if (code == 200) {
						// 联网成功
						InputStream is = hc.getInputStream();
						// 转换流为String
						String result = StreamTools.readFromStream(is);
						Log.i(TAG, "联网成功" + result);
						// json解析
						JSONObject obj = new JSONObject(result);
						// 得到服务器的版本信息
						String version = (String) obj.get("version");
						// 服务器的版本描述
						description = (String) obj.get("description");
						// 服务器apk下载地址
						apkurl = (String) obj.get("apkurl");

						// 校验是否有新版本
						if (getVersionName().equals(version)) {
							// 版本号一致,相同,进入主页
							mes.what = ENTER_HOME;
						} else {
							// 版本号不同,不同,跟新升级
							mes.what = SHOW_UPDATE_DIALOG;
						}
					}

				} catch (MalformedURLException e) {
					mes.what = URL_ERROR;
					e.printStackTrace();
				} catch (IOException e) {
					mes.what = NETWORK_ERROR;
					e.printStackTrace();
				} catch (JSONException e) {
					mes.what = JSON_ERROR;
					e.printStackTrace();
				} finally {
					Long endTime = System.currentTimeMillis();
					Long dTime = endTime - startTime;
					if(dTime < 4000){
						 try {
						 Thread.sleep(4000-dTime);
						 } catch (InterruptedException e) {
						 e.printStackTrace();
						 }
					}
					
					handler.sendMessage(mes);
				}
			};
		}.start();

		 

	}
	
	/**
	 * 弹出升级对话框
	 */
	protected void shouUpdateDialog() {
		AlertDialog.Builder builder = new Builder(this);
		builder.setTitle("提示升级");
		builder.setMessage(description);
		//点击立即升级时,自动下载新版本apk并替换安装
		builder.setPositiveButton("立刻升级", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				//下载并安装新版本apk
				if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
					//SD卡存在
				}else{
					//SD卡不存在
					Toast.makeText(getApplicationContext(), "SD卡不存在,请安装以后再试", 0).show();
					return ;
				}
			}
		});
		//点击暂不升级后,进入主界面
		builder.setNegativeButton("暂不升级", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();//去除对话框
				enterHome();
			}
		});
		builder.show();
	}

	/**
	 * 得到版本号
	 */
	private String getVersionName() {
		// 管理手机APK
		PackageManager pm = getPackageManager();
		try {
			// 得到指定apk的工能清单文件
			PackageInfo packageInfo = pm.getPackageInfo(getPackageName(), 0);
			return packageInfo.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return "";
		}

	}

	/**
	 * 进入主页面
	 */
	protected void enterHome() {
		Intent intent = new Intent(this, HomeActivity.class);
		startActivity(intent);
		finish();//关闭当前页面

//		Intent intent1=new Intent(getApplicationContext(), AddressService.class);
//		startService(intent1);
	}
}
