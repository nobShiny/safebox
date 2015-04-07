package com.lsj.safebox.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.lsj.safebox.R;

/**
 * 隐私保护功能
 * @author Administrator
 *
 */
public class PrivacyViewActivity extends Activity {
	private Button btn_return;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_privacy);
		
		// 返回按钮
				btn_return = (Button) findViewById(R.id.main_back);
				btn_return.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						switch (v.getId()) {
						case R.id.main_back:
							finish();
							break;
						}
					}
				});
	}
	
	public void enterApplock(View view){
		Intent intent = new Intent(this,AppLockActivity.class);
		startActivity(intent);
		
	}
}
