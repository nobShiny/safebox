package com.lsj.safebox.ui;

import com.lsj.safebox.R;
import com.lsj.safebox.utils.MD5Utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

/**
 * ������ʿview
 * @author Administrator
 *
 */
public class DefenseActivityView extends Activity implements OnClickListener{
	
	private SharedPreferences sp;
	private Button main_back;
	private RelativeLayout killVirus,remoteControl;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_defense);
		sp = getSharedPreferences("config", MODE_PRIVATE);
		
		main_back = (Button) findViewById(R.id.main_back);
		killVirus = (RelativeLayout) findViewById(R.id.btn_bingduchasha);
		remoteControl = (RelativeLayout) findViewById(R.id.btn_yuanchengkongzhi);
		
		main_back.setOnClickListener(this);
		killVirus.setOnClickListener(this);
		remoteControl.setOnClickListener(this);
	}
	
	Intent intent;
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		
		case R.id.btn_bingduchasha://������ɱ
			intent = new Intent(this, AntiVirusActivity.class);
			startActivity(intent);
			break;
		case R.id.btn_yuanchengkongzhi://Զ�̿���
			showLostFindDialog();
			break;
			
		case R.id.main_back://����
			finish();
			break;
		}
	}
	
	/**
	 * �ֻ����빦�ܶԻ������ʾ
	 */
	protected void showLostFindDialog() {
		if (isSetupPwd()) {
			// �Ѿ���������
			showEnterPass();
		} else {
			// û����������
			showSetPwdDialog();
		}

	}

	/**
	 * �˷��������ж��ֻ����������Ƿ��Ѿ����ù�����
	 */
	private boolean isSetupPwd() {
		String password = sp.getString("password", null);
//		 �ж������Ƿ����
		
//		 if(TextUtils.isEmpty(password)){
//		 return false;
//		 }else{
//		 return true;
//		 }
		return !TextUtils.isEmpty(password);
	}

	private EditText et_set_pwd;
	private EditText et_set_confirm;
	private Button btn_ok;
	private Button btn_cancel;
	private AlertDialog dialog;

	/**
	 * �������� ������������� 
	 * et_set_pwd ȷ����������� 
	 * et_set_confirm ȷ�ϰ�ť
	 * btn_ok ȡ����ť
	 * btn_cancel
	 * 
	 */
	private void showSetPwdDialog() {
		AlertDialog.Builder builder = new Builder(DefenseActivityView.this);
		// �Զ��岼���ļ�
		// ���������ļ�
		View view = View.inflate(DefenseActivityView.this,
				R.layout.popwindow_setpassword_dialog, null);
//		builder.setView(view);
//		builder.show();

		et_set_pwd = (EditText) view.findViewById(R.id.et_set_pwd);
		et_set_confirm = (EditText) view.findViewById(R.id.et_set_confirm);
		btn_ok = (Button) view.findViewById(R.id.btn_ok);
		btn_cancel = (Button) view.findViewById(R.id.btn_cancel);

		// ���ȷ�ϰ�ť����
		btn_ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// ȡ������
				String password = et_set_pwd.getText().toString().trim();
				String password_confirm = et_set_confirm.getText().toString()
						.trim();
				// �ж������Ƿ�Ϊ��
				if (TextUtils.isEmpty(password)
						|| TextUtils.isEmpty(password_confirm)) {
					Toast.makeText(DefenseActivityView.this, "���벻��Ϊ��", 0).show();
					return;
				}
				// �ж����������Ƿ�һ��
				if (password.equals(password)) {
					// һ��,�������(MD5)�������
					Editor editor = sp.edit();
					editor.putString("password", MD5Utils.md5Password(password));
					editor.commit();
					// �˳��Ի���
					dialog.dismiss();
					// ����Զ�̿���
					Intent intent = new Intent(DefenseActivityView.this,
							RemoteControlActivity.class);
					startActivity(intent);

				} else {
					// ��һ��,��������
					Toast.makeText(DefenseActivityView.this, "�����������벻һ��,����������", 0).show();
					return;
				}

			}
		});

		// ���ȡ����ť����
		btn_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// ȡ���Ի���
				dialog.dismiss();
			}
		});

		// ���ݲ�ͬ�汾����ʾЧ��
		dialog = builder.create();
		dialog.setView(view, 0, 0, 0, 0);
		dialog.show();

	}

	EditText et_enter_pwd;

	/**
	 * ��������Ի���
	 */
	private void showEnterPass() {
		AlertDialog.Builder builder = new Builder(DefenseActivityView.this);
		View view = View.inflate(DefenseActivityView.this,
				R.layout.popwindow_enterpassword_dialog, null);
//		builder.setView(view);
//		builder.show();

		et_enter_pwd = (EditText) view.findViewById(R.id.et_enter_pwd);
		btn_ok = (Button) view.findViewById(R.id.btn_ok);
		btn_cancel = (Button) view.findViewById(R.id.btn_cancel);

		// ���ȷ�ϰ�ť����
		btn_ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// ȡ������
				String password = et_enter_pwd.getText().toString().trim();
				// ȡ���Ѿ����������
				String savePassword = sp.getString("password", "");

				// �ж������Ƿ�Ϊ��
				if (TextUtils.isEmpty(password)) {
					Toast.makeText(DefenseActivityView.this, "�����������Ϊ��", 1).show();
					return;
				}
				// �ж������Ƿ���ȷ
				if (MD5Utils.md5Password(password).equals(savePassword)) {
					// �˳��Ի���
					dialog.dismiss();
					// ����Զ�̿���
					Intent intent = new Intent(DefenseActivityView.this,
							RemoteControlActivity.class);
					startActivity(intent);

				} else {
					// ��һ��,��������
					Toast.makeText(DefenseActivityView.this, "���벻��ȷ", 1).show();
					// ���������������
					et_set_pwd.setText("");
					return;
				}

			}
		});

		// ���ȡ����ť����
		btn_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// ȡ���Ի���
				dialog.dismiss();
			}
		});

		// builder.setView(view);
		// ���ݲ�ͬ�汾����ʾЧ��
		dialog = builder.create();
		dialog.setView(view, 0, 0, 0, 0);
		dialog.show();

	}

}
