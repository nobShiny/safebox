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
 * 防御卫士view
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
		
		case R.id.btn_bingduchasha://病毒查杀
			intent = new Intent(this, AntiVirusActivity.class);
			startActivity(intent);
			break;
		case R.id.btn_yuanchengkongzhi://远程控制
			showLostFindDialog();
			break;
			
		case R.id.main_back://返回
			finish();
			break;
		}
	}
	
	/**
	 * 手机密码功能对话框的显示
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

	/**
	 * 此方法用于判断手机防盗功能是否已经设置过密码
	 */
	private boolean isSetupPwd() {
		String password = sp.getString("password", null);
//		 判断密码是否存在
		
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
	 * 设置密码 设置密码输入框 
	 * et_set_pwd 确认密码输入框 
	 * et_set_confirm 确认按钮
	 * btn_ok 取消按钮
	 * btn_cancel
	 * 
	 */
	private void showSetPwdDialog() {
		AlertDialog.Builder builder = new Builder(DefenseActivityView.this);
		// 自定义布局文件
		// 关联布局文件
		View view = View.inflate(DefenseActivityView.this,
				R.layout.popwindow_setpassword_dialog, null);
//		builder.setView(view);
//		builder.show();

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
					Toast.makeText(DefenseActivityView.this, "密码不能为空", 0).show();
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
					// 进入远程控制
					Intent intent = new Intent(DefenseActivityView.this,
							RemoteControlActivity.class);
					startActivity(intent);

				} else {
					// 不一致,重新输入
					Toast.makeText(DefenseActivityView.this, "两次密码输入不一致,请重新输入", 0).show();
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

		// 兼容不同版本的显示效果
		dialog = builder.create();
		dialog.setView(view, 0, 0, 0, 0);
		dialog.show();

	}

	EditText et_enter_pwd;

	/**
	 * 输入密码对话框
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

		// 添加确认按钮监听
		btn_ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 取出密码
				String password = et_enter_pwd.getText().toString().trim();
				// 取出已经保存的密码
				String savePassword = sp.getString("password", "");

				// 判断密码是否为空
				if (TextUtils.isEmpty(password)) {
					Toast.makeText(DefenseActivityView.this, "您输入的密码为空", 1).show();
					return;
				}
				// 判断密码是否正确
				if (MD5Utils.md5Password(password).equals(savePassword)) {
					// 退出对话框
					dialog.dismiss();
					// 进入远程控制
					Intent intent = new Intent(DefenseActivityView.this,
							RemoteControlActivity.class);
					startActivity(intent);

				} else {
					// 不一致,重新输入
					Toast.makeText(DefenseActivityView.this, "密码不正确", 1).show();
					// 清空密码重新输入
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

		// builder.setView(view);
		// 兼容不同版本的显示效果
		dialog = builder.create();
		dialog.setView(view, 0, 0, 0, 0);
		dialog.show();

	}

}
