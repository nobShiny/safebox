package com.lsj.safebox.ui;

import android.content.Intent;
import android.os.Bundle;

import com.lsj.safebox.R;


/**
 * ������_��һ��
 * @author Administrator
 *
 */
public class SecurityActivity_setup_one extends BaseSetupActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_security_setup_one);
	}
	protected void showNext() {
		Intent intent = new Intent(this, SecurityActivity_setup_two.class);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.tran_in, R.anim.tran_out);
	}
	@Override
	protected void showPre() {
		
	}
	
}
