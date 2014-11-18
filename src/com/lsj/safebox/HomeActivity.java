package com.lsj.safebox;

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
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

public class HomeActivity extends Activity {
	private GridView setting;
	private MyAdapter adapter;
	private static String[] names = { "", "", "", "", "", "", "", "" };
	private SharedPreferences sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		sp = getSharedPreferences("config", MODE_PRIVATE);
		
		setting = (GridView) findViewById(R.id.home_setting);
		adapter = new MyAdapter();//�Զ���������
		setting.setAdapter(adapter);//��������ģ��
		
		setting.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent;
				switch (view.getId()) {
				case R.layout.://�����ֻ�����
					intent = new Intent(HomeActivity.this,SettingActivity.class);
					startActivity(intent);
					break;
				case 1://���밲ȫͨѶ
					intent = new Intent(HomeActivity.this,SettingActivity.class);
					startActivity(intent);
					break;
				case 1://����ϵͳ�Ż�
					intent = new Intent(HomeActivity.this,SettingActivity.class);
					startActivity(intent);
					break;
				case 1://�����������
					intent = new Intent(HomeActivity.this,SettingActivity.class);
					startActivity(intent);
					break;
				case 1://������������
					intent = new Intent(HomeActivity.this,SettingActivity.class);
					startActivity(intent);
					break;
				case 1://���밲ȫҽ��
					intent = new Intent(HomeActivity.this,SettingActivity.class);
					startActivity(intent);
					break;
				case 1://���������ʿ
					intent = new Intent(HomeActivity.this,SafedoctorActivity.class);
					startActivity(intent);
					break;
				case R.layout.h://������������
					intent = new Intent(HomeActivity.this,SettingActivity.class);
					startActivity(intent);
					break;
				case 1://���������
					intent = new Intent(HomeActivity.this,SettingActivity.class);
					startActivity(intent);
					break;
				case 1://�������ⷴ��
					intent = new Intent(HomeActivity.this,SettingActivity.class);
					startActivity(intent);
					break;
				case 1://������ϵ����
					intent = new Intent(HomeActivity.this,SettingActivity.class);
					startActivity(intent);
					break;

				default:
					break;
				}
			}
			
		});
		
		
	}

	private class MyAdapter extends BaseAdapter {

		/**
		 * ����ListViewĳһ�е�view����
		 */
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			return null;
		}

		/**
		 * �ؼ�����
		 */
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return names.length;
		}

		/**
		 * ���ص���Ķ���
		 */
		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 1;
		}
	}

	/**
	 * �˷��������ж��ֻ����������Ƿ��Ѿ����ù�����
	 */
	private boolean isSetupPwd() {
		String password = sp.getString("password", null);
		// �ж������Ƿ����
		// if(TextUtils.isEmpty(password)){
		// return false;
		// }else{
		// return true;
		// }
		return !TextUtils.isEmpty(password);
	}

	/**
	 * �ֻ��������ܶԻ������ʾ
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

	private EditText et_set_pwd;
	private EditText et_set_confirm;
	private Button btn_ok;
	private Button btn_cancel;
	private AlertDialog dialog;

	/**
	 * ��������
	 * ������������� et_set_pwd
	 * ȷ�����������  et_set_confirm
	 * ȷ�ϰ�ť btn_ok 
	 * ȡ����ť btn_cancel
	 * 
	 */
	private void showSetPwdDialog() {
		AlertDialog.Builder builder = new Builder(HomeActivity.this);
		// �Զ��岼���ļ�
		// ���������ļ�
		View view = View.inflate(HomeActivity.this,
				R.layout.popwindow_setpassword_dialog, null);

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
					Toast.makeText(HomeActivity.this, "���벻��Ϊ��", 0).show();
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
					//�����ֻ�����ҳ��
					Intent intent = new Intent(HomeActivity.this,SecurityActivity.class);
					startActivity(intent);
					
				} else {
					// ��һ��,��������
					Toast.makeText(HomeActivity.this, "�����������벻һ��", 0).show();
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

//		builder.setView(view);
		//���ݲ�ͬ�汾����ʾЧ��
		dialog = builder.create();
		dialog.setView(view,0,0,0,0);
		dialog.show();

	}
	
	EditText et_enter_pwd;
	/**
	 * ��������Ի���
	 */
	private void showEnterPass() {
		AlertDialog.Builder builder = new Builder(HomeActivity.this);
		View view = View.inflate(HomeActivity.this,
				R.layout.popwindow_enterpassword_dialog, null);

		et_enter_pwd = (EditText) view.findViewById(R.id.et_enter_pwd);
		btn_ok = (Button) view.findViewById(R.id.btn_ok);
		btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
		
		// ���ȷ�ϰ�ť����
				btn_ok.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// ȡ������
						String password = et_enter_pwd.getText().toString().trim();
						//ȡ���Ѿ����������
						String savePassword = sp.getString("password", "");
						
						// �ж������Ƿ�Ϊ��
						if (TextUtils.isEmpty(password)) {
							Toast.makeText(HomeActivity.this, "�����������Ϊ��", 1).show();
							return ;
						}
						// �ж������Ƿ���ȷ
						if (MD5Utils.md5Password(password).equals(savePassword)) {
							// һ��,��������
							
							// �˳��Ի���
							dialog.dismiss();
							//�����ֻ�����ҳ��
							Intent intent = new Intent(HomeActivity.this,SecurityActivity.class);
							startActivity(intent);

						} else {
							// ��һ��,��������
							Toast.makeText(HomeActivity.this, "���벻��ȷ", 1).show();
							//���������������
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

//				builder.setView(view);
				//���ݲ�ͬ�汾����ʾЧ��
				dialog = builder.create();
				dialog.setView(view,0,0,0,0);
				dialog.show();
				
		
	}

}
