package com.lsj.safebox.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lsj.safebox.R;

/**
 * 手机防盗功能
 * 
 * @author Administrator
 *
 */
public class SecurityActivity extends Activity{

	private SharedPreferences sp;

	private TextView tv_safenumber;
	private ImageView iv_status;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		sp = getSharedPreferences("config", MODE_PRIVATE);
		super.onCreate(savedInstanceState);

		// 是否设置过设置向导，如果没有设置就跳转到设置向导第一个页面，否则就留着手机防盗页面
		boolean configed = sp.getBoolean("configed", false);
		if (configed) {
			setContentView(R.layout.activity_lostfind_state);
			tv_safenumber = (TextView) findViewById(R.id.tv_safenumber);
			iv_status = (ImageView) findViewById(R.id.iv_status);
			// 设置安全号码
			tv_safenumber.setText(sp.getString("safenumber", "5556"));
			if (sp.getBoolean("protectting", false)) {
				// 防盗保护已经开启
				iv_status.setImageResource(R.drawable.lock);
			} else {
				// 防盗保护没有开启
				iv_status.setImageResource(R.drawable.unlock);
			}
		} else {
			// 转到设置向导第一个页面
			Intent intent = new Intent(this, SecurityActivity_setup_one.class);
			startActivity(intent);
			// 关闭当前页面
			finish();
		}

	}

	// 点击事件-重新进入设置向导页面
	public void reEnterSetting(View view) {
		Intent intent = new Intent(this, SecurityActivity_setup_one.class);
		startActivity(intent);
		// 关闭当前页面
		finish();
	}

}
