package com.lsj.safebox.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lsj.safebox.R;
import com.lsj.safebox.db.dao.Address;

public class QueryAddressActivity extends Activity {
	private EditText et_phonenum;
	private TextView tv_address;
	private Address addressDao;
	Button btn_return;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_query_address);

		// ���ذ�ť
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
		addressDao = new Address(getApplicationContext());
		et_phonenum = (EditText) findViewById(R.id.et_phonenum);
		tv_address = (TextView) findViewById(R.id.tv_address);

		// ��editextע��һ���ı��仯�Ļص�����
		et_phonenum.addTextChangedListener(new TextWatcher() {
			// �ı��仯��
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

				String location = addressDao.queryAddress(s.toString());
				if (!TextUtils.isEmpty(location)) {
					tv_address.setText(location);
				} else {
					tv_address.setText("");
				}
			}

			// �ı��仯֮ǰ����
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			// �ı��仯֮�����
			@Override
			public void afterTextChanged(Editable s) {

			}
		});
	}

	public void query(View v) {
		String num = et_phonenum.getText().toString().trim();
		if (TextUtils.isEmpty(num)) {
			Toast.makeText(getApplicationContext(), "���벻��Ϊ��", 0).show();
			Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
			Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
			vibrator.vibrate(2000);
			// shake.setInterpolator(new Interpolator() {
			//
			// @Override
			// public float getInterpolation(float x) {
			// return x*x;
			// }
			// });
			et_phonenum.startAnimation(shake);
		} else {
			String location = addressDao.queryAddress(num);
			if (!TextUtils.isEmpty(location)) {
				tv_address.setText(location);
			}
		}
	}
}
