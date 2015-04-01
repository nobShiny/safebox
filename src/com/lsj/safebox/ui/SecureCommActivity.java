package com.lsj.safebox.ui;

import com.lsj.safebox.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class SecureCommActivity extends Activity implements OnClickListener {
	private RelativeLayout guishudi,heimingdan,lanjieguize,duanxinbeifen;
	private Button main_back;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_securecomm);
		guishudi = (RelativeLayout) findViewById(R.id.btn_guishudi);
		heimingdan = (RelativeLayout) findViewById(R.id.btn_heimingdan);
		lanjieguize = (RelativeLayout) findViewById(R.id.btn_lanjieguize);
		duanxinbeifen = (RelativeLayout) findViewById(R.id.btn_duanxinbeifen);
		main_back = (Button) findViewById(R.id.main_back);
		
		guishudi.setOnClickListener(this);
		heimingdan.setOnClickListener(this);
		lanjieguize.setOnClickListener(this);
		duanxinbeifen.setOnClickListener(this);
		main_back.setOnClickListener(this);
	}

	
	private Intent intent;
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_guishudi://归属地查询
			intent = new Intent(getApplicationContext(), QueryAddressActivity.class);
			startActivity(intent);
			break;
		case R.id.btn_heimingdan://黑名单管理
			intent = new Intent(getApplicationContext(),CallSmsSafeActivity.class);
			startActivity(intent);
			break;
		case R.id.btn_lanjieguize://拦截设置
			intent = new Intent(getApplicationContext(),BlockingRuleViewActivity.class);
			startActivity(intent);
			break;
		case R.id.btn_duanxinbeifen://短信备份
			
			break;
		case R.id.main_back://返回
			finish();
			break;

		}
	}
	
	
	
	
}
