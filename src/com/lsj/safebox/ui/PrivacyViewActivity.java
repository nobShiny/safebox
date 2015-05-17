package com.lsj.safebox.ui;

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
import android.widget.Toast;

import com.lsj.safebox.R;
import com.lsj.safebox.utils.MD5Utils;

/**
 * ��˽��������
 * @author Administrator
 *
 */
public class PrivacyViewActivity extends Activity {
	private Button btn_return;
	public SharedPreferences sp;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_privacy);
		sp = getSharedPreferences("config", MODE_PRIVATE);
		
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
	}
	
	public void enterApplock(View view){
		showLostFindDialog();
//		Intent intent = new Intent(this,AppLockActivity.class);
//		startActivity(intent);
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
		AlertDialog.Builder builder = new Builder(PrivacyViewActivity.this);
		// �Զ��岼���ļ�
		// ���������ļ�
		View view = View.inflate(PrivacyViewActivity.this,
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
					Toast.makeText(PrivacyViewActivity.this, "���벻��Ϊ��", 0).show();
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
					Intent intent = new Intent(PrivacyViewActivity.this,
							AppLockActivity.class);
					startActivity(intent);

				} else {
					// ��һ��,��������
					Toast.makeText(PrivacyViewActivity.this, "�����������벻һ��,����������", 0).show();
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
		AlertDialog.Builder builder = new Builder(PrivacyViewActivity.this);
		View view = View.inflate(PrivacyViewActivity.this,
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
					Toast.makeText(PrivacyViewActivity.this, "�����������Ϊ��", 1).show();
					return;
				}
				// �ж������Ƿ���ȷ
				if (MD5Utils.md5Password(password).equals(savePassword)) {
					// �˳��Ի���
					dialog.dismiss();
					// ����Զ�̿���
					Intent intent = new Intent(PrivacyViewActivity.this,
							AppLockActivity.class);
					startActivity(intent);

				} else {
					// ��һ��,��������
					Toast.makeText(PrivacyViewActivity.this, "���벻��ȷ", 1).show();
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
