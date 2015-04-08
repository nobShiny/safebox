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
 * ∑¿”˘Œ¿ øview
 * @author Administrator
 *
 */
public class DefenseActivityView extends Activity implements OnClickListener{
	
	private Button main_back;
	private RelativeLayout killVirus;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_defense);
		
		main_back = (Button) findViewById(R.id.main_back);
		killVirus = (RelativeLayout) findViewById(R.id.btn_bingduchasha);
		
		main_back.setOnClickListener(this);
		killVirus.setOnClickListener(this);
	}
	
	Intent intent;
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		
		case R.id.btn_bingduchasha://≤°∂æ≤È…±
			intent = new Intent(this, AntiVirusActivity.class);
			startActivity(intent);
			break;
			
		case R.id.main_back://∑µªÿ
			finish();
			break;
		}
	}

}
