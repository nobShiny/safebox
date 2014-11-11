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
		versionNum.setText("版本: v_"+getVersionName());
	}
	
	/**
	 * 得到版本号
	 */
	private String getVersionName(){
		//管理手机APK
		PackageManager pm = getPackageManager();
		try {
			//得到指定apk的工能清单文件
			PackageInfo packageInfo = pm.getPackageInfo(getPackageName(), 0);
			return packageInfo.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return "";
		}
		
	}

}
