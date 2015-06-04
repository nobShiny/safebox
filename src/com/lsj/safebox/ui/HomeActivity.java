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

		//�Զ�������pop
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
	 * ��������¼�
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_shoujifangdao:// �����ֻ���������
			// intent = new Intent(HomeActivity.this,.class);
			// startActivity(intent);
			showLostFindDialog();
			Toast.makeText(getApplicationContext(), "�ֻ�����", 0).show();
			break;
		case R.id.ll_anquantongxun:// ���밲ȫͨѶ����
			intent = new Intent(HomeActivity.this, SecureCommActivity.class);
			startActivity(intent);
			break;
		case R.id.ll_xitongyouhua:// ����ϵͳ�Ż�����
			intent = new Intent(HomeActivity.this, SystemOptActivityView.class);
			startActivity(intent);
			break;
		case R.id.ll_ruanjianguanli:// �������������
			intent = new Intent(HomeActivity.this,
					SoftwareManagerViewActivity.class);
			startActivity(intent);
			break;
		case R.id.ll_shangwangguanli:// ����Ȩ��ҽ������
			intent = new Intent(HomeActivity.this,
					DoctorPermissionViewActivity.class);
			startActivity(intent);
			break;
		case R.id.ll_anquanyisheng:// ������˽��������
			intent = new Intent(HomeActivity.this, PrivacyViewActivity.class);
			startActivity(intent);
			break;
		case R.id.ll_fangyuweishi:// ���������ʿ����
			intent = new Intent(HomeActivity.this, DefenseActivityView.class);
			startActivity(intent);
			break;
		case R.id.ll_shezhizhongxin:// ������������
			intent = new Intent(HomeActivity.this, SettingActivity.class);
			startActivity(intent);
			break;
		// case R.id.icon_about://�������߽���
		// intent = new Intent(HomeActivity.this,AboutActivity.class);
		// startActivity(intent);
		// break;
		// case R.id.icon_contact://������ϵ����
		// intent = new Intent(HomeActivity.this,ContactActivity.class);
		// startActivity(intent);
		// break;
		// case R.id.icon_support://����֧��һ��
		// intent = new Intent(HomeActivity.this,SupportActivity.class);
		// startActivity(intent);
		// break;

		}
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
		AlertDialog.Builder builder = new Builder(HomeActivity.this);
		// �Զ��岼���ļ�
		// ���������ļ�
		View view = View.inflate(HomeActivity.this,
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
					// �����ֻ�����ҳ��
					Intent intent = new Intent(HomeActivity.this,
							SecurityActivity.class);
					startActivity(intent);

				} else {
					// ��һ��,��������
					Toast.makeText(HomeActivity.this, "�����������벻һ��,����������", 0).show();
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
		AlertDialog.Builder builder = new Builder(HomeActivity.this);
		View view = View.inflate(HomeActivity.this,
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
					Toast.makeText(HomeActivity.this, "�����������Ϊ��", 1).show();
					return;
				}
				// �ж������Ƿ���ȷ
				if (MD5Utils.md5Password(password).equals(savePassword)) {
					// �˳��Ի���
					dialog.dismiss();
					// �����ֻ�����ҳ��
					Intent intent = new Intent(HomeActivity.this,
							SecurityActivity.class);
					startActivity(intent);

				} else {
					// ��һ��,��������
					Toast.makeText(HomeActivity.this, "���벻��ȷ", 1).show();
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

	/**
	 * ˫���˳�
	 */
	private static Boolean isQuit = false;
	Timer timer = new Timer();

	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {

			if (isQuit == false) {
				isQuit = true;
				Toast.makeText(getBaseContext(), "������������˳�", Toast.LENGTH_SHORT)
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
