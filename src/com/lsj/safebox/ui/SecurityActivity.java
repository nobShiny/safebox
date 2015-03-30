package com.lsj.safebox.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

/**
 * 手机防盗功能
 * @author Administrator
 *
 */
public class SecurityActivity extends Activity {
	
	private SharedPreferences sp;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.id);
		sp = getSharedPreferences("config", MODE_PRIVATE);
		//判断是否是第一次开启此功能
		boolean configed = sp.getBoolean("configed", false);
		Intent intent;
		if(configed){
			//已经设置,留在手机防盗主页
			//setContentView(R.id.);
		}else{
			/**没有做过设置向导*/
			intent = new Intent(this,SecurityActivity_setup_one.class);
			startActivity(intent);
			//关闭当前activity
			finish();
			
		}
	}
	
	
}
