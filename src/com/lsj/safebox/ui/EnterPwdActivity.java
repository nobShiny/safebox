package com.lsj.safebox.ui;

import com.lsj.safebox.R;
import com.lsj.safebox.utils.MD5Utils;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
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
	private SharedPreferences sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_enter_password);
		iv_icon = (ImageView) findViewById(R.id.iv_icon);
		tv_name = (TextView) findViewById(R.id.tv_name);
		ed_password = (EditText) findViewById(R.id.ed_password);
		sp = getSharedPreferences("config", MODE_PRIVATE);
		//Activity自带的方法
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
			e.printStackTrace();
		}

	}

	@Override
	protected void onStop() {
		super.onStop();
		finish();
	}

	// 点击返回的时候
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		// 到桌面去
		Intent intent = new Intent();
		intent.setAction("android.intent.action.MAIN");
		intent.addCategory("android.intent.category.HOME");
		startActivity(intent);
	}

	public void ok(View view) {
		// 1.得到密码
		String password = ed_password.getText().toString().trim();
		String savePassword = sp.getString("password", "");

		// 2.判空和校验密码是否正确
		if (TextUtils.isEmpty(password)) {
			Toast.makeText(this, "密码不能为空", 1).show();
		} else {

			// 3.判断密码是否正确
			if (MD5Utils.md5Password(password).equals(savePassword)) {
				
				Intent intent = new Intent();
				intent.setAction("com.lsj.safebox.stopprotecting");
				intent.putExtra("packname",packName);
				
				sendBroadcast(intent);
				// 4.放行
				finish();
				Toast.makeText(getApplicationContext(), "程序已解锁", Toast.LENGTH_LONG).show();
				
			}else{
				Toast.makeText(this, "密码错误,请重试", 1).show();
			}

		}

	}

}
