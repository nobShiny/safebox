package com.lsj.safebox.ui;

import com.lsj.safebox.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class SecureCommActivity extends Activity implements OnClickListener {
	private Button guishudi,heimingdan,lanjieguize,duanxinbeifen,main_back;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_securecomm);
		guishudi = (Button) findViewById(R.id.btn_guishudi);
		heimingdan = (Button) findViewById(R.id.btn_heimingdan);
		lanjieguize = (Button) findViewById(R.id.btn_lanjieguize);
		duanxinbeifen = (Button) findViewById(R.id.btn_duanxinbeifen);
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
		case R.id.btn_guishudi://�����ز�ѯ
			intent = new Intent(getApplicationContext(), QueryAddressActivity.class);
			startActivity(intent);
			break;
		case R.id.btn_heimingdan://����������
			Toast.makeText(getApplicationContext(), "88", 0).show();
			break;
		case R.id.btn_lanjieguize://��������
			
			break;
		case R.id.btn_duanxinbeifen://���ű���
			
			break;
		case R.id.main_back://����
			finish();
			break;

		}
	}
}
