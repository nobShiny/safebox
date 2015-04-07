package com.lsj.safebox.ui;

import com.lsj.safebox.R;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class EnterPwdActivity extends Activity {

	private ImageView iv_icon;
	private TextView tv_name;
	private Intent intent;
	private String packName;
	private EditText ed_password;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_enter_password);
		iv_icon = (ImageView) findViewById(R.id.iv_icon);
		tv_name = (TextView) findViewById(R.id.tv_name);
		ed_password = (EditText) findViewById(R.id.ed_password);
		//Activity�Դ��ķ���
		intent = getIntent();
		packName = intent.getStringExtra("packname");
		try {
			PackageManager pm = getPackageManager();
			PackageInfo packInfo = pm.getPackageInfo(packName, 0);
			Drawable icon = packInfo.applicationInfo.loadIcon(pm);
			iv_icon.setImageDrawable(icon);
			String name = packInfo.applicationInfo.loadLabel(pm).toString();
			tv_name.setText(name);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	protected void onStop() {
		super.onStop();
		finish();
	}

	// ������ص�ʱ��
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		// ������ȥ
		Intent intent = new Intent();
		// <action android:name="android.intent.action.MAIN" />
		// <category android:name="android.intent.category.HOME" />
		// <category android:name="android.intent.category.DEFAULT" />
		// <category android:name="android.intent.category.MONKEY"/>
		intent.setAction("android.intent.action.MAIN");
		intent.addCategory("android.intent.category.HOME");
		startActivity(intent);
	}

	public void ok(View view) {
		// 1.�õ�����
		String password = ed_password.getText().toString().trim();

		// 2.�пպ�У�������Ƿ���ȷ
		if (TextUtils.isEmpty(password)) {
			Toast.makeText(this, "���벻��Ϊ��", 1).show();
		} else {

			// 3.�ж������Ƿ���ȷ
			if ("123".equals(password)) {
				
				Intent intent = new Intent();
				intent.setAction("com.lsj.safebox.stopprotecting");
				intent.putExtra("packname", packName);
				
				sendBroadcast(intent);
				// 4.����
				finish();
				
			}else{
				Toast.makeText(this, "�������,������", 1).show();
			}

		}

	}

}
