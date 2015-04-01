package com.lsj.safebox.ui;

import com.lsj.safebox.R;
import com.lsj.safebox.service.AddressService;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;

/**
 * 软件管理view
 * @author Administrator
 *
 */
public class SoftwareManagerViewActivity extends Activity implements OnClickListener {
	
	private RelativeLayout ruanjianguanli,wenjianjia;
	Button main_back;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_software_manager);
		ruanjianguanli = (RelativeLayout) findViewById(R.id.btn_ruanjianguanli);
		wenjianjia = (RelativeLayout) findViewById(R.id.btn_wenjianjia);
		main_back = (Button) findViewById(R.id.main_back);
		
		main_back.setOnClickListener(this);
		
		//软件管理界面
		ruanjianguanli.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent intent = new Intent(getApplicationContext(),SoftManagerActivity.class);
				startActivity(intent);
			}
		});
		
		
		//文件管理界面
		wenjianjia.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),FileExporerTabActivity.class);
				startActivity(intent);
			}
		});
		
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.main_back:
			finish();
			break;
		}
	}
}
