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
		versionNum.setText("�汾: v_" + getVersionName());

		// �������
		checkUpdate();
		//��������ӳٶ���Ч��
		AlphaAnimation aa = new AlphaAnimation(0.2f, 1.0f);
		//����ʱ��
		aa.setDuration(1500);
		findViewById(R.id.container).startAnimation(aa);
		
//		//����widget����
//		Intent intent = new Intent(this, UpdateAppWidgetService.class);
//		startService(intent);
		
		//����������ݿ��ļ�
		copyDB("address.db");
		copyDB("antivirus.db");
		
	}
	
	/**
	 * ����������ݿ�
	 */
	private void copyDB(String dbname) {
		InputStream is = null;
		FileOutputStream fos = null;
		File file = new File(getFilesDir(), dbname);
		if(file.exists()){
			System.out.println("���ݿ��Ѿ����ڲ���Ҫ����");
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
			 * ������Ϣ������ʾ
			 */
			switch (msg.what) {
			case ENTER_HOME:// ������ҳ��
				enterHome();
				break;
			case SHOW_UPDATE_DIALOG:// ��ʾ�����Ի���
				Log.i(TAG, "��ʾ�����Ի���");
				shouUpdateDialog();
				break;
			case URL_ERROR:// ��ַ����
				Toast.makeText(getApplicationContext(), "���ص�ַ����,���Ժ�����", 0).show();
				enterHome();
				break;
			case NETWORK_ERROR:// �������Ӵ���
//				Toast.makeText(getApplicationContext(), "�������Ӵ���,���Ժ�����", 0).show();
				enterHome();
				break;
			case JSON_ERROR:// json��������
				Toast.makeText(Splash_MainActivity.this, "json��������,���Ժ�����", 0).show();
				enterHome();
				break;
			default:
				break;
			}
		}

	};

	/**
	 * �����������
	 */
	private void checkUpdate() {
		new Thread() {
			public void run() {
				Message mes = Message.obtain();// �õ�һ���Ѿ��õ�����Ϣ
				Long startTime = System.currentTimeMillis();
				
				try {

					URL url = new URL(getString(R.string.serverurl));
					// ����
					HttpURLConnection hc;
					hc = (HttpURLConnection) url.openConnection();
					hc.setRequestMethod("GET");
					hc.setConnectTimeout(3000);
					int code = hc.getResponseCode();
					if (code == 200) {
						// �����ɹ�
						InputStream is = hc.getInputStream();
						// ת����ΪString
						String result = StreamTools.readFromStream(is);
						Log.i(TAG, "�����ɹ�" + result);
						// json����
						JSONObject obj = new JSONObject(result);
						// �õ��������İ汾��Ϣ
						String version = (String) obj.get("version");
						// �������İ汾����
						description = (String) obj.get("description");
						// ������apk���ص�ַ
						apkurl = (String) obj.get("apkurl");

						// У���Ƿ����°汾
						if (getVersionName().equals(version)) {
							// �汾��һ��,��ͬ,������ҳ
							mes.what = ENTER_HOME;
						} else {
							// �汾�Ų�ͬ,��ͬ,��������
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
	 * ���������Ի���
	 */
	protected void shouUpdateDialog() {
		AlertDialog.Builder builder = new Builder(this);
		builder.setTitle("��ʾ����");
		builder.setMessage(description);
		//�����������ʱ,�Զ������°汾apk���滻��װ
		builder.setPositiveButton("��������", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				//���ز���װ�°汾apk
				if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
					//SD������
				}else{
					//SD��������
					Toast.makeText(getApplicationContext(), "SD��������,�밲װ�Ժ�����", 0).show();
					return ;
				}
			}
		});
		//����ݲ�������,����������
		builder.setNegativeButton("�ݲ�����", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();//ȥ���Ի���
				enterHome();
			}
		});
		builder.show();
	}

	/**
	 * �õ��汾��
	 */
	private String getVersionName() {
		// �����ֻ�APK
		PackageManager pm = getPackageManager();
		try {
			// �õ�ָ��apk�Ĺ����嵥�ļ�
			PackageInfo packageInfo = pm.getPackageInfo(getPackageName(), 0);
			return packageInfo.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return "";
		}

	}

	/**
	 * ������ҳ��
	 */
	protected void enterHome() {
		Intent intent = new Intent(this, HomeActivity.class);
		startActivity(intent);
		finish();//�رյ�ǰҳ��

//		Intent intent1=new Intent(getApplicationContext(), AddressService.class);
//		startService(intent1);
	}
}
