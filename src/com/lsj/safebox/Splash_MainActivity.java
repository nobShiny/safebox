package com.lsj.safebox;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.widget.TextView;

import com.lsj.safe_box.R;

public class Splash_MainActivity extends Activity {
	
	private TextView versionNum;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		versionNum = (TextView) findViewById(R.id.version);
		versionNum.setText("�汾: v_"+getVersionName());
	}
	
	/**
	 * �õ��汾��
	 */
	private String getVersionName(){
		//�����ֻ�APK
		PackageManager pm = getPackageManager();
		try {
			//�õ�ָ��apk�Ĺ����嵥�ļ�
			PackageInfo packageInfo = pm.getPackageInfo(getPackageName(), 0);
			return packageInfo.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return "";
		}
		
	}

}
