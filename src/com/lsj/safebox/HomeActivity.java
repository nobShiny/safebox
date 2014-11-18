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
		adapter = new MyAdapter();//自定义适配器
		setting.setAdapter(adapter);//设置适配模型
		
		setting.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent;
				switch (view.getId()) {
				case R.layout.://进入手机防盗
					intent = new Intent(HomeActivity.this,SettingActivity.class);
					startActivity(intent);
					break;
				case 1://进入安全通讯
					intent = new Intent(HomeActivity.this,SettingActivity.class);
					startActivity(intent);
					break;
				case 1://进入系统优化
					intent = new Intent(HomeActivity.this,SettingActivity.class);
					startActivity(intent);
					break;
				case 1://进入软件管理
					intent = new Intent(HomeActivity.this,SettingActivity.class);
					startActivity(intent);
					break;
				case 1://进入上网管理
					intent = new Intent(HomeActivity.this,SettingActivity.class);
					startActivity(intent);
					break;
				case 1://进入安全医生
					intent = new Intent(HomeActivity.this,SettingActivity.class);
					startActivity(intent);
					break;
				case 1://进入防御卫士
					intent = new Intent(HomeActivity.this,SafedoctorActivity.class);
					startActivity(intent);
					break;
				case R.layout.h://进入设置中心
					intent = new Intent(HomeActivity.this,SettingActivity.class);
					startActivity(intent);
					break;
				case 1://进入检查更新
					intent = new Intent(HomeActivity.this,SettingActivity.class);
					startActivity(intent);
					break;
				case 1://进入问题反馈
					intent = new Intent(HomeActivity.this,SettingActivity.class);
					startActivity(intent);
					break;
				case 1://进入联系我们
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
		 * 返回ListView某一行的view对象
		 */
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			return null;
		}

		/**
		 * 控件总数
		 */
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return names.length;
		}

		/**
		 * 返回点击的对象
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
	 * 此方法用于判断手机防盗功能是否已经设置过密码
	 */
	private boolean isSetupPwd() {
		String password = sp.getString("password", null);
		// 判断密码是否存在
		// if(TextUtils.isEmpty(password)){
		// return false;
		// }else{
		// return true;
		// }
		return !TextUtils.isEmpty(password);
	}

	/**
	 * 手机防盗功能对话框的显示
	 */
	protected void showLostFindDialog() {
		if (isSetupPwd()) {
			// 已经设置密码
			showEnterPass();
		} else {
			// 没有设置密码
			showSetPwdDialog();
		}

	}

	private EditText et_set_pwd;
	private EditText et_set_confirm;
	private Button btn_ok;
	private Button btn_cancel;
	private AlertDialog dialog;

	/**
	 * 设置密码
	 * 设置密码输入框 et_set_pwd
	 * 确认密码输入框  et_set_confirm
	 * 确认按钮 btn_ok 
	 * 取消按钮 btn_cancel
	 * 
	 */
	private void showSetPwdDialog() {
		AlertDialog.Builder builder = new Builder(HomeActivity.this);
		// 自定义布局文件
		// 关联布局文件
		View view = View.inflate(HomeActivity.this,
				R.layout.popwindow_setpassword_dialog, null);

		et_set_pwd = (EditText) view.findViewById(R.id.et_set_pwd);
		et_set_confirm = (EditText) view.findViewById(R.id.et_set_confirm);
		btn_ok = (Button) view.findViewById(R.id.btn_ok);
		btn_cancel = (Button) view.findViewById(R.id.btn_cancel);

		// 添加确认按钮监听
		btn_ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 取出密码
				String password = et_set_pwd.getText().toString().trim();
				String password_confirm = et_set_confirm.getText().toString()
						.trim();
				// 判断密码是否为空
				if (TextUtils.isEmpty(password)
						|| TextUtils.isEmpty(password_confirm)) {
					Toast.makeText(HomeActivity.this, "密码不能为空", 0).show();
					return;
				}
				// 判断两次密码是否一致
				if (password.equals(password)) {
					// 一致,保存加密(MD5)后的密码
					Editor editor = sp.edit();
					editor.putString("password", MD5Utils.md5Password(password));
					editor.commit();
					// 退出对话框
					dialog.dismiss();
					//进入手机防盗页面
					Intent intent = new Intent(HomeActivity.this,SecurityActivity.class);
					startActivity(intent);
					
				} else {
					// 不一致,重新输入
					Toast.makeText(HomeActivity.this, "两次密码输入不一致", 0).show();
					return;
				}

			}
		});

		// 添加取消按钮监听
		btn_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 取消对话框
				dialog.dismiss();
			}
		});

//		builder.setView(view);
		//兼容不同版本的显示效果
		dialog = builder.create();
		dialog.setView(view,0,0,0,0);
		dialog.show();

	}
	
	EditText et_enter_pwd;
	/**
	 * 输入密码对话框
	 */
	private void showEnterPass() {
		AlertDialog.Builder builder = new Builder(HomeActivity.this);
		View view = View.inflate(HomeActivity.this,
				R.layout.popwindow_enterpassword_dialog, null);

		et_enter_pwd = (EditText) view.findViewById(R.id.et_enter_pwd);
		btn_ok = (Button) view.findViewById(R.id.btn_ok);
		btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
		
		// 添加确认按钮监听
				btn_ok.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// 取出密码
						String password = et_enter_pwd.getText().toString().trim();
						//取出已经保存的密码
						String savePassword = sp.getString("password", "");
						
						// 判断密码是否为空
						if (TextUtils.isEmpty(password)) {
							Toast.makeText(HomeActivity.this, "您输入的密码为空", 1).show();
							return ;
						}
						// 判断密码是否正确
						if (MD5Utils.md5Password(password).equals(savePassword)) {
							// 一致,保存密码
							
							// 退出对话框
							dialog.dismiss();
							//进入手机防盗页面
							Intent intent = new Intent(HomeActivity.this,SecurityActivity.class);
							startActivity(intent);

						} else {
							// 不一致,重新输入
							Toast.makeText(HomeActivity.this, "密码不正确", 1).show();
							//清空密码重新输入
							et_set_pwd.setText("");
							return;
						}

					}
				});

				// 添加取消按钮监听
				btn_cancel.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// 取消对话框
						dialog.dismiss();
					}
				});

//				builder.setView(view);
				//兼容不同版本的显示效果
				dialog = builder.create();
				dialog.setView(view,0,0,0,0);
				dialog.show();
				
		
	}

}
