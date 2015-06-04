package com.lsj.safebox.ui;

import java.util.Timer;
import java.util.TimerTask;

import com.lsj.safebox.R;
import com.lsj.safebox.custom.ui.ImportMenuView;
import com.lsj.safebox.custom.ui.RippleLayout;
import com.lsj.safebox.utils.MD5Utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.media.SoundPool;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewStub;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class HomeActivity extends Activity implements OnClickListener {
	private SharedPreferences sp;

	private LinearLayout ll_shoujifangdao, ll_anquantongxun, ll_xitongyouhua,
			ll_ruanjianguanli, ll_shangwangguanli, ll_anquanyisheng,
			ll_fangyuweishi, ll_shezhizhongxin;

	public static ImageButton add;
	SoundPool soundPool;
	private int soundId;
	private boolean flag = false;

	int width, height;
	public static RippleLayout ripple;
	float density;
	ViewStub viewStub;

	ImportMenuView menuView;

	// private ImageView icon_about,icon_contact,icon_support;
	private Intent intent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		sp = getSharedPreferences("config", MODE_PRIVATE);

		findViewById();
		setOnClickListener();

		//自定义上推pop
		add = (ImageButton) findViewById(R.id.add);
		ripple = (RippleLayout) findViewById(R.id.more2);
		DisplayMetrics metircs = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metircs);
		width = metircs.widthPixels;
		height = metircs.heightPixels;
		density = metircs.density;

		menuView = (ImportMenuView) findViewById(R.id.main_activity_import_menu);
		menuView.setEnabled(false);

		ripple.setRippleFinishListener(new RippleLayout.RippleFinishListener() {
			@Override
			public void rippleFinish(int id) {

				if (id == R.id.more2) {
					menuView.setVisibility(View.VISIBLE);
					menuView.setEnabled(true);
					menuView.setFocusable(true);
					menuView.rl_closeVisiableAnimation();
					menuView.animation(HomeActivity.this);
					menuView.bringToFront();
					ripple.setVisibility(View.GONE);
				}
			}
		});

	}

	private void setOnClickListener() {
		ll_shoujifangdao.setOnClickListener(this);
		ll_anquantongxun.setOnClickListener(this);
		ll_xitongyouhua.setOnClickListener(this);
		ll_ruanjianguanli.setOnClickListener(this);
		ll_shangwangguanli.setOnClickListener(this);
		ll_anquanyisheng.setOnClickListener(this);
		ll_fangyuweishi.setOnClickListener(this);
		ll_shezhizhongxin.setOnClickListener(this);
		// icon_about.setOnClickListener(this);
		// icon_contact.setOnClickListener(this);
		// icon_support.setOnClickListener(this);
	}

	private void findViewById() {
		ll_shoujifangdao = (LinearLayout) findViewById(R.id.ll_shoujifangdao);
		ll_anquantongxun = (LinearLayout) findViewById(R.id.ll_anquantongxun);
		ll_xitongyouhua = (LinearLayout) findViewById(R.id.ll_xitongyouhua);
		ll_ruanjianguanli = (LinearLayout) findViewById(R.id.ll_ruanjianguanli);
		ll_shangwangguanli = (LinearLayout) findViewById(R.id.ll_shangwangguanli);
		ll_anquanyisheng = (LinearLayout) findViewById(R.id.ll_anquanyisheng);
		ll_fangyuweishi = (LinearLayout) findViewById(R.id.ll_fangyuweishi);
		ll_shezhizhongxin = (LinearLayout) findViewById(R.id.ll_shezhizhongxin);
	}

	/**
	 * 监听点击事件
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_shoujifangdao:// 进入手机防盗功能
			// intent = new Intent(HomeActivity.this,.class);
			// startActivity(intent);
			showLostFindDialog();
			Toast.makeText(getApplicationContext(), "手机防盗", 0).show();
			break;
		case R.id.ll_anquantongxun:// 进入安全通讯功能
			intent = new Intent(HomeActivity.this, SecureCommActivity.class);
			startActivity(intent);
			break;
		case R.id.ll_xitongyouhua:// 进入系统优化功能
			intent = new Intent(HomeActivity.this, SystemOptActivityView.class);
			startActivity(intent);
			break;
		case R.id.ll_ruanjianguanli:// 进入软件管理功能
			intent = new Intent(HomeActivity.this,
					SoftwareManagerViewActivity.class);
			startActivity(intent);
			break;
		case R.id.ll_shangwangguanli:// 进入权限医生功能
			intent = new Intent(HomeActivity.this,
					DoctorPermissionViewActivity.class);
			startActivity(intent);
			break;
		case R.id.ll_anquanyisheng:// 进入隐私保护功能
			intent = new Intent(HomeActivity.this, PrivacyViewActivity.class);
			startActivity(intent);
			break;
		case R.id.ll_fangyuweishi:// 进入防御卫士功能
			intent = new Intent(HomeActivity.this, DefenseActivityView.class);
			startActivity(intent);
			break;
		case R.id.ll_shezhizhongxin:// 进入设置中心
			intent = new Intent(HomeActivity.this, SettingActivity.class);
			startActivity(intent);
			break;
		// case R.id.icon_about://进入作者介绍
		// intent = new Intent(HomeActivity.this,AboutActivity.class);
		// startActivity(intent);
		// break;
		// case R.id.icon_contact://进入联系我们
		// intent = new Intent(HomeActivity.this,ContactActivity.class);
		// startActivity(intent);
		// break;
		// case R.id.icon_support://进入支持一下
		// intent = new Intent(HomeActivity.this,SupportActivity.class);
		// startActivity(intent);
		// break;

		}
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
		AlertDialog.Builder builder = new Builder(HomeActivity.this);
		// 自定义布局文件
		// 关联布局文件
		View view = View.inflate(HomeActivity.this,
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
					// 进入手机防盗页面
					Intent intent = new Intent(HomeActivity.this,
							SecurityActivity.class);
					startActivity(intent);

				} else {
					// 不一致,重新输入
					Toast.makeText(HomeActivity.this, "两次密码输入不一致,请重新输入", 0).show();
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
		AlertDialog.Builder builder = new Builder(HomeActivity.this);
		View view = View.inflate(HomeActivity.this,
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
					Toast.makeText(HomeActivity.this, "您输入的密码为空", 1).show();
					return;
				}
				// 判断密码是否正确
				if (MD5Utils.md5Password(password).equals(savePassword)) {
					// 退出对话框
					dialog.dismiss();
					// 进入手机防盗页面
					Intent intent = new Intent(HomeActivity.this,
							SecurityActivity.class);
					startActivity(intent);

				} else {
					// 不一致,重新输入
					Toast.makeText(HomeActivity.this, "密码不正确", 1).show();
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

	/**
	 * 双击退出
	 */
	private static Boolean isQuit = false;
	Timer timer = new Timer();

	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {

			if (isQuit == false) {
				isQuit = true;
				Toast.makeText(getBaseContext(), "连续点击两次退出", Toast.LENGTH_SHORT)
						.show();
				TimerTask task = null;
				task = new TimerTask() {
					@Override
					public void run() {
						isQuit = false;
					}
				};
				timer.schedule(task, 2000);
			} else {
				finish();
			}
		}
		return false;
	}
	
	public float dpiConvertToPixel(float dpi){		
		float pixel = dpi * density;	
		return pixel;
	}
	
	@Override
	protected void onDestroy() {
		if(soundPool!=null){
			soundPool.release();  
	        soundPool = null;  
		}
		super.onDestroy();
	}

}
