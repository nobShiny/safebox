package com.lsj.safebox.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

/**
 * �ֻ���������
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
		//�ж��Ƿ��ǵ�һ�ο����˹���
		boolean configed = sp.getBoolean("configed", false);
		Intent intent;
		if(configed){
			//�Ѿ�����,�����ֻ�������ҳ
			//setContentView(R.id.);
		}else{
			/**û������������*/
			intent = new Intent(this,SecurityActivity_setup_one.class);
			startActivity(intent);
			//�رյ�ǰactivity
			finish();
			
		}
	}
	
	
}
