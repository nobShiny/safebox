package com.lsj.safebox.ui;

import com.lsj.safebox.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;

/**
 * 系统优化view
 * @author Administrator
 *
 */
public class SystemOptActivityView extends Activity implements OnClickListener{
	
	Button main_back;
	private RelativeLayout processManager,cleanCache;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_system_opt);
		
		main_back = (Button) findViewById(R.id.main_back);
		processManager = (RelativeLayout) findViewById(R.id.btn_jinchengguanli);
		cleanCache = (RelativeLayout) findViewById(R.id.btn_huancunqingli);
		
		main_back.setOnClickListener(this);
		processManager.setOnClickListener(this);
		cleanCache.setOnClickListener(this);
	}
	
	Intent intent;
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		
		case R.id.btn_jinchengguanli://进程管理
			intent = new Intent(this, TaskManagerActivity.class);
			startActivity(intent);
			break;
		case R.id.btn_huancunqingli://缓存清理
			intent = new Intent(this, CleanCacheActivity.class);
			startActivity(intent);
			break;
			
		case R.id.main_back://返回
			finish();
			break;
		}
	}

}
